package com.selfservice.service;

import com.common.response.ResponseResult;
import com.selfservice.model.FirstForm;
import com.selfservice.model.FourthForm;
import com.selfservice.model.SecondForm;
import com.selfservice.model.ThirdForm;
import com.selfservice.util.AddInfoToWord;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author: Young
 * @description:
 * @date: Created in 18:56 2018/11/15
 * @modified by:
 */
@Service
public class FormApplicationService {

    public ResponseResult printForm1(FirstForm firstForm,HttpServletRequest request)  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("01", firstForm.getCell11());
        map.put("03", firstForm.getCell12());
        map.put("11", firstForm.getCell21());
        map.put("21", firstForm.getCell31());
        map.put("23", firstForm.getCell32());
        map.put("31", firstForm.getCell41());
        map.put("33", firstForm.getCell42());
        map.put("35", firstForm.getCell43());
        map.put("41", firstForm.getCell51());
        map.put("51", firstForm.getCell61());
        map.put("61", firstForm.getCell71());
        map.put("71", firstForm.getCell81());
        map.put("81", firstForm.getCell91());
        map.put("83", firstForm.getCell92());
        map.put("93", firstForm.getCell101());
        map.put("101", firstForm.getCell111());
        map.put("112", firstForm.getCell121());
        map.put("122", firstForm.getCell131());
        map.put("150", firstForm.getCell151());
        String fileName = firstForm.getName()+String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".pdf";
        String filePath =request.getSession().getServletContext().getRealPath("/");
        String returnPath = "TempFolder" +"/"+fileName;
        filePath += returnPath;
        try {
//            AddInfoToWord.replace(map,"C:\\Users\\dpc\\Desktop\\particalExample.doc","C:\\Users\\dpc\\Desktop\\particalExample"+String.valueOf(System.currentTimeMillis()).substring(4, 13)+".doc",filePath);
//            AddInfoToWord.replace(map,"C:\\openOffice\\OpenOffice\\bak\\particalExample.doc","C:\\openOffice\\OpenOffice\\bak\\particalExample"+String.valueOf(System.currentTimeMillis()).substring(4, 13)+".doc",filePath);
            System.out.println("-------------------------------"+filePath+"------------------------");
            AddInfoToWord.replaceForm(map,"D:\\openOffice\\OpenOffice\\bak\\"+firstForm.getName()+".doc","D:\\openOffice\\OpenOffice\\bak\\"+firstForm.getName()+String.valueOf(System.currentTimeMillis()).substring(4, 13)+".doc",filePath);
//            AddInfoToWord.replace(map,"C:\\Users\\dpc\\Desktop\\particalExample.docx","D:\\openOffice\\OpenOffice\\particalExample"+String.valueOf(System.currentTimeMillis()).substring(4, 13)+".doc",filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.success(returnPath);
    }

    //second
    public ResponseResult printForm2(SecondForm secondForm, HttpServletRequest request)  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("11", secondForm.getCell21());
        map.put("12", secondForm.getCell22());
        map.put("13", secondForm.getCell23());
        map.put("14", secondForm.getCell24());
        map.put("15", secondForm.getCell25());
        map.put("16", secondForm.getCell26());
        map.put("17", secondForm.getCell27());

        map.put("21",secondForm.getCell31());
        map.put("22",secondForm.getCell32());
        map.put("23",secondForm.getCell33());
        map.put("24",secondForm.getCell34());
        map.put("25",secondForm.getCell35());
        map.put("26",secondForm.getCell36());
        map.put("27",secondForm.getCell37());

        map.put("31",secondForm.getCell41());
        map.put("32",secondForm.getCell42());
        map.put("33",secondForm.getCell43());
        map.put("34",secondForm.getCell44());
        map.put("35",secondForm.getCell45());
        map.put("36",secondForm.getCell46());
        map.put("37",secondForm.getCell47());

        map.put("41",secondForm.getCell51());
        map.put("42",secondForm.getCell52());
        map.put("43",secondForm.getCell53());
        map.put("44",secondForm.getCell54());
        map.put("45",secondForm.getCell55());
        map.put("46",secondForm.getCell56());
        map.put("47",secondForm.getCell57());

        map.put("51",secondForm.getCell61());
        map.put("52",secondForm.getCell62());
        map.put("53",secondForm.getCell63());
        map.put("54",secondForm.getCell64());
        map.put("55",secondForm.getCell65());
        map.put("56",secondForm.getCell66());
        map.put("57",secondForm.getCell67());

