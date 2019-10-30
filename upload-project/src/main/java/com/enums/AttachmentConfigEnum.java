package com.enums;

import lombok.Getter;

/**
 * @author: Young
 * @description:
 * @date: Created in 21:02 2018/9/6
 * @modified by:
 */
@Getter
public enum AttachmentConfigEnum {

    DOC(10, "文档"),
    TXT(20, "记事本"),
    PPT(30, "ppt文件"),
    HTM(40, "网页文件"),
    XLS(50, "EXCEL文件"),
    SWF(60, "flash动画"),
    JPEG(70, "JPEG图片"),
    JPG(80, "JPG图片"),
    PNG(90, "PNG图片"),
    GIF(100, "GIF图片"),
    CAN_USE(1, "可用"),
    UN_USE(3, "无效"),


    ;
    private Integer code;

    private String desc;

    AttachmentConfigEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
