package com.assistdecision.core.base;


import java.io.Serializable;

/**
 * 基类
 * leeoohoo@gmail.com
 */
public interface GetService<OT,T,ID extends Serializable> {


    public abstract BaseService<OT,T,ID> getService();

}
