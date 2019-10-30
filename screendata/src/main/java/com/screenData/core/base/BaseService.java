package com.screenData.core.base;

import com.common.model.PageData;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.transaction.Transactional;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 基类
 * leeoohoo@gmail.com
 */
@Service
public abstract class BaseService<OT,T, ID extends Serializable> implements GetMapper<OT,T, ID> {


    public static final int SUCCESS = 0;

    public static final int ERROR = -1;



    public boolean hasCloums(T beanObj,String property) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(property, beanObj.getClass());
            Method getMethod = pd.getReadMethod();
            if (getMethod == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /*该方法用于传入某实例对象以及对象方法名，通过反射调用该对象的某个get方法*/
    public Object getProperty(T beanObj, String property) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        //此处应该判断beanObj,property不为null
        PropertyDescriptor pd = new PropertyDescriptor(property, beanObj.getClass());
        Method getMethod = pd.getReadMethod();
        if (getMethod == null) {

        }
        return (T) getMethod.invoke(beanObj);
    }

    public Object getPropertyObject(Object beanObj, String property) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        //此处应该判断beanObj,property不为null
        PropertyDescriptor pd = new PropertyDescriptor(property, beanObj.getClass());
        Method getMethod = pd.getReadMethod();
        if (getMethod == null) {

        }
        return (Object) getMethod.invoke(beanObj);
    }

    /*该方法用于传入某实例对象以及对象方法名、修改值，通过放射调用该对象的某个set方法设置修改值*/
    public T setProperty(T beanObj, String property, Object value) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        //此处应该判断beanObj,property不为null
        PropertyDescriptor pd = new PropertyDescriptor(property, beanObj.getClass());
        Method setMethod = pd.getWriteMethod();
        if (setMethod == null) {

        }
        return (T) setMethod.invoke(beanObj, value);
    }


    public T copyProperties(T t,T t1,String[] ig) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        for(String str : ig){
            if(str.equals("class")){
                continue;
            }
            setProperty(t,str,getProperty(t1,str));
        }
        return t;
    }

    /**
     * 获取对象的不为null的属性
     */
    public static String[] getNotNullProperties(Object src) {
        //1.获取Bean
        BeanWrapper srcBean = new BeanWrapperImpl(src);
        //2.获取Bean的属性描述
        PropertyDescriptor[] pds = srcBean.getPropertyDescriptors();
        //3.获取Bean的空属性
        Set<String> properties = new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : pds) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = srcBean.getPropertyValue(propertyName);
            if (propertyValue!=null) {
                properties.add(propertyName);
            }
        }
        return properties.toArray(new String[0]);
    }


    public int add(T t) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {

        if(hasCloums(t,"amputated")){
            setProperty(t,"amputated",0);
        }
        t = getMapper().save(t);
        if ( t != null) {
            var i = getProperty(t,"id");
            System.out.println("--------------------------返回的id"+i);
            return (Integer) i;
        }
        return ERROR;
    }

    public int update(Integer id,T t) throws IllegalAccessException, IntrospectionException, InvocationTargetException,MethodArgumentNotValidException{

        T t1 = getMapper().getById(id);
        String[] igre = getNotNullProperties(t);
        t1 = copyProperties(t1,t,igre);
        t = getMapper().save(t1);
        if ( t != null) {
            return (int) getProperty(t,"id");
        }
        return ERROR;
    }

    @Transactional
    public int deleteById(String idList) {
        var strs = idList.split(",");
        for(var str : strs){
            Integer is = 1;
            var id = Integer.parseInt(str);
            getMapper().deleteById(id);
        }

        return SUCCESS;
    }

    @Transactional
    public int logicDelete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException,MethodArgumentNotValidException,MethodArgumentNotValidException {
        var strs = idList.split(",");
        for(var str : strs){
            var id = Integer.parseInt(str);
            T t =  getMapper().getById(id);
            if(t==null){
                continue;
            }
            setProperty(t, "amputated", 1);
            var result = this.update(id,t);
        }
        return SUCCESS;
    }

    public T getById(ID id) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Optional<T> t =  getMapper().findById(id);
        if(!t.isPresent() ){
            return null;
        }else{
            return (T)t.get();
        }
    }

    public List<T> getAll() {
        return getMapper().findAll();
    }

    public List<T> getAll(Specification specification){
        return getMapper().findAll(specification);
    }

    public OT selectById(Integer id){
        return getMybatisBaseMapper().selectByPrimaryKey(id);
    }

    public List<OT> selectAll(boolean isPage, PageData pageData){
        if(isPage){
            Integer pagesize = pageData.getRows();
            Integer page = pageData.getPageIndex();
            PageHelper.startPage(page, pagesize);
            Page<OT> pageList = getMybatisBaseMapper().selectPage(pageData);
            return pageList;
        }
        return getMybatisBaseMapper().selectAll(pageData);
    }



}
