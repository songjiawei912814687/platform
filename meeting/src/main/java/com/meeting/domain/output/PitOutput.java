package com.meeting.domain.output;

import com.meeting.model.Pit;

import java.util.List;

public class PitOutput extends Pit{
    private String typeName;


    public String getTypeName() {
        if(getType()!=null){
            switch (getType()){
                case 0:
                    typeName="开标室";
                    break;
                case 1:
                    typeName="评标室";
                    break;
            }
        }
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    private List<PitApplyOutput> pitApplyOutputList;

    public List<PitApplyOutput> getPitApplyOutputList() {
        return pitApplyOutputList;
    }

    public void setPitApplyOutputList(List<PitApplyOutput> pitApplyOutputList) {
        this.pitApplyOutputList = pitApplyOutputList;
    }
}
