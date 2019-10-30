package com.personnel.mapper.jpa;

import com.personnel.model.FoodOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-01-07  21:04
 * @modified by:
 */
public interface FoodOrganizationRepository extends JpaRepository<FoodOrganization,Integer> {

    /**
     *  根据下发状态查询
     * @param state
     * @return
     */
    List<FoodOrganization> findAllByState(Integer state);

    /**
     * 根据组织编号查询餐盘组织
     * @param deptCode
     * @return
     */
    List<FoodOrganization> findAllBySDepCodeOrderByLastUpdateDateTimeDesc(String deptCode);
}
