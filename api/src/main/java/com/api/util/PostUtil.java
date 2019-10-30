package com.api.util;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class PostUtil {
    public static Log _logger = LogFactory.getLog(PostUtil.class);
    public static String doPost(String url, Map<String,String> params,String charset,boolean pretty){
        StringBuffer response = new StringBuffer();
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(url);
        post.setRequestHeader(new Header("Content-type","application/x-www-form-urlencoded;charset=gbk"));
        post.setRequestHeader(new Header("User-Agent","Mozilla/5.0/Windows;U;(Windows NT 4.1; de; rv:1.9.1.5) Gecko/20091102 Firefox/3.0"));
        //设置Http Post数据
        if(params != null){
            for(Map.Entry<String,String> entry :params.entrySet()){
                post.setParameter(entry.getKey(),entry.getValue());
            }
        }

        try {
            client.executeMethod(post);
            if(post.getStatusCode() == HttpStatus.SC_OK){
                BufferedReader reader = new BufferedReader(new InputStreamReader(post.getResponseBodyAsStream(),charset));
                String line;
                while((line=reader.readLine()) != null){
                    if(pretty){
                        response.append(line).append(System.getProperty("line.separator"));
                    }else{
                        response.append(line);
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            _logger.error("执行HTTP Post请求" + url +"时，发生异常！",e);
        } finally {
            post.releaseConnection();
        }
        return response.toString();
    }
}
