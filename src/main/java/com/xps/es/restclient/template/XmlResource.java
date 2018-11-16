package com.xps.es.restclient.template;

import com.xps.es.restclient.bean.ESReqBean;
import com.xps.es.restclient.exceptions.ESRestClientException;
import com.xps.es.restclient.exceptions.RestClientExceptionComp;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by xiongps on 2017/8/18.
 */
public abstract class XmlResource {

    private Map<String,ESReqBean> jsonMap = new HashMap<>();

    private Resource[] resources;

    public XmlResource(Resource[] resources){
        this.resources = resources;
        parseDocumentToBean();
    }

    public ESReqBean getESReqBean(String reqId){
        ESReqBean bean = jsonMap.get(reqId);
        if(bean ==null || bean.getContent() ==null || "".equals(bean.getContent())) {
            throw new ESRestClientException(RestClientExceptionComp.NOT_EMPTY,"JSON must be not null");
        }
        return bean;
    }

    private void parseDocumentToBean(){
        if(this.resources == null || resources.length == 0) {
            throw new ESRestClientException(RestClientExceptionComp.NOT_EMPTY,"resources must be not null");
        }

        Map<String, Document> docMap = readToDocuments(this.resources);
        for(Map.Entry<String,Document> entry:docMap.entrySet()) {
            String fileName = entry.getKey();
            Document doc = entry.getValue();
            Element rootElement = doc.getRootElement();
            String nameSpace = rootElement.attributeValue("namespace");
            List sqlElements = rootElement.elements();
            Iterator i$ = sqlElements.iterator();

            while(i$.hasNext()) {
                Element element = (Element)i$.next();
                String id = element.attributeValue("id");
                String json = element.getTextTrim();
                jsonMap.put(nameSpace+"."+id,new ESReqBean(fileName,nameSpace,id,json));
            }
        }
    }


    private Map<String, Document> readToDocuments(Resource[] resources) {
        HashMap documents = new HashMap();
        if(resources != null && resources.length > 0) {
            SAXReader saxReader = new SAXReader();
            Resource[] arr$ = resources;
            int len$ = resources.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Resource resource = arr$[i$];

                try {
                    String e = resource.getFilename();
                    InputStream reader = resource.getInputStream();
                    Document doc = saxReader.read(reader);
                    documents.put(e, doc);
                } catch (Exception var11) {
                    throw new ESRestClientException(RestClientExceptionComp.LOAD_CONFIG_FAIL,"readToDocuments");
                }
            }
        }

        return documents;
    }

    public synchronized Resource[] getResources() {
        return resources;
    }

    public synchronized void setResources(Resource[] resources) {
        this.resources = resources;
    }

}
