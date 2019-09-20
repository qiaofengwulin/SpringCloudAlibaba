package com.qh.entity;

public class QhConfig {
    private Integer id;

    private Integer hosId;

    private String hosKey;

    private String hosValue;

    private String content;

    private String mchId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHosId() {
        return hosId;
    }

    public void setHosId(Integer hosId) {
        this.hosId = hosId;
    }

    public String getHosKey() {
        return hosKey;
    }

    public void setHosKey(String hosKey) {
        this.hosKey = hosKey;
    }

    public String getHosValue() {
        return hosValue;
    }

    public void setHosValue(String hosValue) {
        this.hosValue = hosValue;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }
}