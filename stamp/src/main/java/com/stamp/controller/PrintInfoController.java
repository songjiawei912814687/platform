package com.stamp.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.google.common.collect.Lists;
import com.stamp.core.base.BaseController;
import com.stamp.core.base.BaseService;
import com.stamp.domain.output.PrintInfoOutput;
import com.stamp.enums.AttachmentEnum;
import com.stamp.model.Attachment;
import com.stamp.model.Employees;
import com.stamp.model.PrintInfo;
import com.stamp.service.PrintInfoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-04-16  18:04
 * @modified by:
 */
@RestController
@RequestMapping("printInfo")
public class PrintInfoController extends BaseController<PrintInfoOutput, PrintInfo,Integer> {

    @Autowired
    private PrintInfoService printInfoService;
    @Autowired
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public BaseService<PrintInfoOutput, PrintInfo, Integer> getService() {
        return printInfoService;
    }

    @Override
    @PostMapping("formPost")
    public ResponseResult formPost(Integer id, @RequestBody  @Valid PrintInfo printInfo) throws Exception {

        Integer employeesId = this.getService().getUsers().getEmployeeId();
        Employees employees = printInfoService.findByEmpId(employeesId);
        printInfo.setEmpNo(employees.getEmployeeNo());
        printInfo.setEmpName(employees.getName());
        //2。生成推送时间
        if(id==null||"".equals(id)){
            printInfo.setCompanyPushTime(sdf.format(new Date()));
            printInfo = printInfoService.setCompanyPushDuration(id,printInfo);
            //当保存成功时 1:发送给刻章店短信
            if(printInfoService.sendMessage(printInfo)!=0){
                return ResponseResult.error("发送给刻章单位的短信发送失败，请电话联系对应刻章单位");
            }
        }
        ResponseResult result = super.formPost(id, printInfo);
        List<Attachment> attachmentList = Lists.newArrayList() ;
        if(result.getCode()==200){
            if(printInfo.getAttachmentList().size()>0){
                for(Attachment attachment:printInfo.getAttachmentList()){
                    attachment.setResourcesId((Integer) result.getData());
                    attachment.setResourcesType(AttachmentEnum.STAMP.getCode());
                    attachment.setSourceFileName(attachment.getSuffix());
                    attachment.setCreatedDateTime(new Date());
                    attachment.setCreatedUserId(this.getService().getUsers().getId());
                    attachment.setCreatedUserName(this.getService().getUsers().getUsername());
                    attachment.setAmputated(0);
                    attachmentList.add(attachment);
                }
            }
        }

        Integer addListAttachmentResult = printInfoService.addListAttachmentList(attachmentList);
        if(addListAttachmentResult>0){
            return ResponseResult.success();
        }
        return ResponseResult.error("附件上传失败");
    }

