package com.xps.es.restclient;

import com.xps.es.restclient.exceptions.ESRestClientException;
import com.xps.es.restclient.exceptions.RestClientExceptionComp;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.io.IOException;

/**
 * Created by xiongps on 2017/8/16.
 */
public abstract class AbstractESRestClient {

    private RestClient restClient ;
    private RestClientConfig config;

    protected AbstractESRestClient(){
        if(null == this.restClient) {
            initRestClient();
        }
    }
    protected AbstractESRestClient(RestClientConfig config){
        this.config = config;
        if(null == this.restClient) {
            initRestClient();
        }
    }

    private  void initRestClient(){
        if(config == null) {
            throw new ESRestClientException(RestClientExceptionComp.LOAD_CONFIG_FAIL);
        }
        HttpHost []httpHosts = config.getHttpHosts();
        if(null == httpHosts || httpHosts.length == 0) {
            throw new ESRestClientException(RestClientExceptionComp.HTTP_HOST_NOT_CONFIG);
        }

        RestClientBuilder restClientBuilder = null;
        try{
            restClientBuilder = RestClient.builder(httpHosts);
        }catch (Exception e) {
            e.printStackTrace();
            throw new ESRestClientException(RestClientExceptionComp.HTTP_HOST_CONFIG_ERROR);
        }

        Integer maxRetryTimeoutMillis = config.getMaxRetryTimeoutMillis();
        if(maxRetryTimeoutMillis != null
                && maxRetryTimeoutMillis > 0) {
            restClientBuilder.setMaxRetryTimeoutMillis(maxRetryTimeoutMillis);
        }

        Header []headers = config.getHeaders();
        if(null != headers && headers.length > 0) {
            restClientBuilder.setDefaultHeaders(headers);
        }

        final Integer connectTimeout = config.getConnectTimeout();
        final Integer socketTimeout = config.getSocketTimeout();


        if((null !=connectTimeout && connectTimeout > 0)
                || (null !=socketTimeout && socketTimeout > 0)) {
            restClientBuilder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
                @Override
                public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                    if((null !=connectTimeout && connectTimeout > 0)
                            && (null !=socketTimeout && socketTimeout > 0)) {
                        return requestConfigBuilder.setConnectTimeout(connectTimeout)
                                .setSocketTimeout(socketTimeout);
                    } else if(null !=connectTimeout && connectTimeout > 0){
                        return requestConfigBuilder.setConnectTimeout(connectTimeout);
                    } else if(null !=socketTimeout && socketTimeout > 0){
                        return requestConfigBuilder.setSocketTimeout(socketTimeout);
                    }
                    return null;
                }
            });
        }

        final Integer ioThreadCount = config.getIoThreadCount();
        final String userName = config.getUserName();
        final String password = config.getPassword();
        if((null !=ioThreadCount && ioThreadCount > 0)
                || (null != userName && !"".equals(userName))){

            restClientBuilder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                @Override
                public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {

                    if((null !=ioThreadCount && ioThreadCount > 0)
                            && (null != userName && !"".equals(userName))) {
                        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                        credentialsProvider.setCredentials(AuthScope.ANY,
                                new UsernamePasswordCredentials(userName,password));
                        return httpClientBuilder.setDefaultIOReactorConfig(
                                IOReactorConfig.custom().setIoThreadCount(ioThreadCount).build())
                                .setDefaultCredentialsProvider(credentialsProvider);
                    } else if(null !=ioThreadCount && ioThreadCount > 0){
                        return httpClientBuilder.setDefaultIOReactorConfig(
                                IOReactorConfig.custom().setIoThreadCount(ioThreadCount).build());
                    } else if(null != userName && !"".equals(userName)){
                        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                        credentialsProvider.setCredentials(AuthScope.ANY,
                                new UsernamePasswordCredentials(userName,password));
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                    return null;
                }
            });
        }

        RestClient restClient = restClientBuilder.build();
        this.restClient = restClient;
    }

    protected RestClient getRestClient(){
        return this.restClient;
    }

    protected RestClientConfig getConfig(){
        return this.config;
    }

    protected void setConfig(RestClientConfig config){
        this.config = config;
    }
    protected void destroy(){
        this.config = null;
        if(this.restClient != null) {
            try {
                this.restClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
