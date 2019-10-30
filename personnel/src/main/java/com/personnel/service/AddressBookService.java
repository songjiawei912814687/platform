package com.personnel.service;

import com.common.model.PageData;
import com.personnel.core.base.BaseMapper;
import com.personnel.core.base.BaseService;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.AddressBookOutput;
import com.personnel.mapper.jpa.AddressBookRepository;
import com.personnel.mapper.mybatis.AddressBookMapper;
import com.personnel.model.AddressBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressBookService extends BaseService<AddressBookOutput,AddressBook,Integer>{

    @Autowired
    private AddressBookRepository repository;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    public BaseMapper<AddressBook, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<AddressBookOutput> getMybatisBaseMapper() {
        return addressBookMapper;
    }

    public AddressBookOutput getByPlateNo(PageData pageData){
        return addressBookMapper.selectByPlateNo(pageData);
    }

    public AddressBookOutput getByAddressBookId(Integer id){
        AddressBookOutput addressBookOutput=addressBookMapper.selectByPrimaryKey(id);
        if(addressBookOutput.getPlateNo()!=null&&!addressBookOutput.getPlateNo().equals("")){
            String[] s=addressBookOutput.getPlateNo().split(",");
            List<String> list=new ArrayList<String>();
            if(s!=null&&s.length>0){
                for(String s1:s){
                    list.add(s1);
                }
            }
            addressBookOutput.setPlateNoList(list);
        }
        return addressBookOutput;
    }

}
