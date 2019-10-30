package com.personnel.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.response.ResponseResult;
import com.common.utils.HttpRequestUtil;
import com.google.common.collect.Lists;
import com.personnel.config.RedisComponent;
import com.personnel.domian.input.FoodEmpIdInput;
import com.personnel.domian.input.FoodEmployeesInput;
import com.personnel.domian.input.FoodOrganiztionInput;
import com.personnel.domian.output.EmployeesOutput;
import com.personnel.domian.output.GardenOutput;
import com.personnel.domian.output.IdentityOutput;
import com.personnel.mapper.jpa.FoodEmployeesRepository;
import com.personnel.mapper.jpa.FoodOrganizationRepository;
import com.personnel.mapper.jpa.OrganizationRepository;
import com.personnel.mapper.mybatis.EmployeesMapper;
import com.personnel.mapper.jpa.FoodLogRepository;
import com.personnel.mapper.mybatis.OrganizationMapper;
import com.personnel.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-01-02  18:10
 * @modified by:
 */
@Component
public class FoodSystemService {

//    授权应用的Id
    @Value("${foodSystem.client_id}")
    private String clientId;
//    IP加端口
    @Value("${foodSystem.urlPrfix}")
    private String urlPrefix;
//    获取AccessToken
    @Value("${foodSystem.accessTokenUrl}")
    private String accessTokenUrl;
//    同步卡户部门
    @Value("${foodSystem.updateAccDepUrl}")
    private String updateAccDepUrl;
//    删除卡户部门
    @Value("${foodSystem.deleteAccDepUrl}")
    private String deleteAccDepUrl;
//    获取卡户部门
    @Value("${foodSystem.getAccDepUrl}")
    private String getAccDepUrl;
//    同步卡户信息
    @Value("${foodSystem.uploadAccInfoUrl}")
    private String uploadAccInfoUrl;
//    修改卡户状态
    @Value("${foodSystem.updateStateAccUrl}")
    private String updateStateAccUrl;
//    获取卡户信息
    @Value("${foodSystem.getAllAccUrl}")
    private String getAllAccUrl;
//    获取身份参数
    @Value("${foodSystem.getAccClassUrl}")
    private String getAccClassUrl;
//    获取园区参数
    @Value("${foodSystem.getAreaUrl}")
    private String getAreaUrl;
//    代码辅助查询
    @Value("${foodSystem.getErrTxtByCodeUrl}")
    private String getErrTxtByCodeUrl;

    @Autowired
    private RedisComponent redisComponent;
    @Autowired
    private EmployeesMapper employeesMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private FoodEmployeesRepository foodEmployeesRepository;
    @Autowired
    private FoodOrganizationRepository foodOrganizationRepository;
    @Autowired
    private CityCardService cityCardService;
    @Autowired
    private FoodLogRepository foodLogRepository;

    private  final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Logger log = LoggerFactory.getLogger(FoodSystemService.class);

    /**获取AccessToken**/
    public String getToken(){
        String param = "client_id="+clientId;
        String url=urlPrefix+accessTokenUrl;
        String res = HttpRequestUtil.sendGet(url,param);
        if(res==null||res.equals("")){
            return null;
        }
        net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(res);

       Integer rSign =(Integer) object.get("RSign");
        if(rSign.equals(1)){
            String accessToken = (String) object.get("access_token");
            redisComponent. set("foodSystem",accessToken,Long.valueOf(60*60*24*365));
            return accessToken;
        }
        return null;
    }

