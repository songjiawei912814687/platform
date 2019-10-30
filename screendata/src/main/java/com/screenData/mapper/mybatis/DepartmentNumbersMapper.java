package com.screenData.mapper.mybatis;


import com.screenData.core.base.MybatisBaseMapper;
import com.screenData.domain.output.DepartmentNumbersOutput;
import com.screenData.model.DepartmentNumbers;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentNumbersMapper extends MybatisBaseMapper<DepartmentNumbersOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(DepartmentNumbers record);

    int insertSelective(DepartmentNumbers record);

    DepartmentNumbers selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DepartmentNumbers record);

    int updateByPrimaryKey(DepartmentNumbers record);

    /**
     * 根据组织id和年月查询出对应的部门人数
     * @param id
     * @param yearMonth
     * @return
     */
    Integer selectNumberById(@Param("id") Integer id, @Param("yearMonth") String yearMonth);
}
