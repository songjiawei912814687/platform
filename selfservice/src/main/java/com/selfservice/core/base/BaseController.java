package com.selfservice.core.base;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.DateUtils;
import com.common.utils.Valid;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        return ResponseResult.success();
    }

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

    /**
     * 条件分页
     * @param root
     * @param criteriaBuilder
     * @param pageData
     * @return
     */
    public Predicate[] gerWhere(Root root, CriteriaBuilder criteriaBuilder, PageData pageData) {
        List<Predicate> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : pageData.getMap().entrySet()) {
            String[] strs = entry.getKey().split("_");
            if (strs.length != 2) {
                continue;
            }
            switch (strs[1]) {
                case "like":
                    list.add(criteriaBuilder.like(root.get(strs[0]), "%" + entry.getValue() + "%"));
                    break;
                case "notLike":
                    list.add(criteriaBuilder.notLike(root.get(strs[0]), "%" + strs[1] + "%"));
                case "equal":
                    list.add(criteriaBuilder.equal(root.get(strs[0]), entry.getValue()));
                    break;
                case "notEqual":
                    list.add(criteriaBuilder.notEqual(root.get(strs[0]), entry.getValue()));
                    break;
                case "gt"://大于
                    list.add(criteriaBuilder.gt(root.get(strs[0]), getNumber(entry.getValue())));
                    break;
                case "between":
                    String[] strs1 = entry.getValue().split("_");
                    if(strs1.length!=2){
                        throw new RuntimeException("搜索条件错误");
                    }
                    if((Valid.isValidDate(strs1[0]) || Valid.isValidDate2(strs1[0])) && (Valid.isValidDate(strs1[1]) || Valid.isValidDate2(strs1[0]))){
                        Date start = getDate(strs1[0]);
                        Date end = new Date(getDate(strs1[1]).getTime()+60L*60*24*1000);
                        if(start.getTime()>end.getTime()){
                            throw new RuntimeException("搜索条件错误");
                        }
                        list.add(criteriaBuilder.between(root.get(strs[0]),start,end));
                    }else {
                        throw new RuntimeException("搜索条件错误");
                    }
                    break;
                case "lt"://小于
                    list.add(criteriaBuilder.lt(root.get(strs[0]), getNumber(entry.getValue())));
                    break;
                case "ge"://大于等于
                    list.add(criteriaBuilder.ge(root.get(strs[0]), getNumber(entry.getValue())));
                    break;
                case "le"://小于等于
                    list.add(criteriaBuilder.le(root.get(strs[0]), getNumber(entry.getValue())));
                    break;
            }
        }
        Predicate[] strings = new Predicate[list.size()];
        return list.toArray(strings);
    }

}
