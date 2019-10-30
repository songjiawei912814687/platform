package com.wechatsug.mapper.mybatis;


import com.common.model.PageData;
import com.wechatsug.domain.output.WindowOutput;
import com.wechatsug.model.Window;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface WindowMapper  {
    int deleteByPrimaryKey(Integer id);

    int insert(Window record);

    int insertSelective(Window record);

    WindowOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Window record);

    int updateByPrimaryKey(Window record);

    List<WindowOutput> selectAll(PageData pageData);

    List<WindowOutput> selectPage(PageData pageData);

}
