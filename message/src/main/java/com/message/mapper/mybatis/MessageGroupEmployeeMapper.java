package com.message.mapper.mybatis;

import com.common.model.PageData;
import com.message.core.base.MybatisBaseMapper;
import com.message.domain.output.EmployeesOutput;
import com.message.domain.output.MessageGroupEmployeeOutput;
import com.message.domain.output.MessageGroupOutput;
import com.message.model.MessageGroupEmployee;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageGroupEmployeeMapper extends MybatisBaseMapper<MessageGroupEmployeeOutput> {

    int deleteByPrimaryKey(Integer id);

    int insert(MessageGroupEmployee record);

    int insertSelective(MessageGroupEmployee record);

    MessageGroupEmployee selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MessageGroupEmployee record);

    int updateByPrimaryKey(MessageGroupEmployee record);

//    MessageGroupEmployeeOutput selectByName(String employeeName);

    //
    EmployeesOutput selectByEmployeesId(Integer id);

    /***
     * 根据员工id和小组ID查询是否存在于小组成员表中
     * @param empId 员工id
     * @param messageGroupId 小组ID
     * @return
     */
    int selectCountByEmpIdAndGroupId(@Param("empId") Integer empId,@Param("messageGroupId") Integer messageGroupId);

    MessageGroupEmployee selectEmployees();

   int deleteBygroupId(Integer groupId);

    List<MessageGroupEmployeeOutput> selectByGroupId(Integer groupId);

    List<MessageGroupEmployeeOutput> selectByPageData(PageData pageData);
    //
    int deleteByGroupId(Integer groupId);


    MessageGroupEmployee selectByEmpId(@Param("groupId") Integer groupId,@Param("empId") Integer empId);
}