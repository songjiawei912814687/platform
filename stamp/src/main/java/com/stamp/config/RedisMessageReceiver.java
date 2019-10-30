package com.stamp.config;

import com.stamp.service.SMSSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * leeoohoo@gmail.com
 */
@Component
public class RedisMessageReceiver {

    @Autowired
    private SMSSendService smsSendService;

    /**
     * 接收redis消息，并处理
     *
     * @param message 过期的redis key
     */
    public void message(String message) {
//        System.out.println("通知的key是：" + message);
        if(message.indexOf("HZFT_MESSAGE") > 0){
            var strs = message.split(":");
            smsSendService.send(Integer.parseInt(strs[1]));
        }
    }
}
