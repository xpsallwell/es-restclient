package com.xps.es.restclient;

/**
 * Created by xiongps on 2017/8/16.
 */
public class ESRConst {

    public enum Method{
        GET,PUT,POST,DELETE;
    }

    public enum Endpoint{

        SEARCH("_search"),
        BATCH_SEARCH("_msearch"),
        SETTINGS("_settings"),
        MAPPING("_mapping"),
        UPDATE("_update"),
        BATCH_GET("_mget");
        private String name;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        private Endpoint(String name) {
            this.name = name;
        }


    }

}
