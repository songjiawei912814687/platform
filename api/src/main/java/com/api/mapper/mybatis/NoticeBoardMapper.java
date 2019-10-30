package com.api.mapper.mybatis;

import com.api.model.NoticeBoard;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeBoardMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NoticeBoard record);

    int insertSelective(NoticeBoard record);

    NoticeBoard selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NoticeBoard record);

    int updateByPrimaryKey(NoticeBoard record);

    List<NoticeBoard> selectAll(PageData pageData);
}