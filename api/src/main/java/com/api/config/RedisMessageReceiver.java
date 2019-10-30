package com.api.config;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;


/**
 * leeoohoo@gmail.com
 */

public class RedisMessageReceiver {

    @Autowired
    private CountDownLatch latch;

    @Autowired
    public RedisMessageReceiver(CountDownLatch latch) {
        this.latch = latch;
    }



    /**
     * 接收redis消息，并处理
     */
    public void feedBack(String receiveMessage)  {
        System.out.println("通知的key是：" + receiveMessage);
    }
}
