package com.xps.es.restclient;

import com.xps.es.restclient.impl.ESRestClientImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by xiongps on 2017/8/17.
 */
public class ESRestClientTest {

    private static Log log = LogFactory.getLog(ESRestClientTest.class);

    private ESRestClient restClient;

    @Before
    public void loadFileBefore(){
        log.info("测试前执行");

        InputStream is =this.getClass().getClassLoader().getResourceAsStream("es-rest.properties");
        Properties properties = null;
        try {
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RestClientConfig config = new RestClientConfig(properties);
        restClient = new ESRestClientImpl(config);
    }

    //@Before
    public void before(){
        log.info("测试前执行");
        RestClientConfig config = new RestClientConfig();

        List<String> hostList = new ArrayList<>();
        hostList.add("127.0.0.1:9200,http");
        config.setHttpHostList(hostList);

        config.setMaxRetryTimeoutMillis(6000);
        config.setConnectTimeout(6000);
        config.setSocketTimeout(6000);
        config.setIoThreadCount(1);

        List<String> headerList = new ArrayList<>();
        headerList.add("sysId=9892");
        config.setHeaderList(headerList);
        List<Map<String,String>> headerMapList = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        map.put("sysNm","local");
        headerMapList.add(map);
        config.setHeaderMapList(headerMapList);

        restClient = new ESRestClientImpl(config);
    }

    @After
    public void after(){
        log.info("测试后执行");
        ((ESRestClientImpl)restClient).destroy();
        restClient = null;
    }

    @Test
    public void restClientTest(){
        log.info("开始测试了");

        try {
            String json = "{\"query\":{\"match_all\":{}}}";

            json ="{\"query\":{\"ids\":{\"type\":\"tweet\",\"values\":[1,2]}}}";
            long time = System.currentTimeMillis();
            HttpEntity entity = new NStringEntity(json,ContentType.APPLICATION_JSON.withCharset("UTF-8"));
            log.info("请求报文："+ json);
            Response response = restClient.performRequest(ESRConst.Method.POST.name(),"/twitter/_search?pretty",entity);
            log.info((System.currentTimeMillis()-time)+"返1回的结果："+ EntityUtils.toString(response.getEntity()));
            response = restClient.performRequest(ESRConst.Method.POST.name(),"/twitter/_search?pretty",entity);
            log.info((System.currentTimeMillis()-time)+"返2回的结果："+ EntityUtils.toString(response.getEntity()));
            response = restClient.performRequest(ESRConst.Method.POST.name(),"/twitter/_search?pretty",entity);
            log.info((System.currentTimeMillis()-time)+"返3回的结果："+ EntityUtils.toString(response.getEntity()));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