    /**获取身份参数**/
    public IdentityOutput getAccClass() throws IOException {

        String accessToken = redisComponent.get("foodSystem");

        String param = "";
        if(accessToken == null){
            accessToken = getToken();
            param = "access_token="+accessToken;
        }
        param = "access_token="+accessToken;
        String url=urlPrefix+getAccClassUrl;
        String res = HttpRequestUtil.sendPostWithHeader(url,"",accessToken);

        if(res==null||res.equals("")){
            return null;
        }

        JSONObject jsonObject = (JSONObject) JSONObject.parse(res);

        Integer rSign = (Integer) jsonObject.get("RSign");
        if(rSign.equals(1)){
            JSONArray identityArray =(JSONArray) jsonObject.get("RData");
            List<IdentityOutput> identityOutputList = Lists.newArrayList();
            for(int i=0;i<identityArray.size();i++){
                Map<String,Object> map =(Map<String,Object>) identityArray.get(i);

                IdentityOutput identityOutput = new IdentityOutput();
                identityOutput.setClsNum((BigDecimal) map.get("ClsNum"));
                identityOutput.setClsName((String)map.get("ClsName"));
                identityOutput.setRemark((String)map.get("Remark"));

                return identityOutput;
            }
        }
       return null;
    }

    /**获取园区参数**/
    public GardenOutput getArea() throws IOException {

        String accessToken = redisComponent.get("foodSystem");

        String param = "";
        if(accessToken == null){
            accessToken = getToken();
            param = "access_token="+accessToken;
        }
        param = "access_token="+accessToken;
        String url = urlPrefix+getAreaUrl;

        String res = HttpRequestUtil.sendPostWithHeader(url,"",accessToken);

        if(res==null||res.equals("")){
            return null;
        }

        JSONObject jsonObject = (JSONObject) JSONObject.parse(res);

        Integer rSign = (Integer)jsonObject.get("RSign");

        if(rSign.equals(1)) {
            JSONArray identityArray = (JSONArray) jsonObject.get("RData");

            for (int i = 0; i < identityArray.size(); i++) {
                Map<String, Object> map = (Map<String, Object>) identityArray.get(i);

                GardenOutput gardenOutput = new GardenOutput();
                gardenOutput.setAreaNum((BigDecimal) map.get("AreaNum"));
                gardenOutput.setAreaName((String) map.get("AreaName"));
                gardenOutput.setRemark((String) map.get("Remark"));

                return gardenOutput;
            }
        }
        return null;
    }

    /**组织临时下发接口**/
    public Integer addFoodOrganization(){
        //查询出所有的组织列表
        List<Organization> organizationList = organizationMapper.selectAllOrg();

        List<FoodOrganization> foodOrganizationList = Lists.newArrayList();
        if(organizationList.size()==0||organizationList==null){
            return 0;
        }
        for(Organization organization:organizationList){

            FoodOrganization foodOrganization = new FoodOrganization();
            //设置本组织编号
            foodOrganization.setsDepCode(organization.getOrganizationNo());

            foodOrganization.setState(1);
            //如果该部门是顶级部门，对接的顶级编号传根目录
            if(organization.getParentId()==0){
                foodOrganization.setsParentDepCode("根目录");
            }else {
                Organization orgaByParentId = organizationMapper.selectOrNoByOrId(organization.getParentId());
                //设置上级部门
                foodOrganization.setsParentDepCode(orgaByParentId.getOrganizationNo());
            }

            //设置本组织名称
            foodOrganization.setsDepName(organization.getName());

            foodOrganizationList.add(foodOrganization);
        }
        Integer size = foodOrganizationRepository.saveAll(foodOrganizationList).size();
        if(size == 0){
            return 0;
        }
        return size;
    }

