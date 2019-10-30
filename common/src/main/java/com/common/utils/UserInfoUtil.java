package com.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserInfoUtil {


    private Logger logger = LoggerFactory.getLogger(getClass());

    // 1.获取code的请求地址
    public static String Get_Code = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=STAT#wechat_redirect";
    // 替换字符串
    public static String getCode(String APPID, String REDIRECT_URI, String SCOPE) {
        return String.format(Get_Code,APPID,REDIRECT_URI,SCOPE);
    }

    // 2.获取 access_toke 的请求地址
    public static String Web_access_tokenhttps = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s&code=%s&grant_type=authorization_code";

    // 替换字符串
    public static String getWebAccess(String corpid, String corpsecret, String CODE) {
        return String.format(Web_access_tokenhttps, corpid, corpsecret,CODE);
    }

    // 2.获取 access_toke 的请求地址
    public static String Web_access_token = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";

    // 替换字符串
    public static String getWebAccess(String corpid, String corpsecret) {
        return String.format(Web_access_tokenhttps, corpid, corpsecret);
    }

    // 3.拉取用户信息的请求地址（企业微信利用user/getuserinfo，非企业微信利用api.开头的api）
    public static String User_Message = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=%s&code=%s";
//    public static String User_Message = "https://58.251.80.106/cgi-bin/user/getuserinfo?access_token=%s&code=%s";
    // 替换字符串
    public static String getUserMessage(String access_token, String code) {
        return String.format(User_Message, access_token,code);
    }



    public static void main(String[] args) {
        String REDIRECT_URI = "http://wechat.tmqyt.com/url";
        String SCOPE = "snsapi_login"; // snsapi_userinfo // snsapi_login

        //appId
        String appId = "wx365228888853891d";

        String getCodeUrl = getCode(appId, REDIRECT_URI, SCOPE);
        System.out.println("getCodeUrl:"+getCodeUrl);
    }

}
