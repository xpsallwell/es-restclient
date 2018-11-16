package com.xps.es.restclient.exceptions;

public interface IRestClientException {
     String getCode();

     ExceptionLevel getLevel();

     String getMessage();

     enum ExceptionLevel {
        INFO, WARN, ERROR, FAIL;
     }
}
