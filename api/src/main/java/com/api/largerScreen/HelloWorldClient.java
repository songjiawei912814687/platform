package com.api.largerScreen;

import com.common.utils.DealDateFormatUtil;
import com.common.utils.JsonUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

/**
 * @author: $young
 * @PROJECT_NAME: apiwebservice
 * @description: 
 * @date: Created in 2018-11-30  14:23
 * @modified by:
 */
public class HelloWorldClient {
  public static void main(String[] argv) throws ParseException {
      try {
          String token = "hzjyjs287b07ab4b1b4172adeab32f62031b49";
          Service1Locator locator = new Service1Locator();
          Service1Soap_PortType service = locator.getService1Soap();
          String str1 = service.getProjectAfficheList(null,token);
          try {
              str1 = JsonUtils.xml2Json(str1).toString();
              Map<String,Object> jsonObject=com.alibaba.fastjson.JSONObject.parseObject(str1);
              Map<String,Object> str = (Map<String, Object>) jsonObject.get("NewDataSet");
              Map<String,Object> table1= (Map<String, Object>) str.get("table1");
              if(table1 == null){
                  return;
              }
          } catch (IOException e) {
              e.printStackTrace();
          }


          String str2 = service.getProjectAfficheWinBidList(null,token);
          try {
              str2 = JsonUtils.xml2Json(str2).toString();
              Map<String,Object> jsonObject=com.alibaba.fastjson.JSONObject.parseObject(str2);
              Map<String,Object> str = (Map<String, Object>) jsonObject.get("NewDataSet");
              Map<String,Object> table1= (Map<String, Object>) str.get("table1");
              String TenderName =(String) table1.get("TenderName");

              String PublishStartTime = (String) table1.get("PublishStartTime");
              String PublishEndTime = (String) table1.get("PublishEndTime");

              String startTime = DealDateFormatUtil.dealDate(PublishEndTime);
              String endTime = DealDateFormatUtil.dealDate(PublishStartTime);

              String EnterpriseName = (String) table1.get("EnterpriseName");
              String ID = (String) table1.get("ID");
              BigDecimal WinPrice = (BigDecimal) table1.get("WinPrice");
          } catch (IOException e) {
              e.printStackTrace();
          }
          String str3 = service.getSmallProjectAfficheList(null,token);
          String str4 = service.getSmallProjectAfficheWinBidList(null,token);
          String str5 = service.getExposureNews(null,token);
          String str6 = service.getPublishInfo(null,token);
          System.out.println("获取建设工程招标公告"+str1);
          System.out.println("获取建设工程中标公告"+str2);
          System.out.println("获取小额工程交易公告"+str3);
          System.out.println("获取小额工程成交结果"+str4);
          System.out.println("获取曝光台"+str5);
          System.out.println("获取公示栏接口设计"+str6);
      } catch (javax.xml.rpc.ServiceException ex) {
          ex.printStackTrace();
      } catch (java.rmi.RemoteException ex) {
          ex.printStackTrace();
      }
  }
}