    /**新增组织的正式接口 下发到中间表 途径1.组织新增 2.组织编辑**/
    @Async
    public void addFoodOrganization(Organization organization){

        FoodOrganization foodOrganization = new FoodOrganization();
        //设置本组织编号
        foodOrganization.setsDepCode(organization.getOrganizationNo());

        //餐盘系统日志表对象
        FoodLog foodLog = new FoodLog();

        Organization orgaByParentId=new Organization();
        //如果该部门是顶级部门，对接的顶级编号传空字符串
        if(organization.getParentId()==0){
            foodOrganization.setsParentDepCode("根目录");
        }else {
            //查询出上级组织对象
            orgaByParentId = organizationMapper.selectOrNoByOrId(organization.getParentId());
            if(orgaByParentId == null){
                log.error("没有找到上级组织");
                return;
            }
        }
        //设置上级部门
        foodOrganization.setsParentDepCode(orgaByParentId.getOrganizationNo());
        //设置本组织名称
        foodOrganization.setsDepName(organization.getName());
        //设置时间属性
        foodOrganization.setCreatedDateTime(new Date());
        foodOrganization.setLastUpdateDateTime(new Date());
        foodOrganization.setState(0);
        Integer id = foodOrganizationRepository.save(foodOrganization).getId();
        if(id == null||id<=0){
            log.error("餐盘系统组织下发或者跟新失败");

            //餐盘系统日志设置属性
            foodLog.setCreatedDateTime(new Date());
            foodLog.setSysDateTime(new Date());
            foodLog.setLastUpdateDateTime(new Date());
            foodLog.setMatters(organization.getName()+"下发到foodOrganization中间表失败");
            foodLog.setResourceId(id);
            Integer logId = foodLogRepository.save(foodLog).getId();
            if(logId<=0){
                log.info("餐盘系统组织日志添加失败");
            }
            return;
        }
        //餐盘系统日志设置属性
        foodLog.setCreatedDateTime(new Date());
        foodLog.setMatters(organization.getName()+"下发到foodOrganization中间表成功");
        foodLog.setSysDateTime(new Date());
        foodLog.setLastUpdateDateTime(new Date());
        foodLog.setResourceId(id);
        Integer logId = foodLogRepository.save(foodLog).getId();
        if(logId<=0){
            log.info("餐盘系统组织日志添加失败");
        }

        log.info("餐盘系统组织日志添加成功");
        log.info("餐盘系统组织下发或者跟新成功");
        return;
    }
    /**定时同步添加卡户部门*/
    public void updateAccDep() {

        //查询未下发的
        List<FoodOrganization> foodOrganizationList = foodOrganizationRepository.findAllByState(0);

        if(foodOrganizationList.size()==0||foodOrganizationList==null){
            return;
        }
        String accessToken = redisComponent.get("foodSystem");

        if(accessToken ==null){
            accessToken = getToken();
        }

        for(FoodOrganization foodOrganization : foodOrganizationList){
            FoodOrganiztionInput foodOrganiztionInput = new FoodOrganiztionInput();

            FoodOrganization[] foodArray = new FoodOrganization[]{foodOrganization};

            foodOrganiztionInput.setSData(foodArray);
            String url = urlPrefix+updateAccDepUrl;
            String param = foodOrganiztionInput.toString();

            //变成json格式
            try {
                JSONObject paramObject = JSONObject.parseObject(param);
                String paramObject1 = paramObject.toJSONString();

                String res = HttpRequestUtil.sendPostWithHeader(url,paramObject1,accessToken);

                if(res==null||res.equals("")){
                    continue;
                }

                JSONObject jsonObject = JSONObject.parseObject(res);
                net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(res);

                Integer RSign =(Integer) object.get("RSign");

                JSONArray rDataArray = (JSONArray)jsonObject.get("RData");
                Map<String,Object> rDataMap =(Map<String,Object>) rDataArray.get(0);
                String rSuccessSign = (String)rDataMap.get("RSuccessSign");

                FoodLog foodLog = new FoodLog();

                if(RSign.equals(1)&&rSuccessSign.equals("1")){
                    foodOrganization.setLastUpdateDateTime(new Date());
                    //设置成已经下发的状态
                    foodOrganization.setState(1);
                    String result = foodOrganizationRepository.save(foodOrganization).getsDepCode();
                    if(result == null){
                        log.error("组织下发失败");
                        continue;
                    }

                    //餐盘系统日志设置属性
                    foodLog.setCreatedDateTime(new Date());
                    foodLog.setLastUpdateDateTime(new Date());
                    foodLog.setSysDateTime(new Date());
                    foodLog.setCompleteTime(new Date());
                    foodLog.setMatters(foodOrganization.getsDepName()+"下发到餐盘系统成功");
                    foodLog.setResourceId(foodOrganization.getId());
                    Integer logId = foodLogRepository.save(foodLog).getId();
                    if(logId<=0){
                        log.info("餐盘系统组织日志添加失败");
                    }

                }else{
                    foodOrganization.setState(2);
                    foodOrganization.setLastUpdateDateTime(new Date());
                    foodOrganizationRepository.save(foodOrganization);
                    foodLog.setCreatedDateTime(new Date());
                    foodLog.setLastUpdateDateTime(new Date());
                    foodLog.setSysDateTime(new Date());
                    foodLog.setCompleteTime(new Date());
                    foodLog.setMatters(foodOrganization.getsDepName()+"下发失败,RSign："+RSign+",rSuccessSign:"+rSuccessSign);
                    foodLog.setResourceId(foodOrganization.getId());
                    foodLogRepository.save(foodLog);
                }
            } catch (IOException e) {
                List<FoodLog> foodLogList = foodLogRepository.findAllByResourceId(foodOrganization.getId());
                if(foodLogList.size()>0){
                    continue;
                }
                FoodLog foodLog = new FoodLog();
                //餐盘系统日志设置属性
                foodLog.setCreatedDateTime(new Date());
                foodLog.setLastUpdateDateTime(new Date());
                foodLog.setSysDateTime(new Date());
                foodLog.setMatters(foodOrganization.getsDepName()+"下发到餐盘系统失败");
                foodLog.setResourceId(foodOrganization.getId());
                Integer logId = foodLogRepository.save(foodLog).getId();
                if(logId<=0){
                    log.info("餐盘系统组织日志添加失败");
                }
                log.error(e.getMessage());
            }
        }
        log.error("组织下发成功");
        return;
        }

