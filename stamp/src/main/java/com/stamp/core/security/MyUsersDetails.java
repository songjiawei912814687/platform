package com.stamp.core.security;

import com.stamp.model.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: young
 * @project_name: platform
 * @description:
 * @date: Created in 2019-04-12  22:29
 * @modified by:
 */
public class MyUsersDetails extends Users implements UserDetails {


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        List<UserRole> userRoleList = this.getRoles();
        List<String> codes = new ArrayList<>();

        for(UserRole userRole:userRoleList){
            List<RoleMenu> menuList = userRole.getMenus();
            List<RoleAction> roleActionList = userRole.getActions();
            if(menuList.size()>0||menuList!=null){
                codes.addAll(menuList.stream().map(RoleMenu::getMenu)
                .filter(Menu->Menu!=null&&(Menu.getCode()!=null||!"".equals(Menu.getCode())))
                        .collect(Collectors.toList())
                        .stream().map(Menu::getCode).collect(Collectors.toList()));
            }

            if(roleActionList.size()>0||roleActionList!=null){
                codes.addAll(roleActionList.stream().map(RoleAction::getAction)
                .filter(Action->Action!=null&&Action.getCode()!=null)
                        .collect(Collectors.toList())
                        .stream().map(Action::getCode).collect(Collectors.toList()));
            }
        }

        for(String code:codes){
            authorityList.add(new SimpleGrantedAuthority(code));
        }
        return authorityList;
    }

    public MyUsersDetails(){

    }

    public MyUsersDetails(Users users){
        if(users!=null){
            this.setId(users.getId());
            this.setUsername(users.getUsername());
            this.setPassword(users.getPassword());
            this.setIsAccountNonExpired(users.getIsAccountNonExpired());
            this.setIsAccountNonLocked(users.getIsAccountNonLocked());
            this.setIsCredentialsNonExpired(users.getIsCredentialsNonExpired());
            this.setIsEnabled(users.getIsEnabled());
            this.setRoles(users.getRoles());
            this.setAdministratorLevel(users.getAdministratorLevel());
            this.setUserType(users.getUserType());
            this.setEmployeeId(users.getEmployeeId());
            this.setOrganizationId(users.getOrganizationId());
        }
    }

    @Override
    public void setUsername(String userName) {
        super.setUsername(userName);
    }


    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public void setPassword(String password) {
        super.setPassword(password);
    }

    @Override
    public boolean isAccountNonExpired() {
        return super.getIsAccountNonExpired()==0;
    }

    @Override
    public boolean isAccountNonLocked() {
        return super.getIsAccountNonLocked()==0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return super.getIsCredentialsNonExpired()==0;
    }

    @Override
    public boolean isEnabled() {
        return super.getIsEnabled()==0;
    }
}
