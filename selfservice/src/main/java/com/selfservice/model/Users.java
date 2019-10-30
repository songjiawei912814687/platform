package com.selfservice.model;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "users")
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usersGenerator")
    @SequenceGenerator(name = "usersGenerator", sequenceName = "usersNew_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;// ID自增长主键
    @Column(nullable = false, unique = true)
    private String username;// 用户名,不为空，不能重复

    @Column(nullable = false)
    private Integer employeeId;// 真实姓名,不为空

    @Column(nullable = false,length = 1)
    private Integer userType;

    @Column(nullable = false,length = 64)
    private String password;// 密码,不为空

    @Column(nullable = false,length = 1)
    private Integer organizationId;

    @Column(nullable = false,length = 1)
    private Integer administratorLevel;

    @Column(nullable = false,length = 1)
    private Integer isAccountNonExpired;//

    @Column(nullable = false,length = 1)
    private Integer isAccountNonLocked;//

    @Column(nullable = false,length = 1)
    private Integer isCredentialsNonExpired;//

    @Column(nullable = false,length = 1)
    private Integer isEnabled;//

    @Column(nullable = false)
    private Integer createdUserId;//

    @Column(nullable = false,length = 55)
    private String createdUserName;//

    @Column(nullable = false)
    private Date createdDateTime;//

    @Column(nullable = false)
    private Integer lastUpdateUserId;//

    @Column(nullable = false,length = 55)
    private String lastUpdateUserName;//

    @Column(nullable = false)
    private Date lastUpdateDateTime;//

    @Column(nullable = false,length = 1)
    private Integer membershipOrganizationId;


    @OneToMany(mappedBy = "userId",cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<UserRole> roles = new ArrayList<>();

    public Users() {
    }


    public Users(String userName, String password) {
        this.username = userName;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }



    public Integer getIsAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setIsAccountNonExpired(Integer isAccountNonExpired) {
        this.isAccountNonExpired = isAccountNonExpired;
    }

    public Integer getIsAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setIsAccountNonLocked(Integer isAccountNonLocked) {
        this.isAccountNonLocked = isAccountNonLocked;
    }

    public Integer getIsCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public void setIsCredentialsNonExpired(Integer isCredentialsNonExpired) {
        this.isCredentialsNonExpired = isCredentialsNonExpired;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getCreatedUserName() {
        return createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Integer getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(Integer lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    public String getLastUpdateUserName() {
        return lastUpdateUserName;
    }

    public void setLastUpdateUserName(String lastUpdateUserName) {
        this.lastUpdateUserName = lastUpdateUserName;
    }

    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }
//
    public Integer getAdministratorLevel() {
        return administratorLevel;
    }

    public void setAdministratorLevel(Integer administratorLevel) {
        this.administratorLevel = administratorLevel;
    }

    public Integer getMembershipOrganizationId() {
        return membershipOrganizationId;
    }

    public void setMembershipOrganizationId(Integer membershipOrganizationId) {
        this.membershipOrganizationId = membershipOrganizationId;
    }
}
