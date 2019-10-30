package com.attendance.service;

import com.attendance.core.base.BaseMapper;
import com.attendance.core.base.BaseService;
import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.EmployeesCount;
import com.attendance.domian.output.OverTimeReportOutPut;
import com.attendance.domian.output.OvertimeApplicationOutput;
import com.attendance.enums.VerificationEnum;
import com.attendance.mapper.jpa.OvertimeApplicationRepository;
import com.attendance.mapper.mybatis.OffApplicationMapper;
import com.attendance.mapper.mybatis.OvertimeApplicationMapper;
import com.attendance.model.OvertimeApplication;
import com.common.Enum.ApprovalTypeEnum;
import com.common.model.PageData;
import com.common.request.Audit;
import com.common.request.GetConfig;
import com.common.response.ResponseResult;
import com.common.utils.ExportExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.attendance.core.base.BaseController.SYS_EORRO;

/**
 * @author: Young
 * @description: 加班
 * @date: Created in 23:08 2018/9/11
 * @modified by:
 */
@Service("overtimeApplicationService")
public class OvertimeApplicationService extends BaseService<OvertimeApplicationOutput, OvertimeApplication,Integer> {

    @Autowired
    private OvertimeApplicationRepository overtimeApplicationRepository;
    @Autowired
    private OvertimeApplicationMapper overtimeApplicationMapper;
    @Autowired
    private OffApplicationMapper offApplicationMapper;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private OvertimeApplicationRepository repository;
    @Autowired
    private HttpServletRequest request;

    @Override
    public BaseMapper<OvertimeApplication, Integer> getMapper() {
        return overtimeApplicationRepository;
    }

    @Override
    public MybatisBaseMapper<OvertimeApplicationOutput> getMybatisBaseMapper() {
        return overtimeApplicationMapper;
    }

