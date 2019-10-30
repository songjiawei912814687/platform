package com.personnel.service;

import com.personnel.core.base.BaseMapper;
import com.personnel.core.base.BaseService;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.input.EmployeesInput;
import com.personnel.domian.output.JobChangeOutput;
import com.personnel.domian.output.RoleOutput;
import com.personnel.mapper.jpa.EmployeesRepository;
import com.personnel.mapper.jpa.JobChangeRepository;
import com.personnel.mapper.jpa.JobsRepository;
import com.personnel.mapper.jpa.OrganizationRepository;
import com.personnel.mapper.mybatis.JobChangeMapper;
import com.personnel.mapper.mybatis.RoleMapper;
import com.personnel.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Service
public class JobChangeService extends BaseService<JobChangeOutput, JobChange,Integer> {

    @Autowired
    private JobChangeRepository jobChangeRepository;

    @Autowired
    private JobsRepository jobsRepository;


    @Autowired
    private EmployeesService employeesService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private EmployeesRepository employeesRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JobChangeMapper jobChangeMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public BaseMapper<JobChange, Integer> getMapper() {
        return jobChangeRepository;
    }

    @Override
    public MybatisBaseMapper<JobChangeOutput> getMybatisBaseMapper() {
        return jobChangeMapper;
    }

    /*public List<JobChange> getByName(String employeeNo){
        return jobChangeRepository.findByName(employeeNo);
    }*/

    public int add(JobChange jobChange) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        jobChange.setOriginalJobName(jobsRepository.getById(jobChange.getOriginalJobId()).getName());
        jobChange.setNowJobName(jobsRepository.getById(jobChange.getNowJobId()).getName());
        jobChange.setOriginalOrganizationName(organizationRepository.getById(jobChange.getOriginalOrganizationId()).getName());
        jobChange.setNowOrganizationName(organizationRepository.getById(jobChange.getNowOrganizationId()).getName());
        Employees byId = employeesRepository.findById(jobChange.getEmployeeId()).get();
        jobChange.setEmployeeId(byId.getId());
        jobChange.setEmployeeNo(byId.getEmployeeNo());
        jobChange.setEmployeeName(byId.getName());
        return super.add(jobChange);
    }

    public int update(Integer id,JobChange jobChange) throws IllegalAccessException, IntrospectionException, InvocationTargetException,MethodArgumentNotValidException{
        jobChange.setOriginalJobName(jobsRepository.getById(jobChange.getOriginalJobId()).getName());
        jobChange.setNowJobName(jobsRepository.getById(jobChange.getNowJobId()).getName());
        jobChange.setOriginalOrganizationName(organizationRepository.getById(jobChange.getOriginalOrganizationId()).getName());
        jobChange.setNowOrganizationName(organizationRepository.getById(jobChange.getNowOrganizationId()).getName());
        Employees byId = employeesRepository.findById(jobChange.getEmployeeId()).get();
        jobChange.setEmployeeId(byId.getId());
        jobChange.setEmployeeNo(byId.getEmployeeNo());
        jobChange.setEmployeeName(byId.getName());
        return  super.update(id,jobChange);
    }

    @Transactional
    public int updateState(EmployeesInput employeesInput) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        JobChange jobChange=getById(employeesInput.getId());
        jobChange.setState(employeesInput.getState());
        if(super.update(jobChange.getId(),jobChange)>0){
            if(employeesInput.getState()==1){
                Employees employees=new Employees();
                employees.setId(jobChange.getEmployeeId());
                employees.setJobsId(jobChange.getNowJobId());
                employees.setOrganizationId(jobChange.getNowOrganizationId());
                if(employeesService.update(jobChange.getEmployeeId(),employees)<=0){
                    return ERROR;
                }
                Users users=userService.selectByEmployeeId(jobChange.getEmployeeId());
                Users users1=new Users();
                users1.setOrganizationId(jobChange.getNowOrganizationId());
                users1.setId(users.getId());
                if(userService.update(users1.getId(),users1)<=0){
                    return ERROR;
                }
                List<UserRole> userRoles=userRoleService.findByUserId(users.getId());
                List<RoleOutput> outputs=roleMapper.selectByDefaultPermissions();
                if(userRoles!=null&&userRoles.size()>0){
                    for(UserRole userRole:userRoles){
                        if(outputs!=null&&outputs.size()>0){
                            boolean a=true;
                            //判断是否是默认权限
                            for(RoleOutput output:outputs){
                                if(output.getId().equals(userRole.getRoleId())){
                                    a=false;
                                }
                            }
                            if(a){
                                if(userRoleService.deleteByUserIdANdRoleId(userRole.getUserId(),userRole.getRoleId())<=0){
                                    return ERROR;
                                }
                            }
                        }else {
                            if(userRoleService.deleteByUserIdANdRoleId(userRole.getUserId(),userRole.getRoleId())<=0){
                                return ERROR;
                            }
                        }

                    }
                }

            }
        }
        return SUCCESS;
    }


    public List<JobChangeOutput> selectByEmployeeId(Integer id) {
        return jobChangeMapper.selectByEmployeeId(id);
    }
}
