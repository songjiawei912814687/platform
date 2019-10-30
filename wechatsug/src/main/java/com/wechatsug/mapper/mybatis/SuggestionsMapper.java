package com.wechatsug.mapper.mybatis;

import com.common.model.PageData;
import com.wechatsug.domain.output.SuggesstionsOutput;
import com.wechatsug.model.Suggestions;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestionsMapper  {
    int deleteByPrimaryKey(Integer id);

    int insert(Suggestions record);

    int insertSelective(Suggestions record);

    int updateByPrimaryKeySelective(Suggestions record);

    int updateByPrimaryKey(Suggestions record);

    SuggesstionsOutput selectByPrimaryKey(Integer id);

    List<SuggesstionsOutput> selectByMainInfo(Suggestions suggestions);


    SuggesstionsOutput getPhoneNumber(Integer orgaId);

    Page<SuggesstionsOutput> selectOutOfDatePageList(PageData pageData);

    SuggesstionsOutput getOutSuggesstionDetail(Integer id);

    List<SuggesstionsOutput> selectAllOutOfDatePageList(PageData pageData);

    List<SuggesstionsOutput> selectPage(PageData pageData);


    /**根据组织名称查询组织id*/
    Integer selectOrIdByOrName(String orName);

    /**根据窗口名称查询出窗口ID*/
    Integer selectByWindowName(@Param("orId") Integer orId,@Param("windowNo") String windowNo);


    /**根据员工工号查询出员工的id和name*/
    SuggesstionsOutput selectByEmpNo(String empNo);
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

    Integer selectDepartMana(Integer orgaId);
}
