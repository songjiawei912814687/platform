package com.message.mapper.mybatis;

import com.common.model.PageData;
import com.github.pagehelper.Page;
import com.message.core.base.MybatisBaseMapper;
import com.message.domain.output.SuggesstionsOutput;
import com.message.model.Suggestions;
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


    SuggesstionsOutput getPhoneNumber(Integer id);

    Page<SuggesstionsOutput> selectOutOfDatePageList(PageData pageData);

    SuggesstionsOutput getOutSuggesstionDetail(Integer id);

    List<SuggesstionsOutput> selectAllOutOfDatePageList(PageData pageData);

    /**
     * 查询未处理以及来源是短信的投诉建议
     */
    List<SuggesstionsOutput> selectByResourceAndDelState();

    /**
     * 根据组织id查询部门管理员
     */
    SuggesstionsOutput selectctByOrganId(Integer organId);


}