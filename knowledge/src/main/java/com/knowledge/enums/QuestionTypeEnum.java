package com.knowledge.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author: young
 * @description:
 * @date: Created in 2019-08-14  17:01
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum QuestionTypeEnum {

    PERSONNEL("个人办事类",25),
    COMPANY("企业开办类",24),
    PROJECT("项目审批类",23),
    OTHERS("其他类",155),

    ;

    private String desc;
    private Integer code;

    public static Integer getCode(String desc){
        if(desc!=null){
            for(var e:values()){
                if(desc == e.getDesc()){
                    return e.getCode();
                }
            }
        }
        return 155;
    }
}
