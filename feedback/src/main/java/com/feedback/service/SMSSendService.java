package com.feedback.service;

import com.common.Enum.FeedbackInfoSendStateEnum;
import com.common.Enum.MessageTypeEnum;
import com.common.Enum.SmsTemplateEnum;
import com.common.response.ResponseResult;
import com.common.utils.GetMessageTemplate;
import com.feedback.config.RedisComponent;
import com.feedback.core.base.BaseMapper;
import com.feedback.core.base.BaseService;
import com.feedback.core.base.MybatisBaseMapper;
import com.feedback.domain.output.SMSSendOutput;
import com.feedback.domain.output.SmsTemplateOutput;
import com.feedback.enums.IsTimingEnum;
import com.feedback.enums.SendStateEnum;
import com.feedback.mapper.jpa.FeedbackInfoRepository;
import com.feedback.mapper.jpa.SMSSendRepository;
import com.feedback.mapper.mybatis.SMSSendMapper;
import com.feedback.mapper.mybatis.SuggestionsMapper;
import com.feedback.model.FeedbackInfo;
import com.feedback.model.SMSSend;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class SMSSendService extends BaseService<SMSSendOutput, SMSSend, Integer> {

    private final static Logger log = LoggerFactory.getLogger(SMSSendService.class);

    @Autowired
    private SMSSendRepository repository;
    @Autowired
    private FeedbackInfoRepository feedbackInfoRepository;

    @Override
    public BaseMapper<SMSSend, Integer> getMapper() {
        return repository;
    }

    @Autowired
    private SMSSendMapper sMSSendMapper;
    @Autowired
    private SmsTemplateService smsTemplateService;
    @Override
    public MybatisBaseMapper<SMSSendOutput> getMybatisBaseMapper() {
        return sMSSendMapper;
    }
    @Autowired
    private RedisComponent redisComponent;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private SuggestionsMapper suggestionsMapper;

    @Value("${spring.message.deadline}")
    private String deadline;

    /**
     * 第二步定时30秒发送回馈短信，第三步在FeedbackInfoService中
     **/
    public ResponseResult sendFeedbackInfo() {
        //查询出发送状态为0的反馈信息的集合
        List<FeedbackInfo> feedbackInfoList = feedbackInfoRepository.findAllBySendState(FeedbackInfoSendStateEnum.WAIT_SEND.getCode());
        if (feedbackInfoList.size() == 0) {
            return ResponseResult.error("暂时没有可以发送的反馈信息");
        }
        List<SMSSend> smsSendList = Lists.newArrayList();
        for (FeedbackInfo feedbackInfo : feedbackInfoList) {
            //获取群众号码
            String phone = feedbackInfo.getPhone();
            //或者窗口号
            String windowNo = feedbackInfo.getWindowNo();
            //获取办理事项
            String matters = feedbackInfo.getMatters();
            //获取群众名字
            String personName = feedbackInfo.getPersonName();
            SMSSend smsSend = new SMSSend();
            smsSend.setIsTiming(IsTimingEnum.NO.getCode());

            SmsTemplateOutput smsTemplateOutput = smsTemplateService.getByType(SmsTemplateEnum.CKPJ_TYPE.getCode());
            if (smsTemplateOutput == null) {
                return ResponseResult.error("无可用的模板");
            }
            var map = GetMessageTemplate.getKey(smsTemplateOutput.getDescription());

            map.put("ckh", windowNo);
            map.put("blywmc", matters);
            String content = GetMessageTemplate.getContent(smsTemplateOutput.getDescription(), map);
            smsSend.setDescription(content);
            smsSend.setState(SendStateEnum.UN_SEND.getCode());
            //设置为窗口评价
            smsSend.setType(MessageTypeEnum.WINDOW_APPRAISE.getCode());
            smsSend.setReceiveEmployeeName(personName);
            smsSend.setReceiveTelephone(phone);
            smsSend.setAmputated(0);
            smsSend.setCreatedDateTime(new Date());
            smsSend.setCreatedUserName("feedbackInfoJob");
            smsSend.setCreatedUserId(1);
            smsSend.setLastUpdateDateTime(new Date());
            smsSend.setLastUpdateUserId(1);
            smsSend.setLastUpdateUserName("feedbackInfoJob");

            smsSendList.add(smsSend);
            //发送状态设置为已经发送成功状态
            feedbackInfo.setSendState(FeedbackInfoSendStateEnum.SEND_SUCCESS.getCode());
            setFeedbackInfoToRedis(feedbackInfo);
            log.info("成功将反馈信息加入到redis");
            Integer result = feedbackInfoRepository.save(feedbackInfo).getId();
            if(result<0){
                return ResponseResult.error("修改反馈信息短信发送状态失败");
            }

        }
        smsSendList = repository.saveAll(smsSendList);
        if(smsSendList.size()==0){
            return ResponseResult.error("没有成功添加发送给办事群众的短信内容");
        }
        //将待发送的反馈短信的消息队列设置到redis
        this.setValueToRedis(smsSendList);
        return ResponseResult.success("成功将反馈信息发送状态修改成功");
    }


    private void  setFeedbackInfoToRedis(FeedbackInfo feedbackInfo) {
            //过期设置成二小时
//        redisComponent.sentinelSet("FEED_BACK_INFO:" + feedbackInfo.getId(), "", Long.valueOf(60 * 60 * Integer.valueOf(deadline)));
        redisComponent.sentinelSet("FEED_BACK_INFO:" + feedbackInfo.getId(), "", 80L);
        log.info("成功将反馈信息添加到redis");
    }

    private void setValueToRedis(List<SMSSend> smsSends) {
        for (SMSSend sMSSend : smsSends) {
            if (sMSSend.getIsTiming().equals(IsTimingEnum.YES.getCode())) {
                Date date = new Date();

                if (sMSSend.getTimingTime() != null) {
                    long l = sMSSend.getTimingTime().getTime() - date.getTime();
                    if (l < 0) {
                        redisComponent.sentinelSet("HZFT_MESSAGE:" + sMSSend.getId(), "", 5L);
                        continue;
                    }
                    l = l / 1000;

                    redisComponent.sentinelSet("HZFT_MESSAGE:" + sMSSend.getId(), "", l);
                } else {
                    redisComponent.sentinelSet("HZFT_MESSAGE:" + sMSSend.getId(), "", 5L);
                }
            } else {
                redisComponent.sentinelSet("HZFT_MESSAGE:" + sMSSend.getId(), "", 5L);
            }

        }

    }

    private void setValueToRedis(SMSSend sMSSend) {
        if (sMSSend.getIsTiming().equals(IsTimingEnum.YES.getCode())) {
            Date date = new Date();

            if (sMSSend.getTimingTime() != null) {
                long l = sMSSend.getTimingTime().getTime() - date.getTime();
                if (l < 0) {
                    redisComponent.sentinelSet("HZFT_MESSAGE:" + sMSSend.getId(), "", 5L);
                }
                l = l / 1000;

                redisComponent.sentinelSet("HZFT_MESSAGE:" + sMSSend.getId(), "", l);
            } else {
                redisComponent.sentinelSet("HZFT_MESSAGE:" + sMSSend.getId(), "", 5L);
            }
        } else {
            redisComponent.sentinelSet("HZFT_MESSAGE:" + sMSSend.getId(), "", 5L);
        }


    }


    private int count;


}
