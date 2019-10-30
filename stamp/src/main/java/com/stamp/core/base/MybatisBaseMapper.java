package com.stamp.core.base;

import com.common.model.PageData;
import com.github.pagehelper.Page;

import java.util.List;

public interface MybatisBaseMapper<OT> {

    OT selectByPrimaryKey(Integer id);

    List<OT> selectAll(PageData t);

    Page<OT> selectPage(PageData t);
}