    /**删除卡户，修改中间表的state状态为3**/
    public Integer deleteFoodOrganization(String orgaNo){
        FoodOrganization foodOrganization = foodOrganizationRepository.findAllBySDepCodeOrderByLastUpdateDateTimeDesc(orgaNo).get(0);
        if(foodOrganization==null){
            return null;
        }
        //设置下发状态为将要删除状态
        foodOrganization.setState(3);
        foodOrganization.setLastUpdateDateTime(new Date());

        FoodLog foodLog = new FoodLog();
        Integer id = foodOrganizationRepository.save(foodOrganization).getId();
        if(id == null||id == 0){
            foodLog.setCreatedDateTime(new Date());
            foodLog.setLastUpdateDateTime(new Date());
            foodLog.setSysDateTime(new Date());
            foodLog.setMatters(foodOrganization.getsDepName()+"餐盘组织中间表数据删除失败");
            foodLog.setResourceId(id);
            foodLogRepository.save(foodLog);
            return null;
        }

        foodLog.setCreatedDateTime(new Date());
        foodLog.setLastUpdateDateTime(new Date());
        foodLog.setSysDateTime(new Date());
        foodLog.setCompleteTime(new Date());
        foodLog.setMatters(foodOrganization.getsDepName()+"餐盘组织中间表数据删除成功");
        foodLog.setResourceId(id);
        foodLogRepository.save(foodLog);
        return 1;
    }


