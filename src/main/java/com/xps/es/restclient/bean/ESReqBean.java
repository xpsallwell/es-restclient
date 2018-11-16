package com.xps.es.restclient.bean;

import java.io.Serializable;

/**
 * Created by xiongps on 2017/8/18.
 */
public class ESReqBean implements Serializable{

    private  static final long serialVersionUID = 8291132L;

    private String fileName;

    private String nameSpace;

    private String reqId;

    private String content;

    public ESReqBean(){}

    public ESReqBean(String fileName, String nameSpace, String reqId, String content) {
        this.fileName = fileName;
        this.reqId = reqId;
        this.nameSpace = nameSpace;
        this.content = content;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
