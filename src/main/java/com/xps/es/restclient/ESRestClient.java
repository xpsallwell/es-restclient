package com.xps.es.restclient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;

import java.io.IOException;
import java.util.Map;

/**
 * Created by xiongps on 2017/8/16.
 */
public interface ESRestClient {

    public <T> T execute(ClientAction<T> clientAction) throws IOException;

    public void executeAsync(AsyncClientAction asyncClientAction);

    public Response performRequest(final String method, final String endPoint,final Header... headers) throws IOException;

    public Response performRequest(final String method, final String endPoint, final Map<String,String> params,
                                   Header... headers) throws IOException;

    public Response performRequest(final String method, final String endPoint,
                                   final HttpEntity entity,final Header... headers) throws IOException;

    public Response performRequest(final String method, final String endPoint, Map<String,String> params,
                                   HttpEntity entity,Header... headers) throws IOException;

    public Response performRequest(final String method, final String endPoint, Map<String,String> params,
                                   HttpEntity entity,
                                   HttpAsyncResponseConsumerFactory httpAsyncResponseConsumerFactory,
                                   Header... headers) throws IOException;



    public void performRequestAsync(final String method, final String endpoint,
                                    ResponseListener responseListener, Header... headers);

    public void performRequestAsync(final String method, final String endpoint, HttpEntity entity,
                                    ResponseListener responseListener, Header... headers);

    public void performRequestAsync(final String method, final String endpoint, Map<String, String> params,
                                    ResponseListener responseListener, Header... headers);

    public void performRequestAsync(final String method, final String endpoint, Map<String, String> params,
                                    HttpEntity entity, ResponseListener responseListener, Header... headers);

    public void performRequestAsync(final String method, final String endpoint, Map<String, String> params,
                                    HttpEntity entity,
                                    HttpAsyncResponseConsumerFactory httpAsyncResponseConsumerFactory,
                                    ResponseListener responseListener, Header... headers);

}
