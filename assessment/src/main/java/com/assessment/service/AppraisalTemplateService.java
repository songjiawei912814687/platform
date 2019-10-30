package com.assessment.service;

import com.assessment.core.base.BaseMapper;
import com.assessment.core.base.BaseService;
import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.core.util.AppConsts;
import com.assessment.domian.input.IndexRuleInput;
import com.assessment.domian.input.TemplateIndexRuleInput;
import com.assessment.domian.output.*;
import com.assessment.mapper.jpa.*;
import com.assessment.mapper.mybatis.*;
import com.assessment.model.*;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service

public class AppraisalTemplateService extends BaseService<AppraisalTemplateOutput, AppraisalTemplate,Integer> {

    @Autowired
    private AppraisalTemplateMapper appraisalTemplateMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AppraisalTemplateTargetMapper appraisalTemplateTargetMapper;

    @Autowired
    private AppraisalTemplateRepository appraisalTemplateRepository;

    @Autowired
    private AppraisalTemplateTargetRepository appraisalTemplateTargetRepository;

    @Autowired
    private AppraisalPlanRepository appraisalPlanRepository;

    @Autowired
    private AppraisalTemplateRuleMapper appraisalTemplateRuleMapper;

    @Autowired
    private AppraisalPlanMapper appraisalPlanMapper;

    @Autowired
    private  AppraisalRuleMapper appraisalRuleMapper;

    @Autowired
    private AppraisalTemplateRuleRepository appraisalTemplateRuleRepository;

    @Autowired
    private AppraisalTemplateIndexRepository appraisalTemplateIndexRepository;

    @Autowired
    private AppraisalPlanItemRepository appraisalPlanItemRepository;

    @Autowired
    private AppraisalPlanItemMapper appraisalPlanItemMapper;

    @Autowired
    private AppraisalIndexMapper appraisalIndexMapper;

    @Autowired
    private  AssessmentDepartReportMapper assessmentDepartReportMapper;

    @Autowired
    private  AssessmentDepartReportRepository assessmentDepartReportRepository;

    @Autowired
    private AppraisalPlanService appraisalPlanService;

    @Override
    public BaseMapper<AppraisalTemplate, Integer> getMapper() {
        return appraisalTemplateRepository;
    }

    public MybatisBaseMapper<AppraisalTemplateOutput> getMybatisBaseMapper() {
        return appraisalTemplateMapper;
    }

    public boolean isAbleToAdd(AppraisalTemplate appraisalTemplate) {
        if(appraisalTemplate.getObjectType()==AppConsts.StaffMember){
            appraisalTemplate.setState(AppConsts.start);
            List<AppraisalTemplateOutput> template = appraisalTemplateMapper.selectByObjectTypeAndState(appraisalTemplate);
            if(template!=null&&template.size()>0){
                return false;
            }else{
                return true;
            }
        }
        return true;
    }

    @Transactional
    public int logicDelete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException,MethodArgumentNotValidException,MethodArgumentNotValidException {
        var strs = idList.split(",");
        for(var str : strs){
            var id = Integer.parseInt(str);
            AppraisalTemplate t =  getMapper().getById(id);
            if(t==null){
                continue;
            }
            setProperty(t, "amputated", 1);
            this.update(id,t);
            //删除关联表：考核模板和考核规则，考核模板和指标分类，考核模板和考核对象的数据
            appraisalTemplateRuleRepository.deleteAllByTemplateId(t.getId());
            appraisalTemplateIndexRepository.deleteAllByTemplateId(t.getId());
            appraisalTemplateTargetMapper.deleteByTemplate(t.getId());
        }
        return SUCCESS;
    }

    @Transactional
    public int add(AppraisalTemplate appraisalTemplate,String objectIdList) throws IllegalAccessException, IntrospectionException, InvocationTargetException,MethodArgumentNotValidException{
        //先添加模板并获得模板的id,用于添加对象
        int result = this.add(appraisalTemplate);
        if(result<0){
            return result;
        }
        int addResult = this.addAppraisalTemplateObject(appraisalTemplate,objectIdList,result);
        if(addResult<0){
            return addResult;
        }
        return addResult;
    }

