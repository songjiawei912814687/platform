package com.personnel.mapper.mybatis;

import com.common.model.PageData;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.model.CityCard;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityCardMapper extends MybatisBaseMapper<CityCard>{
    int deleteByPrimaryKey(Integer id);

    int insert(CityCard record);

    int insertSelective(CityCard record);
    @Override
    CityCard selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CityCard record);

    int updateByPrimaryKey(CityCard record);

    List<CityCard> selectByityCardNo(String cityCardNo);

    List<CityCard> selectByState(Integer state);

    List<CityCard> selectByPersonId(Integer personId);
}