package com.api.mapper.jpa;

import com.api.model.SmallProjectTrans;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: young
 * @project_name: svn
 * @description: 获取小额工程交易公告接口设计
 * @date: Created in 2018-12-07  17:45
 * @modified by:
 */
public interface SmallProjectTransRepository extends JpaRepository<SmallProjectTrans,String> {


}
