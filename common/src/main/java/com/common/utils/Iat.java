package com.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Iat {
	private static Logger logger = LoggerFactory.getLogger(Iat.class);
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
		"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	/**
	 * Base64加密
	 * @author jlchen4
	 * @date 2017年9月16日 下午3:45:30
	 * @param str	加密字符串
	 * @return
	 */
	public static String getBase64(String str) {
		if (str == null || "".equals(str)) {
			return "";
		}
		try {
			byte[] encodeBase64 = Base64.encodeBase64(str.getBytes("UTF-8"));
			str = new String(encodeBase64);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
}
	/**
	 * md5加密
	 * @author jlchen4
	 * @date 2017年9月16日 下午3:44:46
	 * @param source	加密字符串
	 * @return
	 */
	public static String md5Encode(String source) {
		String result = null;
		try {
			result = source;
			// 获得MD5摘要对象
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			// 使用指定的字节数组更新摘要信息
			messageDigest.update(result.getBytes("utf-8"));
			// messageDigest.digest()获得16位长度
			result = byteArrayToHexString(messageDigest.digest());
		} catch (Exception e) {
			logger.error("Md5 Exception!", e);
		}
		return result;
}
	private static String byteArrayToHexString(byte[] bytes) {
		StringBuilder stringBuilder = new StringBuilder();
		for (byte tem : bytes) {
			stringBuilder.append(byteToHexString(tem));
		}
		return stringBuilder.toString();
}
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
}
	public static String fileToBase64(String path) {
		String base64 = null;
		InputStream in = null;
		try {
			File file = new File(path);
			in = new FileInputStream(file);
			byte[] bytes = new byte[(int) file.length()];
			in.read(bytes);
			base64 = java.util.Base64.getEncoder().encodeToString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return base64;
	}
	public static String aiuiWebApi(String path) throws Exception{

        String appid = "5bd16fe5";//-----------------------------------------------注意这里是科大讯飞注册后提供的appid码
        String appKey = "39c1cbd66267f525acdeecb9483a0d0f";  //-----------------------------------------------注意这里是科大讯飞注册后提供的appKey码
        String curTime = String.valueOf(new Date().getTime()/1000);

        String url = "http://haikangRedis.xfyun.cn/v1/service/v1/iat";

        String xParam = "{\"engine_type\":\"sms8k\",\"aue\":\"raw\"}";
        String param = getBase64(xParam);
        FileInputStream fileInputStream = null;
        final byte[] buffer = new byte[64*1024];
        String body = "audio=" + fileToBase64(path);

        String checkSum = md5Encode(appKey + curTime + param);

        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        StringEntity entity = new StringEntity(body,"utf-8");
        entity.setContentType("application/x-www-form-urlencoded");
        httpPost.setEntity(entity);
        httpPost.setHeader("X-Appid", appid);
        httpPost.setHeader("X-CurTime", curTime);
        httpPost.setHeader("X-Param", param);
        httpPost.setHeader("X-CheckSum", checkSum);

        HttpResponse response = httpClient.execute(httpPost);
        if(response.getStatusLine().getStatusCode() == 200){
            HttpEntity responseEntity = response.getEntity();
            String resJson = EntityUtils.toString(responseEntity,"utf-8");
            JSONObject jsonObject = JSONObject.parseObject(resJson);
            String code = jsonObject.getString("code");
            if(code.equals("0")) { // 成功
                String dataJson = jsonObject.getString("data");
                return dataJson;
            }
            else { // 失败
				System.out.println(code);
                String desc = jsonObject.getString("desc");
                throw new Exception("讯飞语音接口调用失败："+desc);
            }
        }
        return "调用讯飞语音接口失败";
    }

}
