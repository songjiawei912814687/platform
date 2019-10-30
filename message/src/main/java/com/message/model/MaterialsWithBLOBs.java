package com.message.model;

public class MaterialsWithBLOBs extends Materials {
    private String bak;

    private String detailRequirement;

    private String necessityDesc;

    public String getBak() {
        return bak;
    }

    public void setBak(String bak) {
        this.bak = bak == null ? null : bak.trim();
    }

    public String getDetailRequirement() {
        return detailRequirement;
    }

    public void setDetailRequirement(String detailRequirement) {
        this.detailRequirement = detailRequirement == null ? null : detailRequirement.trim();
    }

    public String getNecessityDesc() {
        return necessityDesc;
    }

    public void setNecessityDesc(String necessityDesc) {
        this.necessityDesc = necessityDesc == null ? null : necessityDesc.trim();
    }
}