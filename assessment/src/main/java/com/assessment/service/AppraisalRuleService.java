package com.assessment.service;

import com.assessment.core.base.BaseMapper;
import com.assessment.core.base.BaseService;
import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.domian.output.AppraisalIndexOutput;
import com.assessment.domian.output.AppraisalRuleOutput;
import com.assessment.mapper.jpa.AppraisalRuleRepository;
import com.assessment.mapper.jpa.AppraisalTemplateRuleRepository;
import com.assessment.mapper.mybatis.AppraisalRuleMapper;
import com.assessment.model.AppraisalIndex;
import com.assessment.model.AppraisalRule;
import com.assessment.model.AppraisalTemplateRule;
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
import java.util.ArrayList;
import java.util.List;

import static com.assessment.core.base.BaseController.PARAM_EORRO;
import static com.assessment.core.base.BaseController.SYS_EORRO;

@Service

public class AppraisalRuleService extends BaseService<AppraisalRuleOutput, AppraisalRule,Integer> {

    @Autowired
    private AppraisalRuleMapper appraisalRuleMapper;

    @Autowired
    private AppraisalRuleRepository appraisalRuleRepository;

    @Autowired
    private AppraisalTemplateRuleRepository appraisalTemplateRuleRepository;

    @Override
    public BaseMapper<AppraisalRule, Integer> getMapper() {
        return appraisalRuleRepository;
    }




    public MybatisBaseMapper<AppraisalRuleOutput> getMybatisBaseMapper() {
        return appraisalRuleMapper;
    }


    public boolean nameIsRepeat(Integer id, AppraisalRule appraisalRule) {
        if(id==null){
            List<AppraisalRule> list= appraisalRuleRepository.findByNameAndAmputated(appraisalRule.getName(),appraisalRule.getAmputated());
            if(list==null||list.size()==0){
                return false;
            }else{
                return true;
            }
        }else{
            List<AppraisalRule> list =  appraisalRuleMapper.findByNameNotId(appraisalRule);
            if(list==null||list.size()==0){
                return false;
            }else{
                return true;
            }
        }
    }

    @Transactional
    public int updatestate(String idList,Integer state) throws IllegalAccessException, IntrospectionException, InvocationTargetException,MethodArgumentNotValidException {
        var strs = idList.split(",");
        for(var str : strs){
            var id = Integer.parseInt(str);
            AppraisalRule t =  getMapper().getById(id);
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
        List<AppraisalTemplateRule> list = appraisalTemplateRuleRepository.findByRuleId(id);
        if(list!=null&&list.size()>0){
            return  ResponseResult.error("该考核规则被考核模板使用无法删除");
        }
        AppraisalRule t =  getMapper().getById(id);
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

    public String appraisalRuleExport(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "考核规则信息";
        String excelName = "考核规则信息";
        String[] rowsName = new String[]{"序号","指标分类","规则名称","计分标准项","分值","限额","计分公式","打分设置","默认打分","状态"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        PageData pageData=new PageData(request);
        List<AppraisalRuleOutput> list=this.selectAll(true,pageData);
        if(list.size()>0){
            int i=1;
            for(AppraisalRuleOutput output:list){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=output.getIndexName();
                objs[2]=output.getName();
                objs[3]=output.getDescription();
                objs[4]=output.getScore();
                objs[5]=output.getCumulativeLimit();
                objs[6]=output.getScoreTypeName();
                objs[7]=output.getScoreSourceName();
                objs[8]=output.getDefaultScore();
                objs[9]=output.getStateName();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }

    public ResponseResult getRuleByIndexId(Integer indexId) {
        List<AppraisalRuleOutput> list = appraisalRuleMapper.findRuleByIndexId(indexId);
        return ResponseResult.success(list);
    }
}
