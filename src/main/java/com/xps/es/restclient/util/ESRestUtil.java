package com.xps.es.restclient.util;

import com.xps.es.restclient.exceptions.ESRestClientException;
import com.xps.es.restclient.exceptions.RestClientExceptionComp;

/**
 * Created by xiongps on 2017/8/17.
 */
public class ESRestUtil {

    public static void notEmpty(Object str,String name) {
        if(null == str || "".equals(str)) {
            throw new ESRestClientException(RestClientExceptionComp.NOT_EMPTY,name);
        }
    }

    public static String []split(String str,String split) {
        if(null == str || "".equals(str)){
            return new String[0];
        }
        return str.split(split);
    }

    public static boolean isNotEmpty(String str){
        if(null == str || "".equals(str)) {
            return false;
        }
        return true;
    }
}
