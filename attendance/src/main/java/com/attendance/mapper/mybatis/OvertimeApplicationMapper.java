package com.attendance.mapper.mybatis;


import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.EmployeesCount;
import com.attendance.domian.output.OvertimeApplicationOutput;
import com.attendance.model.OvertimeApplication;
import com.common.model.PageData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OvertimeApplicationMapper extends MybatisBaseMapper<OvertimeApplicationOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(OvertimeApplication record);

    int insertSelective(OvertimeApplication record);

    @Override
    OvertimeApplicationOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OvertimeApplication record);

    int updateByPrimaryKey(OvertimeApplication record);

    Integer selectStatusById(Integer id);

    //查询所有的审核通过的加班信息
    List<OvertimeApplicationOutput> selectAllByCondition(@Param("organizationName") String organizationName,
                                                         @Param("employeesName") String employeesName,
                                                         @Param("bankCardNumber")String bankCardNumber);

    //根据加班主键查询出加班信息且需未核销
    OvertimeApplicationOutput selectOverTimeApplicationByOverTimeId(Integer overTimeId);

    //根据人名查询出加班次数
    Integer selectOverTimeCountByName(String employeesName);

    //查询加班申请的审核状态
    Integer selectStatusByEmployeesIdAndOrganizationId(@Param("employeesId") Integer employeesId,
                                                       @Param("organizationId") Integer organizationId);

    //查询出已经审核通过的加班记录
    List<OvertimeApplicationOutput> selectOvertimeApplication(Integer status);


    //根据人员ID和组织ID查询出所有审核通过以及未核销的加班记录
    List<OvertimeApplicationOutput> selectAllByOrganizationAndEmployees(PageData pageData);


    //根据人员姓名和组织名称查询出所有上月加班数据
    List<OvertimeApplicationOutput> selectOverTime(@Param("organizationId") Integer organizationId,
                                                   @Param("employeesId") Integer employeesId,
                                                   @Param("reportDate") String reportDate);

    List<EmployeesCount> selectOverTimeCount(PageData pageData);

    List<OvertimeApplicationOutput> selectByReport(PageData pageData);

    /**
     * 查看当天已经通过审批的加班记录
     * @param pageData
     * @return
     */
    List<OvertimeApplicationOutput> selectAllByToday(PageData pageData);

//    List<OvertimeApplicationOutput> selectReport(PageData pageData);
}
