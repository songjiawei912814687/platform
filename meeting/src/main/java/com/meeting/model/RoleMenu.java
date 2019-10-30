package com.meeting.model;


import javax.persistence.*;

@Entity
@Table(name = "role_menu")
public class RoleMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roleMenuGenerator")
    @SequenceGenerator(name = "roleMenuGenerator", sequenceName = "roleMenuNew_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @Column(nullable = false)
    private Integer roleId;

    @Column(nullable = false)
    private Integer menuId;

    @OneToOne
    @JoinColumn(name="id", referencedColumnName="menuId")
    private Menu menu;







    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
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

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}
