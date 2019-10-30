package com.assessment.service;

import com.assessment.core.base.BaseMapper;
import com.assessment.core.base.BaseService;
import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.core.util.AppConsts;
import com.assessment.domian.output.*;
import com.assessment.mapper.jpa.AppraisalPlanRepository;
import com.assessment.mapper.mybatis.*;
import com.assessment.model.*;
import com.common.model.PageData;
import com.common.request.GetConfig;
import com.common.request.ServiceCall;
import com.common.response.ResponseResult;
import com.common.utils.AttributesReflect;
import com.common.utils.ExportExcel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Key;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class AppraisalPlanService extends BaseService<AppraisalPlanOutput, AppraisalPlan,Integer> {
    @Autowired
    private AppraisalPlanMapper appraisalPlanMapper;

    @Autowired
    private AppraisalPlanItemMapper appraisalPlanItemMapper;

    @Autowired
    private AppraisalEmployeeRecordMapper appraisalEmployeeRecordMapper;

    @Autowired
    private AppraisalPlanRepository appraisalPlanRepository;

    @Autowired
    private PreApasInfoMapper preApasInfoMapper;

    @Autowired
    private  FeedBackInfoMapper feedBackInfoMapper;

    @Autowired
    private ConfigService configService;

    @Autowired
    private AssessmentDepartReportMapper assessmentDepartReportMapper;

    @Autowired
    private AssessmentDepartReportService assessmentDepartReportService;

    @Autowired
    private AppraisalPlanItemService appraisalPlanItemService;

    @Autowired
    private AttendanceStatisticsMapper attendanceStatisticsMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public BaseMapper<AppraisalPlan, Integer> getMapper() {
        return appraisalPlanRepository;
    }
    @Override
    public MybatisBaseMapper<AppraisalPlanOutput> getMybatisBaseMapper() {
        return appraisalPlanMapper;
    }

    public List<AppraisalPlanOutput> getEmployeesPlan(PageData pageData){
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        Page<AppraisalPlanOutput> pageList = appraisalPlanMapper.selectEmployeesPlan(pageData);
        return pageList;
    }

    public boolean isPermissions(){
        var user = getUsers();
        if(user.getUserType() == 1){//如果是部门的账号
            return true;
        }
        var roleList = roleMapper.findByUserId(user.getId());
        if(roleList == null || roleList.size() <= 0){
            return false;
        }
        for(var r : roleList){
            if(r.getDefaultPermissions() != null){
                if(r.getDefaultPermissions().equals(1)){//开启默认权限
                    return true;
                }
            }
        }
        return false;
    }

    @Transactional
    public int updatestate(String idList,Integer isStar) throws IllegalAccessException, IntrospectionException, InvocationTargetException,MethodArgumentNotValidException,MethodArgumentNotValidException {
        var strs = idList.split(",");
        for(var str : strs){
            var id = Integer.parseInt(str);
            AppraisalPlan t =  getMapper().getById(id);
            if(t==null){
                continue;
            }
            setProperty(t, "isStar", isStar);
            var result = this.update(id,t);
        }
        return SUCCESS;
    }

    public List<AppraisalPlanOutput> findDepartmentPlan(PageData pageData){
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        Page<AppraisalPlanOutput> pageList = appraisalPlanMapper.selectDeparmentPlan(pageData);
        return pageList;
    }
    public List<AppraisalPlanOutput> returnPageInfo(PageData pageData){
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        Page<AppraisalPlanOutput> pageList = appraisalPlanMapper.returnPageInfo(pageData);
        return pageList;
    }


    //员工考核计划报表
    public String EmployeesReportExport(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "员工考核计划报表";
        String excelName = "员工考核计划报表";
        String[] rowsName = new String[]{"序号","单位","窗口/后台","姓名","考核得分"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        PageData pageData=new PageData(request);

        if(pageData.getMap().get("organizationId")!=null&&!pageData.getMap().get("organizationId").equals("")){
            String  s=getPathById(Integer.parseInt(pageData.getMap().get("organizationId")));
            pageData.put("path",s+",");
        }

        pageData.put("userId",getUsers().getId().toString());
        if(getUsers().getAdministratorLevel()!=9){
            if(getUsers().getUserType()==0){
                pageData.put("employeeId",getUsers().getEmployeeId().toString());
            }else {
                pageData.put("orgId",getUsers().getOrganizationId().toString());
            }
        }
        List<AppraisalPlanOutput> list=appraisalPlanMapper.selectEmployeesPlanExport(pageData);
        if(list.size()>0){
            int i=1;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            for(AppraisalPlanOutput appraisal:list){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=appraisal.getOrganizationName();
                objs[2]=appraisal.getWindowOrOfficeName();
                objs[3]=appraisal.getEmployeeName();
                objs[4]=appraisal.getFinalScore();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }

    public  String getPathById(Integer id){
        return  appraisalPlanMapper.selectPathById(id);
    }


    @Transactional
    public void updateFinalScore(String date) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        PageData pageData=new PageData();
        String[] strings=date.split("-");
        Integer year=Integer.parseInt(strings[0]);
        Integer month=Integer.parseInt(strings[1]);
        pageData.put("year",year);
        pageData.put("month",month);
        pageData.put("date",date);
        List<AppraisalPlanOutput> planList=appraisalPlanMapper.selectEmployeePlanByDate(pageData);
        List<AttendanceDailyDate> attendanceDailyDates=AttendanceMonthStatic(date);
        if(planList!=null&&planList.size()>0){//需要打分的考核计划
            for(AppraisalPlanOutput appraisalPlanOutput:planList){
                List<AppraisalPlanItemOutput> planItemOutputList=appraisalPlanItemMapper.selectByPlanId(appraisalPlanOutput.getId());
                if(planItemOutputList==null||planItemOutputList.size()<=0){
                    continue;
                }
                BigDecimal bigDecimal1=new BigDecimal("0");
                List<AppraisalEmployeeRecordOutput> records=new ArrayList<AppraisalEmployeeRecordOutput>();
                pageData.put("planId",appraisalPlanOutput.getId());
                pageData.put("employeeId",appraisalPlanOutput.getEmployeeId());
                List<AppraisalEmployeeRecordOutput> recordOutputs=appraisalEmployeeRecordMapper.selectByPlan(pageData);
                if(recordOutputs.size()>0){
                    for(AppraisalEmployeeRecordOutput employeeRecordOutput:recordOutputs){
                        for(AppraisalPlanItemOutput planItemOutput:planItemOutputList){
                            if(employeeRecordOutput.getAppraisalId().equals(planItemOutput.getIndexId())
                                    &&employeeRecordOutput.getRuleId().equals(planItemOutput.getRuleId())){
                                if(AppConsts.Belate_Interface.equals(planItemOutput.getDataType())
                                        ||AppConsts.LeaveEarly_Interface.equals(planItemOutput.getDataType())
                                        ||AppConsts.NotPunch_Interface.equals(planItemOutput.getDataType())){
                                    continue;
                                }
                               records.add(employeeRecordOutput);
                            }
                        }
                    }

                }
                AppraisalPlan appraisalPlan=new AppraisalPlan();
                //计算员工加减分，并更新与员工加减分记录的每一条考核明细
                getRecordsScore(planItemOutputList,records);
                double a=0;
                appraisalPlan.setAbsenceDays(a);
                appraisalPlan.setIsEnabled(0);
                if(attendanceDailyDates!=null&&attendanceDailyDates.size()>0){
                    AttendanceDailyDate attendanceDailyDate=new AttendanceDailyDate();
                    for(AttendanceDailyDate attendanceDailyDate1:attendanceDailyDates){
                        if(attendanceDailyDate1.getEmployeeId().equals(appraisalPlanOutput.getEmployeeId())){
                            attendanceDailyDate=attendanceDailyDate1;
                            continue;
                        }
                    }
                    if(attendanceDailyDate!=null){
                        if(attendanceDailyDate.getTotalHours()!=null){
                            a=attendanceDailyDate.getTotalHours()/4*0.5;
                            if(a>10){
                                appraisalPlan.setIsEnabled(1);
                            }
                            appraisalPlan.setAbsenceDays(a);
                        }
                        //计算员工考勤得分，并更新与考勤相关的每一条考核明细
                      getAttenceScore(attendanceDailyDate,planItemOutputList);
                    }
                }
                List<AppraisalPlanItemOutput> planItemOutputList1=appraisalPlanItemMapper.selectByPlanId(appraisalPlanOutput.getId());
                List<AppraisalPlanItemOutput> result = new LinkedList<>();
                BigDecimal bigDecimal=new BigDecimal("0");
                for (var o : planItemOutputList1) {
                    boolean b = result.stream().anyMatch(p -> p.getIndexId().compareTo(o.getIndexId())==0);
                    if (!b) {
                        result.add(o);
                    }
                }
                for(AppraisalPlanItemOutput planItemOutput:result){
                    bigDecimal=bigDecimal.add(planItemOutput.getIndexScore());
                }
                appraisalPlan.setFinalScore(bigDecimal);
                appraisalPlan.setState(1);
                appraisalPlan.setId(appraisalPlanOutput.getId());
                appraisalPlan.setLastUpdateUserId(0);
                appraisalPlan.setLastUpdateDateTime(new Date());
                appraisalPlan.setLastUpdateUserName("job");
                AppraisalPlan t1 = getMapper().getById(appraisalPlanOutput.getId());
                String[] igre = getNotNullProperties(appraisalPlan);
                t1 = copyProperties(t1,appraisalPlan,igre);
                getMapper().save(t1);
            }
        }
    }

    @Transactional
    public  BigDecimal getRecordsScore(List<AppraisalPlanItemOutput> planItemOutputs,List<AppraisalEmployeeRecordOutput> recordOutputs) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        List<AppraisalEmployeeRecordOutput> recordOutputList=recordOutputs.stream().sorted(Comparator.comparingInt(AppraisalEmployeeRecordOutput::getRuleId)).collect(toList());
        Map<Integer,BigDecimal> map=new HashMap<Integer, BigDecimal>();
        List<AppraisalPlanItemOutput> planItemOutputList=new ArrayList<AppraisalPlanItemOutput>();
        //统计员工加减分记录，更新数量和更新考核计划明细规得分
        if(recordOutputs!=null&&recordOutputs.size()>0){
            for(AppraisalEmployeeRecordOutput recordOutput:recordOutputList){
                map.put(recordOutput.getRuleId(),new BigDecimal("0"));
            }
            //计算考核指标得分总和
            for(AppraisalEmployeeRecordOutput recordOutput:recordOutputList){
                for(Integer key:map.keySet()){
                    if(recordOutput.getRuleId().equals(key)){
                        map.put(key,map.get(key).add(recordOutput.getScore()));
                    }
                }
            }
            for(AppraisalPlanItemOutput planItemOutput:planItemOutputs){
                planItemOutput.setRuleScore(new BigDecimal("0"));
                planItemOutput.setQuantity(new BigDecimal("0"));
                for(Integer key:map.keySet()){
                    if(planItemOutput.getRuleId().equals(key)){
                        if(planItemOutput.getCumulativeLimit()!=null&&!planItemOutput.getCumulativeLimit().equals("")&&map.get(key).compareTo(planItemOutput.getCumulativeLimit())>=0){
                            planItemOutput.setRuleScore(planItemOutput.getCumulativeLimit());
                            planItemOutput.setQuantity(planItemOutput.getCumulativeLimit().divide(planItemOutput.getScore(),2, RoundingMode.HALF_UP));
                        }else {
                            planItemOutput.setRuleScore(map.get(key).abs());
                            planItemOutput.setQuantity(map.get(key).abs().divide(planItemOutput.getScore(),2,RoundingMode.HALF_UP));
                        }
                        continue;
                    }
                }
                planItemOutputList.add(planItemOutput);
            }
        }else {
            for(AppraisalPlanItemOutput planItemOutput:planItemOutputs){
                planItemOutput.setRuleScore(new BigDecimal("0"));
                planItemOutput.setQuantity(new BigDecimal("0"));
                planItemOutputList.add(planItemOutput);
            }
        }
        Map<Integer,BigDecimal> map1=new HashMap<Integer, BigDecimal>();
        for(AppraisalPlanItemOutput appraisalPlanOutput1:planItemOutputList){
            map1.put(appraisalPlanOutput1.getIndexId(),new BigDecimal("0"));
        }
        //计算考核指标得分总和
        for(AppraisalPlanItemOutput planItemOutput:planItemOutputList){
            BigDecimal bigDecimal=new BigDecimal("0");
            for(Integer key:map1.keySet()){
                if(planItemOutput.getIndexId().equals(key)){
                  if(planItemOutput.getScoreType()==1){
                      bigDecimal=bigDecimal.add(planItemOutput.getRuleScore().negate());
                  }else {
                      bigDecimal=bigDecimal.add(planItemOutput.getRuleScore());
                  }
                  map1.put(key,map1.get(key).add(bigDecimal));
                  continue;
                }
            }
        }
        BigDecimal bigDecimal=new BigDecimal("0");
        //更新考核指标得分
        for (AppraisalPlanItemOutput planItemOutput:planItemOutputList){
            AppraisalPlanItem appraisalPlanItem=new AppraisalPlanItem();
            for(Integer key:map1.keySet()){
                if(planItemOutput.getIndexId().equals(key)){
                    //考核规则得分和大于最大限额
                    if(planItemOutput.getScoreType()==0&&planItemOutput.getMaxBonus()!=null){
                        if(map1.get(key).compareTo(planItemOutput.getMaxBonus())>=0){
                            appraisalPlanItem.setIndexScore(planItemOutput.getMaxBonus().add(planItemOutput.getDatumValue()));
                        }else {
                            appraisalPlanItem.setIndexScore(map1.get(key).add(planItemOutput.getDatumValue()));
                        }
                    } else if(map1.get(key).add(planItemOutput.getDatumValue()).compareTo(new BigDecimal("0"))<0){
                        //考核规则得分和加上默认值小于0
                        appraisalPlanItem.setIndexScore(new BigDecimal("0"));
                    }else {
                        appraisalPlanItem.setIndexScore(map1.get(key).add(planItemOutput.getDatumValue()));
                    }
                    appraisalPlanItem.setRuleScore(planItemOutput.getRuleScore());
                    appraisalPlanItem.setQuantity(planItemOutput.getQuantity());
                    appraisalPlanItem.setId(planItemOutput.getId());
                    appraisalPlanItemService.update(planItemOutput.getId(),appraisalPlanItem);
                    continue;
                }
            }
        }
        return bigDecimal;
    }

    //统计上个月的员工的 迟到  早退  未打卡的考核得分
    @Transactional
    public BigDecimal getAttenceScore(AttendanceDailyDate attendanceDailyDates,List<AppraisalPlanItemOutput> planItemOutputs) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        List<AppraisalPlanItemOutput> itemOutputs=new ArrayList<AppraisalPlanItemOutput>();
        for(AppraisalPlanItemOutput appraisalPlanItemOutput:planItemOutputs){
            if(AppConsts.Belate_Interface.equals(appraisalPlanItemOutput.getDataType())
                    ||AppConsts.LeaveEarly_Interface.equals(appraisalPlanItemOutput.getDataType())
                    ||AppConsts.NotPunch_Interface.equals(appraisalPlanItemOutput.getDataType())){
                if(AppConsts.Belate_Interface.equals(appraisalPlanItemOutput.getDataType())){
                    if(attendanceDailyDates.getBeLateTimes()!=null){
                        appraisalPlanItemOutput.setQuantity(new BigDecimal(""+attendanceDailyDates.getBeLateTimes()));
                    }else {
                        appraisalPlanItemOutput.setQuantity(new BigDecimal("0"));
                    }
                }else if(AppConsts.LeaveEarly_Interface.equals(appraisalPlanItemOutput.getDataType())){
                    if(attendanceDailyDates.getLeaveEarlyTimes()!=null){
                        appraisalPlanItemOutput.setQuantity(new BigDecimal(""+attendanceDailyDates.getLeaveEarlyTimes()));
                    }else {
                        appraisalPlanItemOutput.setQuantity(new BigDecimal("0"));
                    }
                }else if(AppConsts.NotPunch_Interface.equals(appraisalPlanItemOutput.getDataType())){
                    if(attendanceDailyDates.getPunchTimes()!=null){
                        appraisalPlanItemOutput.setQuantity(new BigDecimal(""+attendanceDailyDates.getPunchTimes()));
                    }else {
                        appraisalPlanItemOutput.setQuantity(new BigDecimal("0"));
                    }
                }
                if(appraisalPlanItemOutput.getCumulativeLimit()==null){
                    appraisalPlanItemOutput.setRuleScore(appraisalPlanItemOutput.getScore().multiply(appraisalPlanItemOutput.getQuantity()));
                }else {
                appraisalPlanItemOutput.setRuleScore(appraisalPlanItemOutput.getScore().multiply(appraisalPlanItemOutput.getQuantity()).compareTo(appraisalPlanItemOutput.getCumulativeLimit())>0?appraisalPlanItemOutput.getCumulativeLimit():appraisalPlanItemOutput.getScore().multiply(appraisalPlanItemOutput.getQuantity()));
                }
                itemOutputs.add(appraisalPlanItemOutput);
            }
        }
        Map<Integer,BigDecimal> map=new HashMap<Integer, BigDecimal>();
        BigDecimal bigDecimal=new BigDecimal("0");
        if(itemOutputs!=null&&itemOutputs.size()>0){
            for(AppraisalPlanItemOutput appraisalPlanItemOutput:itemOutputs){
                map.put(appraisalPlanItemOutput.getIndexId(),new BigDecimal("0"));
            }
            for(AppraisalPlanItemOutput planItemOutput:itemOutputs){
                BigDecimal bigDecimal1=new BigDecimal("0");
                for(Integer key:map.keySet()){
                    if(planItemOutput.getIndexId().equals(key)){
                        if(planItemOutput.getScoreType()==1){
                            bigDecimal1=bigDecimal1.add(planItemOutput.getRuleScore().negate());
                        }else {
                            bigDecimal1=bigDecimal1.add(planItemOutput.getRuleScore());
                        }
                        map.put(key,map.get(key).add(bigDecimal1));
                        continue;
                    }
                }
            }
            for (AppraisalPlanItemOutput planItemOutput:itemOutputs){
                AppraisalPlanItem appraisalPlanItem=new AppraisalPlanItem();
                for(Integer key:map.keySet()){
                    if(planItemOutput.getIndexId().equals(key)){
                        if(map.get(key).compareTo(planItemOutput.getMaxBonus())>=0){
                            appraisalPlanItem.setIndexScore(planItemOutput.getMaxBonus().add(planItemOutput.getDatumValue()));
                        }else if(map.get(key).add(planItemOutput.getDatumValue()).compareTo(new BigDecimal("0"))<0){
                            appraisalPlanItem.setIndexScore(new BigDecimal("0"));
                        }else {
                            appraisalPlanItem.setIndexScore(map.get(key).add(planItemOutput.getDatumValue()));
                        }
                        PageData pageData=new PageData();
                        pageData.put("planId",planItemOutput.getPlanId());
                        pageData.put("indexId",planItemOutput.getIndexId());
                        pageData.put("indexScore",appraisalPlanItem.getIndexScore());
                        appraisalPlanItemService.updateByIndexId(pageData);
                        bigDecimal=bigDecimal.add(appraisalPlanItem.getIndexScore());
                        appraisalPlanItem.setRuleScore(planItemOutput.getRuleScore());
                        appraisalPlanItem.setQuantity(planItemOutput.getQuantity());
                        appraisalPlanItem.setId(planItemOutput.getId());
                        appraisalPlanItemService.update(planItemOutput.getId(),appraisalPlanItem);
                        continue;
                    }
                }
            }
        }
        return bigDecimal;
    }






    //给上个月的部门考核计划打分------------------------------------ start ---------------------------------------------------------------------------------------
    @Transactional
    public void setDepartmentPlanScore(String date,Users users,Integer templateId)  {
        //打分设置  系统默认值：0  手动调整 1  自动计算 3
          //1、系统默认值的打分已经在报表生成的时候设定（完成）
          //2、手动调整
          //3、自动计算，根据数据接口给考核规则打分
            //3.1 获得考核数据  考核计划  考核对象  明细表
        //----------------------------------获得上个月的年月的值
        Integer year = null;
        Integer month = null;
        Date time = null;
        if(date!=null&&!"".equals(date)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            try {
                time = simpleDateFormat.parse(date);
                String[] split = date.split("-");
                year = Integer.valueOf(split[0]);
                month = Integer.valueOf(split[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            Calendar instance = Calendar.getInstance();
            instance.setTime(new Date());
            instance.add(Calendar.MONTH,-1);
            year = instance.get(Calendar.YEAR);
            month = instance.get(Calendar.MONTH)+1;
            time = instance.getTime();
        }
        PageData pageData = new PageData();
        pageData.put("year",year);
        pageData.put("month",month);
        pageData.put("objectType", AppConsts.Window);
        pageData.put("time",time);
        pageData.put("templateId",templateId);
        List<AppraisalPlanItemOutput> list = appraisalPlanItemMapper.getPlanItemInfoAndOrgByDate(pageData);
        if(list==null||list.size()==0){
            return;
        }

        //数据接口  迟到：0  早退 1  未打卡 3  办件量 5  网上申报率 7  资源精简率 9 群众满意度 11
        Map<String, Integer> ruleMap = new HashMap<>();
        ruleMap.put("Amount_Interface",5);
        ruleMap.put("OnlineReportingRate_Interface",7);
        ruleMap.put("ResourceReductionRate_Interface",9);
        ruleMap.put("MassSatisfaction_Interface",11);
        //设定办件量的数量（数据来源于反馈信息）
        list = this.setAmountValue(list,pageData,AppConsts.Amount_Interface);
        //设定网上申报率的数量（数据来源办件库）
        list = this.setBjsbValue(list,pageData,AppConsts.OnlineReportingRate_Interface);
        //设定资源精简率数量（数据来源办件库）
        list = this.setCljjlValue(list,pageData,AppConsts.ResourceReductionRate_Interface);
        //设定群众满意度（数据来源办件库）
//        list = this.setMassSatisfaction(list,pageData,AppConsts.MassSatisfaction_Interface);
        //修改考核规则的值和对应考核指标的值并筛选出来需要更新考核明细的数据和需要更新考核计划得分的数据
        Map<String,Object> resultMap = this.getUpdatPlanDetailListAndPlanList(ruleMap,list);
        if(resultMap.get("planList")!=null){
            List<AppraisalPlan> planList = (List<AppraisalPlan>)resultMap.get("planList");
            for (AppraisalPlan appraisalPlan:planList) {
                appraisalPlan.setLastUpdateUserName(users.getUsername());
                appraisalPlan.setLastUpdateUserId(users.getId());
                appraisalPlan.setLastUpdateDateTime(new Date());
                appraisalPlan.setState(1);
                int i = appraisalPlanMapper.updateByPrimaryKeySelective(appraisalPlan);
            }
        }
        if(resultMap.get("planItemList")!=null){
            List<AppraisalPlanItem> planItemList = (List<AppraisalPlanItem>)resultMap.get("planItemList");
            for (AppraisalPlanItem appraisalPlanItem:planItemList) {
                appraisalPlanItemMapper.updateByPrimaryKeySelective(appraisalPlanItem);
            }
        }
        List<String> ids = new ArrayList<>();
        list = list.stream().filter(// 过滤去重
                v -> {
                    boolean flag = !ids.contains(v.getPlanId()+""+v.getIndexId());
                    ids.add(v.getPlanId()+""+v.getIndexId());
                    return flag;
                }
        ).collect(Collectors.toList());
        List<Config> configList = configService.getListByKey("bmkhbbsx");
        var departmentParameterMap = new HashMap<String,String>();
        for (Config config:configList) {
            departmentParameterMap.put(config.getConfigKey(),config.getConfigValue());
        }

        AssessmentDepartReport assessmentDepartReport = new AssessmentDepartReport();
        for (int i = 0;i<list.size();i++) {
            AppraisalPlanItemOutput appraisalPlanItemOutput = list.get(i);
            if(departmentParameterMap.containsValue(appraisalPlanItemOutput.getIndexId().toString())){
                if(assessmentDepartReport.getPlanId()!=null&&!assessmentDepartReport.getPlanId().equals(appraisalPlanItemOutput.getPlanId())){
                    assessmentDepartReport.setLastUpdateUserName(users.getUsername());
                    assessmentDepartReport.setLastUpdateUserId(users.getId());
                    assessmentDepartReport.setLastUpdateDateTime(new Date());
                    assessmentDepartReportMapper.updateByPlanId(assessmentDepartReport);
                    assessmentDepartReport = new AssessmentDepartReport();
                }
                assessmentDepartReport.setPlanId(appraisalPlanItemOutput.getPlanId());
                String key = this.getKeyByValue(departmentParameterMap,appraisalPlanItemOutput.getIndexId());
                try {
                    AttributesReflect.setValue(assessmentDepartReport, assessmentDepartReport.getClass(), key, AssessmentDepartReport.class.getDeclaredField(key).getType(), appraisalPlanItemOutput.getIndexScore());
                    assessmentDepartReport.setMouthIndex((assessmentDepartReport.getMouthIndex()==null?new BigDecimal(0):assessmentDepartReport.getMouthIndex()).add(appraisalPlanItemOutput.getIndexScore()==null?new BigDecimal(0):appraisalPlanItemOutput.getIndexScore()));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                if(i==list.size()-1){
                    assessmentDepartReport.setLastUpdateUserName(users.getUsername());
                    assessmentDepartReport.setLastUpdateUserId(users.getId());
                    assessmentDepartReport.setLastUpdateDateTime(new Date());
                    assessmentDepartReportMapper.updateByPlanId(assessmentDepartReport);
                }
            }
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


    private Map<String, Object> getUpdatPlanDetailListAndPlanList(Map<String, Integer> ruleMap, List<AppraisalPlanItemOutput> list) {
        //设定指标规则的值，并获得各计划各指标下的指标规则的差值
        BigDecimal ruleDifference = new BigDecimal(0);
        Map map = new HashMap<String,BigDecimal>();
        //先计算出总办件量
        HashMap maps = new HashMap<Integer,BigDecimal>();
        for (AppraisalPlanItemOutput o:list) {
            if(o.getIndexId().equals(AppConsts.personAvg)){
                if(maps.containsKey(o.getPlanId())){
                    BigDecimal val = (BigDecimal)maps.get(o.getPlanId());
                    maps.replace(o.getPlanId(),maps.get(o.getPlanId()),val.add(o.getQuantity()==null?new BigDecimal(0):o.getQuantity()));
                }else{
                    maps.put(o.getPlanId(),o.getQuantity()==null?new BigDecimal(0):o.getQuantity());
                }
            }
        }
        for(int i = 0;i<list.size();i++){
            AppraisalPlanItemOutput appraisalPlanItemOutput = list.get(i);
            if(!appraisalPlanItemOutput.getScoreSource().equals(AppConsts.System_Default)&&appraisalPlanItemOutput.getQuantity()!=null&&appraisalPlanItemOutput.getScore()!=null){
                BigDecimal ruleScore = null;
                ruleScore = this.calculationScore(appraisalPlanItemOutput,(BigDecimal) maps.get(appraisalPlanItemOutput.getPlanId()));
                if(appraisalPlanItemOutput.getCumulativeLimit()!=null){
                    ruleScore = ruleScore.compareTo(appraisalPlanItemOutput.getCumulativeLimit())>0?appraisalPlanItemOutput.getCumulativeLimit():ruleScore;
                    appraisalPlanItemOutput.setRuleScore(ruleScore);
                    ruleDifference = ruleDifference.add(ruleScore.multiply(appraisalPlanItemOutput.getScoreType()==AppConsts.Add_Points?new BigDecimal(1):new BigDecimal(-1)));
                }else{
                    appraisalPlanItemOutput.setRuleScore(ruleScore);
                    ruleDifference = ruleDifference.add(ruleScore.multiply(appraisalPlanItemOutput.getScoreType()==AppConsts.Add_Points?new BigDecimal(1):new BigDecimal(-1)));
                }
                if(i+1<list.size()){
                    if(!appraisalPlanItemOutput.getIndexId().equals(list.get(i+1).getIndexId())){
                        BigDecimal indexScore = this.getIndexScore(ruleDifference,appraisalPlanItemOutput);
                        map.put(appraisalPlanItemOutput.getPlanId()+""+appraisalPlanItemOutput.getIndexId(),indexScore);
                        ruleDifference = new BigDecimal(0);
                    }
                }
                if(i+1==list.size()){
                    BigDecimal indexScore = this.getIndexScore(ruleDifference,appraisalPlanItemOutput);
                    map.put(appraisalPlanItemOutput.getPlanId()+""+appraisalPlanItemOutput.getIndexId(),indexScore);
                }
            }
        }
        ArrayList<AppraisalPlanItem> planItems = new ArrayList<>();
        for (AppraisalPlanItemOutput appraisalPlanItemOutput:list) {
            if(appraisalPlanItemOutput.getPlanId()!=null&&appraisalPlanItemOutput.getIndexId()!=null&&map.containsKey(appraisalPlanItemOutput.getPlanId()+""+appraisalPlanItemOutput.getIndexId())){
                appraisalPlanItemOutput.setIndexScore((BigDecimal) map.get(appraisalPlanItemOutput.getPlanId()+""+appraisalPlanItemOutput.getIndexId()));
                planItems.add(appraisalPlanItemOutput);
            }
        }
        ArrayList<AppraisalPlan> planList = new ArrayList<>();
        BigDecimal finalScore = new BigDecimal(0);
        for (int i = 0;i<list.size();i++) {
            AppraisalPlanItemOutput appraisalPlanItemOutput = list.get(i);
            if(i+1<list.size()){
                if(appraisalPlanItemOutput.getPlanId()!=null&&list.get(i+1).getPlanId()!=null&&appraisalPlanItemOutput.getPlanId().equals(list.get(i+1).getPlanId())){
                    if(!appraisalPlanItemOutput.getIndexId().equals(list.get(i+1).getIndexId())){
                        finalScore = finalScore.add(appraisalPlanItemOutput.getIndexScore()==null?new BigDecimal(0):appraisalPlanItemOutput.getIndexScore());
                    }
                }else{
                    finalScore = finalScore.add(appraisalPlanItemOutput.getIndexScore()==null?new BigDecimal(0):appraisalPlanItemOutput.getIndexScore());
                    AppraisalPlan appraisalPlan = new AppraisalPlan();
                    appraisalPlan.setId(appraisalPlanItemOutput.getPlanId());
                    appraisalPlan.setFinalScore(finalScore);
                    planList.add(appraisalPlan);
                    finalScore = new BigDecimal(0);
                }
            }
            if(i+1==list.size()){
           /*     if(i-1>=0){
                    if(appraisalPlanItemOutput.getPlanId()!=null&&list.get(i-1).getPlanId()!=null&&appraisalPlanItemOutput.getPlanId().equals(list.get(i-1).getPlanId())){
                        if(!appraisalPlanItemOutput.getIndexId().equals(list.get(i-1).getIndexId())){
                            finalScore = finalScore.add(appraisalPlanItemOutput.getIndexScore()==null?new BigDecimal(0):appraisalPlanItemOutput.getIndexScore());
                        }
                    }else{
                        finalScore = finalScore.add(appraisalPlanItemOutput.getIndexScore()==null?new BigDecimal(0):appraisalPlanItemOutput.getIndexScore());
                    }
                }else{
                    finalScore = finalScore.add(appraisalPlanItemOutput.getIndexScore()==null?new BigDecimal(0):appraisalPlanItemOutput.getIndexScore());
                }*/
                finalScore = finalScore.add(appraisalPlanItemOutput.getIndexScore()==null?new BigDecimal(0):appraisalPlanItemOutput.getIndexScore());
                AppraisalPlan appraisalPlan = new AppraisalPlan();
                appraisalPlan.setId(appraisalPlanItemOutput.getPlanId());
                appraisalPlan.setFinalScore(finalScore);
                planList.add(appraisalPlan);
            }
        }
        Map<String, Object> returnval = new HashMap<>();
        returnval.put("planList",planList);
        returnval.put("planItemList",planItems);
        return  returnval;
    }

    private BigDecimal calculationScore(AppraisalPlanItem appraisalPlanItemOutput,BigDecimal sumQualitity) {
        //若是人均办件量需要将获得员工人数计算
        if(appraisalPlanItemOutput.getIndexId().equals(AppConsts.personAvg)){
            //根据考核计划明细获得部门人数
            Integer personQuantity = appraisalPlanItemMapper.getPersonQuantity(appraisalPlanItemOutput.getId());
            personQuantity = personQuantity==null||personQuantity==0?1:personQuantity;
            BigDecimal a=new BigDecimal(0);
            a=appraisalPlanItemOutput.getQuantity().multiply(appraisalPlanItemOutput.getScore());
            a = a.divide(new BigDecimal(personQuantity),2,BigDecimal.ROUND_HALF_UP);
            appraisalPlanItemOutput.setRatingDescription("办件量/部门人数*分值="+appraisalPlanItemOutput.getQuantity()+"/"+personQuantity+"*"+appraisalPlanItemOutput.getScore());
            return a;
        }
        if(appraisalPlanItemOutput.getIndexId().equals(AppConsts.jointRate)){
            //获得当前考核计划中人均办件量总数量
            PageData pageData = new PageData();
            pageData.put("itemId",appraisalPlanItemOutput.getId());
            pageData.put("indexId",AppConsts.personAvg);
            BigDecimal a= new BigDecimal(0);
            if(sumQualitity.compareTo(new BigDecimal(0))==0){
                appraisalPlanItemOutput.setRatingDescription("联办事项量/办件总量*100*分值="+appraisalPlanItemOutput.getQuantity()+"/"+sumQualitity+"*100*"+appraisalPlanItemOutput.getScore());
                return a;
            }
            a=appraisalPlanItemOutput.getQuantity().multiply(appraisalPlanItemOutput.getScore()).multiply(new BigDecimal(100));
            a = a.divide(sumQualitity,2,BigDecimal.ROUND_HALF_UP);
            appraisalPlanItemOutput.setRatingDescription("联办事项量/办件总量*100*分值="+appraisalPlanItemOutput.getQuantity()+"/"+sumQualitity+"*100*"+appraisalPlanItemOutput.getScore());
            return a;
        }
        BigDecimal a=appraisalPlanItemOutput.getQuantity().multiply(appraisalPlanItemOutput.getScore());
        return a;
    }

    private BigDecimal getIndexScore(BigDecimal ruleDifference, AppraisalPlanItemOutput appraisalPlanItemOutput) {
        BigDecimal indexScore = null;
        if(appraisalPlanItemOutput.getIndexScore()==null){
            if(ruleDifference.compareTo(new BigDecimal(0))>0){
                if(appraisalPlanItemOutput.getMaxBonus()==null){
                    indexScore = appraisalPlanItemOutput.getDatumValue()==null?new BigDecimal(0):appraisalPlanItemOutput.getDatumValue().add(ruleDifference);
                }else{
                    indexScore = ruleDifference.compareTo(appraisalPlanItemOutput.getMaxBonus())>0?appraisalPlanItemOutput.getMaxBonus().add(appraisalPlanItemOutput.getDatumValue()==null?new BigDecimal(0):appraisalPlanItemOutput.getDatumValue()):ruleDifference.add(appraisalPlanItemOutput.getDatumValue()==null?new BigDecimal(0):appraisalPlanItemOutput.getDatumValue());
                }
            }else{
                indexScore = appraisalPlanItemOutput.getDatumValue()==null?new BigDecimal(0):appraisalPlanItemOutput.getDefaultScore().add(ruleDifference).compareTo(new BigDecimal(0))>0?appraisalPlanItemOutput.getDefaultScore().add(ruleDifference):new BigDecimal(0);
            }
        }else{
            indexScore = appraisalPlanItemOutput.getIndexScore().add(ruleDifference);
            if(indexScore.compareTo(new BigDecimal(0))>0){
                BigDecimal datumValue = appraisalPlanItemOutput.getDatumValue()==null?new BigDecimal(0):appraisalPlanItemOutput.getDatumValue();
                BigDecimal maxBonus = appraisalPlanItemOutput.getMaxBonus() == null ? new BigDecimal(0) : appraisalPlanItemOutput.getMaxBonus();
                indexScore = indexScore.compareTo(datumValue.add(maxBonus))>0?datumValue.add(maxBonus):indexScore;
            }else{
                indexScore = new BigDecimal(0);
            }
        }
        return indexScore;
    }

    private List<AppraisalPlanItemOutput> setAmountValue(List<AppraisalPlanItemOutput> list, PageData pageData, Integer dateType) {
        List<FeedBackInfo> feedBackInfos = feedBackInfoMapper.findByDate(pageData);
        if(feedBackInfos==null||feedBackInfos.size()==0){
            return list;
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        for (FeedBackInfo feedBackInfo:feedBackInfos) {
            map.put(feedBackInfo.getOrganizationId(),feedBackInfo.getSizeVal());
        }
        for (AppraisalPlanItemOutput appraisalPlanItemOutput:list ) {
            if(appraisalPlanItemOutput.getScoreSource()==AppConsts.Automatic_Calculation&&dateType.equals(appraisalPlanItemOutput.getDataType())&&map.containsKey(appraisalPlanItemOutput.getOrganizationId())){
                if(appraisalPlanItemOutput.getQuantity()==null||appraisalPlanItemOutput.getQuantity().compareTo(new BigDecimal(0))==0){
                    AppraisalOrganizationOutput organizationOutput=organizationMapper.getById(appraisalPlanItemOutput.getOrganizationId());
                    //获取新登分中心的数据
                    List<AppraisalOrganizationOutput> organizationOutputList=organizationMapper.getByCopyOrganNo(organizationOutput.getOrgaNo());
                    if(organizationOutputList !=null || organizationOutputList.size()>0){
                        for(AppraisalOrganizationOutput appraisalOrganizationOutput:organizationOutputList){
                            if(map.containsKey(appraisalOrganizationOutput.getOrganizationId())){
                                Integer integer = map.get(appraisalPlanItemOutput.getOrganizationId());
                                Integer i=map.get(appraisalOrganizationOutput.getOrganizationId());
                                map.put(appraisalPlanItemOutput.getOrganizationId(),i+integer);
                            }
                        }
                    }
                    appraisalPlanItemOutput.setQuantity(new BigDecimal(map.get(appraisalPlanItemOutput.getOrganizationId())));
                }
            }
        }
        return list;
    }

    private List<AppraisalPlanItemOutput> setBjsbValue(List<AppraisalPlanItemOutput> list, PageData pageData,Integer dateType) {
        List<PreApasInfoOutput> preApasInfoOutputs = preApasInfoMapper.findByDate(pageData);
        if(preApasInfoOutputs==null||preApasInfoOutputs.size()==0){
            return list;
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        for (PreApasInfoOutput preApasInfoOutput:preApasInfoOutputs) {
            map.put(preApasInfoOutput.getOrganizationId(),preApasInfoOutput.getSize());
        }
        for (AppraisalPlanItemOutput appraisalPlanItemOutput:list ) {
            if(appraisalPlanItemOutput.getScoreSource()==AppConsts.Automatic_Calculation&&dateType.equals(appraisalPlanItemOutput.getDataType())&&map.containsKey(appraisalPlanItemOutput.getOrganizationId())){
                if(appraisalPlanItemOutput.getQuantity()==null||appraisalPlanItemOutput.getQuantity().compareTo(new BigDecimal(0))==0){

                    appraisalPlanItemOutput.setQuantity(new BigDecimal(map.get(appraisalPlanItemOutput.getOrganizationId())));
                }
            }
        }
        return list;
    }

    private List<AppraisalPlanItemOutput> setCljjlValue(List<AppraisalPlanItemOutput> list, PageData pageData, Integer dateType) {
        Integer year = Integer.valueOf(pageData.get("year").toString());
        Integer month =Integer.valueOf(pageData.get("month").toString());
        List<HchenStatisticalOutput> countList = (List<HchenStatisticalOutput>)assessmentDepartReportService.getCountList(year, month).getData();
        HashMap<Integer, Integer> map = new HashMap<>();
        for (HchenStatisticalOutput hchenStatisticalOutput:countList) {
            map.put(hchenStatisticalOutput.getOrganizationId(),hchenStatisticalOutput.getCount());
        }
        for (AppraisalPlanItemOutput appraisalPlanItemOutput:list ) {
            if(appraisalPlanItemOutput.getScoreSource()==AppConsts.Automatic_Calculation&&dateType.equals(appraisalPlanItemOutput.getDataType())&&map.containsKey(appraisalPlanItemOutput.getOrganizationId())){
                if(appraisalPlanItemOutput.getQuantity()==null||appraisalPlanItemOutput.getQuantity().compareTo(new BigDecimal(0))==0){
                    appraisalPlanItemOutput.setQuantity(new BigDecimal(map.get(appraisalPlanItemOutput.getOrganizationId())));
                }
            }
        }
        return list;
    }

    private List<AppraisalPlanItemOutput> setMassSatisfaction(List<AppraisalPlanItemOutput> list, PageData pageData, Integer dateType) {
        Integer year = Integer.valueOf(pageData.get("year").toString());
        Integer month =Integer.valueOf(pageData.get("month").toString());
        List<HchenStatisticalOutput> countList = (List<HchenStatisticalOutput>)assessmentDepartReportService.getCountList(year, month).getData();
        HashMap<Integer, Integer> map = new HashMap<>();
        for (HchenStatisticalOutput hchenStatisticalOutput:countList) {
            map.put(hchenStatisticalOutput.getOrganizationId(),hchenStatisticalOutput.getCount());
        }
        for (AppraisalPlanItemOutput appraisalPlanItemOutput:list ) {
            if(appraisalPlanItemOutput.getScoreSource()==AppConsts.Automatic_Calculation&&dateType.equals(appraisalPlanItemOutput.getDataType())&&map.containsKey(appraisalPlanItemOutput.getOrganizationId())){
                if(appraisalPlanItemOutput.getQuantity()==null||appraisalPlanItemOutput.getQuantity().compareTo(new BigDecimal(0))==0){
                    appraisalPlanItemOutput.setQuantity(new BigDecimal(map.get(appraisalPlanItemOutput.getOrganizationId())));
                }
            }
        }
        return list;
    }
        //给上个月的部门考核计划打分-------------------------------- end ---------------------------------------------------------------------------------------

    public List<AttendanceDailyDate> AttendanceMonthStatic(String date){
        List<AttendanceDailyDate> list=new ArrayList<AttendanceDailyDate>();
        List<AttendanceStatistics> statisticsList=attendanceStatisticsMapper.selectByDate(date);
        List<AttendanceStatistics> result = new LinkedList<>();
        if(statisticsList!=null){
            for (var o : statisticsList) {
                boolean b = result.stream().anyMatch(p -> p.getEmployeeId().compareTo(o.getEmployeeId())==0);
                if (!b) {
                    result.add(o);
                }
            }
            for(AttendanceStatistics attendanceStatistics:result){
                AttendanceDailyDate attendanceDailyDate=new AttendanceDailyDate();
                int a=0;//迟到次数
                int b=0;//早退次数
                int c=0;//未打卡次数
                int d=0;//缺勤时间
                for(AttendanceStatistics statistics:statisticsList){
                    if(attendanceStatistics.getEmployeeId().equals(statistics.getEmployeeId())){
                        if(statistics.getStatusName().equals("异常")){
                            if(statistics.getBeLate().equals("是")){
                                a++;
                            }else if(statistics.getLeaveEarly().equals("是")){
                                b++;
                            }else if(statistics.getPunch().equals("是")){
                                c++;
                            }
                        }
                        if(statistics.getAbsenceHours()!=null){
                            d+=statistics.getAbsenceHours();
                        }
                    }
                }
                attendanceDailyDate.setTotalHours(d);
                attendanceDailyDate.setEmployeeId(attendanceStatistics.getEmployeeId());
                attendanceDailyDate.setBeLateTimes(a);
                attendanceDailyDate.setLeaveEarlyTimes(b);
                attendanceDailyDate.setPunchTimes(c);
                attendanceDailyDate.setTotal(a+b+c);
                list.add(attendanceDailyDate);
            }
        }

        return list;
    }

    public ResponseResult updateFinalScoreById(Integer id, Double finalScore) {
        AppraisalPlan appraisalPlan = new AppraisalPlan();
        appraisalPlan.setId(id);
        appraisalPlan.setFinalScore(new BigDecimal(finalScore));
        appraisalPlan.setLastUpdateDateTime(new Date());
        appraisalPlan.setLastUpdateUserId(getUsers().getId());
        appraisalPlan.setLastUpdateUserName(getUsers().getUsername());
        int i = appraisalPlanMapper.updateByPrimaryKeySelective(appraisalPlan);
        if(i>0){
            return ResponseResult.success();
        }else{
            return ResponseResult.error("更新失败");
        }
    }

    public List<AppraisalPlanOutput> findDepartmentPlanWithNoPage(PageData pageData) {
        List<AppraisalPlanOutput> appraisalPlanOutputs = appraisalPlanMapper.selectDeparmentPlan(pageData);
        return appraisalPlanOutputs;
    }
}
