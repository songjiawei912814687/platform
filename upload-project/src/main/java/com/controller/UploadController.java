package com.controller;

import com.domain.ChangeToPdf;
import com.domain.FileOutput;
import com.response.ResponseResult;
import com.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Young
 * @description:
 * @date: Created in 17:07 2018/9/7
 * @modified by:
 */
@RestController
@Slf4j
public class UploadController {

    @Autowired
    private FileUploadService fileUploadService;


    @PostMapping("/upload")
    public ResponseResult upload(@RequestParam("file") MultipartFile file,
                                 @RequestParam("resourceType") Integer resourceType) throws IOException {
        ResponseResult result = new ResponseResult();
        String rtMge = "";
        List<FileOutput> returnList = new ArrayList<>();


            FileOutput fileOutput = fileUploadService.upload(file,resourceType);
            if(fileOutput.isSuccess()) {
                returnList.add(fileOutput);
                result.setMessage(rtMge);
                result.setSuccess(true);
                result.setData(returnList);
                return result;
            }
            returnList.add(fileOutput);
            result.setCode(1);
            result.setData(returnList);
            return result;

    }

    @PostMapping("/changeToPdf")
    public ResponseResult upload(@RequestBody ChangeToPdf changeToPdf) throws IOException {
       return fileUploadService.changeToPdf(changeToPdf);
    }


    //文件压缩接口
    /**
     * @param srcFilePath 压缩文件源原件路径
     * @param destFilePath 压缩后存放路径
     * @return
     */
    @PostMapping("/zip")
    public ResponseResult zipFile(String srcFilePath,String destFilePath) throws IOException {
        ResponseResult result = new ResponseResult();
        String rtMge = "";
        FileOutput fileOutput = new FileOutput();
        try{
             fileOutput =  fileUploadService.fileToZip(srcFilePath, destFilePath);

        }catch (Exception e){
            rtMge = "文件压缩异常";
        }

        result.setSuccess(true);
        result.setMessage(rtMge);
        result.setData(fileOutput);

        return result;
    }

    @PostMapping("/deleteFile")
    public ResponseResult deleteFile(String destFilePath)  {
        ResponseResult result = new ResponseResult();
        if(destFilePath==null||"".equals(destFilePath)||"null".equals(destFilePath)){
            result.setSuccess(true);
            return result;
        }
        try{
            fileUploadService.deleteFile(destFilePath);
        }catch (Exception e){
            return ResponseResult.error("删除失败");
        }
        result.setSuccess(true);
        return result;
    }

    //文件下载相关接口
    @RequestMapping("/download")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response, String fileName) {

        if (fileName != null) {
            //当前是从该工程的WEB-INF//File//下获取文件(该目录可以在下面一行代码配置)然后下载到C:\\users\\downloads即本机的默认下载的目录
            String realPath = request.getServletContext().getRealPath(
                    "C:\\Users\\Administrator\\Desktop\\nginx");
            File file = new File(realPath, fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition",
                        "attachment;fileName=" + fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }


}

