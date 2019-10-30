package com.message.model;

import javax.persistence.*;
import java.io.Serializable;


/**
 * @author: Young
 * @description:
 * @date: Created in 16:08 2018/10/22
 * @modified by:
 */

//用户回复的短信内容
@Entity
public class MoModels implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moModelsGenerator")
    @SequenceGenerator(name = "moModelsGenerator", sequenceName = "MoModels_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    private String mobile;

    private String smsContent;

    private String sendTime;

    private String addSerial;

    private Integer amputated;
    public MoModels() {
    }

    public Integer getAmputated() {
        return amputated;
    }

    public void setAmputated(Integer amputated) {
        this.amputated = amputated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getAddSerial() {
        return addSerial;
    }

    public void setAddSerial(String addSerial) {
        this.addSerial = addSerial;
    }
}
