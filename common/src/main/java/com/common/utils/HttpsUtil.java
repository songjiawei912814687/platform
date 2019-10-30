//package com.common.utils;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSocketFactory;
//import javax.net.ssl.TrustManager;
//import java.io.*;
//import java.net.URL;
//
//public class HttpsUtil {
//    private final Logger logger = LoggerFactory.getLogger(HttpsUtil.class);
//
//    /**
//     * 以https方式发送请求并将请求响应内容以String方式返回
//     *
//     * @param path   请求路径
//     * @param method 请求方法
//     * @param body   请求数据体
//     * @return 请求响应内容转换成字符串信息
//     */
//    public  synchronized static String httpsRequestToString(String path, String method, String body) {
//        if (path == null || method == null) {
//            return null;
//        }
//
//        String response = null;
//        InputStream inputStream = null;
//        InputStreamReader inputStreamReader = null;
//        BufferedReader bufferedReader = null;
//        HttpsURLConnection conn = null;
//        OutputStream outputStream = null;
//
//        try {
//            // 创建SSLConrext对象，并使用指定的信任管理器初始化
//            TrustManager[] tm = {new JEEWeiXinX509TrustManager()};
//            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
//            sslContext.init(null, tm, new java.security.SecureRandom());
//
//            // 从上述对象中的到SSLSocketFactory
//            SSLSocketFactory ssf = sslContext.getSocketFactory();
//
//
//            URL url = new URL(path);
//            conn = (HttpsURLConnection) url.openConnection();
//            conn.setSSLSocketFactory(ssf);
//
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setUseCaches(false);
//            conn.setReadTimeout(10000);
//            conn.setConnectTimeout(10000);
//
//            //设置请求方式（git|post）
//            conn.setRequestMethod(method);
//
//
//            conn.connect();
//            //有数据提交时
//            if (null != body) {
//                outputStream = conn.getOutputStream();
//                outputStream.write(body.getBytes("UTF-8"));
//                outputStream.flush();
//            }
//
//
//            // 将返回的输入流转换成字符串
//            System.out.println("conn:"+conn);
//
//
//            inputStream = conn.getInputStream();
//
//
//
//
//            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
//
//
//
//
//            bufferedReader = new BufferedReader(inputStreamReader);
//
//
//            System.out.println("bufferedReader:"+bufferedReader);
//            String str = null;
//            StringBuffer buffer = new StringBuffer();
//            System.out.println(buffer);
//            while ((str = bufferedReader.readLine()) != null) {
//                System.out.println(str);
//                buffer.append(str);
//            }
//
//            response = buffer.toString();
//        } catch (Exception e) {
//            System.out.println("我错在这里"+e.toString());
//
//        } finally {
//            if (conn != null) {
//                conn.disconnect();
//
//            }
//            try {
//
//                if(outputStream!=null){
//                    outputStream.close();
//                }
//                if(bufferedReader!=null){
//                    bufferedReader.close();
//
//                }
//                if(inputStreamReader!=null){
//                    inputStreamReader.close();
//
//                }
//                if(inputStream!=null){
//                    inputStream.close();
//
//                }
//            } catch (IOException execption) {
//
//            }
//        }
//        return response;
//    }
//
//
//}
