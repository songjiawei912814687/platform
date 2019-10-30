package com.personnel.service;

import com.common.model.PageData;
import com.common.utils.DateUtils;
import com.personnel.core.base.BaseMapper;
import com.personnel.core.base.BaseService;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.ActionOutput;
import com.personnel.domian.output.DepartmentNumbersOutput;
import com.personnel.domian.output.EmployeesOutput;
import com.personnel.domian.output.OrganizationOutput;
import com.personnel.mapper.jpa.ActionRepository;
import com.personnel.mapper.jpa.DepartmentNumbersRepository;
import com.personnel.mapper.mybatis.ActionMapper;
import com.personnel.mapper.mybatis.DepartmentNumbersMapper;
import com.personnel.mapper.mybatis.EmployeesMapper;
import com.personnel.mapper.mybatis.OrganizationMapper;
import com.personnel.model.Action;
import com.personnel.model.DepartmentNumbers;
import com.personnel.model.Organization;
import com.personnel.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service

public class DepartmentNumbersService extends BaseService<DepartmentNumbersOutput, DepartmentNumbers,Integer> {

    @Autowired
    private DepartmentNumbersMapper departmentNumbersMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private EmployeesMapper employeesMapper;

    @Autowired
    private DepartmentNumbersRepository repository;

    @Override
    public BaseMapper<DepartmentNumbers, Integer> getMapper() {
        return repository;
    }

    public MybatisBaseMapper<DepartmentNumbersOutput> getMybatisBaseMapper() {
        return departmentNumbersMapper;
    }


    //计算部门下的员工数量
    public int setDepartmentNumbers(){
        //1、获得所有组织
        List<Organization> organization = organizationMapper.selectAllOrg();
        //2、获得所有员工并同时获得员工的组织信息
            //2.1 <=10入职的计入 >10入职的不计入
            //2.2 <20离职的不计入 >=20入职的计入
        PageData pageData = new PageData();
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH,-1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        instance.set(Calendar.DATE,11);
        Date inTime = instance.getTime();
        instance.set(Calendar.DATE,21);
        Date leaveTime = instance.getTime();
        inTime = DateUtils.reMoveExceptDate(inTime);
        leaveTime = DateUtils.reMoveExceptDate(leaveTime);
        String format = formatter.format(inTime);
        String format1 = formatter.format(leaveTime);
        pageData.put("inTime",format);
        pageData.put("leaveTime",format1);
        List<EmployeesOutput> employeesOutputs = employeesMapper.selectAllAndOrgPath(pageData);
//        int year = 2018;
        int year = instance.get(Calendar.YEAR);
//        int month = 12;
        int month = instance.get(Calendar.MONTH)+1;
        //先删除
        departmentNumbersMapper.deleteByDate(year+"-"+month);
        List<DepartmentNumbers> departmentNumbers = new ArrayList<>();
        //3核算组织下员工的数量
        for (Organization org :organization) {
            Integer count = 0;
            String orgId = org.getId()+",";
            for (EmployeesOutput employeesOutput:employeesOutputs) {
                if((employeesOutput.getPath()+",").indexOf(orgId)>=0){
                    count++;
                }
            }
            DepartmentNumbers departmentNumber = new DepartmentNumbers();
            departmentNumber.setCreatedDateTime(new Date());
            departmentNumber.setCreatedDateTime(new Date());
            departmentNumber.setLastUpdateDateTime(new Date());
            departmentNumber.setCreatedUserName("job");
            departmentNumber.setLastUpdateUserName("job");

            departmentNumber.setName(org.getName());
            departmentNumber.setYearMonth(year+"-"+month);
            departmentNumber.setDepartId(org.getId());
            departmentNumber.setPersonNum(new BigDecimal(count));
            departmentNumbers.add(departmentNumber);
        }
        //保存数据
        List<DepartmentNumbers> departmentNumbers1 = repository.saveAll(departmentNumbers);
        if(departmentNumbers1==null||departmentNumbers1.size()==0){
            return -1;
        }else{
            return 1;
        }
    }

}
