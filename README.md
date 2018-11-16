
# es-restclient
对elasticsearch-rest-client二次开发工具类，新增加freemarker的支持。可以方便的结合spring使用。

配置示例：
 ES搜索引擎配置 
 #elasticsearch
elasticsearch.hosts=172.16.103.13:9200;172.16.103.16:9200
elasticsearch.connectTimeout=6000

    <bean id="restClientConfig" class="com.xps.es.restclient.RestClientConfig" >
         <constructor-arg index="0">
             <props>
                 <prop key="es-hosts">${elasticsearch.hosts}</prop>
                 <prop key="es-connectTimeout">${elasticsearch.connectTimeout}</prop>
             </props>
         </constructor-arg>
        <!--
        <property name="httpHostList">
            <list>
                <value>172.16.103.13:9200</value>
                <value>172.16.103.16:9200</value>
            </list>
        </property>
        <property name="connectTimeout" value="6000"></property>-->
    </bean>
    <bean id="restClient" class="com.xps.es.restclient.impl.ESRestClientImpl">
        <constructor-arg index="0" ref="restClientConfig"></constructor-arg>
    </bean>

    <bean id="reqJsonTemplate" class="com.xps.es.restclient.template.ReqJsonTemplateImpl">
        <constructor-arg index="0" value="classpath*:es-template/es-*.xml"></constructor-arg>
    </bean>

 @Autowired
    private ESRestClient restClient;
    @Autowired
    private ReqJsonTemplate reqJsonTemplate;

    //在对应的配置文件中配置相应的业务json
     HttpEntity entity = reqJsonTemplate.getJsonNStringEntity("es-config.queryConfigClassifyList",tempParams);
   //发送请求
            Response response = restClient.performRequest(ESRConst.Method.POST.name(),
                    SearchConst.ES_SEARCH,entity);
                    //获取返回的结果
            String ret = EntityUtils.toString(response.getEntity());
            //对结果进行json解析
             SearchResultHits<SearchConfigClassifyInfo> resultHits
                    = SearchUtil.parseSearchHitsResult(ret,SearchConfigClassifyInfo.class,null);


package com.yaoyaohao.haoyl.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yaoyaohao.haoyl.bean.ExtraParams;
import com.yaoyaohao.haoyl.bean.SearchResultHits;
import com.yaoyaohao.haoyl.search.protocol.SearchGoodsResultGoodsInfo;
import com.yyh.framework.data.IData;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xiongps on 2017/12/4.
 */
public class SearchUtil {

