package com.attendance.mapper.mybatis;

import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.EmployeesCount;
import com.attendance.domian.output.OffApplicationOutput;
import com.attendance.model.OffApplication;
import com.common.model.PageData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffApplicationMapper extends MybatisBaseMapper<OffApplicationOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(OffApplication record);

    int insertSelective(OffApplication record);

    @Override
    OffApplicationOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OffApplication record);

    int updateByPrimaryKey(OffApplication record);

    Integer selectStatusById(Integer id);

    //根据名字查询出调休次数
    Integer selectRestCountByEmployeesName(String name);


    //根据人员id和组织id查询出上月调休列表
    List<OffApplicationOutput> selectOffTime(@Param("organizationId") Integer organizationId,
                                             @Param("employeesId") Integer employeesId,
                                             @Param("reportDate") String reportDate);

    List<EmployeesCount> selectRestCount(PageData pageData);

    /**
     * 查看当天所有的通过申请调休记录
     * @param pageData
     * @return
     */
    List<OffApplicationOutput> selectAllByToday(PageData pageData);
}
