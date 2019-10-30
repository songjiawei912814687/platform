package com.feedback.mapper.mybatis;

import com.common.model.PageData;
import com.feedback.core.base.MybatisBaseMapper;
import com.feedback.domain.output.FeedbackInfoOutput;
import com.feedback.model.FeedbackInfo;
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

    //跟新遗漏的没有评价的反馈信息
    void updateByAppState();

    //统计满意数
    Integer selectSatisfactionNumber(PageData pageData);
    Integer selectNotSatisfactionNumber(PageData pageData);
    //统计跑一次数
    Integer selectRunOnce(PageData pageData);
    Integer selectRunMany(PageData pageData);
    //总数
    Integer selectTotal(PageData pageData);
    // ----------
    List<Integer> selectOrg(PageData pageData);

    FeedbackInfoOutput selectBySuggessId(Integer suggessId);

    /**
     * 查询回复时间为空,2天内的数据
     * @return
     */
    List<FeedbackInfoOutput> selectByAppraiseTime();


    //窗口满意率统计--------------------------------------------------
    Integer selectSatisfactoryNumbers(PageData pageData);
    Integer selectUnsatisfactoryNumbers(PageData pageData);
    Integer selectRunOneTimes(PageData pageData);
    Integer selectRunManyTimes(PageData pageData);
    Integer selectSum(PageData pageData);
    List<FeedbackInfoOutput> selectCount(PageData pageData);
    List<FeedbackInfoOutput> selectSum1(PageData pageData);

    List<FeedbackInfoOutput> selectUnstatis(PageData pageData);
}