    private int addAppraisalTemplateObject(AppraisalTemplate appraisalTemplate,String objectIdList, int result) {
        String[] splits = null;
        if(objectIdList!=null&&objectIdList.length()>0){
            splits = objectIdList.split(",");
        }
        if(splits!=null&&splits.length>0){
            for (String str:splits) {
                AppraisalTemplateTarget target = new AppraisalTemplateTarget();
                target.setObjectType(appraisalTemplate.getObjectType());
                if(target.getObjectType()==AppConsts.Window){
                    target.setOrganizationId(Integer.valueOf(str));
                }else{
                    target.setRoleId(Integer.valueOf(str));
                }
                target.setTemplateId(result);
                AppraisalTemplateTarget save = appraisalTemplateTargetRepository.save(target);
                if(save==null){
                    return -1;
                }
            }
        }
        return  1;
    }

    @Transactional
    public int update(Integer id,AppraisalTemplate input,String objectIdList) throws IllegalAccessException, IntrospectionException, InvocationTargetException,MethodArgumentNotValidException{
        //先查找是否有考核对象，根据id删除模板关联的对象后重新新增考核对象
        List<AppraisalTemplateTarget> list = appraisalTemplateTargetMapper.findByTemplateId(input.getId());
        if(list!=null&&list.size()>0){
            int delete  = appraisalTemplateTargetMapper.deleteByTemplate(input.getId());
            if(delete==0){
                return  ERROR;
            }
        }
        int update = this.update(id,input);
        if(update>0){
            return this.addAppraisalTemplateObject(input,objectIdList,input.getId());
        }else{
            return ERROR;
        }
    }

    public boolean nameIsRepeat(Integer id, AppraisalTemplate appraisalTemplate) {
        if(id==null){
            List<AppraisalTemplate> list= appraisalTemplateRepository.findByNameAndAmputated(appraisalTemplate.getName(),appraisalTemplate.getAmputated());
            if(list==null||list.size()==0){
                return false;
            }else{
                return true;
            }
        }else{
            List<AppraisalTemplate> list =  appraisalTemplateMapper.findByNameNotId(appraisalTemplate);
            if(list==null||list.size()==0){
                return false;
            }else{
                return true;
            }
        }
    }

    public AppraisalTemplateOutput selectById(Integer id){
        AppraisalTemplateOutput appraisalTemplateOutput = appraisalTemplateMapper.selectByPrimaryKey(id);
        Map<String, String> map = this.getObjectNameList(appraisalTemplateOutput);
        appraisalTemplateOutput.setObjectNameList(map.get("objectNameList"));
        appraisalTemplateOutput.setObjectIdList(map.get("objectIdList"));
        return appraisalTemplateOutput;
    }

    @Transactional
    public String updatestate(Integer id,Integer state) throws IllegalAccessException, IntrospectionException, InvocationTargetException,MethodArgumentNotValidException {
            AppraisalTemplate t =  getMapper().getById(id);
            //判断是否有
            if(t==null){
                return "模板不存在";
            }
            //若使用则判断模板对象是否已经被其他启用的模板作为对象，若有则提升哪些对象已经被使用
        StringBuilder stringBuilder = new StringBuilder("");
            //员工考核模板不能同时启用多个
        if(state==AppConsts.start&&t.getObjectType()==AppConsts.StaffMember){
            AppraisalTemplate appraisalTemplate = new AppraisalTemplate();
            appraisalTemplate.setState(AppConsts.start);
            appraisalTemplate.setObjectType(AppConsts.StaffMember);
            List<AppraisalTemplateOutput> appraisalTemplateOutputs = appraisalTemplateMapper.selectByObjectTypeAndState(appraisalTemplate);
            if(appraisalTemplateOutputs!=null&&appraisalTemplateOutputs.size()>0){
                return "不能同时启用多个员工考核模板";
            }
        }
        if(state==AppConsts.start){
            PageData pageData = new PageData();
            pageData.put("templateId",id);
            pageData.put("state",AppConsts.start);
            pageData.put("type",t.getType());
            if(t.getObjectType()==AppConsts.Window){
                List<AppraisalOrganizationOutput> list = organizationMapper.judgeOrganizationIsInUse(pageData);
                if(list!=null&&list.size()>0){
                    for (AppraisalOrganizationOutput appraisalOrganizationOutput:list ) {
                        stringBuilder.append(appraisalOrganizationOutput.getOrganizationName()+",");
                    }
                }
            }
            if(!"".equals(stringBuilder.toString())){
                return stringBuilder.substring(0,stringBuilder.length()-1)+"对象已经被其他模板使用，请在编辑页面更改对象为未勾选状态并保存后方重新启用";
            }
        }
            setProperty(t, "state", state);
            var result = this.update(id,t);
            if(result==ERROR){
                return String.valueOf(result);
            }else{
                return "0";
            }

    }

