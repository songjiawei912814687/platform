package com.selfservice.domain.output;


import com.selfservice.model.MinimumParticle;

public class MinimumParticleOutput extends MinimumParticle {
    private String qlName;

    private boolean hashChild;

    private  Integer child;

    /**涉及内容*/
    private String contentInvolve;

    public String getQlName() {
        return qlName;
    }

    public void setQlName(String qlName) {
        this.qlName = qlName;
    }

    public String getContentInvolve() {
        return contentInvolve;
    }

    public void setContentInvolve(String contentInvolve) {
        this.contentInvolve = contentInvolve;
    }

    public Integer getChild() {
        return child;
    }

    public void setChild(Integer child) {
        this.child = child;
    }

    public boolean getHashChild() {
        if (getChild() != null) {
            if (getChild()>0) {
                hashChild = true;
            } else {
                hashChild = false;
            }
        }
        return hashChild;
    }

    public void setHashChild(boolean hashChild) {
        this.hashChild = hashChild;
    }
}
