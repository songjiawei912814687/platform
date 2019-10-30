package com.attendance.core.security;

import com.attendance.mapper.jpa.UserRoleRepository;
import com.attendance.model.*;
import com.attendance.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MyUsersDetails extends Users implements UserDetails {



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<UserRole> roles = this.getRoles();
        List<String> codes = new ArrayList<>();

        for (UserRole userRole : roles) {
            List<RoleMenu> menuList = userRole.getMenus();
            List<RoleAction> actionList = userRole.getActions();
            if (menuList != null || menuList.size() > 0) {
                codes.addAll(menuList.stream().map(RoleMenu::getMenu)
                        .filter(Menu -> Menu != null && Menu.getCode() != null & !"".equals(Menu.getCode()))
                        .collect(toList())
                        .stream().map(Menu::getCode).collect(toList()));
            }
            if (actionList != null || actionList.size() > 0) {
                codes.addAll(actionList.stream().map(RoleAction::getAction)
                        .filter(Action -> Action != null && Action.getCode() != null & !"".equals(Action.getCode()))
                        .collect(toList())
                        .stream().map(Action::getCode).collect(toList()));
            }
        }
        for (String code : codes) {
            authorities.add(new SimpleGrantedAuthority(code));
        }
        return authorities;
    }

    public MyUsersDetails(Users users) {
        if (users != null) {
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

    public MyUsersDetails() {
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
        if (super.getIsAccountNonExpired() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (super.getIsAccountNonLocked() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        if (super.getIsCredentialsNonExpired() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isEnabled() {
        if (super.getIsEnabled() == 0) {
            return true;
        }
        return false;
    }
}
