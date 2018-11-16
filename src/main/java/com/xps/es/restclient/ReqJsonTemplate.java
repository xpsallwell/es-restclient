package com.xps.es.restclient;

import org.apache.http.HttpEntity;

import java.util.Map;

/**
 * Created by xiongps on 2017/8/18.
 */
public interface ReqJsonTemplate {

    public HttpEntity getJsonNStringEntity(String reqId, Map<String, Object> params);

    public String processTemplate(String reqId, Map<String, Object> params);
}
