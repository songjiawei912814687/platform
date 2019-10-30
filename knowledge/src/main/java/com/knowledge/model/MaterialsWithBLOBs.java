package com.knowledge.model;

import com.knowledge.domain.output.MaterialsOutput;
import lombok.Getter;

@Getter
public class MaterialsWithBLOBs extends MaterialsOutput {
    private String bak;

    private String detailRequirement;

    private String necessityDesc;

    @Override
    public void setBak(String bak) {
        this.bak = bak == null ? null : bak.trim();
    }

    @Override
    public void setDetailRequirement(String detailRequirement) {
        this.detailRequirement = detailRequirement == null ? null : detailRequirement.trim();
    }

    @Override
    public void setNecessityDesc(String necessityDesc) {
        this.necessityDesc = necessityDesc == null ? null : necessityDesc.trim();
    }
}
