package com.personnel.mapper.mybatis;

import com.common.model.PageData;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.AddressBookOutput;
import com.personnel.model.AddressBook;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressBookMapper extends MybatisBaseMapper<AddressBookOutput>{
    int deleteByPrimaryKey(Integer id);

    int insert(AddressBook record);

    int insertSelective(AddressBook record);

    @Override
    AddressBookOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AddressBook record);

    int updateByPrimaryKey(AddressBook record);

    AddressBookOutput selectByPlateNo(PageData pageData);
}