    /**定时删除卡户部门**/
    public void deleteAccDep()  {

        //去餐盘组织中间表查询状态为3已经删除的组织列表
        List<FoodOrganization> foodOrganizationList = foodOrganizationRepository.findAllByState(3);
        if(foodOrganizationList.size()==0){
            log.error("暂时没有可以删除的部门");
            return;
        }
        String accessToken = redisComponent.get("foodSystem");
        if(accessToken ==null){
            accessToken = getToken();
        }
        for(FoodOrganization foodOrganization:foodOrganizationList){

            String param = foodOrganization.toStringDept();

            String url = urlPrefix+deleteAccDepUrl;

            try {
                String res = HttpRequestUtil.sendPostWithHeader(url,param,accessToken);
                if(res==null||res.equals("")){
                    continue;
                }
                JSONObject jsonObject = JSONObject.parseObject(res);
                net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(res);

                Integer RSign =(Integer) object.get("RSign");

                JSONArray rDataArray = (JSONArray)jsonObject.get("RData");
                Map<String,Object> rDataMap =(Map<String,Object>) rDataArray.get(0);
                String rSuccessSign = (String)rDataMap.get("RSuccessSign");

                FoodLog foodLog = new FoodLog();

                if(RSign.equals(1)&&rSuccessSign.equals("1")){
                    //设置删除的状态为4
                    foodOrganization.setState(4);
                    foodOrganization.setLastUpdateDateTime(new Date());
                    Integer id = foodOrganizationRepository.save(foodOrganization).getId();
                    if(id==null||id<=0){
                        log.error("删除组织失败");
                    }
                    foodLog.setCreatedDateTime(new Date());
                    foodLog.setLastUpdateDateTime(new Date());
                    foodLog.setSysDateTime(new Date());
                    foodLog.setCompleteTime(new Date());
                    foodLog.setMatters(foodOrganization.getsDepName()+"组织在餐盘系统删除成功");
                    foodLog.setResourceId(id);
                    foodLogRepository.save(foodLog);
                }else{
                    foodOrganization.setState(2);
                    foodOrganization.setLastUpdateDateTime(new Date());
                    foodOrganizationRepository.save(foodOrganization);
                    foodLog.setCreatedDateTime(new Date());
                    foodLog.setLastUpdateDateTime(new Date());
                    foodLog.setSysDateTime(new Date());
                    foodLog.setCompleteTime(new Date());
                    foodLog.setMatters(foodOrganization.getsDepName()+"删除失败,RSign："+RSign+",rSuccessSign:"+rSuccessSign);
                    foodLog.setResourceId(foodOrganization.getId());
                    foodLogRepository.save(foodLog);
                }
            } catch (IOException e) {
                log.error("删除组织失败");
            }
        }
        log.error("暂无可以删除的组织");
        return ;
    }

    /**人员下发临时接口**/
    public Integer addAllFoodEmp() throws IOException {
        List<EmployeesOutput>employeesOutputList = employeesMapper.selectAllFoodEmp();
        for(EmployeesOutput employeesOutput : employeesOutputList){
            Employees employees = new Employees();
            BeanUtils.copyProperties(employeesOutput,employees);
            addFoodEmployees(employees);
        }
        return employeesOutputList.size();
    }

    /**查询部门信息*/
    public ResponseResult getOrga(FoodOrganiztionInput foodOrganiztionInput) throws IOException {

        String accessToken = redisComponent.get("foodSystem");
        if(accessToken ==null||accessToken==""){
            accessToken = getToken();
        }
        String param = foodOrganiztionInput.toString();
        String url = urlPrefix+getAccDepUrl;
        String res = HttpRequestUtil.sendPostWithHeader(url,param,accessToken);
        if(res==null||res.equals("")){
            return ResponseResult.error("没有找到连接");
        }
        JSONObject jsonObject = JSONObject.parseObject(res);
        net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(res);

        Integer RSign =(Integer) object.get("RSign");

        if(RSign.equals(1)){

            return ResponseResult.success(JSONObject.parseObject(res));
        }

        return ResponseResult.success();
    }

