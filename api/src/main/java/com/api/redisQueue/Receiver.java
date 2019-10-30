package com.api.redisQueue;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.model.StationPeople;
import com.api.service.EvaluationResultService;
import com.api.service.QueWindowService;
import com.api.service.StationPeopleService;
import com.common.Enum.QueRedisMessageTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Receiver {
    @Autowired
    private CountDownLatch latch;
    @Autowired
    private StationPeopleService stationPeopleService;
    @Autowired
    private EvaluationResultService evaluationResultService;

    private static final Logger log = LoggerFactory.getLogger(Receiver.class);

    @Autowired
    public Receiver(CountDownLatch latch) {
        this.latch = latch;
    }

    public void message(String message) throws ParseException {
//        System.out.println("Received <" + message + ">");
        Map maps = (Map) JSONObject.parse(message);
        Integer code = Integer.parseInt(maps.get("code").toString());
        System.out.println("-------------------code------------"+code);
//        /**1登陆*/
//        if(QueRedisMessageTypeEnum.USER_LOGIN.getCode().equals(code)){
//            try {
//                queWindowService.login((Map<String, Object>) maps.get("attachObject"),(Map<String, Object>) maps.get("attachObjectEx"));
//            }catch (Exception e){
//                log.error("登陆出现错误:{}",e.getMessage());
//            }
//        }
//
//        /**2注销*/
//        if(QueRedisMessageTypeEnum.USER_LOGOUT.getCode().equals(code)){
//            try {
//                queWindowService.logOut((Map<String, Object>) maps.get("attachObject"),(Map<String, Object>) maps.get("attachObjectEx"));
//            }catch (Exception e ){
//                log.error("注销出现错误:{}",e.getMessage());
//            }
//        }
        /**4等候着入队*/
        if(code.equals(QueRedisMessageTypeEnum.WAIT_QUEUE.getCode())){
            try {
                stationPeopleService.addRedisMessage((Map<String, Object>) maps.get("attachObjectEx"),(Map<String, Object>)maps.get("attachObject") ,null);
                //推送数据到市政务厅
//                stationPeopleService.addNsfw((Map<String, Object>) maps.get("attachObjectEx"),(Map<String, Object>) maps.get("attachObject"),"nsfw.saveticket");

            }catch (Exception e){
                log.error("取号出现错误:{}",e.getMessage());
            }
        }


        /**32开始办理业务同等候着入队4.1模型*/
        if(QueRedisMessageTypeEnum.START_BUSINESS.getCode().equals(code)){
            try {
                stationPeopleService.startWork((Map<String, Object>) maps.get("attachObjectEx"),(Map<String, Object>) maps.get("attachObject"));
            }catch (Exception e ){
                log.error("开始办理业务出现错误:{}",e.getMessage());
            }
        }

        /**呼叫等待着*/
        if(QueRedisMessageTypeEnum.CALL_WAIT.getCode().equals(code)){
            stationPeopleService.callAndWait((Map<String, Object>) maps.get("attachObjectEx"),(Map<String, Object>) maps.get("attachObject"));
            //推送数据到市政务厅
            stationPeopleService.addNsfw((Map<String, Object>) maps.get("attachObjectEx"),(Map<String, Object>) maps.get("attachObject"),"nsfw.saveticket","0");
        }

        /**64完成办理业务**/
        if(QueRedisMessageTypeEnum.COMPLETE_BUSINESS.getCode().equals(code)){
            stationPeopleService.complete((Map<String, Object>) maps.get("attachObjectEx"),(Map<String, Object>) maps.get("attachObject"));

        }

        /**128提交评价结果**/
        if(QueRedisMessageTypeEnum.SUBMIT_COMMENTS.getCode().equals(code)){

            JSONArray jsonArray = (JSONArray) maps.get("attachObject");
            Map<String,Object> evaluationResultMap = (Map<String,Object>)jsonArray.get(0);
            evaluationResultService.addEvaluationResult(evaluationResultMap);
            //推送数据到市政务厅
            stationPeopleService.addNsfw((Map<String, Object>) maps.get("attachObjectEx"),(Map<String, Object>) maps.get("attachObject"),"nsfw.saveticket","3");
        }

//        /**256暂停服务*/
//        if(QueRedisMessageTypeEnum.PAUSE_SERVICE.getCode().equals(code)){
//            try{
//                queWindowService.pauseService((Map<String, Object>) maps.get("attachObject"));
//            }catch (Exception e ){
//                log.error("暂停服务出现错误:{}",e.getMessage());
//            }
//        }
//        /**8192取消暂停*/
//        if(QueRedisMessageTypeEnum.CANCEL_PAUSE_SERVICE.getCode().equals(code)){
//            try{
//                queWindowService.cancelPause((Map<String, Object>) maps.get("attachObject"));
//            }catch (Exception e ){
//                log.error("取消暂停出现错误:{}",e.getMessage());
//            }
//        }


        /**8388608过号*/
        if(QueRedisMessageTypeEnum.PASS_NUMBER.getCode().equals(code)){
            try{
                stationPeopleService.passNumber((Map<String,Object>) maps.get("attachObjectEx"));
            }catch (Exception e ){
                log.error("过号出现错误:{}",e.getMessage());
            }
        }

        /**33554432取消排队*/
        if(QueRedisMessageTypeEnum.CANCEL_QUEUE.getCode().equals(code)){
            try{
                stationPeopleService.updateStatusCodeByResourceID((Map<String, Object>) maps.get("attachObject"));
            }catch (Exception e ){
                log.error("过号出现错误:{}",e.getMessage());
            }
        }
        latch.countDown();
    }
}
