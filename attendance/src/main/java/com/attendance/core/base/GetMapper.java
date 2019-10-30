package com.attendance.core.base;

import java.io.Serializable;

/**
 * 基类
 * leeoohoo@gmail.com
 */
public interface GetMapper<OT,T,ID extends Serializable> {

    public abstract BaseMapper<T,ID> getMapper();

    public abstract MybatisBaseMapper<OT> getMybatisBaseMapper();

}