    public List<AppraisalOrganizationOutput> getAppraisalOrganization(PageData pageData) {
        //排除启用模板的考核对象
        List<AppraisalOrganizationOutput> appraisalOrganizationList = organizationMapper.getAppraisalOrganization(pageData);
        return appraisalOrganizationList;
    }

    public List<Role> getRolesNotInUse(PageData pageData) {
        //排除已经被启用的考核模板使用的角色
        List<Role> list = roleMapper.getRolesNotInUse(pageData);
        return list;
    }

    public List<TemplateSetting> getAllIndexRule(Integer templatetId) {
        //1、根据模板的类型和对象类型获得对应的指标分类和对应的考核规则
        //1.1 获得考核指标 部门的的年度考核指标是常态效能类型的指标，部门的月度考核指标是动态效能类型的指标，人员的月度考核指标是工作人员考核类型的指标
        AppraisalTemplate byId = appraisalTemplateRepository.getById(templatetId);
        if(byId==null){
            return null;
        }
        Integer indexType = 0;
        if(byId.getObjectType()== AppConsts.Window&&byId.getType()==AppConsts.Monthly_Assessment){
            indexType = AppConsts.Dynamic_Index;
        }else if(byId.getObjectType()== AppConsts.Window&&byId.getType()==AppConsts.Annual_Assessment){
            indexType = AppConsts.Normal_Index;
        }else if(byId.getObjectType()== AppConsts.StaffMember&&byId.getType()==AppConsts.Monthly_Assessment){
            indexType = AppConsts.StaffMember_Index;
        }
        PageData pageData = new PageData();
        pageData.put("indexType",indexType);
        pageData.put("emplatetId",templatetId);
        //根据类型获得关联的指标和考核规则
        List<AppraisalRuleOutput> appraisalRuleOutputList = appraisalRuleMapper.selectRuleRelationIndex(pageData);
        ArrayList<TemplateSetting> templateSettings = new ArrayList<>();
        if(appraisalRuleOutputList==null&&appraisalRuleOutputList.size()==0){
            return null;
        }else{
            AppraisalRuleOutput currentObj = null;
            TemplateSetting templateSetting = new TemplateSetting();
            for (int i = 0 ;i<appraisalRuleOutputList.size();i++){
                currentObj = appraisalRuleOutputList.get(i);
                TemplateSetting child = new TemplateSetting();
                child.setName(currentObj.getName());
                child.setId(currentObj.getId());
                child.setParentId(currentObj.getAppraisalId());
                child.setType(AppConsts.Rule_type);
                child.setHasChild(0);
                child.setCheckState(currentObj.getTemplateRuleId()==null?false:true);
                //若存在下一个则判断当前指标和下一个指标是否相等
                if(i+1<appraisalRuleOutputList.size()){
                    //若不相等则存入templateSetting对象
                    if(!currentObj.getAppraisalId().equals(appraisalRuleOutputList.get(i+1).getAppraisalId())){
                        templateSetting = this.addChildren(templateSetting,child,currentObj);
                        templateSettings.add(templateSetting);
                        templateSetting = new TemplateSetting();
                    }else{
                        // 若相等则添加当前的规则信息到当前的对象的child集合中
                        templateSetting = this.createNewAndAddChildren(templateSetting,currentObj,child);
                    }
                }else{
                    //若不存在下一个则和前面的对象比较
                        //若前面不存在对象
                        if(i-1<0){
                            templateSetting = this.createNewAndAddChildren(templateSetting,currentObj,child);
                            templateSettings.add(templateSetting);
                        }else{
                            //若当前指标和前面的指标相等则添加当前考核规则到当前templateSetting后添加到返回的结果集中
                            if(currentObj.getAppraisalId().equals(appraisalRuleOutputList.get(i-1))){
                                templateSetting = this.addChildren(templateSetting,child,currentObj);
                                templateSettings.add(templateSetting);
                            }else{
                                templateSetting = this.createNewAndAddChildren(templateSetting,currentObj,child);
                                templateSettings.add(templateSetting);
                            }
                        }

                }
            }
        }
        return templateSettings;
    }

    private TemplateSetting createNewAndAddChildren(TemplateSetting templateSetting,AppraisalRuleOutput currentObj,TemplateSetting child) {
        if(templateSetting.getId()==null){
            templateSetting.setId(currentObj.getAppraisalId());
            templateSetting.setParentId(-1);
            templateSetting.setName(currentObj.getIndexName());
            templateSetting.setType(AppConsts.Index_Type);
            templateSetting.setCheckState(currentObj.getTemplateIndexId()==null?false:true);
        }
        List<TemplateSetting> childrenList = templateSetting.getChildren()==null?new ArrayList<>():templateSetting.getChildren();
        if(child.getId()!=null){
            childrenList.add(child);
        }
        templateSetting.setChildren(childrenList);
        templateSetting.setHasChild(childrenList.size());
        return templateSetting;
    }

