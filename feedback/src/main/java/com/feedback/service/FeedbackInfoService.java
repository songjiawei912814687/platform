package com.feedback.service;


import com.common.Enum.*;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.ExportExcel;
import com.feedback.config.RedisComponent;
import com.feedback.core.base.BaseMapper;
import com.feedback.core.base.BaseService;
import com.feedback.core.base.MybatisBaseMapper;
import com.feedback.core.util.AppConsts;
import com.feedback.domain.output.*;
import com.feedback.enums.IsTimingEnum;
import com.feedback.enums.SendStateEnum;
import com.feedback.mapper.jpa.*;
import com.feedback.mapper.mybatis.*;
import com.feedback.model.*;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: Young
 * @description: 反馈信息
 * @date: Created in 15:41 2018/11/5
 * @modified by:
 */
@Service
public class FeedbackInfoService extends BaseService<FeedbackInfoOutput, FeedbackInfo,Integer> {

    @Autowired
    private FeedbackInfoRepository feedbackInfoRepository;
    @Autowired
    private FeedbackInfoMapper feedbackInfoMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private EmployeesMapper employeesMapper;
    @Autowired
    private WindowMapper windowMapper;
    @Autowired
    private SMSReceiveMapper smsReceiveMapper;
    @Autowired
    private RedisComponent redisComponent;
    @Autowired
    private SuggesstionsRepository suggesstionsRepository;
    @Autowired
    private SmsTemplateService smsTemplateService;
    @Autowired
    private SMSSendRepository smsSendRepository;

    @Value("${spring.message.deadline}")
    private String deadline;



    @Override
    public BaseMapper<FeedbackInfo, Integer> getMapper() {
        return feedbackInfoRepository;
    }

    @Override
    public MybatisBaseMapper<FeedbackInfoOutput> getMybatisBaseMapper() {
        return feedbackInfoMapper;
    }

    private static final Logger log = LoggerFactory.getLogger(FeedbackInfoService.class);

    //30分钟接到redis过期消息开始发送信息或者1秒后接收到redis推送来的评价器的评价短信
    public void reciveMessage(Integer feedbackId) {
        FeedbackInfo feedbackInfo = feedbackInfoRepository.findFeedbackInfoById(feedbackId);
        if(feedbackInfo==null){
            log.error("没有找到对应的反馈信息");
            return;
        }
        //设置到以前做的redis中过期时间为2小时
        setValueToRedis(feedbackInfo);
        log.info("成功将反馈信息推送到redis中");
        return;
    }

