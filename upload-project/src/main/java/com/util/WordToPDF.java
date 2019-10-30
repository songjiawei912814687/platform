package com.util;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import com.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

public class WordToPDF {

    private static final int wdFormatPDF = 17;// PDF 格式

    public static void WordToPDF(String startFile, String overFile) throws IOException {
        // 源文件目录
        File inputFile = new File(startFile);
        if (!inputFile.exists()) {
            System.out.println("源文件不存在！");
            return;
        }
        // 输出文件目录
        File outputFile = new File(overFile);
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().exists();
        }

        // 调用openoffice服务线程
        /** 我把openOffice下载到了 C:/Program Files (x86)/下  ,下面的写法自己修改编辑就可以**/
//        String command = "C:/openOffice/OpenOffice/program/soffice.exe -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\"";
        String command = "D:/openOffice/OpenOffice/program/soffice.exe -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\"";
        System.out.println("----------------"+command+"------------------------");
        Process p = Runtime.getRuntime().exec(command);

        // 连接openoffice服务
        OpenOfficeConnection connection = new SocketOpenOfficeConnection("127.0.0.1", 8100);
        connection.connect();

        // 转换
        DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
//        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        converter.convert(inputFile, outputFile);

        // 关闭连接
        connection.disconnect();

        // 关闭进程
        p.destroy();
    }

}
