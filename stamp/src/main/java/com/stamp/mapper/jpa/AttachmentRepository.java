package com.stamp.mapper.jpa;


import com.stamp.core.base.BaseRepository;
import com.stamp.model.Attachment;

import java.util.List;


public interface AttachmentRepository extends BaseRepository<Attachment,Integer> {

    Integer deleteAllByResourcesIdAndResourcesType(Integer resourceId,Integer type);

    List<Attachment> findAllByResourcesIdAndResourcesType(Integer resourceId,Integer type);
}