    /**修改反馈信息状态*/
    public int updateFeedBack(Integer id,Integer status,Integer complete) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        FeedbackInfoOutput feedbackInfoOutput = feedbackInfoMapper.selectByPrimaryKey(id);
        //如果是满意且跑一次
        if(SatisfactionEnum.SATISFIED.getCode().equals(feedbackInfoOutput.getSatisfaction())){
            return SUCCESS;
        }
        feedbackInfoOutput.setSatisfaction(status);
        feedbackInfoOutput.setCompleteRate(complete);
        FeedbackInfo feedbackInfoItem = new FeedbackInfo();
        BeanUtils.copyProperties(feedbackInfoOutput,feedbackInfoItem);
        feedbackInfoItem.setAppraiseState(AppraiseStateEnum.IS_APPRAISE.getCode());
        int result = this.update(id,feedbackInfoItem);
        if(result < 0){
            return ERROR;
        }
        return SUCCESS;
    }

    /**第一步导入反馈信息,第二步在SMSSendService中**/
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult checkedFile(MultipartFile file) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().trim();
        if(!suffixName.equals(".xls")&&!suffixName.equals(".xlsx")){
            return ResponseResult.error("数据表格式不正确");
        }
        int index = fileName.lastIndexOf("\\");
        if (index != -1) {
            fileName = fileName.substring(index + 1);
        }
        int hashCode = fileName.hashCode();
        //把hash值转换为十六进制
        String hex = Integer.toHexString(hashCode);
        String pathHeader = "C:";
        StringBuilder sb = new StringBuilder();
        sb.append("/" + hex.charAt(0));
        sb.append("/" + hex.charAt(1));
        sb.append("/" + System.currentTimeMillis() + "_" + fileName);
        String pathEnd = sb.toString();
        File fileInfo = new File(pathHeader + pathEnd);
        if (!fileInfo.getParentFile().exists()) {
            fileInfo.getParentFile().mkdirs();
        }
        file.transferTo(fileInfo);
        Workbook wb = null;
        try {
            wb = new HSSFWorkbook(new FileInputStream(pathHeader + pathEnd));
        } catch (Exception e) {
            wb = new XSSFWorkbook(new FileInputStream(pathHeader + pathEnd));
        }
        return importFeedbackInfo(wb);
    }

    /**数据导入**/
    private ResponseResult importFeedbackInfo(Workbook wb) throws InvocationTargetException, IntrospectionException,  IllegalAccessException{
        // 得到第一个sheet
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        int rows = sheet.getPhysicalNumberOfRows();
        int totalCells = 0;
        // 得到Excel的列数(前提是有行数)
        if (rows > 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<FeedbackInfo>feedbackInfoList = Lists.newArrayList();
        for (int r = 1; r <rows; r++) {
            Row row = sheet.getRow(r);
            FeedbackInfo feedbackInfo = new FeedbackInfo();
            if (row == null) {
                continue;
            }
            // 循环Excel的列
            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 6) {
                        try {
                            if (0 == cell.getCellType()) {
                                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                    //用于转化为日期格式
                                    Date d = cell.getDateCellValue();
                                    feedbackInfo.setFeedbackTime(d);
                                } else {
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    try {
                                        Date date = simpleDateFormat.parse(String.valueOf(cell.getStringCellValue()));
                                        feedbackInfo.setFeedbackTime(date);
                                    } catch (ParseException px) {
                                        px.printStackTrace();
                                        return ResponseResult.error("第" + r + "行,第七列反馈时间填写错误");
                                    }
                                }
                            }
                        }catch (Exception e){
                            return ResponseResult.error("第" + r + "行,第七列反馈时间格式填写错误");
                        }
                    }
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String value = String.valueOf(cell.getStringCellValue()).trim();
                    if (!value.equals("") && value != null) {
                        Organization organization = null;
                        Window window = null;
                        if (c == 0) {
                            //设置群众名字
                            feedbackInfo.setPersonName(value);
                        } else if (c == 1) {
                            //设置群众号码
                            feedbackInfo.setPhone(value);
                        } else if (c == 2) {
                            //组织名称精确匹配查找
                            organization = organizationMapper.selectByName(value);
                            if(organization!=null){
                                //根据组织编号从新获取对象
                                organization = organizationMapper.selectByOrgaNo(organization.getOrganizationNo());
                                feedbackInfo.setOrganizationName(value);
                                feedbackInfo.setOrganizationId(organization.getId());
                            }else {
                                return ResponseResult.error("第"+r+"行,第三列组织名称填写错误");
                            }
                        } else if (c == 3) {
                            List<Window> windowList = windowMapper.selectByWindowNo(value);
                            if(windowList.size() == 0){
                                return ResponseResult.error("第"+r+"行,第四列窗口号填写错误");
                            }
                            else {
                                feedbackInfo.setWindowNo(value);
                            }
                        } else if (c == 4) {
                            //员工工号
                            int count = employeesMapper.selectCountEmployeesNo(value);
                            if(count == 0){
                                return ResponseResult.error("第"+r+"行,第五列员工号填写错误");
                            }else {
                                feedbackInfo.setEmployeesNo(value);
                                String employeesName = employeesMapper.selectByNo(value);
                                feedbackInfo.setEmployeesName(employeesName);
                            }
                        } else if (c == 5) {
                            feedbackInfo.setMatters(value);
                        }
                    }
                }
            }
            //设置成未评价状态
            feedbackInfo.setAppraiseState(AppraiseStateEnum.UN_APPRAISE.getCode());
            SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //得到反馈时间
            Date feedbackTime = feedbackInfo.getFeedbackTime();
            if(feedbackTime==null){
                continue;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(feedbackTime);
            calendar.add(Calendar.HOUR,2);
            Date twoDate = calendar.getTime();
            //最后评价时间设置成2小时后
            feedbackInfo.setDeadline(sdf.format(twoDate));
            //设置成短信待发送状态
            feedbackInfo.setSendState(FeedbackInfoSendStateEnum.WAIT_SEND.getCode());
            setProperty(feedbackInfo, "createdUserId", getUsers().getId());
            setProperty(feedbackInfo, "createdUserName", getUsers().getUsername());
            setProperty(feedbackInfo, "createdDateTime", new Date());
            setProperty(feedbackInfo, "lastUpdateUserId", getUsers().getId());
            setProperty(feedbackInfo, "lastUpdateUserName", getUsers().getUsername());
            setProperty(feedbackInfo, "lastUpdateDateTime", new Date());
            setProperty(feedbackInfo,"amputated",0);
            feedbackInfoList.add(feedbackInfo);
        }

        feedbackInfoList = feedbackInfoRepository.saveAll(feedbackInfoList);
        if(feedbackInfoList.size()==0){
            return ResponseResult.error("导入失败");
        }
        log.info("导入到数据中数据导入成功:{}",feedbackInfoList);
        return ResponseResult.success(feedbackInfoList);
    }


    /**第三步,接受评价短信内容**/
    @Transactional(rollbackFor = Exception.class)
    public void findFeedbackInfoInTwoHours(Integer feedbackId) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FeedbackInfo feedbackInfo = feedbackInfoRepository.findFeedbackInfoById(feedbackId);
        //根据发送的号码和发送的短信类型去收件表中查询数据
        SMSReceiveOutPut smsReceive = smsReceiveMapper.selectByPhone(feedbackInfo.getPhone(),MessageTypeEnum.WINDOW_APPRAISE.getCode());
        //当未回复的时候
        if(smsReceive == null || sdf.parse(smsReceive.getSendTime()).compareTo(feedbackInfo.getCreatedDateTime())<0){
            resolveNoReceive(feedbackInfo);
            return;
        }else {
            //得到回复的内容
            String content = smsReceive.getDescription();
            //如果得到的回复内容不为空,设置评价时间
            feedbackInfo.setAppraiseTime(smsReceive.getSendTime());
            //调用处理短信的私有方法
            assembleMessage(content,feedbackInfo);
            //设置成 已经评价状态
            feedbackInfo.setAppraiseState(AppraiseStateEnum.IS_APPRAISE.getCode());
            //反馈信息设置回复内容
            feedbackInfo.setAppraiseContent(content);
            feedbackInfo.setLastUpdateDateTime(new Date());
            /**第四步跟新反馈信息的评价状态*/
            int resultId = feedbackInfoRepository.save(feedbackInfo).getId();
            if(resultId<=0){
                log.info("反馈信息跟新评价内容失败");
                return;
            }
            /**第五步，不满意添加到投诉建议*/
            if(content.equals("2")|| content.equals("3")||content.equals("4")){
                addToSuggestion(feedbackInfo,AppConsts.Resource_Message);
                /**第六步添加投诉建议成功发送不满意询问短信给群众,第七步在suggestionService里面*/
                sendMessageToPerson(feedbackInfo, SmsTemplateEnum.CKHF_TYPE.getCode(),
                        MessageTypeEnum.WINDOW_VISIT.getCode(),
                        "窗口第一次回访",
                        "窗口第一次回访");
            }
        }
    }

    /**
     * 处理未回复的信息
     * @param feedbackInfo
     */
    private void resolveNoReceive(FeedbackInfo feedbackInfo){
        //未回复，设置默认回复状态
        feedbackInfo.setAppraiseState(AppraiseStateEnum.IS_APPRAISE.getCode());
        //设置满意度满意
        feedbackInfo.setSatisfaction(SatisfactionEnum.SATISFIED.getCode());
        //设置实现率，跑一次
        feedbackInfo.setCompleteRate(CompleteRateEnum.ONE.getCode());
        //设置处理时间
        feedbackInfo.setAppraiseTime(feedbackInfo.getDeadline());
        //将发送状态设置为处理成功状态
        feedbackInfo.setSendState(FeedbackInfoSendStateEnum.DEAL_SUCCESS.getCode());
        feedbackInfo.setLastUpdateDateTime(new Date());
        //设置回复内容
        feedbackInfo.setAppraiseContent("未回复,默认评价为非常满意且跑一次");

        int result = feedbackInfoRepository.save(feedbackInfo).getId();
        if(result<0){
            log.info("反馈信息跟新未评价失败");
        }
    }

    /**
     * 不满意添加到投诉建议
     * @param feedbackInfo
     */
    private void addToSuggestion(FeedbackInfo feedbackInfo,Integer resource){
        Suggestions suggestions = new Suggestions();
        //设置反馈信息id
        suggestions.setFeedbackId(feedbackInfo.getId());
        suggestions.setParentId(0);
        suggestions.setAmputated(0);

        //设置资源类型
        suggestions.setDateResource(resource);
        //未回复投诉建议状态
        suggestions.setReplyType(AppConsts.Reply_Un);

        //设置群众属性
        suggestions.setSuggestionName(feedbackInfo.getPersonName());
        suggestions.setSuggestionPhoneNumber(feedbackInfo.getPhone());

        //设置组织属性
        suggestions.setOrganizationName(feedbackInfo.getOrganizationName());
        suggestions.setOrganizationId(feedbackInfo.getOrganizationId());

        //设置指定回复人
        OrganizationOutput organizationOutput = organizationMapper.selectByPrimaryKey(feedbackInfo.getOrganizationId());
        if(organizationOutput.getDepartmentalManager() == null){
            suggestions.setReplyUserId(null);
        }
        suggestions.setReplyUserId(organizationOutput.getDepartmentalManager());

        //设置窗口号
        suggestions.setWindowName(feedbackInfo.getWindowNo());

        //设置员工的属性
        suggestions.setEmployeeName(feedbackInfo.getEmployeesName());
        suggestions.setEmployeeNo(feedbackInfo.getEmployeesNo());

        //设置待发布状态
        suggestions.setPublish(AppConsts.Publish_No);
        //设置成未处理状态
        suggestions.setDealState(AppConsts.DealState_No);
        //设置成未逾期状态
        suggestions.setOutOfDate(AppConsts.OutOfDate_No);

        suggestions.setCreatedUserName("不满意推送投诉建议");
        //设置投诉时间
        suggestions.setCreatedDateTime(feedbackInfo.getFeedbackTime());
        suggestions.setCreatedUserId(0);
        suggestions.setLastUpdateDateTime(new Date());
        suggestions.setLastUpdateUserId(0);
        suggestions.setLastUpdateUserName("不满意推送投诉建议");
        Integer suggestIonsId = suggesstionsRepository.save(suggestions).getId();
        if(suggestIonsId <0){
            return;
        }
        log.info("不满意推送到投诉建议添加成功");
    }

    /**
     * 发送第一次回访信息和第二次回访信息
     * @param feedbackInfo
     */
    private void sendMessageToPerson(FeedbackInfo feedbackInfo,String smsTemplateEnum,
                                     Integer messageTypeEnum,String createdUserName,
                                     String lastUpdateUserName){
        PageData pageData = new PageData();
        pageData.put("type", smsTemplateEnum);
        SmsTemplateOutput smsTemplateOutput = smsTemplateService.getByType(smsTemplateEnum);
        if (smsTemplateOutput == null) {
            log.error("无可用窗口回访的模板");
            return;
        }
        //得到模板里面的回访内容
        String  hfMessage = smsTemplateOutput.getDescription();
        log.info("添加模板内容成功:{}",hfMessage);
        SMSSend sendInfo = new SMSSend();
        sendInfo.setAmputated(0);
        //设置短信发送类型为窗口回访
        sendInfo.setType(messageTypeEnum);
        sendInfo.setDescription(hfMessage);
        sendInfo.setReceiveTelephone(feedbackInfo.getPhone());
        sendInfo.setReceiveEmployeeName(feedbackInfo.getPersonName());
        sendInfo.setIsTiming(0);
        sendInfo.setState(SendStateEnum.UN_SEND.getCode());
        sendInfo.setCreatedDateTime(new Date());
        sendInfo.setCreatedUserId(0);
        sendInfo.setCreatedUserName(createdUserName);
        sendInfo.setLastUpdateDateTime(new Date());
        sendInfo.setLastUpdateUserId(0);
        sendInfo.setLastUpdateUserName(lastUpdateUserName);
        Integer smsSendId = smsSendRepository.save(sendInfo).getId();
        if(smsSendId<0){
            return;
        }
        setMessageValueToRedis(sendInfo);
        log.info("成功发送给群众不满意情况的询问短信");
    }

    /**
     * 将发送给用户的短信推送到redis中
     * @param sMsSend
     */
    private void setMessageValueToRedis(SMSSend sMsSend) {
        if (sMsSend.getIsTiming().equals(IsTimingEnum.YES.getCode())) {

            if (sMsSend.getTimingTime() != null) {
                long l = sMsSend.getTimingTime().getTime() - System.currentTimeMillis();
                if (l < 0) {
                    redisComponent.sentinelSet("HZFT_MESSAGE:" + sMsSend.getId(), "", 5L);
                }
                l = l / 1000;
                redisComponent.sentinelSet("HZFT_MESSAGE:" + sMsSend.getId(), "", l);
            } else {
                redisComponent.sentinelSet("HZFT_MESSAGE:" + sMsSend.getId(), "", 5L);
            }
        } else {
            redisComponent.sentinelSet("HZFT_MESSAGE:" + sMsSend.getId(), "", 5L);
        }
    }

    /**处理回复的短信*/
    private FeedbackInfo assembleMessage(String content,FeedbackInfo feedbackInfo){
        if (content.equals("1")) {
            //设置成满意
            feedbackInfo.setSatisfaction(SatisfactionEnum.SATISFIED.getCode());
            //设置成跑一次
            feedbackInfo.setCompleteRate(CompleteRateEnum.ONE.getCode());
            //将发送状态设置为处理成功状态
            feedbackInfo.setSendState(FeedbackInfoSendStateEnum.DEAL_SUCCESS.getCode());
        } else if (content.equals("2")) {
            //设置成满意
            feedbackInfo.setSatisfaction(SatisfactionEnum.SATISFIED.getCode());
            //设置成跑多次
            feedbackInfo.setCompleteRate(CompleteRateEnum.MORE.getCode());
            //将发送状态设置为处理成功状态
            feedbackInfo.setSendState(FeedbackInfoSendStateEnum.DEAL_SUCCESS.getCode());
        } else if (content.equals("3")) {
            //设置成不满意
            feedbackInfo.setSatisfaction(SatisfactionEnum.UN_SATISFIED.getCode());
            //设置成跑多次
            feedbackInfo.setCompleteRate(CompleteRateEnum.ONE.getCode());
            //将发送状态设置为处理成功状态
            feedbackInfo.setSendState(FeedbackInfoSendStateEnum.DEAL_SUCCESS.getCode());
        } else if (content.equals("4")) {
            //设置成不满意
            feedbackInfo.setSatisfaction(SatisfactionEnum.UN_SATISFIED.getCode());
            //设置成跑多次
            feedbackInfo.setCompleteRate(CompleteRateEnum.MORE.getCode());
            //将发送状态设置为处理成功状态
            feedbackInfo.setSendState(FeedbackInfoSendStateEnum.DEAL_SUCCESS.getCode());
        } else {
            //设置成满意
            feedbackInfo.setSatisfaction(SatisfactionEnum.SATISFIED.getCode());
            //设置成跑一次
            feedbackInfo.setCompleteRate(CompleteRateEnum.ONE.getCode());
            //设置成无效短信
            feedbackInfo.setSendState(FeedbackInfoSendStateEnum.UN_VALID.getCode());
        }
        return feedbackInfo;
    }


    /**第11步接受2次回访信息*/
    @Transactional(rollbackFor = Exception.class)
    public void sloveReturnVist(Integer feedbackId) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //查询反馈信息
        FeedbackInfo feedbackInfo = feedbackInfoRepository.findFeedbackInfoById(feedbackId);
        //发送的号码去收件表中查询数据
        SMSReceiveOutPut smsReceive = smsReceiveMapper.selectByPhone(feedbackInfo.getPhone(),MessageTypeEnum.WINDOW_RESEND.getCode());
        if (smsReceive == null ||sdf.parse(smsReceive.getSendTime()).compareTo(feedbackInfo.getLastUpdateDateTime())<0) {
            resolveNoReceive(feedbackInfo);
        }else {
            //得到回复的内容
            String content = smsReceive.getDescription();
            //如果得到的回复内容不为空,设置评价时间
            feedbackInfo.setAppraiseTime(smsReceive.getSendTime());
            //处理评价信息
            assembleMessage(content,feedbackInfo);
            //设置成 已经评价状态
            feedbackInfo.setAppraiseState(AppraiseStateEnum.IS_APPRAISE.getCode());
            int success = feedbackInfoRepository.save(feedbackInfo).getId();
            if (success == 0) {
                log.error("二次回访的回复信息处理失败");
                return;
            }
            /**第十二步不满意再次添加投诉建议*/
            if(content.equals("2")|| content.equals("3")|| content.equals("4")) {
                addToSuggestion(feedbackInfo,AppConsts.Resource_Second);
                /**第十三步再次发送不满意原因 14 步在suggetsionService中*/
                sendMessageToPerson(feedbackInfo,SmsTemplateEnum.CKZCHF_TYPE.getCode(),
                        MessageTypeEnum.WINDOW_RE_VISIT.getCode(),
                        "窗口第二次回访","窗口第二次回访");
            }
        }
    }

    /**设置到redis**/
    private void setValueToRedis(FeedbackInfo feedbackInfo) {
        //过期设置成二小时
        redisComponent.sentinelSet("FEED_BACK_INFO:" + feedbackInfo.getId(), "", Long.valueOf(60*60*Integer.valueOf(deadline)));
    }

    public ResponseResult findPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        if(pageData.getMap().get("organizationId")!=null&&!pageData.getMap().get("organizationId").equals("")){
            String path = feedbackInfoMapper.selectPathById(Integer.valueOf(pageData.getMap().get("organizationId")));
            pageData.put("path",path);
        }
        pageData.put("userId",getUsers().getId().toString());
        if(getUsers().getAdministratorLevel()!=9){
            if(getUsers().getUserType()==0){
                pageData.put("employeeNo",getUsers().getUsername());
            }else {
                pageData.put("orgId",getUsers().getOrganizationId().toString());
            }
        }
        List<FeedbackInfoOutput> feedbackInfoOutputList = this.selectAll(true,pageData);
        PageInfo pageInfo = new PageInfo(feedbackInfoOutputList);
        return ResponseResult.success(pageInfo);
    }

    /**数据导出**/
    public String export(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "反馈信息表";
        String excelName = "反馈信息表";
        String[] rowsName = new String[]{"序列","群众姓名","群众电话","受理部门", "窗口号","业务名称","人工工号", "办理时间", "评价状态","满意度","实现率"};
        List<Object[]> dataList = new ArrayList<>();
        Object[] objs ;
        PageData pageData = new PageData(request);
        if(getUsers().getAdministratorLevel()!=9){
            pageData.put("userId",getUsers().getId().toString());
            if(getUsers().getUserType()==0){
                pageData.put("employeeNo",getUsers().getUsername());
            }else {
                pageData.put("orgId",getUsers().getOrganizationId().toString());
            }
        }
        List<FeedbackInfoOutput> feedbackInfoOutputList=feedbackInfoMapper.selectAll(pageData);


        if(feedbackInfoOutputList.size()>0){
            int i=1;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for(FeedbackInfoOutput feedbackInfoOutput:feedbackInfoOutputList){

                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=feedbackInfoOutput.getPersonName();
                objs[2]=feedbackInfoOutput.getPhone();
                objs[3]=feedbackInfoOutput.getOrganizationName();
                objs[4]=feedbackInfoOutput.getWindowNo();
                if(StringUtils.isBlank(feedbackInfoOutput.getMatter())){
                    objs[5]=feedbackInfoOutput.getMatters();
                }else {
                    objs[5]=feedbackInfoOutput.getMatter();
                }
                objs[6]=feedbackInfoOutput.getEmployeesNo();
                objs[7]=formatter.format(feedbackInfoOutput.getFeedbackTime());
                objs[8]=feedbackInfoOutput.getAppraiseStateName();
                objs[9]=feedbackInfoOutput.getSatisfactionName();
                objs[10]=feedbackInfoOutput.getCompleteRateName();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }

    /**当有redis的遗漏信息时，每天20:30点同步反馈信息修改状态为满意，跑一次*/
    public void sysFeedback(){
        feedbackInfoMapper.updateByAppState();
        log.info("同步完成反馈信息");
    }

}
