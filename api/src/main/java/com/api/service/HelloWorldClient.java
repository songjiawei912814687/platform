package com.api.service;


import com.api.largerScreen.Service1Locator;
import com.api.largerScreen.Service1Soap_PortType;

/**
 * @author: $young
 * @PROJECT_NAME: apiwebservice
 * @description:
 * @date: Created in 2018-11-30  14:23
 * @modified by:
 */
public class HelloWorldClient {
  public static void main(String[] argv) {
      try {
          String token = "hzjyjs287b07ab4b1b4172adeab32f62031b49";
          Service1Locator locator = new Service1Locator();
          Service1Soap_PortType service = locator.getService1Soap();
          // If authorization is required
          //((Service1Soap12Stub)main.java.com.apiwebservice.service).setUsername("user3");
          //((Service1Soap12Stub)main.java.com.apiwebservice.service).setPassword("pass3");
          // invoke business method
          String str1 = service.getProjectAfficheList(null,token);
          String str2 = service.getProjectAfficheWinBidList(null,token);
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
