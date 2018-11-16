package com.xps.es.restclient.template;

import com.xps.es.restclient.ReqJsonTemplate;
import com.xps.es.restclient.bean.ESReqBean;
import com.xps.es.restclient.exceptions.ESRestClientException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiongps on 2017/8/18.
 */
public class ReqJsonTemplateImpl extends XmlResource implements ReqJsonTemplate {

    private static final String DEFAULT_TEMPLATE_KEY = "default_template_key";
    private static Map<String, Template> templateCache;
    private static Configuration configuration;

    public ReqJsonTemplateImpl(Resource []resources) {
        super(resources);
    }

    public HttpEntity getJsonNStringEntity(String reqId,Map<String, Object> params){
        HttpEntity entity = new NStringEntity(this.processTemplate(reqId,params),
                ContentType.APPLICATION_JSON);
        return entity;
    }

    public String processTemplate(String reqId,Map<String, Object> params) {
        StringReader reader = null;
        StringWriter out = null;
        Template template = null;

        String e;
        try {
            if(templateCache.get(reqId) != null) {
                template = templateCache.get(reqId);
            }
            if(template == null) {
                ESReqBean bean = super.getESReqBean(reqId);
                reader = new StringReader(bean.getContent());
                template = new Template(DEFAULT_TEMPLATE_KEY, reader, configuration);
                template.setNumberFormat("#");
                templateCache.put(reqId, template);
            }

            out = new StringWriter();
            template.process(params, out);
            e = out.toString();
        } catch (Exception var15) {
            var15.printStackTrace();
            throw new ESRestClientException("读取ES-template的配置文件出错误");
        } finally {
            try {
                if(reader != null) {
                    reader.close();
                }
                if(out != null) {
                    out.close();
                }
            } catch (IOException var14) {
                return null;
            }

        }
        return e;
    }

    static {
        configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setClassicCompatible(true);
        templateCache = new HashMap<>();
    }

}
