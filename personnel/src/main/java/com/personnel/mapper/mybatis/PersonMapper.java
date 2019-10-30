package com.personnel.mapper.mybatis;

import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.PersonOutput;
import com.personnel.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PersonMapper extends MybatisBaseMapper<Person>{
    @Override
    Person selectByPrimaryKey(Integer id);

    List<Person> selectByState(Integer state);

    Person selectByEmployeeId(Integer employeeId);

    List<PersonOutput> selectByState1(Integer state);

}