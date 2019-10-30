package com.assessment.mapper.jpa;

import com.assessment.core.base.BaseMapper;
import com.assessment.model.Attachment;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends BaseMapper<Attachment,Integer> {
}
