package com.message.service;

import com.common.model.PageData;
import com.common.utils.ExportExcel;
import com.google.common.collect.Lists;
import com.message.core.base.BaseMapper;
import com.message.core.base.BaseService;
import com.message.core.base.MybatisBaseMapper;
import com.message.domain.output.SMSReceiveOutPut;
import com.message.mapper.jpa.MoModelsRepository;
import com.message.mapper.jpa.SMSReceiveRepository;
import com.message.mapper.mybatis.MoModelsMapper;
import com.message.mapper.mybatis.SMSReceiveMapper;
import com.message.mapper.mybatis.SMSSendMapper;
import com.message.mapper.mybatis.SuggestionsMapper;
import com.message.model.MoModels;
import com.message.model.SMSReceive;
import com.message.model.SMSSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Young
 * @description:
 * @date: Created in 11:44 2018/10/25
 * @modified by:
 */
@Service
public class SMSReceiveService extends BaseService<SMSReceiveOutPut, SMSReceive,Integer> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SMSReceiveRepository receiveRepository;
    @Autowired
    private SMSReceiveMapper smsReceiveMapper;
    @Autowired
    private MoModelsMapper moModelsMapper;
    @Autowired
    private SMSSendMapper smsSendMapper;
    @Autowired
    private MoModelsRepository moModelsRepository;
    @Autowired
    private SuggestionsMapper suggestionsMapper;

    @Override
    public BaseMapper<SMSReceive, Integer> getMapper() {
        return receiveRepository;
    }

    @Override
    public MybatisBaseMapper<SMSReceiveOutPut> getMybatisBaseMapper() {
        return smsReceiveMapper;
    }


    /**
     * 定时每15秒向收件箱中保存数据
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveMessageReceive(){
        logger.info("开始向收件箱源数据表中拉取没有逻辑删除的源数据短信");
        List<MoModels> moModelsList = moModelsMapper.selectPageList();
        if(moModelsList.size()==0){
            return;
        }
        logger.info("拉取出的源数据短信是"+moModelsList.size()+"条");
        List<SMSReceive> smsReceiveList = Lists.newArrayList();
        try{
            //遍历回复的内容
            for(MoModels moModels: moModelsList){
                logger.info("回复的短信号码是:{},回复的内容是:{}",moModels.getMobile(),moModels.getSmsContent());
                //根据回复的号码和回复的时间查询发送人的姓名和发送编号和业务类型
                SMSSend smsSend = smsSendMapper.selectByNumber(moModels.getMobile(),moModels.getSendTime());
                if(smsSend== null){
                    moModels.setAmputated(1);
                    moModelsRepository.saveAndFlush(moModels);
                    logger.info("未查出匹配的发送人");
                    continue;
                }
                //实例化收件箱
                SMSReceive smsReceive = new SMSReceive();
                //设置回复方id
                smsReceive.setSendId(smsSend.getId());

                if(smsSend.getReceiveEmployeeName()==null){
                    smsReceive.setSendName("未知姓名");
                }
                //回复方的姓名
                smsReceive.setSendName(smsSend.getReceiveEmployeeName());
                //设置回复方的业务类型
                smsReceive.setType(smsSend.getType());
                //设置回复方的手机号
                smsReceive.setSendTelephone(moModels.getMobile());
                //设置回复方的回复内容
                smsReceive.setDescription(moModels.getSmsContent());
                //设置回复方的回复时间
                smsReceive.setSendTime(moModels.getSendTime());
                smsReceive.setCreatedDateTime(new Date());
                smsReceive.setCreatedUserId(0);
                smsReceive.setCreatedUserName("smsReceiveJob");
                smsReceive.setLastUpdateDateTime(new Date());
                smsReceive.setLastUpdateUserId(0);
                smsReceive.setLastUpdateUserName("smsReceiveJob");
                smsReceive.setAmputated(0);
                smsReceiveList.add(smsReceive);
                //设置源数据表的删除状态设置为已经删除
                moModels.setAmputated(1);
                //将数据新增到收件箱
                moModelsRepository.saveAndFlush(moModels);
            }
            smsReceiveList = receiveRepository.saveAll(smsReceiveList);
            if(smsReceiveList.size()>0){
                logger.info("收件箱保存了"+smsReceiveList.size()+"条回复短信");
                return;
            }
        }catch (Exception e){
            logger.error("保存到收件箱失败");
        }
    }


    public String exportExcel(HttpServletResponse response , HttpServletRequest request) throws Exception {
        String title = "收件箱表";
        String excelName = "收件箱表";
        List<Object[]> dataList = new ArrayList<>();
        Object[] objs = null;
        PageData pageData = new PageData(request);
        List<SMSReceiveOutPut> smsReceiveOutPutList =smsReceiveMapper.selectAll(pageData);
        if(smsReceiveOutPutList.size()==0||smsReceiveOutPutList==null){
            return "";
        }
        String[]rowsName = {"序列","业务类型","发送号码","发送姓名","接收时间","短信内容"};

        for(int i=0;i<smsReceiveOutPutList.size();i++){
            SMSReceiveOutPut smsReceiveOutPut = smsReceiveOutPutList.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = smsReceiveOutPut.getTypeName();
            objs[2] = smsReceiveOutPut.getSendTelephone();
            objs[3] = smsReceiveOutPut.getSendName();
            objs[4] = smsReceiveOutPut.getSendTime();
            objs[5] = smsReceiveOutPut.getDescription();
            dataList.add(objs);
        }
        ExportExcel exportExcel = new ExportExcel(title,rowsName,dataList,excelName);
        return exportExcel.export(response, request);

    }


}
