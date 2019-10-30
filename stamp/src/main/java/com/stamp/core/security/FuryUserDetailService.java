package com.stamp.core.security;

import com.stamp.config.RedisComponent;
import com.stamp.mapper.jpa.EmployeesRepository;
import com.stamp.mapper.mybatis.UsersMapper;
import com.stamp.model.Employees;
import com.stamp.model.UserRole;
import com.stamp.model.Users;
import com.stamp.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: young
 * @project_name: platform
 * @description:
 * @date: Created in 2019-04-12  13:05
 * @modified by:
 */
@Component
@Slf4j
public class FuryUserDetailService  implements UserDetailsService {

    @Autowired
    private EmployeesRepository employeesRepository;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private RedisComponent redisComponent;
    @Autowired
    private UserRoleService userRoleService;

    private static final Integer USER_TYPE = 0;
    private static final Integer ORGAN_TYPE = 1;
    private static final String COOKIE_NAME = "tbdkso";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("读取出的用户名是:{}",username);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(username == null||username == ""){
            //获取cookie
            Cookie[]cookies = request.getCookies();
            if(cookies!=null){
                for(int i=0;i<cookies.length;i++){
                    Cookie cookie = cookies[i];
                    if(COOKIE_NAME.equals(cookie.getName())){
                        String account = redisComponent.get(cookie.getValue());
                        String[]strs = account.split("-");
                        username = strs[0];
                    }
                }
            }
        }

        Users users = usersMapper.selectByUserName(username);
        if(1==users.getIsAccountNonLocked()){
            users = null;
        }
        List<UserRole> userRoleList = new ArrayList<>();
        if(users!=null){
            userRoleList = userRoleService.findByUserId(users.getId());
        }
        if(userRoleList.size()>0||userRoleList!=null){
            users.setRoles(userRoleList);
        }

        if(USER_TYPE.equals(users.getUserType())){
            Employees employeesById = employeesRepository.findEmployeesByIdAndAmputated(users.getEmployeeId(),0);
            users.setOrganizationId(employeesById.getOrganizationId());
        }
        return new MyUsersDetails(users);
    }
}

