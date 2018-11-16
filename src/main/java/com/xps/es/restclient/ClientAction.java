package com.xps.es.restclient;

import org.elasticsearch.client.RestClient;

import java.io.IOException;

/**
 * Created by xiongps on 2017/8/16.
 */
public interface ClientAction<T> {

    <T> T doAction(RestClient restClient) throws IOException;
}
