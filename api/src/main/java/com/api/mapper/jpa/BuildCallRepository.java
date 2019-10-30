package com.api.mapper.jpa;

import com.api.model.BuildCall;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-07  15:51
 * @modified by:
 */
public interface BuildCallRepository extends JpaRepository<BuildCall,String> {
}
