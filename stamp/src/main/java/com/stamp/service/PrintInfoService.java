package com.stamp.service;

import com.common.model.PageData;
import com.common.utils.ExportExcel;
import com.stamp.core.base.BaseRepository;
import com.stamp.core.base.BaseService;
import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.input.SMSSendInput;
import com.stamp.domain.output.PrintInfoOutput;
import com.stamp.domain.output.StampOrganOutput;
import com.stamp.enums.AttachmentEnum;
import com.stamp.mapper.jpa.EmployeesRepository;
import com.stamp.mapper.jpa.PrintInfoRepository;
import com.stamp.mapper.jpa.AttachmentRepository;
import com.stamp.mapper.mybatis.PrintInfoMapper;
import com.stamp.mapper.mybatis.StampOrganMapper;
import com.stamp.model.Attachment;
import com.stamp.model.Employees;
import com.stamp.model.PrintInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-04-16  17:58
 * @modified by:
 */
@Service
public class PrintInfoService extends BaseService<PrintInfoOutput, PrintInfo,Integer> {

    @Autowired
    private PrintInfoRepository printInfoRepository;
    @Autowired
    private PrintInfoMapper printInfoMapper;
    @Autowired
    private EmployeesRepository employeesRepository;
    @Autowired
    private StampOrganMapper stampOrganOutput;
    @Autowired
    private SMSSendService smsSendService;
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Override
    public BaseRepository<PrintInfo, Integer> getMapper() {
        return printInfoRepository;
    }

    @Override
    public MybatisBaseMapper<PrintInfoOutput> getMybatisBaseMapper() {
        return printInfoMapper;
    }


    public Employees findByEmpId(Integer empId){
        Employees employees = employeesRepository.findEmployeesByIdAndAmputated(empId,0);
        if(employees == null){
            return null;
        }
        return employees;
    }


    public Integer sendMessage(PrintInfo printInfo) throws Exception {
        //根据刻章单位id查询出刻章单位
        StampOrganOutput organizationOutput = stampOrganOutput.selectByPrimaryKey(printInfo.getStampCompany());
        if(organizationOutput==null){
            return ERROR;
        }
        String messageContent = organizationOutput.getName()+":"+printInfo.getCompanyName()+"新办刻章已经申请，请及时" +
                "登陆压缩企业开办数据平台交换平台接收信息，刻制相应公章，按时送达窗口";

        List<String> sendList = new ArrayList<>();
        sendList.add(organizationOutput.getPhoneNumber()+ "/" +organizationOutput.getName());


        SMSSendInput smsSendInput = new SMSSendInput();
        smsSendInput.setSendList(sendList);
        smsSendInput.setDescription(messageContent);
        smsSendInput.setCreatedDateTime(new Date());
        smsSendInput.setCreatedUserId(this.getUsers().getId());
        smsSendInput.setCreatedUserName(this.getUsers().getUsername());
        smsSendInput.setLastUpdateUserId(this.getUsers().getId());
        smsSendInput.setLastUpdateUserName(this.getUsers().getUsername());
        smsSendInput.setLastUpdateDateTime(new Date());

        //返回1成功发送短信
        if(smsSendService.addMessages(smsSendInput)>0){
            return SUCCESS;
        }
        return ERROR;
    }

    @Transactional
    public int addListAttachmentList(List<Attachment> attachmentList) {
        HashSet<String> objects = new HashSet<>();
        for (Attachment attachment:attachmentList) {
            objects.add(attachment.getResourcesId() + "/" + attachment.getResourcesType());
        }
        Iterator<String> it = objects.iterator();
        while(it.hasNext()) {
            String next = it.next();
            String[] split = next.split("/");
            attachmentRepository.deleteAllByResourcesIdAndResourcesType(Integer.valueOf(split[0]),Integer.valueOf(split[1]));
        }
        var result = attachmentRepository.saveAll(attachmentList);
        if(result != null){
            return 1;
        }
        return 0;
    }

