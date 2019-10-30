package com.personnel.service;

import com.personnel.core.base.BaseMapper;
import com.personnel.core.base.BaseService;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.EmployeesOutput;
import com.personnel.domian.output.UsersOutput;
import com.personnel.mapper.jpa.RoleRepository;
import com.personnel.mapper.jpa.UserRepository;
import com.personnel.mapper.mybatis.EmployeesMapper;
import com.personnel.mapper.mybatis.UsersMapper;
import com.personnel.model.UserRole;
import com.personnel.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


@Service
public class UserService extends BaseService<UsersOutput, Users,Integer> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private EmployeesMapper employeesMapper;

    @Override
    public BaseMapper<Users,Integer> getMapper() {
        return userRepository;
    }

    @Override
    public MybatisBaseMapper<UsersOutput> getMybatisBaseMapper() {
        return usersMapper;
    }


    public Users getByUserName(String name){
        Users users = new Users();
//        Sort sort = new Sort(Sort.Direction.ASC, "id");
//        Pageable p = new PageRequest(1,2,sort);
        return userRepository.findByUsername(name);
    }

    public UsersOutput selectByEmployeeId(Integer employeeId) {
        return usersMapper.selectByEmployeeId(employeeId);
    }

    @Async
    public void setDefaultRole(Integer userId) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        var roles = roleRepository.findAllByDefaultPermissions(1);
        if(roles == null || roles.size() <= 0){
            return;
        }
        for(var r : roles){
            var userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(r.getId());
            userRoleService.add(userRole);
        }

    }


    public void addAccount() throws Exception{
        //找到没有账号的员工
        List<EmployeesOutput> list = employeesMapper.findNoAccount();
        for (EmployeesOutput em:list
             ) {
            var user = new Users();
            user.setEmployeeId(em.getId());
            user.setUserType(0);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setIsEnabled(0);
            user.setIsCredentialsNonExpired(0);
            user.setIsAccountNonLocked(0);
            user.setIsAccountNonExpired(0);
            user.setUsername(em.getEmployeeNo());
            user.setAdministratorLevel(1);
            user.setOrganizationId(em.getOrganizationId());
            var userId = this.add(user);
            if (userId > 0) {
                this.setDefaultRole(userId);
            }
        }

    }
}