    /**
     * 新增/跟新加班信息
     * @param id
     * @param overtimeApplication
     * @return
     */
//    @Transactional
    public ResponseResult formPost(Integer id, OvertimeApplication overtimeApplication) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        var result = "";
        //id==null新增加班申请
        if(id==null){
            //根据输入员工的Id和申请的加班日期
            List<OvertimeApplication> overtimeApplicationList = repository.findByEmployeesIdAndOverTimeDateAndAmputatedAndStatusNotLike(overtimeApplication.getEmployeesId(),overtimeApplication.getOverTimeDate(),0,2);
            if(overtimeApplicationList.size()>0){
                return ResponseResult.error("请勿重复申请");
            }
            //如果该名员工,没有相同时间的加班记录,则将新增加班记录的核销状态设置为未核销
            overtimeApplication.setVerification(VerificationEnum.UN_VERIFICATION.getCode());
            //新增
            var oId = this.add(overtimeApplication);
            if(oId <= 0){
                return ResponseResult.error(SYS_EORRO);
            }
            //调用审批
            if(Audit.apply(loadBalancerClient,oId,this.getUsers().getId(), ApprovalTypeEnum.OVERTIME_TYPE.getCode())){
                result = "提交审批成功";
                return ResponseResult.success(result);
            }else {
                this.deleteById(String.valueOf(oId));
                result = "提交审批失败";
                return ResponseResult.error(result);
            }
        }else {
            //如果是跟新操作,根据传入的主键id查询该条加班记录
            OvertimeApplication overtimeApplicationCurrent = repository.findOvertimeApplicationById(id);
            if(overtimeApplicationCurrent == null){
                return ResponseResult.error("未找到该条加班记录");
            }
            if(!overtimeApplicationCurrent.getCreatedUserId().equals(this.getUsers().getId())){
                return ResponseResult.error("不是本人创建无法编辑");
            }
            //根据输入员工的Id和申请的加班日期
            List<OvertimeApplication> overtimeApplicationList = repository.findByEmployeesIdAndOverTimeDateAndAmputatedAndStatusNotLike(overtimeApplication.getEmployeesId(),overtimeApplication.getOverTimeDate(),0,2);
            if(overtimeApplicationList.size()>0){
                return ResponseResult.error("请勿重复申请");
            }
            int rowCount = this.update(id, overtimeApplication);
            if(rowCount==0){
                return ResponseResult.error("跟新失败");
            }
            return ResponseResult.success("跟新成功");
        }
    }


    /**
     * 分页查询本月已经通过申请，以及未核销的加班列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ResponseResult selectOverTimeWithStatus(Integer pageNum, Integer pageSize){
        PageData pageData = new PageData(request);
        Integer currentId = this.getUsers().getId();
        pageData.put("currentId",currentId);
        PageHelper.startPage(pageNum,pageSize);
        List<OvertimeApplicationOutput> overtimeApplicationOutputList= overtimeApplicationMapper.selectAllByOrganizationAndEmployees(pageData);
        PageInfo pageInfo = new PageInfo(overtimeApplicationOutputList);
        return ResponseResult.success(pageInfo);
    }



    public List<OverTimeReportOutPut> overTimeReport(PageData pageData){
        pageData.put("userId",getUsers().getId().toString());
        if(getUsers().getAdministratorLevel()!=9){
            if(getUsers().getUserType()==0){
                pageData.put("employeeId",getUsers().getEmployeeId().toString());
            }else {
                pageData.put("orgId",getUsers().getOrganizationId().toString());
            }
        }
        List<OvertimeApplicationOutput> list =overtimeApplicationMapper.selectByReport(pageData);
        List<OvertimeApplicationOutput> result = new LinkedList<>();
        List<OverTimeReportOutPut> outPuts=new ArrayList<>();
        List<EmployeesCount> overTimeCount=overtimeApplicationMapper.selectOverTimeCount(pageData);
        List<EmployeesCount> restTimeCount=offApplicationMapper.selectRestCount(pageData);
        for (var o : list) {
            boolean b = result.stream().anyMatch(p -> p.getEmployeesId().compareTo(o.getEmployeesId())==0);
            if (!b) {
                result.add(o);
            }
        }
        for(OvertimeApplicationOutput output:result){
            OverTimeReportOutPut reportOutPut=new OverTimeReportOutPut();
            reportOutPut.setEmployeesId(output.getEmployeesId());
            reportOutPut.setEmployeesName(output.getEmployeesName());
            reportOutPut.setId(output.getId());
            reportOutPut.setBankCardNumber(output.getBankCardNumber());
            reportOutPut.setOrganizationId(output.getOrganizationId());
            reportOutPut.setOrganizationName(output.getOrganizationName());
            reportOutPut.setYear(output.getOverTimeDate().getYear()+1900);
            reportOutPut.setMonth(output.getOverTimeDate().getMonth()+1);
            reportOutPut.setRestCount(0);
            reportOutPut.setOverTimeCount(0);
            for(EmployeesCount employeesCount:overTimeCount){
                if(output.getEmployeesId().equals(employeesCount.getEMPLOYEES_ID())){
                    if(overTimeCount.size() == 0){
                        reportOutPut.setOverTimeCount(0);
                    }
                    reportOutPut.setOverTimeCount(employeesCount.getCount());
                }
            }
            for(EmployeesCount employeesCount:restTimeCount){
                if(output.getEmployeesId().equals(employeesCount.getEMPLOYEES_ID())){
                    if(restTimeCount.size() == 0){
                        reportOutPut.setRestCount(0);
                    }
                    reportOutPut.setRestCount(employeesCount.getCount());
                }
            }
            pageData.clear();
            pageData.put("key","overAmount");
            var map = GetConfig.getByKey(loadBalancerClient,pageData);
            Integer money = Integer.valueOf(map.get("overAmount"));

            Integer count = reportOutPut.getOverTimeCount()-reportOutPut.getRestCount()>4?4:reportOutPut.getOverTimeCount()-reportOutPut.getRestCount();
            reportOutPut.setAmount(new BigDecimal((count)*money));
            outPuts.add(reportOutPut);
        }
        return  outPuts;
    }

    public String export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String title = "加班申请详情表";
        String excelName = "加班申请详情表";
        String[] rowsName = new String[]{"序列","组织名称","人员名称","加班日期", "加班原因","审核状态"};
        List<Object[]> dataList = new ArrayList<>();
        Object[] objs = null;
        PageData pageData = new PageData(request);
        pageData.put("userId",getUsers().getId().toString());
        if(getUsers().getAdministratorLevel()!=9){
            if(getUsers().getUserType()==0){
                pageData.put("employeeId",getUsers().getEmployeeId().toString());
            }else {
                pageData.put("orgId",getUsers().getOrganizationId().toString());
            }
        }
        List<OvertimeApplicationOutput> ovetimeList =  overtimeApplicationMapper.selectAll(pageData);
        if(pageData.containsKey("idList")){
            pageData = new PageData(request);
            String idList = (String) pageData.get("idList");
            StringBuilder stringBuilder = new StringBuilder(idList);

            List<String> resourceList = Lists.newArrayList();

            stringBuilder = stringBuilder.delete(0,1);
            stringBuilder = stringBuilder.deleteCharAt(stringBuilder.indexOf("]"));
            idList = stringBuilder.toString();
            String[]str = idList.split(",");
            resourceList = Arrays.asList(str);
            ovetimeList = new ArrayList<>();
            for(String id :resourceList){
                OvertimeApplicationOutput overtimeApplicationOutput = overtimeApplicationMapper.selectByPrimaryKey(Integer.parseInt(id.trim()));
                ovetimeList.add(overtimeApplicationOutput);
            }
        }

        if(ovetimeList.size()>0){
            int i=1;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for(OvertimeApplicationOutput overtimeApplicationOutput:ovetimeList){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=overtimeApplicationOutput.getOrganizationName();
                objs[2]=overtimeApplicationOutput.getEmployeesName();
                objs[3]=sdf.format(overtimeApplicationOutput.getOverTimeDate());
                objs[4]=overtimeApplicationOutput.getDescription();
                objs[5]=overtimeApplicationOutput.getStatusName();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }

    public int updateOverTime(OvertimeApplication overtimeApplication){
        Integer id = overtimeApplicationRepository.save(overtimeApplication).getId();
        if(id<=0){
            return ERROR;
        }
        return SUCCESS;
    }

}
