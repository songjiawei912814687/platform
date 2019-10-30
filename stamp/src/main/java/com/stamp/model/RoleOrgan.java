package com.stamp.model;

import javax.persistence.*;

@Entity
@Table(name = "role_organ")
public class RoleOrgan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roleOrganGenerator")
    @SequenceGenerator(name = "roleOrganGenerator", sequenceName = "roleOrgan_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @Column(nullable = false)
    private Integer roleId;

    @Column(nullable = false)
    private Integer organId;

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

    public Integer getOrganId() {
        return organId;
    }

    public void setOrganId(Integer organId) {
        this.organId = organId;
    }
}
