package com.api.mapper.mybatis;

import com.api.domain.output.MeetingApply;
import com.common.model.PageData;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PitApplyMapper {

    Page<MeetingApply> selectPage(PageData pageData);

    List<MeetingApply> selectAll(PageData pageData);

    List<MeetingApply> selectNextMeeting(PageData pageData);
}