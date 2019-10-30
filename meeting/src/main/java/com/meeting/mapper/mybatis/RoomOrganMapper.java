package com.meeting.mapper.mybatis;

import com.common.model.PageData;
import com.meeting.core.base.MybatisBaseMapper;
import com.meeting.domain.output.OrganPermissterOutput;
import com.meeting.domain.output.RoomOrganOutput;
import com.meeting.model.RoomOrgan;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomOrganMapper extends MybatisBaseMapper<RoomOrganOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(RoomOrgan record);

    int insertSelective(RoomOrgan record);

    RoomOrganOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoomOrgan record);

    int updateByPrimaryKey(RoomOrgan record);

    List<OrganPermissterOutput> findOrganByuserId(PageData pageData);

    List<OrganPermissterOutput> findAllOrgan();
}
