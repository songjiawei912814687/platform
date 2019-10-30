package com.personnel.mapper.jpa;

import com.personnel.model.FoodEmployees;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-01-06  03:33
 * @modified by:
 */
public interface FoodEmployeesRepository extends JpaRepository<FoodEmployees,Integer> {

    //根据下发状态和删除状态
    List<FoodEmployees> findAllByStateAndAmputated(Integer status,Integer amputated);

    //根据sID查询餐盘人员
    List<FoodEmployees> findAllBySIDAndAmputatedOrderByLastUpdateDateTimeDesc(String sId,Integer amputated);
}
