package com.stamp.core.base;

import java.io.Serializable;

/**
 * 基类
 * leeoohoo@gmail.com
 */
public interface GetMapper<OT,T,ID extends Serializable> {

    BaseRepository<T,ID> getMapper();

    MybatisBaseMapper<OT> getMybatisBaseMapper();

}
