package com.personnel.mapper.jpa;

import com.personnel.core.base.BaseMapper;
import com.personnel.model.Person;
import org.springframework.stereotype.Repository;

/**
 * @author: XiGuoQing
 * @description:
 * @date: Created in 下午 8:37 2018/10/18 0018
 * @modified by:
 */
@Repository
public interface PersonRepository extends BaseMapper<Person,Integer> {
    Person findByEmployeeId(Integer id);
}
