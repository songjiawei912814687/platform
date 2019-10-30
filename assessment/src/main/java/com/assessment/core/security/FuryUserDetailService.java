package com.assessment.core.security;

import com.assessment.config.RedisComponent;
import com.assessment.mapper.jpa.EmployeesRepository;
import com.assessment.mapper.mybatis.UsersMapper;
import com.assessment.model.Employees;
import com.assessment.model.UserRole;
import com.assessment.model.Users;
import com.assessment.service.UserRoleService;
import com.assessment.core.security.MyUsersDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Component
public class FuryUserDetailService implements UserDetailsService {
    /**
     * 依赖注入密码加密解密工具（PS: 需要在springsecurity的配置文件中配置这个Bean）
     */
    @Autowired
    private EmployeesRepository employeesRepository;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private UserRoleService userRoleService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("前端闯过来的用户名为：{}" + username);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        if (username == null || username.equals("")) {
            Cookie[] cks = request.getCookies();
            if (cks != null) {
                for (int i = 0; i < cks.length; i++) {
                    Cookie ck = cks[i];
                    if (username != null && !"".equals(username)) {
                        continue;
                    }
                    if (ck.getName().equals("tbdkso")) {
                        var account = redisComponent.get(ck.getValue());
                        var strs = account.split("-");
                        username = strs[0];
                    }
                }
            }
        }
        Users users = usersMapper.selectByUserName(username);
        if(users.getIsAccountNonLocked() == 1){
            users = null;
        }
        List<UserRole> roles = null;
        if (users != null) {
            roles = userRoleService.findByUserId(users.getId());
        }
        if(roles != null){
            users.setRoles(roles);
        }
        // 模拟数据库中的数据

        // 返回一个User对象（技巧01：这个User对象的密码是从数据库中取出来的密码）
//      // 技巧02：数据库中的密码是在创建用户时将用户的密码利用SpreingSecurity配置中相同的密码加密解密工具加密过的
        if(users.getUserType()==0){
            //个人账户
            Employees employeesById = employeesRepository.findEmployeesById(users.getEmployeeId());
            users.setOrganizationId(employeesById.getOrganizationId());
        }
        return new MyUsersDetails(users);
    }
}