        map.put("61",secondForm.getCell71());
        map.put("62",secondForm.getCell72());
        map.put("63",secondForm.getCell73());
        map.put("64",secondForm.getCell74());
        map.put("65",secondForm.getCell75());
        map.put("66",secondForm.getCell76());
        map.put("67",secondForm.getCell77());

        map.put("71",secondForm.getCell81());
        map.put("72",secondForm.getCell82());
        map.put("73",secondForm.getCell83());
        map.put("74",secondForm.getCell84());
        map.put("75",secondForm.getCell85());
        map.put("76",secondForm.getCell86());
        map.put("77",secondForm.getCell87());

        map.put("81",secondForm.getCell91());
        map.put("82",secondForm.getCell92());
        map.put("83",secondForm.getCell93());
        map.put("84",secondForm.getCell94());
        map.put("85",secondForm.getCell95());
        map.put("86",secondForm.getCell96());
        map.put("87",secondForm.getCell97());

        String fileName = secondForm.getName()+String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".pdf";
        String filePath =request.getSession().getServletContext().getRealPath("/");
        String returnPath = "TempFolder" +"/"+fileName;
        filePath += returnPath;
        try {
//            AddInfoToWord.replace(map,"C:\\Users\\dpc\\Desktop\\particalExample.doc","C:\\Users\\dpc\\Desktop\\particalExample"+String.valueOf(System.currentTimeMillis()).substring(4, 13)+".doc",filePath);
//            AddInfoToWord.replace(map,"C:\\openOffice\\OpenOffice\\bak\\particalExample.doc","C:\\openOffice\\OpenOffice\\bak\\particalExample"+String.valueOf(System.currentTimeMillis()).substring(4, 13)+".doc",filePath);
            System.out.println("-------------------------------"+filePath+"------------------------");
            AddInfoToWord.replaceForm(map,"D:\\openOffice\\OpenOffice\\bak\\"+secondForm.getName()+".doc","D:\\openOffice\\OpenOffice\\bak\\"+secondForm.getName()+String.valueOf(System.currentTimeMillis()).substring(4, 13)+".doc",filePath);
//            AddInfoToWord.replace(map,"C:\\Users\\dpc\\Desktop\\particalExample.docx","D:\\openOffice\\OpenOffice\\particalExample"+String.valueOf(System.currentTimeMillis()).substring(4, 13)+".doc",filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.success(returnPath);
    }

    // third
    public ResponseResult printForm3(ThirdForm thirdForm, HttpServletRequest request)  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("01", thirdForm.getCell22());
        map.put("03", thirdForm.getCell24());
        map.put("05", thirdForm.getCell26());

        String fileName = thirdForm.getName()+String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".pdf";
        String filePath =request.getSession().getServletContext().getRealPath("/");
        String returnPath = "TempFolder" +"/"+fileName;
        filePath += returnPath;
        try {
            System.out.println("-------------------------------"+filePath+"------------------------");
            AddInfoToWord.replaceForm(map,"D:\\openOffice\\OpenOffice\\bak\\"+thirdForm.getName()+".doc","D:\\openOffice\\OpenOffice\\bak\\"+thirdForm.getName()+String.valueOf(System.currentTimeMillis()).substring(4, 13)+".doc",filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.success(returnPath);
    }

    // fourth
    public ResponseResult printForm4(FourthForm fourthForm, HttpServletRequest request)  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("01", fourthForm.getCell12());
        map.put("03", fourthForm.getCell14());

//        map.put("11",fourthForm.getCell22());
//        map.put("13",fourthForm.getCell24());

        map.put("21", fourthForm.getCell32());
        map.put("31", fourthForm.getCell42());
        map.put("33", fourthForm.getCell44());
        map.put("41", fourthForm.getCell52());
        map.put("61", fourthForm.getCell72());
        map.put("63", fourthForm.getCell74());

        String fileName = fourthForm.getName()+String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".pdf";
        String filePath =request.getSession().getServletContext().getRealPath("/");
        String returnPath = "TempFolder" +"/"+fileName;
        filePath += returnPath;
        try {
            System.out.println("-------------------------------"+filePath+"------------------------");
            AddInfoToWord.replaceForm(map,"D:\\openOffice\\OpenOffice\\bak\\"+fourthForm.getName()+".doc","D:\\openOffice\\OpenOffice\\bak\\"+fourthForm.getName()+String.valueOf(System.currentTimeMillis()).substring(4, 13)+".doc",filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.success(returnPath);
    }

}
