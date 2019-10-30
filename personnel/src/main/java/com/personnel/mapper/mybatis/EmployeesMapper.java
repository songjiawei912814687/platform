package com.personnel.mapper.mybatis;

import com.common.model.PageData;
import com.github.pagehelper.Page;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.EmployeesOutput;
import com.personnel.model.Employees;
import com.personnel.model.Organization;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeesMapper extends MybatisBaseMapper<EmployeesOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(Employees record);

    int insertSelective(Employees record);

    @Override
    EmployeesOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Employees record);

    int updateByPrimaryKey(Employees record);

    int selectCountName(String name);

    Integer selectEmployeeIdByEmployeeName(String name);

     Page<EmployeesOutput> selectByPath(PageData t);

    String  selectMaxNo(PageData t);

    List<EmployeesOutput> selectAllAndOrgPath(PageData pageData);

    List<EmployeesOutput> selectByIdCard(PageData pageData);

    List<EmployeesOutput> selectByWindowId(Integer id);

    List<EmployeesOutput> selectByPlateNo(PageData pageData);

    EmployeesOutput selectByCitizenCards(String citizenCards);

    List<EmployeesOutput> getByOrganCode(Integer organId);

    List<EmployeesOutput> selectByWindowState(Integer windowState);

    Integer selectUserIdByEmpId(Integer empId);

    /***
     * 下发所有餐盘人员
     * @return
     */
    List<EmployeesOutput> selectAllFoodEmp();

    Page<EmployeesOutput> selectPageListWithinAuthority(PageData pageData);

    int updateOrgId(PageData pageData);

    Page<EmployeesOutput> selectByOrgId(PageData pageData);

    List<EmployeesOutput> selectByPhoneNumber(PageData pageData);

    List<EmployeesOutput> selectByIdCards(PageData pageData);

    List<EmployeesOutput> findNoAccount();

    /**
     * 查询新登人员信息
     * @return
     */
    List<EmployeesOutput> selectXindeng();

    List<EmployeesOutput> selectCK();
}
