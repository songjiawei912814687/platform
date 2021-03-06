package com.api.mapper.mybatis;

import com.api.core.base.MybatisBaseMapper;
import com.api.domain.output.WindowOutput;
import com.api.model.Window;
import com.common.model.PageData;
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

    int selectCount();

    List<WindowOutput> selectByOrganizationId(PageData t);

    List<WindowOutput> selectByOrganCode(@Param("list") List<String> list);

    //根据窗口号查询出窗口对象
    WindowOutput selectByWindowNo(@Param("windowNo") String windowNo,@Param("orgId") Integer orgId);


    WindowOutput selectByWindowName(String windowName);
}