    private TemplateSetting addChildren(TemplateSetting templateSetting, TemplateSetting child,AppraisalRuleOutput currentObj) {
        List<TemplateSetting> childrenList = templateSetting.getChildren();
        if (templateSetting.getId()==null){
            templateSetting.setId(currentObj.getAppraisalId());
            templateSetting.setParentId(-1);
            templateSetting.setName(currentObj.getIndexName());
            templateSetting.setType(AppConsts.Index_Type);
            templateSetting.setCheckState(currentObj.getTemplateIndexId()==null?false:true);
        }
        if(childrenList ==null){
            childrenList = new ArrayList<>();
        }
        if(child.getId()!=null){
            childrenList.add(child);
        }
        templateSetting.setChildren(childrenList);
        templateSetting.setHasChild(childrenList.size());
        return templateSetting;
    }

    public List<AppraisalTemplateOutput> selectAll(boolean isPage, PageData pageData){
        if(isPage){
            Integer pagesize = pageData.getRows();
            Integer page = pageData.getPageIndex();
            PageHelper.startPage(page, pagesize);
            Page<AppraisalTemplateOutput> pageList = appraisalTemplateMapper.selectPage(pageData);
            pageList = this.setObjectNameList(pageList);
            return pageList;
        }
        List<AppraisalTemplateOutput> list = getMybatisBaseMapper().selectAll(pageData);
        return  this.setObjectNameList((Page<AppraisalTemplateOutput>)list);
    }

    private Page<AppraisalTemplateOutput> setObjectNameList(Page<AppraisalTemplateOutput> pageList) {
        for (AppraisalTemplateOutput appraisalTemplateOutput:pageList) {
            //根据模板id获得对象结果集
            Map<String,String> map = this.getObjectNameList(appraisalTemplateOutput);
            String objectNameList = map.get("objectNameList");
            appraisalTemplateOutput.setObjectNameList(objectNameList);
        }
        return  pageList;
    }

    private Map<String,String> getObjectNameList(AppraisalTemplateOutput appraisalTemplateOutput) {
        String objectNameList = "";
        String objectIdList = "";
        PageData pageData = new PageData();
        pageData.put("templateId",appraisalTemplateOutput.getId());
        pageData.put("objectType",appraisalTemplateOutput.getObjectType());
        List<AppraisalTemplateOutput> appraisalTemplateOutputList = null;
        if (appraisalTemplateOutput.getObjectType() == AppConsts.Window) {
                appraisalTemplateOutputList = appraisalTemplateMapper.selectByTemplateIdAndWindowType(pageData);
            if(appraisalTemplateOutputList!=null&&appraisalTemplateOutputList.size()>0){
                StringBuilder nameList =  new StringBuilder("");
                StringBuilder idList =  new StringBuilder("");
                if(appraisalTemplateOutput.getObjectType()==AppConsts.Window){
                    for (AppraisalTemplateOutput AppraisalTemplateOutput:appraisalTemplateOutputList) {
                        if(AppraisalTemplateOutput.getOrganizationName()!=null&&!"".equals(AppraisalTemplateOutput.getOrganizationName())){
                            nameList.append(AppraisalTemplateOutput.getOrganizationName()+",");
                            idList.append(AppraisalTemplateOutput.getOrganizationId()+",");
                        }
                    }
                    objectNameList = nameList.toString().substring(0,nameList.length()-1);
                    objectIdList = idList.toString().substring(0,idList.length()-1);
                }
            }
        }else{
            objectNameList ="所有考核部门下的后台和窗口人员";
            objectIdList = "";
        }
//        else if (appraisalTemplateOutput.getObjectType() == AppConsts.StaffMember) {
//                appraisalTemplateOutputList = appraisalTemplateMapper.selectByTemplateIdAndRoleType(pageData);
//        }

//            if(appraisalTemplateOutput.getObjectType()==AppConsts.StaffMember){
//                for (AppraisalTemplateOutput AppraisalTemplateOutput:appraisalTemplateOutputList) {
//                    System.out.println(AppraisalTemplateOutput.getRoleName()!=null);
//                    if(AppraisalTemplateOutput.getRoleName()!=null&&!"".equals(AppraisalTemplateOutput.getRoleName())){
//                        nameList.append(AppraisalTemplateOutput.getRoleName()+",");
//                        idList.append(AppraisalTemplateOutput.getRoleId()+",");
//                    }
//                }
//                objectNameList = nameList.toString().substring(0,nameList.length()-1);
//                objectIdList = idList.toString().substring(0,idList.length()-1);
//            }

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("objectNameList",objectNameList);
        stringStringHashMap.put("objectIdList",objectIdList);
        return stringStringHashMap;
    }

