package com.personnel.model;




import javax.persistence.*;

@Entity
@Table(name = "role_action")
public class RoleAction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roleActionGenerator")
    @SequenceGenerator(name = "roleActionGenerator", sequenceName = "roleActionNew_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @Column(nullable = false)
    private Integer roleId;

    @Column(nullable = false)
    private Integer actionId;

    @Column(nullable = false)
    private Integer menuId;

    @OneToOne
    @JoinColumn(name="id", referencedColumnName="actionId")
    private Action action;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
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

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}
