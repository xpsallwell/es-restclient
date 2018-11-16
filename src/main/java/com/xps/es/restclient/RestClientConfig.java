package com.xps.es.restclient;

import com.xps.es.restclient.exceptions.ESRestClientException;
import com.xps.es.restclient.exceptions.RestClientExceptionComp;
import com.xps.es.restclient.util.ESRestUtil;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;

import java.io.Serializable;
import java.util.*;

/**
 * Created by xiongps on 2017/8/16.
 */
public final class RestClientConfig implements Serializable{

    private static final long serialVersionUID = 99829042L;

    private HttpHost []httpHosts;

    private Header []headers;

    private String userName;

    private String password;

    private Integer maxRetryTimeoutMillis;

    private Integer socketTimeout;

    private Integer connectTimeout;

    private Integer ioThreadCount;

    private List<String> httpHostList;

    private List<String> headerList;

    private List<Map<String,String>> headerMapList;

    private RestClient.FailureListener failureListener;

    public RestClientConfig(){}

    public RestClientConfig(Properties properties) {
        if(null == properties) {
            throw new ESRestClientException(RestClientExceptionComp.NOT_EMPTY,"RestClientConfig properties");
        }
        this.handlerProperties(properties);
    }

    public RestClientConfig(String host,int port) {
        this.httpHosts = new HttpHost[]{new HttpHost(host,port)};
    }


    public HttpHost[] getHttpHosts() {
        return httpHosts;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMaxRetryTimeoutMillis() {
        return maxRetryTimeoutMillis;
    }

    public void setMaxRetryTimeoutMillis(Integer maxRetryTimeoutMillis) {
        this.maxRetryTimeoutMillis = maxRetryTimeoutMillis;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getIoThreadCount() {
        return ioThreadCount;
    }

    public void setIoThreadCount(Integer ioThreadCount) {
        this.ioThreadCount = ioThreadCount;
    }

    public void setHttpHosts(HttpHost[] httpHosts) {
        this.httpHosts = httpHosts;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public List<Map<String, String>> getHeaderMapList() {
        return headerMapList;
    }

    public void setHeaderMapList(List<Map<String, String>> headerMapList) {
        this.headerMapList = headerMapList;
        if(null != headerMapList && headerMapList.size() > 0) {
            List<Header> tmpList = new ArrayList<Header>();
            if(this.headers != null) {
                tmpList.addAll(Arrays.asList(this.headers));
            }
            for(int i=0;i<headerMapList.size();i++) {
                Map<String,String> map = headerMapList.get(i);
                for(Map.Entry<String,String> entry:map.entrySet()) {
                    tmpList.add(new BasicHeader(entry.getKey(),entry.getValue()));
                }
            }
            this.headers = tmpList.toArray(new Header[0]);
        }
    }

    public List<String> getHttpHostList() {
        return httpHostList;
    }

    //String format:127.0.0.1:9200,http or 127.0.0.1,http or 127.0.0.1
    public void setHttpHostList(List<String> httpHostList) {
        this.httpHostList = httpHostList;
        this.httpHosts = handlerHosts(httpHostList).toArray(new HttpHost[0]);
    }

    public List<String> getHeaderList() {
        return headerList;
    }

    public void setHeaderList(List<String> headerList) {
        this.headerList = headerList;
        if(null != headerList && headerList.size() > 0) {
            List<Header> tmpList = new ArrayList<Header>();
            if(this.headers != null) {
                tmpList.addAll(Arrays.asList(this.headers));
            }
            for(String h:headerList) {
                if(null == h || "".equals(h) || !h.contains("=")) {
                    continue;
                }
                String hArr[] = h.split("=");
                tmpList.add(new BasicHeader(hArr[0],hArr[1]));
            }
            this.headers = tmpList.toArray(new Header[0]);
        }


    }

    public RestClient.FailureListener getFailureListener() {
        return failureListener;
    }

    public void setFailureListener(RestClient.FailureListener failureListener) {
        this.failureListener = failureListener;
    }

    private void handlerProperties(Properties properties) {
        String hosts = properties.getProperty("es-hosts");
        String maxRetryTimeoutMillis = properties.getProperty("es-maxRetryTimeoutMillis");
        String connectTimeout = properties.getProperty("es-connectTimeout");
        String socketTimeout = properties.getProperty("es-socketTimeout");
        String ioThreadCount = properties.getProperty("es-ioThreadCount");
        String headers = properties.getProperty("es-headers");
        if(ESRestUtil.isNotEmpty(hosts)) {
            this.httpHosts = handlerHosts(Arrays.asList(hosts.split(";"))).toArray(new HttpHost[0]);
        } else {
            this.httpHosts = new HttpHost[]{new HttpHost("127.0.0.1",
                    9200,"http")};
        }
        if(ESRestUtil.isNotEmpty(maxRetryTimeoutMillis)) {
            this.maxRetryTimeoutMillis = Integer.valueOf(maxRetryTimeoutMillis);
        }
        if(ESRestUtil.isNotEmpty(connectTimeout)) {
            this.connectTimeout = Integer.valueOf(connectTimeout);
        }
        if(ESRestUtil.isNotEmpty(socketTimeout)) {
            this.socketTimeout = Integer.valueOf(socketTimeout);
        }
        if(ESRestUtil.isNotEmpty(ioThreadCount)) {
            this.ioThreadCount = Integer.valueOf(ioThreadCount);
        }

        if(ESRestUtil.isNotEmpty(headers)) {
            List<Header> tmpList = new ArrayList<>();
            if(headers.contains(";")) {
                String []hd = headers.split(";");
                for(int i=0;i<hd.length;i++) {
                    String h[] = hd[i].split("=");
                    tmpList.add(new BasicHeader(h[0],h[1]));
                }
            } else if(headers.contains("=")){
                String h[] = headers.split("=");
                tmpList.add(new BasicHeader(h[0],h[1]));
            }
            this.headers = tmpList.toArray(new Header[0]);
        }
    }

    private List<HttpHost> handlerHosts(List<String> httpHostList){

        List<HttpHost> tmpHostList = new ArrayList<>();
        if(null != httpHostList && httpHostList.size() > 0) {
            String []defaultHost = new String[]{"127.0.0.1","9200","http"};
            for(int i=0;i<httpHostList.size();i++) {
                String httpHostStr = httpHostList.get(i);
                if(null == httpHostStr || "".equals(httpHostStr)) {
                    continue;
                }
                if(httpHostStr.contains(",")) {
                    String []hh1 = httpHostStr.split(",");
                    defaultHost[2] = hh1[1];
                    if(hh1[0].contains(":")) {
                        hh1 = hh1[0].split(":");
                        defaultHost[0] = hh1[0];
                        defaultHost[1] = hh1[1];
                    }
                } else if(httpHostStr.contains(":")) {
                    String []hh1 = httpHostStr.split(":");
                    defaultHost[0] = hh1[0];
                    defaultHost[1] = hh1[1];
                } else {
                    defaultHost[0] = httpHostStr;
                }
                tmpHostList.add(new HttpHost(defaultHost[0],
                        Integer.valueOf(defaultHost[1]),defaultHost[2]));
            }
        } else {
            tmpHostList.add(new HttpHost("127.0.0.1",9200,"http"));
        }
        return tmpHostList;
    }


}
