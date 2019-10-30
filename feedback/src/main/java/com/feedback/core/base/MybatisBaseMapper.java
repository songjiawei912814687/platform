package com.feedback.core.base;

import com.common.model.PageData;
import com.github.pagehelper.Page;

import java.util.List;

public interface MybatisBaseMapper<OT> {

    public abstract OT selectByPrimaryKey(Integer id);

    public abstract List<OT> selectAll(PageData t);

    public abstract Page<OT> selectPage(PageData t);
}
