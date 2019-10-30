package com.api.mapper.jpa;

import com.api.model.Suggestions;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-15  17:41
 * @modified by:
 */
public interface SuggestionsRepository extends JpaRepository<Suggestions,Integer> {
}
