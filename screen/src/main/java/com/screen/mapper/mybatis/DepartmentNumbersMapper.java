package com.screen.mapper.mybatis;


import com.screen.core.base.MybatisBaseMapper;
import com.screen.domain.output.DepartmentNumbersOutput;
import com.screen.model.DepartmentNumbers;
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