    /**把人员下发到餐盘人员中间表中 下发的途径1.人员出现编辑 2.人员审批通过 3.人员导入*/
    @Async
    public void addFoodEmployees(Employees employees) throws IOException {

        //实例化餐盘人员对象
        FoodEmployees foodEmployees = new FoodEmployees();
        //第三方对接号设置成员工工号
        foodEmployees.setsID(employees.getEmployeeNo());

        String sIdCard="";

        if(employees.getCitizenCardPhysicalAddress()==null){
            sIdCard ="";
        } else {
            //获取市民卡物理地址
            String sAddress = employees.getCitizenCardPhysicalAddress();
            //如果是10进制的
            if(isOctNumber(sAddress)){
                sIdCard=sAddress;
            }else {
                //将16进制转换成10进制
                sIdCard=String.valueOf(Long.parseLong(sAddress,16));
            }
        }
        //设置市民卡物理卡号
        foodEmployees.setsCardID(sIdCard);
        //设置个人编号
        foodEmployees.setsPerCode(employees.getEmployeeNo());
        //获取校区编号
        foodEmployees.setnAreaNum(getArea().getAreaNum());
        //卡户姓名
        foodEmployees.setsAccName(employees.getName());

        Organization organization = organizationMapper.selectOrNoByOrId(employees.getOrganizationId());
        if(organization == null){
            log.error("餐盘人员没有找到对应的组织");
            return;
        }
        //设置部门编号
        foodEmployees.setsDepCode(organization.getOrganizationNo());
        //获取身份编号
        foodEmployees.setnClsNum(getAccClass().getClsNum());
        //设置手机号码
        foodEmployees.setsMobileCode(employees.getPhoneNumber());
        //设置邮箱
        foodEmployees.setsEMail(employees.getEmail());
        //设置性别
        if(employees.getSex() == 0){
            foodEmployees.setsAccSex("男");
        }
        if(employees.getSex() == 1){
            foodEmployees.setsAccSex("女");
        }
        //设置邮编
        foodEmployees.setsPostCode("311400");
        //社会生日
        foodEmployees.setsBirthDay(SIMPLE_DATE_FORMAT.format(employees.getDateBirth()));
        //设置身份证号码
        foodEmployees.setsCertCode(employees.getIdCard());
        //设置市民卡物理卡号地址
        foodEmployees.setsAddress(employees.getCitizenCardPhysicalAddress());
        //设置未下发状态
        foodEmployees.setState(0);
        foodEmployees.setAmputated(0);
        foodEmployees.setCreatedDateTime(new Date());

        FoodLog foodLog = new FoodLog();
        Integer id = foodEmployeesRepository.save(foodEmployees).getId();
        if(id == null || id <=0){
            foodLog.setCreatedDateTime(new Date());
            foodLog.setLastUpdateDateTime(new Date());
            foodLog.setCompleteTime(new Date());
            foodLog.setMatters(foodEmployees.getsAccName()+"餐盘人员中间表添加失败");
            foodLog.setResourceId(id);
            foodLogRepository.save(foodLog);
            log.error("没有将餐盘人员添加到中间表");
            return;
        }

        foodLog.setCreatedDateTime(new Date());
        foodLog.setLastUpdateDateTime(new Date());
        foodLog.setCompleteTime(new Date());
        foodLog.setMatters(foodEmployees.getsAccName()+"餐盘人员中间表添加成功");
        foodLog.setResourceId(id);
        foodLogRepository.save(foodLog);
        log.info("成功将餐盘人员添加到中间表");
        return;
    }

    //判断是否是10进制的数据
    private static boolean isOctNumber(String str) {
        boolean flag = false;
        for(int i=0,n=str.length();i<n;i++){
            char c = str.charAt(i);
            if(c=='0'||c=='1'||c=='2'||c=='3'||c=='4'||c=='5'||c=='6'||c=='7'||c=='8'||c=='9'){
                flag=true;
            }else {
                flag=false;
                break;
            }
        }
        return flag;
    }