    public static <T> SearchResultHits<T> parseSearchHitsResult(String ret, Class<T> clazz,ExtraParams extra) {
        JSONObject json = JSON.parseObject(ret);
        int sortDisPos = 0;
        if(extra != null) {
            sortDisPos = extra.getSortDistancePos();
        }

        SearchResultHits<T> retObj = new SearchResultHits();

        if(json.containsKey("aggregations")) {
            retObj.setAggregations(json.getJSONObject("aggregations"));
        }
        String []hitsNameValues = null;
        if(extra != null) {
            String [] extraKeys = extra.getExtraJsonKeys();
            if(extraKeys != null && extraKeys.length > 0) {
                JSONObject extraJson = new JSONObject();
                int hitsSrart = 0;
                for(String ky:extraKeys) {
                    if(ky.startsWith("hits.")) {
                        hitsSrart++;
                        continue;
                    }
                    if(ky.contains(".")) {
                        JSONObject tmpJson = new JSONObject(new HashMap<>(json));
                        String []kyArr = ky.split(".");
                        for(String ka:kyArr) {
                            if(tmpJson.containsKey(ka)) {
                                tmpJson = tmpJson.getJSONObject(ka);
                            }
                        }
                        extraJson.put(ky,tmpJson);
                    } else {
                        extraJson.put(ky,json.getJSONObject(ky));
                    }

                }
                if(extraJson.size() > 0) {
                    retObj.setExtraJSON(extraJson);
                }
                if(hitsSrart > 0) {
                    hitsNameValues = new String[hitsSrart];
                    int _idx = 0;
                    for(String ky:extraKeys) {
                        if (ky.startsWith("hits.")) {
                            hitsNameValues[_idx++] = ky.substring(5);
                        }
                    }
                }

            }
        }

        json = json.getJSONObject("hits");

        int total = json.getIntValue("total");
        JSONArray arr = json.getJSONArray("hits");
        List<T> retList = new ArrayList<>();
        boolean isHasSource;
        for(int i=0;i<arr.size();i++) {
            JSONObject jsonObject = arr.getJSONObject(i);
            if(jsonObject.containsKey("_source")) {
                isHasSource = true;
            } else {
                isHasSource = false;
            }
            if(jsonObject.containsKey("sort")) {
                JSONArray ar = jsonObject.getJSONArray("sort");
                if(ar != null && ar.size() > 0) {
                    String sortStr = ar.getString(sortDisPos);
                    Double d = null;
                    try{
                        d = Double.parseDouble(sortStr);
                    }catch ( NumberFormatException e) {}
                    if(isHasSource && d != null) {
                        jsonObject.getJSONObject("_source").put("distance",d);
                    }
                }
            }
            if(jsonObject.containsKey("highlight")) {
                JSONObject hltObj = jsonObject.getJSONObject("highlight");
                Iterator<String> it = hltObj.keySet().iterator();
                while (it.hasNext()) {
                    String k = it.next();
                    JSONArray ar = hltObj.getJSONArray(k);
                    if(ar != null && ar.size() > 0) {
                        if(isHasSource) {
                            jsonObject.getJSONObject("_source").put("highlight_"+k,ar.getString(0));
                        }
                    }

                }
            }
            if(hitsNameValues != null && hitsNameValues.length > 0) {
                for(String nv:hitsNameValues) {
                    if(nv.contains("-->")) {
                        String []nvArr = nv.split("-->");
                        if(nvArr.length == 2) {
                            String []attrPair = nvArr[1].split(",");
                            if(nvArr[0].contains(".")) {
                                String []nv2Arr = nvArr[0].split("\\.");
                                JSONObject tempJSON = new JSONObject(new HashMap<>(jsonObject));
                                for(String nv2:nv2Arr) {
                                    if(tempJSON == null){break;}
                                    if(nv2.contains("|")) {
                                        String []nv3 = nv2.split("\\|");
                                        if("Array".equals(nv3[1])
                                                && tempJSON.getJSONArray(nv3[0]).size() > 0) {
                                            tempJSON = tempJSON.getJSONArray(nv3[0]).getJSONObject(0);
                                        }
                                    } else {
                                        tempJSON = tempJSON.getJSONObject(nv2);
                                    }
                                }
                                for(String attr:attrPair) {
                                    String []attrArr = attr.split("-");
                                    if(isHasSource && tempJSON != null) {
                                        jsonObject.getJSONObject("_source").put(attrArr[0],tempJSON.get(attrArr[1]));
                                    }
                                }
                            }
                        }
                    }
                }
            }


            if(isHasSource) {
                retList.add(jsonObject.getObject("_source",clazz));
            }

        }

        retObj.setTotal(total);
        retObj.setList(retList);
        return retObj;
    }

    public static <T> SearchResultHits<T> parseSearchAggsResult(String ret, Class<T> clazz) {
        JSONObject json = JSON.parseObject(ret);
        if(!json.containsKey("aggregations")) {
            return null;
        }
        json = json.getJSONObject("aggregations");
        Iterator<String> it = json.keySet().iterator();
        while (it.hasNext()) {
            String aggsKey = it.next();
            JSONObject aggsJson = json.getJSONObject(aggsKey);
            if(aggsJson.containsKey("doc_count")) {
                Integer doc_count = aggsJson.getInteger("doc_count");
            }


        }

        return null;
    }
}


