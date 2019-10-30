package com.personnel.model;

import javax.persistence.*;

@Entity
@Table(name = "action")
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actionGenerator")
    @SequenceGenerator(name = "actionGenerator", sequenceName = "actionNew_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @Column(nullable = false)
    private Integer menuId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String code;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
