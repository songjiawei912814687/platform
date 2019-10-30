package com.knowledge.domain.output;

import org.apache.solr.client.solrj.beans.Field;

public class SolrQuestionOutput {
    @Field("que_id")
    private Integer queId;

    @Field("que_title")
    private String queTitle;

    @Field("que_description")
    private String queDescription;

    public Integer getQueId() {
        return queId;
    }

    public void setQueId(Integer queId) {
        this.queId = queId;
    }

    public String getQueTitle() {
        return queTitle;
    }

    public void setQueTitle(String queTitle) {
        this.queTitle = queTitle;
    }

    public String getQueDescription() {
        return queDescription;
    }

    public void setQueDescription(String queDescription) {
        this.queDescription = queDescription;
    }
}
