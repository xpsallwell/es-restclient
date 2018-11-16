package com.xps.es.restclient.exceptions;

import java.text.MessageFormat;

/**
 * Created by xiongps on 2017/8/16.
 */
public class ESRestClientException extends RuntimeException {

    private static final long serialVersionUID = 90822198567894L;

    private String code;

    private String message;

    private IRestClientException restClientException;


    public ESRestClientException(String message){
        this.code = "9999";
        this.message = message;
    }

    public ESRestClientException(String code,String message){
        this.code = code;
        this.message = message;
    }

    public ESRestClientException(IRestClientException restClientException){
        this.code = restClientException.getCode();
        this.message = restClientException.getMessage();
        this.restClientException = restClientException;
    }

    public ESRestClientException(IRestClientException restClientException,String ...extendMsg){
        this.code = restClientException.getCode();
        this.message = restClientException.getMessage();
        this.restClientException = restClientException;
        this.message = MessageFormat.format(restClientException.getMessage(), extendMsg);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public IRestClientException getRestClientException() {
        return restClientException;
    }

    public void setRestClientException(IRestClientException restClientException) {
        this.restClientException = restClientException;
    }
}