    @Transactional
    public int savaIndexRuleInfoByEmplatetId(Integer templatetId, @RequestBody TemplateIndexRuleInput templateIndexRuleInput) throws Exception{
        List<IndexRuleInput> indexRuleInputs = templateIndexRuleInput.getIndexRuleInputs();
        //先根据模板id删除模板指标和模板考核规则中的信息
        appraisalTemplateIndexRepository.deleteAllByTemplateId(templatetId);
        appraisalTemplateRuleRepository.deleteAllByTemplateId(templatetId);
        if (indexRuleInputs!=null&&indexRuleInputs.size() > 0) {
            AppraisalTemplateRule appraisalTemplateRule = new AppraisalTemplateRule();
            AppraisalTemplateIndex appraisalTemplateIndex = new AppraisalTemplateIndex();
            for (IndexRuleInput indexRuleInput : indexRuleInputs) {
                if (indexRuleInput.getType() == AppConsts.Index_Type) {
                    appraisalTemplateIndex = new AppraisalTemplateIndex();
                    appraisalTemplateIndex.setIndexId(indexRuleInput.getId());
                    appraisalTemplateIndex.setTemplateId(templatetId);
                    AppraisalTemplateIndex save = appraisalTemplateIndexRepository.save(appraisalTemplateIndex);
                    if (save == null) {
                        return ERROR;
                    }
                }
                if (indexRuleInput.getType() == AppConsts.Rule_type) {
                    appraisalTemplateRule = new AppraisalTemplateRule();
                    appraisalTemplateRule.setRuleId(indexRuleInput.getId());
                    appraisalTemplateRule.setTemplateIndexId(indexRuleInput.getParentId());
                    appraisalTemplateRule.setTemplateId(templatetId);
                    AppraisalTemplateRule save = appraisalTemplateRuleRepository.save(appraisalTemplateRule);
                    if (save == null) {
                        return ERROR;
                    }
                }
            }
        }
        //重新设定模板的基准分，基准分为考核指标的基准分的和
        ArrayList<Integer> idList = new ArrayList<>();
        for (IndexRuleInput indexRuleInput : indexRuleInputs) {
            if(indexRuleInput.getType() == AppConsts.Index_Type){
                idList.add(indexRuleInput.getId());
            }
        }
        AppraisalTemplate appraisalTemplate = new AppraisalTemplate();
        appraisalTemplate.setId(templatetId);
        if(idList==null||idList.size()==0){
            appraisalTemplate.setDatumValue(new BigDecimal(0));
        }else{
            Integer val = appraisalIndexMapper.sumdatumValue(idList);
            appraisalTemplate.setDatumValue(new BigDecimal(val));
        }
        int update = this.update(templatetId, appraisalTemplate);
        if(update<=0){
            return ERROR;
        }
        return SUCCESS;
    }

//    @Transactional
    @Async
    public void createPlanAndPlanDetail(Integer templatetId, Integer year, Integer month, Users users) throws  Exception {
        //若未传入年则默认为系统时间所在的年
        Calendar date = Calendar.getInstance();
        if(year==null){
            year =date.get(Calendar.YEAR);
        }
        AppraisalTemplate byId = appraisalTemplateRepository.getById(templatetId);
        if(byId.getType()==AppConsts.Monthly_Assessment){
            //若未传入月则默认为系统时间所在的月
            if(month==null){
                month =date.get(Calendar.MONTH)+1;
            }
        }
        //1 判断之前是否已经生成过考核计划，若生成过考核计划则删除考核计划(假删除)
        this.deleteRemainPlan(templatetId,year,month,byId,users);
        //2、生成考核计划,并生成考核计划明细
        //3、若生成的考核计划的时间是当前月之前则生成打分
        this.createDate(templatetId,byId,year,month,users);
    }