    @Override
    @GetMapping("logicDelete")
    public ResponseResult logicDelete(String idList) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        return super.logicDelete(idList);
    }

    @Override
    @GetMapping("selectById")
    public ResponseResult selectById(Integer id)  {
        List<Attachment> attachmentList = printInfoService.selectResource(id);
        PrintInfoOutput printInfoOutput = this.getService().selectById(id);
        printInfoOutput.setAttachmentList(attachmentList);
        return ResponseResult.success(printInfoOutput);
    }

    //查询列表
    @ApiImplicitParams({
            @ApiImplicitParam(name="empNo",value="员工工号",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="empName",value="员工姓名",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="doName",value="经办人姓名",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="doNumber",value="经办人手机号",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="stampCompany",value="刻章单位",required=false,dataType="integer", paramType = "query"),
            @ApiImplicitParam(name="servicePlan",value="服务套餐",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="startDate",value="推送开始时间",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="endDate",value="推送结束时间",required=false,dataType="string", paramType = "query"),
    })
    @GetMapping("selectPageList")
    public ResponseResult selectPageList(HttpServletRequest request){

        PageData pageData = new PageData(request);
        Integer createUserId = this.getService().getUsers().getId();

        if(createUserId.equals(1661)){
            return super.selectPageList(pageData);
        }
        if(this.getService().getUsers().getUserType()!=9){
            //如果是员工账号
            if(this.getService().getUsers().getUserType()==0){

                pageData.put("createUserId",createUserId);
            }else if(this.getService().getUsers().getUserType()==2){
                //如果是刻章单位账号登陆
                //获取组织id
                Integer organizationId = this.getService().getUsers().getOrganizationId();
                pageData.put("organizationId",organizationId);
            }
        }
        return super.selectPageList(pageData);
    }

    /**刻章单位点击刻章接收生成时间，并且计算时长*/
    @GetMapping("countCompanyPushTimeDuration")
    public ResponseResult countCompanyPushTimeDuration(Integer id) throws IllegalAccessException, IntrospectionException, InvocationTargetException, ParseException, MethodArgumentNotValidException {
        PrintInfo printInfo = this.getService().getById(id);
        if(printInfo == null){
            return ResponseResult.success();
        }
        //设置刻章接受时间
        printInfo.setStampReceiveTime(sdf.format(new Date()));
        printInfo = printInfoService.setTotalTime(printInfo);
        Integer result = this.getService().update(id,printInfo);
        if(result<0){
            return ResponseResult.error("刻章单位接收刻章失败");
        }
        return ResponseResult.success("刻章单位接收成功");
    }

    /**窗口人员点击公章送达生成时间，并生成时长*/
    @GetMapping("countStampDuration")
    public ResponseResult countStampDuration(Integer id) throws Exception{
        PrintInfo printInfo = this.getService().getById(id);
        if(printInfo == null){
            return ResponseResult.success();
        }
        Long duration = (sdf.parse(sdf.format(new Date())).getTime()-sdf.parse(printInfo.getCompanyPushTime()).getTime())/1000/60;
        if(duration<=0){
            duration = 1L;
        }
        BigDecimal stampDuration = new BigDecimal(duration);
        printInfo.setStampDuration(stampDuration);
        printInfo = printInfoService.setTotalTime(printInfo);
        Integer result = this.getService().update(id,printInfo);
        if(result<0){
            return ResponseResult.error("公章送达点击失败");
        }
        return ResponseResult.success("公章送达点击成功");
    }

    /**窗口人员点击税务发票生成时间，并计算时长*/
    @GetMapping("countTaxDuration")
    public ResponseResult countTaxDuration(Integer id) throws Exception{
        PrintInfo printInfo = this.getService().getById(id);
        if(printInfo == null){
            return ResponseResult.success();
        }
        Long duration = (sdf.parse(sdf.format(new Date())).getTime()-sdf.parse(printInfo.getCompanyPushTime()).getTime())/1000/60;
        if(duration<=0){
            duration = 1L;
        }
        BigDecimal taxDuration = new BigDecimal(duration);
        printInfo.setTaxDuration(taxDuration);
        printInfo = printInfoService.setTotalTime(printInfo);
        Integer result = this.getService().update(id,printInfo);
        if(result<0){
            return ResponseResult.error("税务送达点击失败");
        }
        return ResponseResult.success("税务送达点击成功");
    }

    /**窗口人员点击银行开户生成时间，并且计算时长*/
    @GetMapping("countBankDuration")
    public ResponseResult countBankDuration(Integer id)throws Exception{
        PrintInfo printInfo = this.getService().getById(id);
        if(printInfo == null){
            return ResponseResult.success();
        }
        Long duration = (sdf.parse(sdf.format(new Date())).getTime()-sdf.parse(printInfo.getCompanyPushTime()).getTime())/1000/60;
        if(duration <=0){
            duration = 1L;
        }
        BigDecimal bankDuration = new BigDecimal(duration);
        printInfo.setBankDuration(bankDuration);
        printInfo = printInfoService.setTotalTime(printInfo);
        Integer result = this.getService().update(id,printInfo);
        if(result<0){
            return ResponseResult.error("银行开户点击失败");
        }
        return ResponseResult.success("银行开户点击成功");
    }

    /**计算总时长*/
    @GetMapping("countAllDuration")
    public ResponseResult countAllDuration(Integer id) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        PrintInfo printInfo = this.getService().getById(id);
        if(printInfo == null){
            return ResponseResult.success();
        }
        BigDecimal companyPushDuration=printInfo.getCompanyPushDuration()==null?BigDecimal.ZERO:printInfo.getCompanyPushDuration();
        BigDecimal stampDuration = printInfo.getStampDuration()==null?BigDecimal.ZERO:printInfo.getStampDuration();
        BigDecimal taxDuration = printInfo.getTaxDuration() == null? BigDecimal.ZERO:printInfo.getTaxDuration();
        BigDecimal bankDuration = printInfo.getBankDuration() == null?BigDecimal.ZERO:printInfo.getBankDuration();


        BigDecimal maxDuration = printInfoService.getMax(stampDuration,taxDuration,bankDuration);
        //设置总时长
        printInfo.setAllDuration(maxDuration.add(companyPushDuration));
        Integer result = this.getService().update(id,printInfo);
        if(result<0){
            return ResponseResult.success();
        }
        return ResponseResult.success("总时长跟新成功");
    }

    /**4.导出到excel**/
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ResponseResult export(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str = printInfoService.export(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.error("导出失败");
    }
}