    public List<Attachment>  selectResource(Integer id){
        List<Attachment> attachmentList = attachmentRepository.findAllByResourcesIdAndResourcesType(id, AttachmentEnum.STAMP.getCode());
        if(attachmentList.size()==0){
            return null;
        }
        return attachmentList;
    }

    public BigDecimal getMax(BigDecimal a,BigDecimal b,BigDecimal c){
        if(a.doubleValue()>b.doubleValue()){
            if(a.doubleValue()>c.doubleValue()){
                return a;
            }else {
                return c;
            }
        }else {
            if(b.doubleValue()>c.doubleValue()){
                return b;
            }else {
                return c;
            }
        }
    }

    /**导出*/
    public String export(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "压缩企业开办数据交换平台导出报表";
        String excelName = "压缩企业开办数据交换平台详情表";
        String[] rowsName = new String[]{"序列","服务套餐","窗口人员名称","经办人姓名", "营业执照推送时间", "刻章单位接收时长","刻章单位","总时长","备注","企业名称","企业地址"};
        List<Object[]> dataList = new ArrayList<>();
        Object[] objs = null;
        PageData pageData = new PageData(request);
        //如果不是管理员账户
        if(getUsers().getAdministratorLevel()!=9){
            if(getUsers().getUserType()==0){
                pageData.put("createUserId",getUsers().getId().toString());
            }else if (getUsers().getUserType()==2){
                pageData.put("organizationId",getUsers().getOrganizationId().toString());
            }
        }
        List<PrintInfoOutput> printInfoOutputList =  printInfoMapper.selectPage(pageData);
        if(printInfoOutputList.size()>0){
            int i=1;
            for(PrintInfoOutput printInfoOutput:printInfoOutputList){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=printInfoOutput.getServicePlanName();
                objs[2]=printInfoOutput.getEmpName();
                objs[3]=printInfoOutput.getDoName();
                objs[4]=printInfoOutput.getCompanyPushTime();
                objs[5]=printInfoOutput.getCompanyPushDuration();
                objs[6]=printInfoOutput.getStampCompanyName();
                objs[7]=printInfoOutput.getAllDuration();
                objs[8]=printInfoOutput.getBak();
                objs[9]=printInfoOutput.getCompanyName();
                objs[10]=printInfoOutput.getCompanyAddress();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }

    public PrintInfo setTotalTime(PrintInfo printInfo) {
        BigDecimal companyPushDuration=printInfo.getCompanyPushDuration()==null?BigDecimal.ZERO:printInfo.getCompanyPushDuration();
        BigDecimal stampDuration = printInfo.getStampDuration()==null?BigDecimal.ZERO:printInfo.getStampDuration();
        BigDecimal taxDuration = printInfo.getTaxDuration() == null? BigDecimal.ZERO:printInfo.getTaxDuration();
        BigDecimal bankDuration = printInfo.getBankDuration() == null?BigDecimal.ZERO:printInfo.getBankDuration();
        BigDecimal maxDuration = this.getMax(stampDuration,taxDuration,bankDuration);
        //设置总时长
        printInfo.setAllDuration(maxDuration.add(companyPushDuration));
        return printInfo;
    }

    public PrintInfo setCompanyPushDuration(Integer id, PrintInfo printInfo)throws Exception{
        String pushTime = "";
        if(id==null||"".equals(id)){
            pushTime = printInfo.getCompanyPushTime();
        }else{
            PrintInfoOutput printInfoOutput = this.selectById(id);
            pushTime = printInfoOutput.getCompanyPushTime();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Long duration = (sdf.parse(pushTime).getTime()-sdf.parse(printInfo.getCallTime()).getTime())/1000/60;
        if(duration<=0){
            duration = 1L;
        }
        printInfo.setCompanyPushDuration(new BigDecimal(duration));
        return printInfo;
    }
}
