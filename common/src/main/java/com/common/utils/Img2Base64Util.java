package com.common.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-06  18:16
 * @modified by:
 */
public class Img2Base64Util {

    /***
     *
     * 将图片转换为Base64<br>
     * 将base64编码字符串解码成img图片
     * @param imgFile
     * @return
     */
    public static String getImgStr(String imgFile){
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            // 创建URL
            URL url = new URL(imgFile);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            // 关闭流
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        return Base64.encodeBase64String(data.toByteArray());
    }

    public static void main(String[] args) {
        String str = getImgStr("http://10.32.250.84:8089/uploadFile/-/2/1559030163875_人社—张碧芸.png");
        System.out.println(str);
    }
}
