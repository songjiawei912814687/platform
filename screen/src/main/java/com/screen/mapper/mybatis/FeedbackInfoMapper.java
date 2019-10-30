package com.screen.mapper.mybatis;

import com.common.model.PageData;
import com.screen.core.base.MybatisBaseMapper;
import com.screen.domain.output.FeedbackInfoOutput;
import com.screen.domain.output.HotDoOutput;
import com.screen.model.FeedbackInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackInfoMapper extends MybatisBaseMapper<FeedbackInfoOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(FeedbackInfo record);

    int insertSelective(FeedbackInfo record);

    @Override
    FeedbackInfoOutput selectByPrimaryKey(Integer id);

    //
    String selectPathById(int id);

    int updateByPrimaryKeySelective(FeedbackInfo record);

    int updateByPrimaryKey(FeedbackInfo record);

    /**
     * 根据反馈时间查询有多少条反馈信息
     * @param feedbackTime
     * @return
     */
    Integer selectByFeedbackTime(String feedbackTime);

    /**
     * 查询回复时间为空,2天内的数据
     * @return
     */

    /**查看热门事项办件量*/
    List<HotDoOutput> selectHotDo();


    List<FeedbackInfoOutput> selectByAppraiseTime();


    //窗口满意率统计--------------------------------------------------
    int selectSatisfactoryNumbers();
    int selectUnsatisfactoryNumbers();
    int selectRunOneTimes();
    int selectRunManyTimes();
    int selectSum();


    //根据窗口号查询出反馈信息查询出跑一次
    Integer selectByWindowNoAndDate(@Param("windowNo") String windowNo, @Param("date") String date);

    //根据满意数
   Integer selectByWindowNoAndDate1(@Param("windowNo") String windowNo, @Param("date") String date);

   int selectCount(@Param("windowNo") String windowNo, @Param("date") String date);

    //满意数据
    Integer selectDissatisfaction(Integer organizationId);

    //不满意详情
    List<FeedbackInfoOutput> selectUnstatis(Integer organizationId);

    //根据组织机构id查询已经评价的反馈信息
    Integer selectByOrganizationId(Integer organizationId);

    //查询出今日办件
    List<FeedbackInfoOutput> selectTodayByOrganizationId(Integer organizationId);

    //根据组织和窗口号查询出反馈信息
    List<FeedbackInfoOutput> selectByWindowNoAndOrgNo(@Param("windowNo") String windowNo,
                                                      @Param("organizationNo") String organizationNo);
    //根据窗口号查询出办件量
    Integer selectQueCount(String windowNo);


    /**
     *  查询出当月办件量
     */
    Integer selectByThisMonth(PageData pageData);

    /**
     * 查询当月办件
     * @return
     */
    List<FeedbackInfoOutput>selectThisDay();

}