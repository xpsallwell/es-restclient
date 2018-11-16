package com.xps.es.restclient.impl;

import com.xps.es.restclient.*;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiongps on 2017/8/16.
 */
public class ESRestClientImpl extends AbstractESRestClient implements ESRestClient {

    public static final Map<String,String> EMPTY_MAP = new HashMap<>();

    public ESRestClientImpl() {}
    public ESRestClientImpl(RestClientConfig config) {
        super(config);
    }

    @Override
    public <T> T execute(ClientAction<T> clientAction) throws IOException {
        RestClient restClient = null;
        try{
            restClient = super.getRestClient();
            return clientAction.doAction(restClient);
        }catch (IOException e) {
            throw e;
        }finally {}
    }

    @Override
    public void executeAsync(AsyncClientAction asyncClientAction){
        RestClient restClient = null;
        try{
            restClient = super.getRestClient();
            asyncClientAction.doAction(restClient);
        }finally {}
    }

    @Override
    public Response performRequest(final String method, final String endPoint, final Header... headers) throws IOException {
        return this.execute(new ClientAction<Response>() {
            @Override
            public Response doAction(RestClient restClient) throws IOException {
                return restClient.performRequest(method,endPoint,EMPTY_MAP,headers);
            }
        });
    }

    @Override
    public Response performRequest(final String method, final String endPoint,final Map<String, String> params,final Header... headers) throws IOException {
        return this.execute(new ClientAction<Response>() {
            @Override
            public Response doAction(RestClient restClient) throws IOException {
                return restClient.performRequest(method,endPoint,params,headers);
            }
        });
    }

    @Override
    public Response performRequest(final String method, final String endPoint,final HttpEntity entity,final Header... headers) throws IOException {
        return this.execute(new ClientAction<Response>() {
            @Override
            public Response doAction(RestClient restClient) throws IOException {
                return restClient.performRequest(method,endPoint,EMPTY_MAP,entity,headers);
            }
        });
    }

    @Override
    public Response performRequest(final String method, final String endPoint,final Map<String, String> params,
                                   final HttpEntity entity,final Header... headers) throws IOException{
        return this.execute(new ClientAction<Response>() {
            @Override
            public Response doAction(RestClient restClient) throws IOException {
                return restClient.performRequest(method,endPoint,params,entity,headers);
            }
        });
    }

    @Override
    public Response performRequest(final String method, final String endPoint,final Map<String, String> params,final HttpEntity entity,
                                   final HttpAsyncResponseConsumerFactory httpAsyncResponseConsumerFactory,
                                   final Header... headers) throws IOException {
        return this.execute(new ClientAction<Response>() {
            @Override
            public Response doAction(RestClient restClient) throws IOException {
                return restClient.performRequest(method,endPoint,params,entity,httpAsyncResponseConsumerFactory,headers);
            }
        });
    }

    @Override
    public void performRequestAsync(final String method, final String endpoint,final ResponseListener responseListener,
                                    final Header... headers) {
            this.executeAsync(new AsyncClientAction() {
                @Override
                public void doAction(RestClient restClient) {
                    restClient.performRequestAsync(method,endpoint,responseListener,headers);
                }
            });
           // this.executeAsync((restClient) ->
            //        restClient.performRequestAsync(method,endpoint,responseListener,headers));
    }

    @Override
    public void performRequestAsync(final String method, final String endpoint,final HttpEntity entity,
                                    final ResponseListener responseListener,final Header... headers) {
        this.executeAsync(new AsyncClientAction() {
            @Override
            public void doAction(RestClient restClient) {
                restClient.performRequestAsync(method,endpoint,
                        EMPTY_MAP,entity,responseListener,headers);
            }
        });
       // this.executeAsync((restClient)->
        //        restClient.performRequestAsync(method,endpoint,
         //               EMPTY_MAP,entity,responseListener,headers));
    }

    @Override
    public void performRequestAsync(final String method, final String endpoint,final Map<String, String> params,
                                    final ResponseListener responseListener,final Header... headers) {
        this.executeAsync(new AsyncClientAction() {
            @Override
            public void doAction(RestClient restClient) {
                restClient.performRequestAsync(method,endpoint,
                        params,responseListener,headers);
            }
        });

        //this.executeAsync((restClient)->
        //        restClient.performRequestAsync(method,endpoint,
         //               params,responseListener,headers));
    }

    @Override
    public void performRequestAsync(final String method, final String endpoint,final Map<String, String> params,
                                    final HttpEntity entity,final ResponseListener responseListener,final Header... headers) {
        this.executeAsync(new AsyncClientAction() {
            @Override
            public void doAction(RestClient restClient) {
                restClient.performRequestAsync(method,endpoint,
                        params,entity,responseListener,headers);
            }
        });
        //this.executeAsync((restClient)->
        //        restClient.performRequestAsync(method,endpoint,
        //                params,entity,responseListener,headers));
    }

    @Override
    public void performRequestAsync(final String method, final String endpoint,final Map<String, String> params,
                                    final HttpEntity entity,final HttpAsyncResponseConsumerFactory httpAsyncResponseConsumerFactory,
                                    final ResponseListener responseListener,final Header... headers) {
        this.executeAsync(new AsyncClientAction() {
            @Override
            public void doAction(RestClient restClient) {
                restClient.performRequestAsync(method,endpoint,
                        params,entity,httpAsyncResponseConsumerFactory,responseListener,headers);
            }
        });
       // this.executeAsync((restClient)->
       //         restClient.performRequestAsync(method,endpoint,
        //                params,entity,httpAsyncResponseConsumerFactory,responseListener,headers));
    }
}
