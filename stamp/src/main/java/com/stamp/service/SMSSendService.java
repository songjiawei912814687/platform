package com.stamp.service;

import com.common.response.ResponseResult;
import com.common.utils.HolidayUtils;
import com.common.utils.HttpRequestUtil;
import com.google.common.collect.Lists;
import com.stamp.config.RedisComponent;
import com.stamp.domain.input.SMSSendInput;
import com.stamp.mapper.jpa.SMSSendRepository;
import com.stamp.model.SMSSend;
import com.stamp.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;



@Service
@Slf4j
public class SMSSendService {

    @Autowired
    private SMSSendRepository repository;
    @Autowired
    private RedisComponent redisComponent;

    @Value("${sendMessageUrl}")
    private String sendMessageUrl;

    public Users getUsers() {
        Users users = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return users;
    }

    public Integer addMessages(SMSSendInput smsSendInput) throws Exception {
        List<String> sendList = smsSendInput.getSendList();

        boolean flag = this.verificationMessage(sendList);
        if (!flag) {
            return 0;
        }
        Date date = null;
        List<SMSSend> list = this.removeRepeat(sendList);

        List<SMSSend> smsSends = Lists.newArrayList();
        if (sendList != null && sendList.size() > 0) {
            for (SMSSend sendInfo : list) {
                sendInfo.setIsTiming(0);
                sendInfo.setReplyLimitDate(date == null ? null : date);
                sendInfo.setIsReply(smsSendInput.getIsReply());
                sendInfo.setAmputated(0);
                sendInfo.setType(99);//手动下发
                sendInfo.setState(0);
                sendInfo.setDescription(smsSendInput.getDescription());
                smsSends.add(sendInfo);
            }
            List<SMSSend> smsSendList = repository.saveAll(smsSends);
            this.setValueToRedis(smsSendList);
            if (smsSendList != null && smsSendList.size() > 0) {
                return 1;
            } else {
                return 0;
            }

        }
        return 0;
    }





    private Date getNextDay(Date date, List<String> holidayList, List<String> workDayList) throws Exception {
        if (!HolidayUtils.isWorkDay(date, holidayList, workDayList)) {
            Date tomorrow = HolidayUtils.getTomorrow(date);
            getNextDay(tomorrow, holidayList, workDayList);
        }
        return date;
    }


    private void setValueToRedis(List<SMSSend> smsSends) {
        for (SMSSend sMSSend : smsSends) {
            redisComponent.sentinelSet("HZFT_MESSAGE:" + sMSSend.getId(), "", 5L);
        }

    }

    private void setValueToRedis(SMSSend sMSSend) {
        redisComponent.sentinelSet("HZFT_MESSAGE:" + sMSSend.getId(), "", 5L);

    }

    private boolean verificationMessage(List<String> sendList) {
        for (String str : sendList) {
            if (str == null || "".equals(str) || str.split("/").length != 2) {
                return false;
            }
        }
        return true;
    }

    private List<SMSSend> removeRepeat(List<String> sendList) {
        HashSet<String> sets = new HashSet<>();
        ArrayList<SMSSend> objects = new ArrayList<>();
        for (String str : sendList) {
            if (sets.contains(str)) {
                continue;
            } else {
                SMSSend smsSend = new SMSSend();
                String[] split = str.split("/");
                smsSend.setReceiveEmployeeName(split[1]);
                smsSend.setReceiveTelephone(split[0]);
                objects.add(smsSend);
                sets.add(split[0]);
            }
        }
        return objects;
    }


    private int count;

    public void send(Integer messageId) {
        count++;
        var smsSend = repository.getById(messageId);
        if(smsSend == null){
            return;
        }
        Map<String, Object> map = new HashMap<>();
        String[] strs = new String[]{smsSend.getReceiveTelephone()};
        map.put("mobiles", strs);
        map.put("smsContent", smsSend.getDescription());
        ResponseResult responseResult = HttpRequestUtil.sendPostRequest(sendMessageUrl, map);
        if (responseResult.getCode() == 200) {
            log.info("号码"+Arrays.toString(strs)+"发送成功");
            smsSend.setState(1);
            smsSend.setTimingTime(new Date());
            repository.save(smsSend);
        } else {
            log.info("号码"+strs+"发送失败");
            smsSend.setState(responseResult.getCode());
            smsSend.setTimingTime(new Date());
            repository.save(smsSend);
        }
        System.out.println(count);
    }
}
