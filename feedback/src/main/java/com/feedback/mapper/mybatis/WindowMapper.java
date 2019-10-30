package com.feedback.mapper.mybatis;


import com.feedback.core.base.MybatisBaseMapper;
import com.feedback.domain.output.WindowOutput;
import com.feedback.model.Window;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WindowMapper extends MybatisBaseMapper<WindowOutput> {

    int deleteByPrimaryKey(Integer id);

    int insert(Window record);

    int insertSelective(Window record);

    @Override
    WindowOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Window record);

    int updateByPrimaryKey(Window record);

    List<Window> selectByWindowNo(String no);

    /**根据组织id和窗口号查询出窗口对象*/
    WindowOutput selectByOrAndWindowNo(@Param("orId") Integer orId, @Param("windowNo") String windowNo);

}
