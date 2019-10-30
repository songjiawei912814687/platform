package com.screen.mapper.jpa;

import com.screen.model.FeedbackInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-15  23:07
 * @modified by:
 */
public interface FeedbackInfoRepository  extends JpaRepository<FeedbackInfo,Integer> {
    List<FeedbackInfo> findAllByResourceId(Integer resourceId);

}
