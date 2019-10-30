package com.stamp.core.base;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.DateUtils;
import com.common.utils.Valid;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.beans.IntrospectionException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 基类
 * leeoohoo@gmail.com
 */
public abstract class BaseController<OT,T, ID extends Serializable> implements GetService<OT,T, ID> {


    private final Logger logger = LoggerFactory.getLogger(BaseController.class);

    public static final String PARAM_EORRO = "参数错误";

    public static final String SYS_EORRO = "后台错误";



    private Date getDate(String str)  {
        try {
            if(str.length() == 10){
                return DateUtils.stringToDate(str);
            }
            if(str.length() == 18){
                return DateUtils.stringToDates(str);
            }
        }catch (ParseException e){
            throw new RuntimeException("搜索条件错误");
        }
        throw new RuntimeException("搜索条件错误");
    }

    private Integer getNumber(String str) {
        Integer l = null;
        if (Valid.isNumeric(str)) {
            l = Integer.parseInt(str);
        } else {
            throw new RuntimeException("搜索条件错误");
        }
        if (l == null) {
            throw new RuntimeException("搜索条件错误");
        }
        return l;
    }


    @Transactional(rollbackFor = Exception.class)
    public ResponseResult formPost(Integer id, @RequestBody T t) throws Exception {
        if (t == null) {
            return ResponseResult.error(PARAM_EORRO);
        }
        int result = 0;

        if (id == null) {
            result = getService().add(t);
        } else {
            result = getService().update(id, t);
        }

        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success(result);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseResult delete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        if (idList == null || idList.length() <= 0) {
            return ResponseResult.error(PARAM_EORRO);
        }
        int result = getService().deleteById(idList);
        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }


        return ResponseResult.success();
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseResult logicDelete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException ,MethodArgumentNotValidException{
        if (idList == null || idList.length() <= 0) {
            return ResponseResult.error(PARAM_EORRO);
        }
        int result = getService().logicDelete(idList);
        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }

        return ResponseResult.success();
    }

    public ResponseResult get(ID id) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        if (id == null) {
            return ResponseResult.error(PARAM_EORRO);
        }
        T t = getService().getById(id);
        if (t == null) {
            return ResponseResult.success();
        }
        return ResponseResult.success(t);
    }

    public ResponseResult selectPageList(PageData pageData){
        try {
            for (Map.Entry<String, String> entry : pageData.getMap().entrySet()) {
                if (entry.getValue() == null || "".equals(entry.getValue())) {
                    System.out.println(entry.getKey());
                    pageData.getMap().remove(entry.getKey());
                }
            }
        }catch (Exception e){

        }
        return ResponseResult.success(new PageInfo<>(getService().selectAll(true,pageData)));
    }

    public ResponseResult selectAll(PageData pageData){
        return ResponseResult.success(getService().selectAll(false,pageData));
    }

    public ResponseResult selectById(Integer id){
        if( id==null ){
            ResponseResult.error(PARAM_EORRO);
        }
        return ResponseResult.success(getService().selectById(id));
    }
}
