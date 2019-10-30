package com.selfservice.domain.output;


import com.common.Enum.MaterialFormEnum;
import com.selfservice.model.MaterialList;


public class MaterialListOutput extends MaterialList {
    private String materialFormName;

    public String getMaterialFormName() {
        StringBuilder stringBuilder = new StringBuilder("");
        if (getMaterialForm() != null) {
            String materialForm = getMaterialForm();
            String[] split = materialForm.split(",");
            if(split.length>0){
                for (String str:split) {
                    if(str.equals(MaterialFormEnum.ORIGINAL.getCode().toString())){
                        stringBuilder.append(MaterialFormEnum.ORIGINAL.getMsg()+",");
                    }else if(str.equals(MaterialFormEnum.COPY.getCode().toString())){
                        stringBuilder.append(MaterialFormEnum.COPY.getMsg()+",");
                    }else if(str.equals(MaterialFormEnum.ORIGINALOrCOPY.getCode().toString())){
                        stringBuilder.append(MaterialFormEnum.ORIGINALOrCOPY.getMsg()+",");
                    }else if(str.equals(MaterialFormEnum.PAPER.getCode().toString())){
                        stringBuilder.append(MaterialFormEnum.PAPER.getMsg()+",");
                    }else if(str.equals(MaterialFormEnum.ELEFILE.getCode().toString())){
                        stringBuilder.append(MaterialFormEnum.ELEFILE.getMsg()+",");
                    }else if(str.equals(MaterialFormEnum.SYS.getCode().toString())){
                        stringBuilder.append(MaterialFormEnum.SYS.getMsg()+",");
                    }else if(str.equals(MaterialFormEnum.PERSON.getCode().toString())){
                        stringBuilder.append(MaterialFormEnum.PERSON.getMsg()+",");
                    }
                }
            }
        }
        if(stringBuilder.length()==0){
            return stringBuilder.toString();
        }
        return stringBuilder.substring(0,stringBuilder.length()-1);
    }
}
