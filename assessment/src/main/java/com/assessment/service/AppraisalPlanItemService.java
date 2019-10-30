package com.assessment.service;

import com.assessment.core.base.BaseMapper;
import com.assessment.core.base.BaseService;
import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.core.util.AppConsts;
import com.assessment.domian.input.AppraisalPlanItemAttachmentInput;
import com.assessment.domian.input.PlanItemAdditionInput;
import com.assessment.domian.output.AppraisalPlanItemOutput;
import com.assessment.domian.output.AppraisalPlanOutput;
import com.assessment.domian.output.AssessmentDepartReportOutput;
import com.assessment.mapper.jpa.AppraisalPlanItemRepository;
import com.assessment.mapper.jpa.AppraisalPlanRepository;
import com.assessment.mapper.jpa.PlanItemAdditionRepository;
import com.assessment.mapper.mybatis.AppraisalPlanItemMapper;
import com.assessment.mapper.mybatis.AppraisalPlanMapper;
import com.assessment.mapper.mybatis.AssessmentDepartReportMapper;
import com.assessment.mapper.mybatis.PlanItemAdditionMapper;
import com.assessment.model.*;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.AttributesReflect;
import com.common.utils.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.assessment.core.base.BaseController.SYS_EORRO;
import static java.util.stream.Collectors.toList;


@Service
public class AppraisalPlanItemService extends BaseService<AppraisalPlanItemOutput,AppraisalPlanItem,Integer> {
    @Autowired
    private AppraisalPlanItemRepository repository;

    @Autowired
    private AppraisalPlanItemMapper appraisalPlanItemMapper;

    @Autowired
    private AppraisalPlanMapper appraisalPlanMapper;

    @Autowired
    private PlanItemAdditionMapper planItemAdditionMapper;

    @Autowired
    private PlanItemAdditionRepository planItemAdditionRepository;

    @Autowired
    private AssessmentDepartReportMapper assessmentDepartReportMapper;

    @Autowired
    private ConfigService configService;

    @Override
    public BaseMapper<AppraisalPlanItem, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<AppraisalPlanItemOutput> getMybatisBaseMapper() {
        return appraisalPlanItemMapper;
    }

    public List<AppraisalPlanItemOutput> getByPlanId(Integer planId){
//        List<AppraisalPlanItemOutput> list= appraisalPlanItemMapper.selectByPlanId(planId).stream().sorted(Comparator.comparingInt(AppraisalPlanItemOutput::getIndexId)).collect(toList());
        List<AppraisalPlanItemOutput> list= appraisalPlanItemMapper.selectByPlanId(planId);
        AppraisalPlanOutput o = appraisalPlanMapper.selectByPrimaryKey(planId);
        if(o.getObjectType().equals(AppConsts.Window)&&o.getState().equals(AppConsts.created)){
            for (AppraisalPlanItemOutput itm:list) {
                itm.setIndexScore(null);
                itm.setRuleScore(null);
            }
        }
        //计算这个考核计划下明细的对应的附件的数量
   /*     if(o.getObjectType().equals(AppConsts.Window)) {
            List<PlanItemAddition> sum = planItemAdditionMapper.findSumByPlanId(planId);
            HashMap<Integer, Integer> map = new HashMap<>();
            for (PlanItemAddition p : sum) {
                map.put(p.getPlanItemId(), p.getTotal());
            }
            for (AppraisalPlanItemOutput out : list) {
                if (map.containsKey(out.getId())) {
                    out.setTotal(map.get(out.getId()));
                } else {
                    out.setTotal(0);
                }
            }
        }*/
        return list;
    }

    public List<AppraisalPlanItemOutput> getByIndexId(Integer indexId){
        return appraisalPlanItemMapper.selectByIndexId(indexId);
    }

