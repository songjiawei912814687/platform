package com.assessment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "UserRole")
@Table(name = "user_role")
public class UserRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userRoleGenerator")
    @SequenceGenerator(name = "userRoleGenerator", sequenceName = "userRoleNew_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @Column(nullable = false)
    private Integer roleId;

    @Column(nullable = false)
    private Integer userId;



    @OneToMany(mappedBy = "roleId")
    private List<RoleAction> actions;

    @OneToMany(mappedBy = "roleId")
    private List<RoleMenu> menus;

    public List<RoleAction> getActions() {
        return actions;
    }

    public void setActions(List<RoleAction> actions) {
        this.actions = actions;
    }

    public List<RoleMenu> getMenus() {
        return menus;
    }

    public void setMenus(List<RoleMenu> menus) {
        this.menus = menus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", userId=" + userId +
                '}';
    }
}
