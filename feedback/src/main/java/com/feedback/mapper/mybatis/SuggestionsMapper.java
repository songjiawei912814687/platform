package com.feedback.mapper.mybatis;

import com.common.model.PageData;
import com.feedback.core.base.MybatisBaseMapper;
import com.feedback.domain.output.SuggesstionsOutput;
import com.feedback.model.Suggestions;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestionsMapper extends MybatisBaseMapper<SuggesstionsOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(Suggestions record);

    int insertSelective(Suggestions record);

    int updateByPrimaryKeySelective(Suggestions record);

    int updateByPrimaryKey(Suggestions record);

    List<SuggesstionsOutput> selectByMainInfo(Suggestions suggestions);

    /**找到首席代表*/
    List<SuggesstionsOutput> getPhoneNumber(Integer orgaId);

    Page<SuggesstionsOutput> selectOutOfDatePageList(PageData pageData);

    Page<SuggesstionsOutput> selectPage(PageData pageData);

    SuggesstionsOutput getOutSuggesstionDetail(Integer id);

    List<SuggesstionsOutput> selectAllOutOfDatePageList(PageData pageData);


    /**
     * 根据手机号码和添加回复状态查询出一条投诉建议
     * @param phone
     * @param
     * @return
     */
    SuggesstionsOutput selectByPhoneAndPersonName(@Param("phone") String phone,
                                                  @Param("replyType") Integer replyType,
                                                  @Param("resourceType") Integer resourceType);




    SuggesstionsOutput selectByFeedbackId(Integer feedbackId);


    /**
     * /查询199服务器上的数据
     * @return
     */
    List<SuggesstionsOutput> selectSyncData();
}