    //员工考核计划报表
    public String EmployeesPlanItemExport(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "员工考核明细信息";
        String excelName = "员工考核明细信息";
        String[] rowsName = new String[]{"序号","指标项","基准分","最高加分","得分","效能指标评分标准","打分设置","计分公式","限额"
                ,"数量","分值","得分","评分说明"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        PageData pageData=new PageData(request);
        List<AppraisalPlanItemOutput> list=getByPlanId(Integer.parseInt(pageData.getMap().get("planId")));
        if(list.size()>0){
            int i=1;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            for(AppraisalPlanItemOutput output:list){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=output.getIndexName();
                objs[2]=output.getDatumValue();
                objs[3]=output.getMaxBonus();
                objs[4]=output.getIndexScore();
                objs[5]=output.getDescription();
                objs[6]=output.getScoreSourceName();
                objs[7]=output.getScoreTypeName();
                objs[8]=output.getCumulativeLimit();
                objs[9]=output.getQuantity();
                objs[10]=output.getScore();
                objs[11]=output.getRuleScore();
                objs[12]=output.getRatingDescription();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }

    //员工考核计划报表
    public String departmentPlanItemExport(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "部门考核明细信息";
        String excelName = "部门考核明细信息";
        String[] rowsName = new String[]{"序号","指标项","基准分","最高加分","得分","效能指标评分标准","打分设置","计分公式","限额"
                ,"数量","分值","得分","评分说明"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        PageData pageData=new PageData(request);
        List<AppraisalPlanItemOutput> list=getByPlanId(Integer.parseInt(pageData.getMap().get("planId")));
        if(list.size()>0){
            int i=1;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            for(AppraisalPlanItemOutput output:list){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=output.getIndexName();
                objs[2]=output.getDatumValue();
                objs[3]=output.getMaxBonus();
                objs[4]=output.getIndexScore();
                objs[5]=output.getDescription();
                objs[6]=output.getScoreSourceName();
                objs[7]=output.getScoreTypeName();
                objs[8]=output.getCumulativeLimit();
                objs[9]=output.getQuantity();
                objs[10]=output.getScore();
                objs[11]=output.getRuleScore();
                objs[12]=output.getRatingDescription();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }

    public ResponseResult updatePlanAndDetail(Integer id, AppraisalPlanItem appraisalPlanItem) throws Exception{
        //部门考核计划的修改，在生成状态前只修改数量
        AppraisalPlanOutput plan = appraisalPlanMapper.findByItemId(id);
        if(plan.getObjectType().equals(AppConsts.Window)&&plan.getState().equals(AppConsts.created)){
            AppraisalPlanItem item = new  AppraisalPlanItem();
            item.setId(id);
            item.setQuantity(appraisalPlanItem.getQuantity());
            if(this.update(id,appraisalPlanItem)<0){
                return ResponseResult.error(SYS_EORRO);
            }else{
                return ResponseResult.success();
            }
        }
        //获得考核规则的规则值
        AppraisalPlanItem appraisalPlanItemOutput = this.getById(id);
        //根据数量和考核规则的分值给考核规则打分
        BigDecimal a= this.calculationScore(id,appraisalPlanItem,appraisalPlanItemOutput);
        //比较打分和限额的大小，合计最后的考核规则打分的值
        if(appraisalPlanItemOutput.getCumulativeLimit()!=null&&a.compareTo(appraisalPlanItemOutput.getCumulativeLimit())>=0){
            appraisalPlanItem.setRuleScore(appraisalPlanItemOutput.getCumulativeLimit());
        }else {
            appraisalPlanItem.setRuleScore(a);
        }
        //修改考核指标
            //若考核指标无分数则给考核指标设定基准分值
        if(appraisalPlanItemOutput.getIndexScore()==null){
            appraisalPlanItemOutput.setIndexScore(appraisalPlanItemOutput.getDatumValue()==null?new BigDecimal(0):appraisalPlanItemOutput.getDatumValue());
        }
            //更新考核指标的得分，分值为现有的分数+考核规则的差值*计算公式
        //根据appraisalPlanItemOutput获得其他的规则
        PageData pageData1 = new PageData();
        pageData1.put("indexId",appraisalPlanItemOutput.getIndexId());
        pageData1.put("planId",appraisalPlanItemOutput.getPlanId());
        pageData1.put("itemId",appraisalPlanItemOutput.getId());
        List<AppraisalPlanItemOutput> list1 = appraisalPlanItemMapper.getOthersRule(pageData1);
        BigDecimal indexScore = new BigDecimal(0);
        for (AppraisalPlanItemOutput o:list1) {
            indexScore = indexScore.add((o.getRuleScore()==null?new BigDecimal(0):o.getRuleScore()).multiply(o.getScoreType().equals(0)?new BigDecimal(1):new BigDecimal(-1)));
        }
        indexScore = indexScore.add(appraisalPlanItem.getRuleScore().multiply(appraisalPlanItemOutput.getScoreType().equals(0)?new BigDecimal(1):new BigDecimal(-1))).add(appraisalPlanItemOutput.getDatumValue()==null?new BigDecimal(0):appraisalPlanItemOutput.getDatumValue());
        //计算和更新考核指标得分
        BigDecimal score = appraisalPlanItemOutput.getDatumValue()==null?new BigDecimal(0):appraisalPlanItemOutput.getDatumValue();
        BigDecimal toIndexScore = new BigDecimal(0);
        if(appraisalPlanItemOutput.getMaxBonus()!=null){
            score = score.add(appraisalPlanItemOutput.getMaxBonus());
        }
        PageData indexPageData = new PageData();
        if(indexScore.subtract(score).compareTo(new BigDecimal(0))>0){
            toIndexScore = score;
        }else {
            if(indexScore.compareTo(new BigDecimal(0))>0){
                toIndexScore = indexScore;
            }
        }

        indexPageData.put("indexScore",toIndexScore);
        indexPageData.put("planId",appraisalPlanItemOutput.getPlanId());
        indexPageData.put("indexId",appraisalPlanItem.getIndexId());
        int i1 = appraisalPlanItemMapper.updateByIndexIdAndPlanId(indexPageData);
        //修改考核计划得分
        BigDecimal indexScoreBefore = appraisalPlanItemOutput.getIndexScore();
        appraisalPlanItemOutput.setIndexScore(toIndexScore);
        AppraisalPlanOutput appraisalPlanOutput = appraisalPlanMapper.selectByPrimaryKey(appraisalPlanItemOutput.getPlanId());
        if(appraisalPlanOutput.getState()>=1){
            BigDecimal finalScore = appraisalPlanOutput.getFinalScore()==null?new BigDecimal(0):appraisalPlanOutput.getFinalScore();
            finalScore = finalScore.add(toIndexScore.subtract(indexScoreBefore));
            appraisalPlanOutput.setFinalScore(finalScore);
            appraisalPlanOutput.setLastUpdateDateTime(new Date());
            appraisalPlanOutput.setLastUpdateUserId(getUsers().getId());
            appraisalPlanOutput.setLastUpdateUserName(getUsers().getUsername());
            int i = appraisalPlanMapper.updateByPrimaryKeySelective(appraisalPlanOutput);
            if(i<0){
                return ResponseResult.error(SYS_EORRO);
            }
        }
        if(i1<0){
            return ResponseResult.error(SYS_EORRO);
        }
        appraisalPlanItem.setIndexScore(toIndexScore);
        if(this.update(id,appraisalPlanItem)<0){
            return ResponseResult.error(SYS_EORRO);
        }
        //如果修改的是人均办件量则需要修改当前考核计划中的其他事项联办率
        if(appraisalPlanItemOutput.getIndexId().equals(AppConsts.personAvg)){
            //根据明细的id获得所有的事项联办率考核明细
            PageData pageData = new PageData();
            pageData.put("itemId",id);
            pageData.put("indexId",AppConsts.jointRate);
            List<AppraisalPlanItem> list = appraisalPlanItemMapper.getAllJointRateByItemId(pageData);
            for (AppraisalPlanItem o:list) {
                this.updatePlanAndDetail(o.getId(),o);
            }
        }
        //更新部门考核报表中的明细
        if(!plan.getState().equals(AppConsts.created)){
            updateDepartmentReport(id,appraisalPlanItem);
        }
        return ResponseResult.success(appraisalPlanItemOutput);
    }

    private void updateDepartmentReport(Integer id, AppraisalPlanItem appraisalPlanItem) {
        List<Config> configList = configService.getListByKey("bmkhbbsx");
        var departmentParameterMap = new HashMap<String,String>();
        for (Config config:configList) {
            departmentParameterMap.put(config.getConfigKey(),config.getConfigValue());
        }
        String key = this.getKeyByValue(departmentParameterMap,appraisalPlanItem.getIndexId());

        AssessmentDepartReportOutput output = assessmentDepartReportMapper.findByPlanId(id);
        AssessmentDepartReport assessmentDepartReport = new AssessmentDepartReport();
        BeanUtils.copyProperties(output,assessmentDepartReport);
        try {
            AttributesReflect.setValue(assessmentDepartReport, assessmentDepartReport.getClass(), key, AssessmentDepartReport.class.getDeclaredField(key).getType(), appraisalPlanItem.getIndexScore());
            BigDecimal decimal1 = assessmentDepartReport.getDoThings() == null ? new BigDecimal(0) : assessmentDepartReport.getDoThings();
            BigDecimal decimal2 = assessmentDepartReport.getWindowAccept() == null ? new BigDecimal(0) : assessmentDepartReport.getWindowAccept();
            BigDecimal decimal3 = assessmentDepartReport.getOnlineReporting() == null ? new BigDecimal(0) : assessmentDepartReport.getOnlineReporting();
            BigDecimal decimal4 = assessmentDepartReport.getMatterSeec() == null ? new BigDecimal(0) : assessmentDepartReport.getMatterSeec();
            BigDecimal decimal5 = assessmentDepartReport.getDataCompaction() == null ? new BigDecimal(0) : assessmentDepartReport.getDataCompaction();
            BigDecimal decimal6 = assessmentDepartReport.getIntermediaryService() == null ? new BigDecimal(0) : assessmentDepartReport.getIntermediaryService();
            BigDecimal decimal7 = assessmentDepartReport.getPeopleSatisfaction() == null ? new BigDecimal(0) : assessmentDepartReport.getPeopleSatisfaction();
            BigDecimal decimal8 = assessmentDepartReport.getJobLogging() == null ? new BigDecimal(0) : assessmentDepartReport.getJobLogging();
            BigDecimal decimal9 = assessmentDepartReport.getTransactNorm() == null ? new BigDecimal(0) : assessmentDepartReport.getTransactNorm();
            BigDecimal decimal10 = assessmentDepartReport.getInnovationWork() == null ? new BigDecimal(0) : assessmentDepartReport.getInnovationWork();
            assessmentDepartReport.setMouthIndex(decimal1.add(decimal2).add(decimal3).add(decimal4).add(decimal5).add(decimal6).add(decimal7).add(decimal8).add(decimal9).add(decimal10));
            assessmentDepartReportMapper.updateByPrimaryKeySelective(assessmentDepartReport);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getKeyByValue(Map<String, String> departmentParameterMap, Integer ruleId) {
        for(String key: departmentParameterMap.keySet()){
            if(departmentParameterMap.get(key).equals(String.valueOf(ruleId))){
                return key;
            }
        }
        return "";
    }

    private BigDecimal calculationScore(Integer id, AppraisalPlanItem appraisalPlanItem, AppraisalPlanItem appraisalPlanItemOutput) {
        //若是人均办件量需要将获得员工人数计算
        if(appraisalPlanItemOutput.getIndexId().equals(AppConsts.personAvg)){
            //根据考核计划明细获得部门人数
            Integer personQuantity = appraisalPlanItemMapper.getPersonQuantity(id);
            BigDecimal a= new BigDecimal(0);
            personQuantity = personQuantity==null||personQuantity==0?1:personQuantity;
            a=appraisalPlanItem.getQuantity().multiply(appraisalPlanItemOutput.getScore());
            a = a.divide(new BigDecimal(personQuantity),2,BigDecimal.ROUND_HALF_UP);
            appraisalPlanItem.setRatingDescription("办件量/部门人数*分值="+appraisalPlanItem.getQuantity()+"/"+personQuantity+"*"+appraisalPlanItemOutput.getScore());
            return a;
        }
        if(appraisalPlanItemOutput.getIndexId().equals(AppConsts.jointRate)){
            //获得当前考核计划中人均办件量总数量
            PageData pageData = new PageData();
            pageData.put("itemId",id);
            pageData.put("indexId",AppConsts.personAvg);
            Integer sumQualitity = appraisalPlanItemMapper.getSumQualitityByPlanItemId(pageData);
            BigDecimal a= new BigDecimal(0);
            sumQualitity = sumQualitity==null?0:sumQualitity;
            if(sumQualitity==null||sumQualitity==0){
                appraisalPlanItem.setRatingDescription("联办事项量/办件总量*100*分值="+appraisalPlanItem.getQuantity()+"/"+sumQualitity+"*100*"+appraisalPlanItemOutput.getScore());
                return a;
            }
            a=appraisalPlanItem.getQuantity().multiply(appraisalPlanItemOutput.getScore()).multiply(new BigDecimal(100));
            a = a.divide(new BigDecimal(sumQualitity),2,BigDecimal.ROUND_HALF_UP);
            appraisalPlanItem.setRatingDescription("联办事项量/办件总量*100*分值="+appraisalPlanItem.getQuantity()+"/"+sumQualitity+"*100*"+appraisalPlanItemOutput.getScore());
            return a;
        }
        BigDecimal a=appraisalPlanItem.getQuantity().multiply(appraisalPlanItemOutput.getScore());
        return a;
    }

    public boolean updateByIndexId(PageData pageData){
        if(appraisalPlanItemMapper.updateByIndexIdAndPlanId(pageData)<0){
            return false;
        }
        return true;
    }

    public ResponseResult updateState(Integer id, Integer state) {
        AppraisalPlan plan = new AppraisalPlan();
        plan.setId(id);
        plan.setState(state);
        appraisalPlanMapper.updateByPrimaryKeySelective(plan);
        return ResponseResult.success();
    }

    public ResponseResult saveAddtition(Integer id, PlanItemAdditionInput planItemAdditionInput) {
        //先根据考核明细id删除附件
        planItemAdditionMapper.deleteByItemId(id);
        if(planItemAdditionInput!=null&&planItemAdditionInput.getList()!=null&&planItemAdditionInput.getList().size()>0){
            for (PlanItemAddition addition:planItemAdditionInput.getList() ) {
                addition.setAmputated(0);
                addition.setCreatedUserId(getUsers().getId());
                addition.setCreatedUserName(getUsers().getUsername());
                addition.setCreatedDateTime(new Date());
                addition.setPlanItemId(id);
            }
            List<PlanItemAddition> planItemAdditions = planItemAdditionRepository.saveAll(planItemAdditionInput.getList());
            if(planItemAdditions.size()>0){
                return ResponseResult.success();
            }else {
                return ResponseResult.error(SYS_EORRO);
            }
        }
        return   ResponseResult.success();
    }

    public ResponseResult saveAppraisalPlanItemAttachment(AppraisalPlanItemAttachmentInput appraisalPlanItemAttachmentInput) {
        //先根据考核明细id删除附件
        planItemAdditionMapper.deleteByItemId(appraisalPlanItemAttachmentInput.getPlanItemId());
        if(!CollectionUtils.isEmpty(appraisalPlanItemAttachmentInput.getList())){
            for (PlanItemAddition addition:appraisalPlanItemAttachmentInput.getList() ) {
                addition.setAmputated(0);
                addition.setCreatedUserId(getUsers().getId());
                addition.setCreatedUserName(getUsers().getUsername());
                addition.setCreatedDateTime(new Date());
                addition.setPlanItemId(appraisalPlanItemAttachmentInput.getPlanItemId());
            }
            List<PlanItemAddition> planItemAdditions = planItemAdditionRepository.saveAll(appraisalPlanItemAttachmentInput.getList());
            if(planItemAdditions.size()<=0){
                return ResponseResult.error(SYS_EORRO);
            }
        }
        return   ResponseResult.success();
    }

    public ResponseResult getAdditionById(Integer id) {
        List<PlanItemAddition> list = planItemAdditionMapper.findByPlanItemId(id);
        return  ResponseResult.success(list);
    }


    //部门办件量统计信息
    public String banjianExport(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "部门办件量统计报表";
        String excelName = "部门办件量统计信息";
        String[] rowsName = new String[]{"序号","组织名称","规则名称","数量"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        PageData pageData=new PageData(request);
        pageData.put("indexId",32);
        List<AppraisalPlanItemOutput> list=appraisalPlanItemMapper.selectByYearAndMonthAndIndexId(pageData);
        if(list.size()>0){
            int i=1;
            for(AppraisalPlanItemOutput output:list){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=output.getOrganizationName();
                objs[2]=output.getRuleName();
                objs[3]=output.getQuantity();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }

    //部门联办率统计信息
    public String liangbanExport(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "部门“一件事”联办率统计报表";
        String excelName = "部门“一件事”联办率统计信息";
        String[] rowsName = new String[]{"序号","组织名称","规则名称","数量"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        PageData pageData=new PageData(request);
        pageData.put("indexId",36);
        List<AppraisalPlanItemOutput> list=appraisalPlanItemMapper.selectByYearAndMonthAndIndexId(pageData);
        if(list.size()>0){
            int i=1;
            for(AppraisalPlanItemOutput output:list){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=output.getOrganizationName();
                objs[2]=output.getRuleName();
                objs[3]=output.getQuantity();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }

    //部门五办实现率统计信息
    public String wubanshixianExport(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "部门五办实现率统计报表";
        String excelName = "部门五办实现率统计信息";
        String[] rowsName = new String[]{"序号","组织名称","规则名称","数量"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        PageData pageData=new PageData(request);
        pageData.put("indexId",37);
        List<AppraisalPlanItemOutput> list=appraisalPlanItemMapper.selectByYearAndMonthAndIndexId(pageData);
        if(list.size()>0){
            int i=1;
            for(AppraisalPlanItemOutput output:list){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=output.getOrganizationName();
                objs[2]=output.getRuleName();
                objs[3]=output.getQuantity();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }
}
