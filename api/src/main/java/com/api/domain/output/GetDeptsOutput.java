package com.api.domain.output;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-29  09:40
 * @modified by:
 */
public class GetDeptsOutput {

    private Integer id;
    //部门编号
    private String code;
    //部门名称
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
