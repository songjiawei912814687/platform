package com.personnel.domian.input;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author: Young
 * @description:
 * @date: Created in 22:32 2018/9/11
 * @modified by:
 */
@Data
public class formDataInput implements Serializable {

    private String vocationType;
    private Date vocationStart;
    private Date vocationEnd;
    private Double duration;
    //请假说明
    private String desc;
    //图片
    private String image;

    private List<String> processInstanceIdList;

    private List<String> ccUserIdList;

    public formDataInput() {
    }
}
