package com.api.domain.output;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-29  10:13
 * @modified by:
 */
public class GetBookableGroupsOutput {
    //主键
    private Integer id;

    private String deptCode;
    //窗口编号
    private String code;
    //窗口名称也可以认为是业务名称
    private String name;

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

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
