package com.knowledge.domain.output;

import org.apache.solr.client.solrj.beans.Field;

public class SolrQltOutput {
    @Field("id")
    private String id;

    @Field("qlt_id")
    private String qlt_id;

    @Field("ql_name")
    private String ql_name;

    @Field("content_involve")
    private String contentInvolve;

    @Field("applicable_object")
    private String applicableObject;

    @Field("acp_institution")
    private String acpInstitution;

    @Field("dec_institution")
    private String decInstution;

    @Field("lead_dept")
    private String leadDept;

    @Field("que_id")
    private Long queId;

    @Field("que_title")
    private String queTitle;

    @Field("que_description")
    private String queDescription;

    @Field("type")
    private Long type;



    public String getQlt_id() {
        return qlt_id;
    }

    public void setQlt_id(String qlt_id) {
        this.qlt_id = qlt_id;
    }

    public String getQl_name() {
        return ql_name;
    }

    public void setQl_name(String ql_name) {
        this.ql_name = ql_name;
    }

    public String getContentInvolve() {
        return contentInvolve;
    }

    public void setContentInvolve(String contentInvolve) {
        this.contentInvolve = contentInvolve;
    }

    public String getApplicableObject() {
        return applicableObject;
    }

    public void setApplicableObject(String applicableObject) {
        this.applicableObject = applicableObject;
    }

    public String getAcpInstitution() {
        return acpInstitution;
    }

    public void setAcpInstitution(String acpInstitution) {
        this.acpInstitution = acpInstitution;
    }

    public String getDecInstution() {
        return decInstution;
    }

    public void setDecInstution(String decInstution) {
        this.decInstution = decInstution;
    }

    public String getLeadDept() {
        return leadDept;
    }

    public void setLeadDept(String leadDept) {
        this.leadDept = leadDept;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public Long getQueId() {
        return queId;
    }

    public void setQueId(Long queId) {
        this.queId = queId;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
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
