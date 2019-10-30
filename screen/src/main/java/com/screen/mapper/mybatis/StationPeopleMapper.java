package com.screen.mapper.mybatis;

import com.common.model.PageData;
import com.screen.domain.output.StationPeopleOutput;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationPeopleMapper {

    StationPeopleOutput selectByPrimaryKey(Integer resourceId);

    int updateByResultId(PageData pageData);

    StationPeopleOutput selectByPhone(String phone);

    StationPeopleOutput selectByResourceId(Integer resourceId);

    /***
     * 根据状态和完成时间统计出办件量
     * @param status
     * @param completeDate
     * @return
     */
    List<StationPeopleOutput> selectByStatus(@Param("status") Integer status, @Param("completeDate") String completeDate);


    List<StationPeopleOutput> selectByStatusAndTime(@Param("status") Integer status, @Param("takeNumberDate") String takeNumberDate);

    //10分钟之内的当前大厅等待人数
    List<StationPeopleOutput> selectByTakeTime(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**查询出组织集合*/
    List<String> selectDeptName();


    //查询出取号数
//    List<StationPeopleOutput> selectByTake (@Param("takeDate")String takeDate);

    Integer selectByTake(String takeDate);

    //查询出办结数
//    List<StationPeopleOutput> selectByComp(@Param("compDate")String compDate);

    Integer selectByComp(String compDate);
}
