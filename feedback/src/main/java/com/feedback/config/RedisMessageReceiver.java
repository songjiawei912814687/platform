package com.feedback.config;

import com.feedback.service.FeedbackInfoService;
import com.feedback.service.SuggesstionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;


/**
 * leeoohoo@gmail.com
 */
@Component
public class RedisMessageReceiver {

    @Autowired
    private SuggesstionsService SuggesstionsService;
    @Autowired
    private FeedbackInfoService feedbackInfoService;

    /**
     * 接收redis消息，并处理
     *
     * @param message 过期的redis key
     */
    public void message(String message)  {
        //添加逾期未处理
//        if(message.indexOf("HZFT_FEEDBACK")>0){
//            var strs = message.split(":");
//            SuggesstionsService.judgeOutOfDateState(Integer.parseInt(strs[1]));
//        }
//        //发送一次回访信息
//        if(message.indexOf("FEED_BACK_INFO")>0){
//            var strs = message.split(":");
//            try {
//                feedbackInfoService.findFeedbackInfoInTwoHours(Integer.parseInt(strs[1]));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        //发送二次回访信息
//        if(message.indexOf("FEED_INFO")>0){
//            var strs = message.split(":");
//            try {
//                feedbackInfoService.sloveReturnVist(Integer.parseInt(strs[1]));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//        //评价器30分钟或者1秒后key过期后发送评价短信
//        if(message.contains("EVL_FEED_BACK")){
//            var strs = message.split(":");
//            feedbackInfoService.reciveMessage(Integer.parseInt(strs[1]));
//        }
    }
}