    /**定时下发人员到餐盘系统**/
    public void uploadAccInfo() {
        //查询出未下发的集合
        List<FoodEmployees> foodEmployeesList = foodEmployeesRepository.findAllByStateAndAmputated(0,0);
        if(foodEmployeesList.size()==0||foodEmployeesList==null){
            log.error("暂时没有人员可以下发");
            return;
        }
        //去redis中查询token
        String accessToken = redisComponent.get("foodSystem");
        //如果不存在从新获取
        if(accessToken == null){
            accessToken=getToken();
        }
        for(FoodEmployees foodEmployees:foodEmployeesList){
            FoodEmployeesInput foodEmployeesInput = new FoodEmployeesInput();
            FoodEmployees[] foodEmployeesArray = new FoodEmployees[]{foodEmployees};
            foodEmployeesInput.setSData(foodEmployeesArray);
            String param = foodEmployeesInput.toString();

            try {
                JSONObject paramObject = JSONObject.parseObject(param);
                String paramObject1 = paramObject.toJSONString();
                String url = urlPrefix+uploadAccInfoUrl;
                String res = HttpRequestUtil.sendPostWithHeader(url,paramObject1,accessToken);
                if(res==null||res.equals("")){
                    continue;
                }
                JSONObject jsonObject = JSONObject.parseObject(res);
                net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(res);

                Integer RSign =(Integer) object.get("RSign");

                JSONArray rDataArray = (JSONArray)jsonObject.get("RData");
                Map<String,Object> rDataMap =(Map<String,Object>) rDataArray.get(0);
                String rSuccessSign = (String)rDataMap.get("RSuccessSign");


                FoodLog foodLog = new FoodLog();
                if(RSign.equals(1)&&rSuccessSign.equals("1")){
                    foodEmployees.setState(1);
                    foodEmployees.setLastUpdateDateTime(new Date());
                    String sId = foodEmployeesRepository.save(foodEmployees).getsID();
                    if(sId==null||sId==""){
                        continue;
                    }
                    foodLog.setCreatedDateTime(new Date());
                    foodLog.setLastUpdateDateTime(new Date());
                    foodLog.setCompleteTime(new Date());
                    foodLog.setSysDateTime(new Date());
                    foodLog.setMatters(foodEmployees.getsAccName()+"餐盘人员同步到餐盘系统成功");
                    foodLog.setResourceId(foodEmployees.getId());
                    Integer id= foodLogRepository.save(foodLog).getId();
                    if(id>0){
                        log.info("人员同步到餐盘系统成功");
                    }
                }else{
                    foodEmployees.setState(2);
                    foodEmployees.setLastUpdateDateTime(new Date());
                    foodEmployeesRepository.save(foodEmployees);
                    foodLog.setCreatedDateTime(new Date());
                    foodLog.setLastUpdateDateTime(new Date());
                    foodLog.setSysDateTime(new Date());
                    foodLog.setCompleteTime(new Date());
                    foodLog.setMatters(foodEmployees.getsAccName()+"下发失败,RSign："+RSign+",rSuccessSign:"+rSuccessSign);
                    foodLog.setResourceId(foodEmployees.getId());
                    foodLogRepository.save(foodLog);
                }
            } catch (Exception e) {
                log.error("下发到餐盘系统出现系统");
            }
        }
        return;
    }

    /**删除卡户*/
    @Async
    public void delFoodEmp(String sID){
        FoodEmployees foodEmployees =
                foodEmployeesRepository.findAllBySIDAndAmputatedOrderByLastUpdateDateTimeDesc(sID,0).get(0);
        foodEmployees.setAmputated(1);
        //4是将要删除状态
        foodEmployees.setState(4);
        FoodLog foodLog = new FoodLog();

        String result = foodEmployeesRepository.save(foodEmployees).getsID();
        if(result == null||result==""){
            log.error("删除餐盘系统中间表人员失败");
            foodLog.setCreatedDateTime(new Date());
            foodLog.setLastUpdateDateTime(new Date());
            foodLog.setCompleteTime(new Date());
            foodLog.setMatters(foodEmployees.getsAccName()+"餐盘人员在中间表删除失败");
            foodLog.setResourceId(foodEmployees.getId());
            Integer id = foodLogRepository.save(foodLog).getId();
            if(id>0){
                return;
            }
            return;
        }
        foodLog.setCreatedDateTime(new Date());
        foodLog.setLastUpdateDateTime(new Date());
        foodLog.setCompleteTime(new Date());
        foodLog.setMatters(foodEmployees.getsAccName()+"餐盘人员在中间表删除成功");
        foodLog.setResourceId(foodEmployees.getId());
        Integer id = foodLogRepository.save(foodLog).getId();
        if(id>0){
            log.info("删除餐盘系统中间表人员成功");
            return;
        }
        return;
    }

