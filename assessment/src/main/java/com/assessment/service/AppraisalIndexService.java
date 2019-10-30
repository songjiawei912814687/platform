package com.assessment.service;

import com.assessment.core.base.BaseMapper;
import com.assessment.core.base.BaseService;
import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.domian.output.*;
import com.assessment.mapper.jpa.AppraisalIndexRepository;
import com.assessment.mapper.jpa.AppraisalTemplateIndexRepository;
import com.assessment.mapper.mybatis.AppraisalIndexMapper;
import com.assessment.mapper.mybatis.AppraisalRuleMapper;
import com.assessment.mapper.mybatis.AppraisalTemplateMapper;
import com.assessment.model.*;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.ExportExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.assessment.core.base.BaseController.PARAM_EORRO;
import static com.assessment.core.base.BaseController.SYS_EORRO;

@Service

public class AppraisalIndexService extends BaseService<AppraisalIndexOutput, AppraisalIndex,Integer> {

    @Autowired
    private AppraisalIndexMapper appraisalIndexMapper;

    @Autowired
    private AppraisalIndexRepository appraisalIndexRepository;

    @Autowired
    private AppraisalRuleMapper appraisalRuleMapper;

    @Autowired
    private AppraisalTemplateMapper appraisalTemplateMapper;

    @Autowired
    private AppraisalTemplateIndexRepository appraisalTemplateIndexRepository;

    @Override
    public BaseMapper<AppraisalIndex, Integer> getMapper() {
        return appraisalIndexRepository;
    }




    public MybatisBaseMapper<AppraisalIndexOutput> getMybatisBaseMapper() {
        return appraisalIndexMapper;
    }


    public boolean nameIsRepeat(Integer id, AppraisalIndex appraisalIndex) {
        if(id==null){
            List<AppraisalIndex> list= appraisalIndexRepository.findByNameAndAmputated(appraisalIndex.getName(),appraisalIndex.getAmputated());
            if(list==null||list.size()==0){
                return false;
            }else{
                return true;
            }
        }else{
            List<AppraisalIndexOutput> list =  appraisalIndexMapper.findByNameNotId(appraisalIndex);
            if(list==null||list.size()==0){
                return false;
            }else{
                return true;
            }
        }
    }

    @Transactional
    public int updatestate(String idList,Integer state) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException,MethodArgumentNotValidException {
        var strs = idList.split(",");
        for(var str : strs){
            var id = Integer.parseInt(str);
            AppraisalIndex t =  getMapper().getById(id);
            if(t==null){
                continue;
            }
            setProperty(t, "state", state);
            var result = this.update(id,t);
        }
        return SUCCESS;
    }

    public ResponseResult logicDelete(Integer id) throws IllegalAccessException, IntrospectionException, InvocationTargetException ,MethodArgumentNotValidException{
        if (id == null ) {
            return ResponseResult.error(PARAM_EORRO);
        }
        //删除指标判断有没有绑定考核规则
        PageData pageData = new PageData();
        pageData.put("indexId",id);
        List<AppraisalRuleOutput> list = appraisalRuleMapper.selectByIndexId(pageData);
        if(list!=null&&list.size()>0){
            return  ResponseResult.error("该指标下存在考核规则无法删除");
        }
        //判断是否被模板使用

        List<AppraisalTemplateIndex> list1 = appraisalTemplateIndexRepository.findByIndexId(id);
        if(list1!=null&&list1.size()>0){
            return  ResponseResult.error("该指标被模板使用无法删除");
        }
        AppraisalIndex t =  getMapper().getById(id);
        if(t==null){
            return  ResponseResult.error(SYS_EORRO);
        }
        setProperty(t, "amputated", 1);
        var result = this.update(id,t);
        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success();
    }

    public int update(Integer id,AppraisalIndex t) throws IllegalAccessException, IntrospectionException, InvocationTargetException,MethodArgumentNotValidException{
        if(hasCloums(t,"lastUpdateUserId")){
            setProperty(t, "lastUpdateUserId", getUsers().getId());
            setProperty(t, "lastUpdateUserName", getUsers().getUsername());
            setProperty(t, "lastUpdateDateTime", new Date());
        }
        AppraisalIndex t1 = getMapper().getById(id);
        BigDecimal datumValue = t1.getDatumValue();
        String[] igre = getNotNullProperties(t);
        t1 = copyProperties(t1,t,igre);
        t = getMapper().save(t1);
        if ( t != null) {
            //更新使用到的考核模板的基础分
            List<AppraisalTemplateIndex> byIndexId = appraisalTemplateIndexRepository.findByIndexId(t.getId());
            for (AppraisalTemplateIndex appraisalTemplateIndex:byIndexId) {
                AppraisalTemplateOutput appraisalTemplateOutput = appraisalTemplateMapper.selectByPrimaryKey(appraisalTemplateIndex.getTemplateId());
                AppraisalTemplate appraisalTemplate = new AppraisalTemplate();
                appraisalTemplate.setDatumValue(appraisalTemplateOutput.getDatumValue().add(t.getDatumValue().subtract(datumValue)));
                appraisalTemplate.setId(appraisalTemplateOutput.getId());
                appraisalTemplateMapper.updateByPrimaryKeySelective(appraisalTemplate);
            }
            return (int) getProperty(t,"id");
        }
        return ERROR;
    }

    public String appraisalIndexExport(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "考核指标信息";
        String excelName = "考核指标信息";
        String[] rowsName = new String[]{"序号","指标类型","指标名称","基准分","最高加分","对象类型","状态"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        PageData pageData=new PageData(request);
        List<AppraisalIndexOutput> list=this.selectAll(true,pageData);
        if(list.size()>0){
            int i=1;
            for(AppraisalIndexOutput output:list){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=output.getTypeName();
                objs[2]=output.getName();
                objs[3]=output.getDatumValue();
                objs[4]=output.getMaxBonus();
                objs[5]=output.getObjectName();
                objs[6]=output.getStateName();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }

    public boolean isInUse(String idList) {
        String[] split = idList.split(",");
        List<AppraisalTemplateIndex> byIndexId = appraisalTemplateIndexRepository.findByIndexId(Integer.valueOf(split[0]));
        if(byIndexId==null||byIndexId.size()==0){
            return false;
        }
        return true;
    }
}