    public void createDate(Integer templatetId, AppraisalTemplate byId, Integer year, Integer month,Users users) throws Exception{
        this.generatePlanAndPlanItem(templatetId,byId,year,month,users);
        if(year!=null&&month!=null){
            Calendar instance = Calendar.getInstance();
            instance.set(Calendar.YEAR,year);
            instance.set(Calendar.MONTH,month-1);
            Date date1 = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            try {
                String format = simpleDateFormat.format(date1);
                date1 = simpleDateFormat.parse(format);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //判断当前是否为一号，若是一号则部门考核计划生成上个月之前的给打分，若不是一号则生成上个月及上个月之前的给打分
            if(byId.getObjectType()==AppConsts.Window) {
                Calendar instance1 = Calendar.getInstance();
                Calendar instance2 = Calendar.getInstance();
                instance1.setTime(new Date());
                instance2.setTime(instance.getTime());
                if(instance1.get(Calendar.DATE)==1){
                    instance2.set(Calendar.DATE,1);
                }else{
                    instance2.set(Calendar.DATE,0);
                }
                instance2.add(Calendar.MONTH,1);
                if(instance2.getTime().before(date1)){
                    appraisalPlanService.setDepartmentPlanScore(simpleDateFormat.format(instance.getTime()),users,templatetId);
                }
            }
            if(byId.getObjectType()==AppConsts.StaffMember){
                appraisalPlanService.updateFinalScore(simpleDateFormat.format(instance.getTime()));
            }
        }
    }

    private int generatePlanAndPlanItem(Integer templatetId, AppraisalTemplate byId, Integer year, Integer month,Users users) {
        //若生成员工月考核计划，则生成的员工考核计划,生成条数与考核对象所有角色下的人员去除条数相同
        PageData pageData = new PageData();
        pageData.put("templateId",templatetId);
        List<AppraisalTemplateOutput> appraisalplans = null;
        if(byId.getObjectType().equals(AppConsts.StaffMember)){
            /*List<Integer> ids = new ArrayList<>();
            appraisalplans = appraisalTemplateMapper.selectAppraisalEmployees(pageData);
            appraisalplans = appraisalplans.stream().filter(// 过滤去重
                    v -> {
                        boolean flag = !ids.contains(v.getEmployeeId());
                        ids.add(v.getEmployeeId());
                        return flag;
                    }
            ).collect(Collectors.toList());*/
            //获得所有的考核部门下的窗口和后台人员
            appraisalplans = appraisalTemplateMapper.selectAllAppraisalEmployees();
        }else{
            appraisalplans = appraisalTemplateMapper.selectAppraisalOrganization(pageData);
        }

        for (AppraisalTemplateOutput appraisalTemplate:appraisalplans) {
            AppraisalPlan addAppraisalPlan= new AppraisalPlan();
            addAppraisalPlan.setTemplateId(templatetId);
            addAppraisalPlan.setYear(year);
            if(byId.getType()==AppConsts.Monthly_Assessment){
                addAppraisalPlan.setMonth(month);
                addAppraisalPlan.setName(String.valueOf(year)+"年"+String.valueOf(month)+"月"+byId.getName());
            }else{
                addAppraisalPlan.setName(String.valueOf(year)+"年"+byId.getName());
            }
            if(byId.getObjectType()==AppConsts.Window){
                addAppraisalPlan.setOrganizationId(appraisalTemplate.getOrganizationId());
            }else{
                addAppraisalPlan.setEmployeeId(appraisalTemplate.getEmployeeId());
            }
            addAppraisalPlan.setType(byId.getType());
            addAppraisalPlan.setState(AppConsts.Unreviewed);
            addAppraisalPlan.setEmployeeId(appraisalTemplate.getEmployeeId());
            addAppraisalPlan.setAmputated(0);
            addAppraisalPlan.setCreatedDateTime(new Date());
            addAppraisalPlan.setCreatedUserId(users.getId());
            addAppraisalPlan.setCreatedUserName(users.getUsername());
            addAppraisalPlan.setLastUpdateUserId(users.getId());
            addAppraisalPlan.setLastUpdateDateTime(new Date());
            addAppraisalPlan.setLastUpdateUserName(users.getUsername());
            addAppraisalPlan.setType(byId.getType());
            addAppraisalPlan.setDatumValue(byId.getDatumValue());
            addAppraisalPlan.setIsStar(0);
            addAppraisalPlan.setObjectType(byId.getObjectType());
            AppraisalPlan save = appraisalPlanRepository.save(addAppraisalPlan);
            //生成考核明细
            BigDecimal result = this.generatePlanItem(save);
            //若是部门考核模板则添加部门考核报表
            if(byId.getObjectType()==AppConsts.Window){
                this.generateDepartReport(save,byId,year,month,appraisalTemplate,users);
            }
            if(result.compareTo(new BigDecimal(0))<0){
                return -1;
            }else{
                if(save.getObjectType()==AppConsts.Window&&save.getType()==AppConsts.Annual_Assessment){
                    save.setFinalScore(result);
                    save.setState(1);
                    appraisalPlanRepository.save(save);
                }
            }
        }
        return 1;
    }

    private int generateDepartReport(AppraisalPlan save,AppraisalTemplate byId,Integer year,Integer month,AppraisalTemplateOutput appraisalTemplate,Users users) {
        AssessmentDepartReport assessmentDepartReport = new AssessmentDepartReport();
        assessmentDepartReport.setPlanId(save.getId());
        assessmentDepartReport.setTemplateId(byId.getId());
        assessmentDepartReport.setOrganizationId(save.getOrganizationId());
        assessmentDepartReport.setOrganizationName(appraisalTemplate.getOrganizationName());
        assessmentDepartReport.setMonth(month);
        assessmentDepartReport.setYear(year);
        assessmentDepartReport.setCreatedDateTime(new Date());
        assessmentDepartReport.setCreatedUserId(users.getId());
        assessmentDepartReport.setCreatedUserName(users.getUsername());
        assessmentDepartReport.setLastUpdateUserId(users.getId());
        assessmentDepartReport.setLastUpdateDateTime(new Date());
        assessmentDepartReport.setLastUpdateUserName(users.getUsername());
        assessmentDepartReport.setAmputated(0);
        return  assessmentDepartReportRepository.save(assessmentDepartReport).getId();
    }

    private BigDecimal generatePlanItem(AppraisalPlan save) {
        //根据考核计划的模板id获得模板的考核指标和规则的信息
        PageData pageData1 = new PageData();
        pageData1.put("templateId",save.getTemplateId());
        List<AppraisalPlanItem> list = new ArrayList<>();
        list = appraisalTemplateRuleMapper.selectByTemplateId(pageData1);
        //若考核规则的打分设置为系统默认，则给考核规则的得分设置为默认值，并计算设置对应的指标的得分
        for (AppraisalPlanItem AppraisalPlanItem:list) {
            if(AppraisalPlanItem.getScoreSource()==AppConsts.System_Default){
                if(AppraisalPlanItem.getCumulativeLimit()!=null&&AppraisalPlanItem.getDefaultScore().compareTo(AppraisalPlanItem.getCumulativeLimit())>0){
                    AppraisalPlanItem.setRuleScore(AppraisalPlanItem.getCumulativeLimit());
                }else {
                    AppraisalPlanItem.setRuleScore(AppraisalPlanItem.getDefaultScore());
                }
            }else{
                AppraisalPlanItem.setRuleScore(new BigDecimal(0));
            }
        }
        HashMap<Integer, BigDecimal> integerBigDecimalHashMap = new HashMap<>();
        BigDecimal decimal = new BigDecimal(0);
        for(int i = 0;i<list.size();i++){
            if(list.get(i).getRuleScore()!=null){
                decimal =decimal.add(list.get(i).getRuleScore().multiply(list.get(i).getScoreType()==AppConsts.Add_Points?new BigDecimal(1):new BigDecimal(-1)));
            }
            if(i+1<list.size()){
                if(!list.get(i+1).getIndexId().equals(list.get(i).getIndexId())){
                    decimal = this.IndexScore(list.get(i),decimal);
                    integerBigDecimalHashMap.put(list.get(i).getIndexId(),decimal);
                    decimal = new BigDecimal(0);
                }
            }
            if(i+1==list.size()){
//                if(decimal.compareTo(new BigDecimal(0))!=0){
                    decimal = this.IndexScore(list.get(i),decimal);
                    integerBigDecimalHashMap.put(list.get(i).getIndexId(),decimal);
//                }
            }
        }
        for (AppraisalPlanItem AppraisalPlanItem:list) {
            AppraisalPlanItem.setPlanId(save.getId());
            AppraisalPlanItem.setId(0);
            if(integerBigDecimalHashMap.containsKey(AppraisalPlanItem.getIndexId())){
                AppraisalPlanItem.setIndexScore(integerBigDecimalHashMap.get(AppraisalPlanItem.getIndexId()));
                AppraisalPlanItem.setQuantity(new BigDecimal(0));
            }
        }
        List<AppraisalPlanItem> appraisalPlanItems = appraisalPlanItemRepository.saveAll(list);
        if(appraisalPlanItems!=null&&appraisalPlanItems.size()>0){
            BigDecimal decimal1 = new BigDecimal(0);
            List<Integer> ids = new ArrayList<>();
            appraisalPlanItems = appraisalPlanItems.stream().filter(// 过滤去重
                    v -> {
                        boolean flag = !ids.contains(v.getIndexId());
                        ids.add(v.getIndexId());
                        return flag;
                    }
            ).collect(Collectors.toList());
            for (AppraisalPlanItem appraisalPlanItem:appraisalPlanItems) {
                decimal1 = decimal1.add(appraisalPlanItem.getIndexScore());
            }
            return decimal1;
        }else{
            return new BigDecimal(0);
        }

    }

    private BigDecimal IndexScore(AppraisalPlanItem appraisalPlanItem, BigDecimal decimal) {
        if(decimal.compareTo(new BigDecimal(0))>0){
            if(appraisalPlanItem.getMaxBonus()!=null){
                decimal = decimal.compareTo(appraisalPlanItem.getMaxBonus())>0?appraisalPlanItem.getMaxBonus():decimal;
            }
            if(appraisalPlanItem.getDatumValue()!=null){
                decimal =  decimal.add(appraisalPlanItem.getDatumValue());
            }
        }else{
            if(appraisalPlanItem.getDatumValue()!=null){
                decimal =  decimal.add(appraisalPlanItem.getDatumValue());
            }
            if(decimal.compareTo(new BigDecimal(0))<0){
                decimal = new BigDecimal(0);
            }
        }
        return decimal;
    }

    private void deleteRemainPlan(Integer templatetId, Integer year, Integer month,AppraisalTemplate byId,Users users) {
        AppraisalPlan appraisalPlan = new AppraisalPlan();
        appraisalPlan.setTemplateId(templatetId);
        appraisalPlan.setYear(year);
        appraisalPlan.setAmputated(1);
        //若是年度考核的考核模板
        if(byId.getType()==AppConsts.Annual_Assessment){
            appraisalPlan.setType(AppConsts.Annual_Assessment);
            appraisalPlanMapper.deleteByTypeAndTemplateIdAndYear(appraisalPlan);
        }
        //若是月度考核的考核模板
        if(byId.getType()==AppConsts.Monthly_Assessment){
            appraisalPlan.setMonth(month);
            appraisalPlan.setType(AppConsts.Monthly_Assessment);
            appraisalPlanMapper.deleteByTypeAndTemplateIdAndYearAndMonth(appraisalPlan);
        }
        //若是月度部门考核模板 则根据假删除部门考核报表
        if(byId.getType()==AppConsts.Monthly_Assessment&&byId.getObjectType()==AppConsts.Window){
            AssessmentDepartReport assessmentDepartReport = new AssessmentDepartReport();
            assessmentDepartReport.setTemplateId(templatetId);
            assessmentDepartReport.setMonth(month);
            assessmentDepartReport.setYear(year);
            assessmentDepartReport.setLastUpdateDateTime(new Date());
            assessmentDepartReport.setLastUpdateUserId(users.getId());
            assessmentDepartReport.setLastUpdateUserName(users.getUsername());
            //根据考核模板id和年月去逻辑删除部门考核报表
            assessmentDepartReportMapper.deleteByTemplateIdAndDate(assessmentDepartReport);
        }
    }

    @Transactional
    public void timingCreatePlanAndPlanDetail(Integer templatetId, Integer year, Integer month) {
        //若未传入年则默认为系统时间所在的年
        Calendar date = Calendar.getInstance();
        if(year==null){
            year =date.get(Calendar.YEAR);
        }
        //若未传入月则默认为系统时间所在的月
        if(month==null){
            month =date.get(Calendar.MONTH)+1;
        }
        //获得所有的月度考核模板
        List<AppraisalTemplate> list = appraisalTemplateRepository.findByTypeAndAmputated(AppConsts.Monthly_Assessment,0);
        for (AppraisalTemplate appraisalTemplate:list) {
            //1 判断之前是否已经生成过考核计划，若生成过考核计划则删除考核计划(假删除)
            if(appraisalTemplate.getState()==AppConsts.stop){
                break;
            }
            Users users = null;
            try {
                users = getUsers();
            } catch (Exception e) {
                e.printStackTrace();
                users = new Users();
                users.setId(0);
                users.setUsername("job");
            }
            this.deleteRemainPlan(appraisalTemplate.getId(),year,month,appraisalTemplate,users);
            //2、生成考核计划,并生成考核计划明细,s
            int result = this.generatePlanAndPlanItem(appraisalTemplate.getId(),appraisalTemplate,year,month,users);
        }
    }


}