    /**定时删除餐盘人员*/
    public void updateStateAcc() {
        List<FoodEmployees> foodEmployeesList = foodEmployeesRepository.findAllByStateAndAmputated(4,1);
        if(foodEmployeesList.size()==0||foodEmployeesList==null){
            log.error("暂时没有可以删除的餐盘人员");
            return;
        }
        //去redis中查询token
        String accessToken = redisComponent.get("foodSystem");
        //如果token为空，则重新获取
        if(accessToken==null){
            accessToken = getToken();
        }
        for(FoodEmployees foodEmployees : foodEmployeesList){
            String param = foodEmployees.toStringId();

            JSONObject jsonParam = JSONObject.parseObject(param);
            param = jsonParam.toJSONString();
            String url = urlPrefix+updateStateAccUrl;
            try {
                String res = HttpRequestUtil.sendPostWithHeader(url,param,accessToken);
                if(res==null||res.equals("")){
                    log.error("没有连接到餐盘系统");
                    continue;
                }
                JSONObject jsonObject = JSONObject.parseObject(res);
                net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(res);

                Integer RSign =(Integer) object.get("RSign");

                JSONArray rDataArray = (JSONArray)jsonObject.get("RData");
                Map<String,Object> rDataMap =(Map<String,Object>) rDataArray.get(0);
                String rSuccessSign = (String)rDataMap.get("RSuccessSign");
                FoodLog foodLog = new FoodLog();
                if(RSign.equals(1)&&rSuccessSign.equals("1")){
                    //5是已经删除
                    foodEmployees.setState(5);
                    foodEmployees.setLastUpdateDateTime(new Date());
                    String id = foodEmployeesRepository.save(foodEmployees).getsID();
                    if(id==null||id==""){
                        continue;
                    }
                    foodLog.setCreatedDateTime(new Date());
                    foodLog.setLastUpdateDateTime(new Date());
                    foodLog.setSysDateTime(new Date());
                    foodLog.setCompleteTime(new Date());
                    foodLog.setMatters(foodEmployees.getsAccName()+"餐盘人员在系统中删除成功");
                    foodLog.setResourceId(foodEmployees.getId());
                    Integer foodEmpId = foodLogRepository.save(foodLog).getId();
                    if(foodEmpId>0){
                        log.info("---------------餐盘人员删除成功---------------------");
                        continue;
                    }
                }else{
                    foodEmployees.setState(2);
                    foodEmployees.setLastUpdateDateTime(new Date());
                    foodEmployeesRepository.save(foodEmployees);
                    foodLog.setCreatedDateTime(new Date());
                    foodLog.setLastUpdateDateTime(new Date());
                    foodLog.setSysDateTime(new Date());
                    foodLog.setCompleteTime(new Date());
                    foodLog.setMatters(foodEmployees.getsAccName()+"删除失败,RSign："+RSign+",rSuccessSign:"+rSuccessSign);
                    foodLog.setResourceId(foodEmployees.getId());
                    foodLogRepository.save(foodLog);
                }
            } catch (IOException e) {
                log.error("删除餐盘系统人员失败");
            }
        }
    }


    public ResponseResult getEmpInfo(FoodEmpIdInput foodEmpIdInput) throws IOException {
        //去redis中查询token
        String accessToken = redisComponent.get("foodSystem");
        //如果token为空，则重新获取
        if(accessToken==null){
            accessToken = getToken();
        }
        String url =  urlPrefix+getAllAccUrl;
        String param = foodEmpIdInput.toString();

        String res = HttpRequestUtil.sendPostWithHeader(url,param,accessToken);

        try {
            JSONObject jsonObject = JSONObject.parseObject(res);
            net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(res);

            Integer RSign =(Integer) object.get("RSign");

            if(RSign==1){
                return ResponseResult.success(JSONObject.parse(res));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseResult.error("没有查询出人员信息");
        }
        return ResponseResult.error("没有查询出人员信息");
    }
}
