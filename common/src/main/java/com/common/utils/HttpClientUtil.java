package com.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Date;

/**
 * @Description
 * @author Kane
 * @date 2018年8月7日15:40:37
 */
public class HttpClientUtil {

	final static String APPID = "5bd16fe5";//科大讯飞APPID
	final static String APPKEY = "39c1cbd66267f525acdeecb9483a0d0f";//科大讯飞APPKEY
	final static String url = "http://haikangRedis.xfyun.cn/v1/service/v1/iat";//科大讯飞语音听写API接口地址
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d","e", "f" };

	/**
	 *
	 * 功能说明：发送Post请求到科大讯飞语音听写服务，返回解析文字字符串
	 * 修改说明：
	 * @author KaneYang
	 * @date 2018年8月7日 下午3:41:02
	 * @param base64_audio 经过BASE64编码的音频文件
	 * @return
	 * @throws Exception
	 */
	public static String sendPost(String base64_audio) throws Exception {
		CloseableHttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try {
			String xParam = "{\"engine_type\": \"sms16k\", \"aue\": \"raw\"}";
			String param = getBase64(xParam);

			// 建立连接
			httpClient = HttpClients.createDefault();
			httpPost = new HttpPost(url);
			String curTime = String.valueOf(new Date().getTime() / 1000);
			// 设置body
			String body = "audio=" + base64_audio;
			String checkSum = md5Encode(APPKEY + curTime + param);
			StringEntity entity = new StringEntity(body, "utf-8");
			entity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(entity);
			httpPost.setHeader("X-Appid", APPID);
			httpPost.setHeader("X-CurTime", curTime);
			httpPost.setHeader("X-Param", param);
			httpPost.setHeader("X-CheckSum", checkSum);
			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity responseEntity = response.getEntity();
				String resJson = EntityUtils.toString(responseEntity, "utf-8");
				JSONObject jsonObject = JSONObject.parseObject(resJson);
				String code = jsonObject.getString("code");
				if (code.equals("0")) { // 成功
					String dataJson = jsonObject.getString("data");
					System.out.println(dataJson);

					return dataJson;
				} else { // 失败
					System.out.println(code);
					String desc = jsonObject.getString("desc");
					throw new Exception("讯飞语音接口调用失败：" + desc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 *
	 * 功能说明：Base64加密算法
	 * 修改说明：
	 * @author KaneYang
	 * @date 2018年8月7日 下午3:42:22
	 * @param str
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
	 *
	 * 功能说明：MD5加密算法
	 * 修改说明：
	 * @author KaneYang
	 * @date 2018年8月7日 下午3:42:32
	 * @param source
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
			e.printStackTrace();
		}
		return result;
	}

	/**
	 *
	 * 功能说明：Bytes数组 转化成 HexString
	 * 修改说明：
	 * @author KaneYang
	 * @date 2018年8月7日 下午3:42:46
	 * @param bytes
	 * @return
	 */
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
}
