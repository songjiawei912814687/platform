package com.attendance.mapper.mybatis;

import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.AttendancePunch;
import com.attendance.domian.output.LeaveApplicationOutput;
import com.attendance.model.LeaveApplication;
import com.common.model.PageData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LeaveApplicationMapper extends MybatisBaseMapper<LeaveApplicationOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(LeaveApplication record);

    int insertSelective(LeaveApplication record);

    int updateByPrimaryKeySelective(LeaveApplication record);

    int updateByPrimaryKey(LeaveApplication record);

    /**
     * 根据ID查询出审核状态
     */
    Integer selectStatusById(Integer id);

    /**
     * 根据id和状态查询是否补录
     * @param id
     * @param status
     * @return
     */
    Integer selectIsCollectionByIdAndStatus(@Param("id") Integer id, @Param("status") Integer status);

    /**
     * 根据审核状态查询对象
     * @param status
     * @return
     */
    List<LeaveApplicationOutput> selectListByStatus(Integer status);


    /**
     * 根据id，补录的状态和审核通过的状态去查看补录信息
     * @param id 主键id
     * @param isCollection 是补录状态
     * @param status 是审核通过状态
     * @return
     */
    LeaveApplicationOutput selectIdAndIsCollectionByIdAndStatus(@Param("id") Integer id,
                                                                @Param("isCollection")Integer isCollection,
                                                                @Param("status") Integer status);

    List<AttendancePunch> notPunch(Date workTime, Date workOutTime);

    List<AttendancePunch> latestWorkingTime(PageData pageData);

    List<AttendancePunch> firstOffWorkTime(PageData pageData);

    List<LeaveApplicationOutput> findLeaveDetailByEmployeeIdAndDate(PageData pageData);

    List<LeaveApplicationOutput> absence(PageData pageData);

    List<LeaveApplicationOutput> temporaryAbsence(PageData pageData);

    //请假记录 开始时间当天工作开始时间前，结束时间在当天工作开始时间之后
    List<LeaveApplicationOutput> findByEmployeeId(PageData pageData);

    //请假结束时间直到今天的请假记录
    List<LeaveApplicationOutput> findTodayRecord(PageData pageData);

    /**
     * 根据条件查询是否有请假记录
     * @param empId 员工 id
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param startTime 开始时间
     * @param endTime 结束日期
     * @param statusList 状态列表
     * @return
     */
    Integer selectCountByEmpAndDate(@Param("empId") Integer empId,
                                    @Param("startDate") Date startDate,
                                    @Param("endDate") Date endDate,
                                    @Param("startTime") String startTime,
                                    @Param("endTime") String endTime,
                                    @Param("statusList") List<Integer>statusList);

    List<LeaveApplicationOutput> selectAllToday(PageData pageData);

    List<LeaveApplicationOutput> getLeaveDetailByEmployeeNoAndDate(PageData pageData);

    /**拉取微信端的请假数据*/
    List<LeaveApplicationOutput> addLeaveWechat();

    /**向微信端跟新审批结果*/
    void updateApprovalResult(@Param("leaveWeChatId") Integer leaveWeChatId,@Param("status") Integer status);

    /**向微信端跟新同步*/
    void updateIsSync(Integer leaveWeChatId);
}
