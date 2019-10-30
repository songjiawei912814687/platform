package com.stamp.mapper.jpa;

import com.stamp.core.base.BaseRepository;
import com.stamp.model.StampOrgan;

import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-04-18  10:08
 * @modified by:
 */
public interface StampOrganRepository extends BaseRepository<StampOrgan,Integer> {

    StampOrgan findByNameAndAmputated(String name,Integer amputated);

    List<StampOrgan> findByParentIdAndAmputated(Integer parentId, Integer amputated);

}
