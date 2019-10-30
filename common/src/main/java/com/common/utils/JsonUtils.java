package com.common.utils;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;

import org.dom4j.*;
import org.json.JSONException;


import java.io.IOException;
import java.util.*;

public class JsonUtils {

    public static Map<String, String> getMap(Object o, String... keys) {
        Map<String, String> map = new HashMap<>();
        var jStr = JSONUtils.toJSONString(o);
        var jsonObject = JSONObject.parseObject(jStr);
        for (var str : keys) {
            map.put(str, jsonObject.getString(str));
        }
        return map;
    }

    public static org.json.JSONObject xml2Json(String xml) throws IOException, JSONException {
        org.json.JSONObject xmlJson = org.json.XML.toJSONObject(xml);
        return xmlJson;
    }


    public static void main(String[] args) throws IOException, JSONException, DocumentException {
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>" +
                "<MoBaoAccount MessageType=\"UserMobilePay\" PlatformID=\"b2ctest\">" +
                "<OrderNo>M20150521084825</OrderNo><TradeAmt>5000.00</TradeAmt>" +
                "<Commission>0.5</Commission><UserID>zhuxiaolong</UserID>" +
                "<MerchID>zhuxiaolong1</MerchID>" +
                "<tradeType>0</tradeType>" +
                "<CustParam>123</CustParam>" +
                " <" +
                "NotifyUrl>http://mobaopay.com/callback.do</NotifyUrl><TradeSummary>订单</TradeSummary>" +
                "</MoBaoAccount>";
        String xml1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><DATAAREA><CHANGE_FLAG>1</CHANGE_FLAG><MATERIALS><MATERIAL><MATERIALGUID>a818291f-59a6-466b-adc5-74c4fe8dcc42</MATERIALGUID><NAME>《非公司企业法人登记(备案）申请书》。（原件1份）</NAME><FORMAT>4</FORMAT><DETAIL_REQUIREMENT>A4纸</DETAIL_REQUIREMENT><NECESSITY>1</NECESSITY><NECESSITY_DESC></NECESSITY_DESC><MATERIAL_RES>申请人提交</MATERIAL_RES><IS_RQ>66</IS_RQ><EMPTYTABLE><FILENAME>66817c71-7c28-d4b9-1568-1d85965f0506.doc</FILENAME><FILECONTENT /><FILEURL>http://zjqlk.oss-cn-hangzhou.aliyuncs.com/e/4/46d11b9c-81a3-4005-a773-524ebb14acb4.doc</FILEURL></EMPTYTABLE><EXAMPLETABLE><FILENAME>774a4397-e2aa-129c-9ef3-4cab7621a565.doc</FILENAME><FILECONTENT /><FILEURL>http://zjqlk.oss-cn-hangzhou.aliyuncs.com/5/f/f36db726-7ba5-47b9-9e5d-b8d7fdafd14b.doc</FILEURL></EXAMPLETABLE><BAK></BAK></MATERIAL><MATERIAL><MATERIALGUID>349e97a2-7fec-42dc-9729-61275d087f08</MATERIALGUID><NAME>《指定代表或者共同委托代理人授权委托书》（原件1份），及指定代表或委托代理人的身份证件。（复印件1份）</NAME><FORMAT>4</FORMAT><DETAIL_REQUIREMENT>A4纸</DETAIL_REQUIREMENT><NECESSITY>1</NECESSITY><NECESSITY_DESC></NECESSITY_DESC><MATERIAL_RES>申请人提交或者经办单位自行获取</MATERIAL_RES><IS_RQ>66</IS_RQ><EMPTYTABLE><FILENAME>9a16c78e-6eb9-4683-7224-5ef66570091a.doc</FILENAME><FILECONTENT /><FILEURL>http://zjqlk.oss-cn-hangzhou.aliyuncs.com/c/9/93aa78e0-fb8e-4c8e-bd22-6b4b2fc7c089.doc</FILEURL></EMPTYTABLE><EXAMPLETABLE><FILENAME>a47cb24b-485b-de53-5be8-db02f3de09bd.doc</FILENAME><FILECONTENT /><FILEURL>http://zjqlk.oss-cn-hangzhou.aliyuncs.com/0/b/b78eff6a-e4c9-4c65-b020-52df6b35406e.doc</FILEURL></EXAMPLETABLE><BAK></BAK></MATERIAL><MATERIAL><MATERIALGUID>c1982e98-eab6-4ccc-8d83-e12796898063</MATERIALGUID><NAME>法律、行政法规规定设立企业必须报经批准的，提交有关的批准文件或者许可证件复印件；（详见《工商登记前置审批事项目录》及其《指导目录》）”。（原件或者复印件1份）</NAME><FORMAT>3</FORMAT><DETAIL_REQUIREMENT>A4纸</DETAIL_REQUIREMENT><NECESSITY>2</NECESSITY><NECESSITY_DESC>非法律、行政法规规定设立企业必须报经批准的非提交</NECESSITY_DESC><MATERIAL_RES>申请人提交或者经办单位自行获取</MATERIAL_RES><IS_RQ>66</IS_RQ><EMPTYTABLE><FILENAME></FILENAME><FILECONTENT></FILECONTENT><FILEURL></FILEURL></EMPTYTABLE><EXAMPLETABLE><FILENAME></FILENAME><FILECONTENT></FILECONTENT><FILEURL></FILEURL></EXAMPLETABLE><BAK></BAK></MATERIAL><MATERIAL><MATERIALGUID>bb9e2209-bbe2-474b-ada7-93f1792af970</MATERIALGUID><NAME>变更事项相关证明文件；</NAME><FORMAT>3</FORMAT><DETAIL_REQUIREMENT>A4纸,用电脑打印</DETAIL_REQUIREMENT><NECESSITY>1</NECESSITY><NECESSITY_DESC></NECESSITY_DESC><MATERIAL_RES>申请人提交或者经办单位自行获取</MATERIAL_RES><IS_RQ>66</IS_RQ><EMPTYTABLE><FILENAME></FILENAME><FILECONTENT></FILECONTENT><FILEURL></FILEURL></EMPTYTABLE><EXAMPLETABLE><FILENAME></FILENAME><FILECONTENT></FILECONTENT><FILEURL></FILEURL></EXAMPLETABLE><BAK>（1）变更名称的，提供名称登记机关出具的《名称变更登记通知书》。（原件1份）（已领取通知书的提交）\n" +
                "（2）变更住所（经营场所）的，提交变更后住所（经营场所）的使用证明。（原件或者复印件1份）\n" +
                "（3）变更法定代表人的，提交原任法定代表人的免职证明、新任法定代表人的任职证明（原件1份）及其身份证件复印件。（复印件一份）\n" +
                "（4）变更经济性质的，提交变更批准文件或相关证明文件，（原件或者复印件1份）\n" +
                "企业法人因资产权属转移而导致经济性质变化的，应当同时申请主管部门（出资人）变动备案，按非公司企业法人主管部门（出资人）变动备案提交材材料规范提交材料。\n" +
                "（5）变更经营范围的，企业申请的经营范围中含有法律、行政法规和国务院决定规定必须在登记前报经批准的项目，应当提交有关的批准文件或者许可证件复印件。（详见《工商登记前置审批事项目录》及其《指导目录》）。（原件或者复印件1份） \n" +
                "（6）变更注册资金的，主管部门（出资人）为国有企业或者事业法人的，提交国有资产管理部门出具的国有资产产权登记证明；主管部门（出资人）为集体所有制企业或者社团组织、民办非企业单位的，提交依法设立的验资机构出具的验资证明；主管部门（出资人）为工会的，由上一级工会出具证明。（原件或者复印件1份）\n" +
                "（7）变更经营期限的，提交主管部门（出资人）出具的变更企业法人营业期限的文件；修改后的企业章程或者企业章程修正案（主管部门（出资人）加盖公章）。（原件1份）\n" +
                "（8）以上各项涉及登记事项变更的，应当同时申请变更登记，按相应的提交材料规范提交相应的材料。</BAK></MATERIAL><MATERIAL><MATERIALGUID>1de2bf78-c683-4951-bdfe-5345bc055330</MATERIALGUID><NAME>企业法人营业执照副本。（原件1份）</NAME><FORMAT>1</FORMAT><DETAIL_REQUIREMENT></DETAIL_REQUIREMENT><NECESSITY>1</NECESSITY><NECESSITY_DESC></NECESSITY_DESC><MATERIAL_RES>申请人提交</MATERIAL_RES><IS_RQ>66</IS_RQ><EMPTYTABLE><FILENAME></FILENAME><FILECONTENT></FILECONTENT><FILEURL></FILEURL></EMPTYTABLE><EXAMPLETABLE><FILENAME></FILENAME><FILECONTENT></FILECONTENT><FILEURL></FILEURL></EXAMPLETABLE><BAK></BAK></MATERIAL></MATERIALS></DATAAREA>";
        String xml2 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><DATAAREA><CHANGE_FLAG>" +
                "</CHANGE_FLAG><ACCEPT_ADDRESSS><ACCEPT_ADDRESS><ADDRESS_KIND>2;</ADDRESS_KIND>" +
                "<ADDRESS>杭州市富阳地税局直属税务分局办税服务厅（杭州市富阳区富春街道桂花西路82-2号）</ADDRESS><ACCEPT_TIMEDESC>8:30-11:30  14:00-17:30</ACCEPT_TIMEDESC><PHONE></PHONE><UUID>6e278333-6996-4bd3-a485-dc3ca29d6f70</UUID></ACCEPT_ADDRESS><ACCEPT_ADDRESS><ADDRESS_KIND>2;</ADDRESS_KIND><ADDRESS>杭州市富阳地税局大源税务分局办税服务厅（杭州市富阳区大源镇经纬路1号）</ADDRESS><ACCEPT_TIMEDESC>8:00-11:30  13:30-16:30</ACCEPT_TIMEDESC><PHONE></PHONE><UUID>a2dc013c-26c5-42a4-8028-47b444218cd3</UUID></ACCEPT_ADDRESS><ACCEPT_ADDRESS><ADDRESS_KIND>2;</ADDRESS_KIND><ADDRESS>杭州市富阳地税局新登税务分局办税服务厅（杭州市富阳区新登镇新兴西路1号）</ADDRESS><ACCEPT_TIMEDESC>8:00-11:30  13:30-16:30</ACCEPT_TIMEDESC><PHONE></PHONE><UUID>f3384540-069f-4c1d-8fd2-49acd94fc6cf</UUID></ACCEPT_ADDRESS><ACCEPT_ADDRESS><ADDRESS_KIND>2;</ADDRESS_KIND><ADDRESS>杭州市富阳地税局场口税务分局办税服务厅（杭州市富阳区场口镇百丈畈1号）</ADDRESS><ACCEPT_TIMEDESC>8:00-11:30  13:00-16:10（冬令时）8:00-11:30  13:00-16:30（夏令时）</ACCEPT_TIMEDESC><PHONE></PHONE><UUID>ef8ba2d1-0056-4339-9db9-d40801590a92</UUID></ACCEPT_ADDRESS><ACCEPT_ADDRESS><ADDRESS_KIND>2;</ADDRESS_KIND><ADDRESS>杭州市富阳地税局龙羊税务分局办税服务厅（杭州市富阳区万市镇万市路30号）</ADDRESS><ACCEPT_TIMEDESC>8:30-11:30  12:30-16:00（冬令时）8:00-11:30  13:00-16:00（夏令时</ACCEPT_TIMEDESC><PHONE></PHONE><UUID>58bdece8-dce0-4dda-8b63-66c460b25b45</UUID></ACCEPT_ADDRESS></ACCEPT_ADDRESSS></DATAAREA>";
        String xml3 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><DATAAREA><CHANGE_FLAG>1</CHANGE_FLAG><ACCEPT_ADDRESSS><ACCEPT_ADDRESS><ADDRESS_KIND>1;</ADDRESS_KIND><ADDRESS>杭州市富阳区行政服务中心（一楼不动产综合受理窗口）（杭州市富阳区富春街道体育馆路471号）</ADDRESS><ACCEPT_TIMEDESC>上午8：00-11：00，下午13：00-16：30</ACCEPT_TIMEDESC><PHONE>0571-63156826</PHONE><UUID>f33af9d9-527e-455c-aa08-d48f64a2f38b</UUID></ACCEPT_ADDRESS></ACCEPT_ADDRESSS></DATAAREA>";

        String xml4 = "<NewDataSet>\n" +
                "  <xs:schema id=\"NewDataSet\" xmlns=\"\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:msdata=\"urn:schemas-microsoft-com:xml-msdata\">\n" +
                "    <xs:element name=\"NewDataSet\" msdata:IsDataSet=\"true\" msdata:UseCurrentLocale=\"true\">\n" +
                "      <xs:complexType>\n" +
                "        <xs:choice minOccurs=\"0\" maxOccurs=\"unbounded\">\n" +
                "          <xs:element name=\"table1\">\n" +
                "            <xs:complexType>\n" +
                "              <xs:sequence>\n" +
                "                <xs:element name=\"ID\" type=\"xs:string\" minOccurs=\"0\" />\n" +
                "                <xs:element name=\"TenderName\" type=\"xs:string\" minOccurs=\"0\" />\n" +
                "                <xs:element name=\"TendererName\" type=\"xs:string\" minOccurs=\"0\" />\n" +
                "                <xs:element name=\"ThisBudget\" type=\"xs:double\" minOccurs=\"0\" />\n" +
                "                <xs:element name=\"OpenBidTime\" type=\"xs:dateTime\" minOccurs=\"0\" />\n" +
                "                <xs:element name=\"PublishEndTime\" type=\"xs:dateTime\" minOccurs=\"0\" />\n" +
                "              </xs:sequence>\n" +
                "            </xs:complexType>\n" +
                "          </xs:element>\n" +
                "        </xs:choice>\n" +
                "      </xs:complexType>\n" +
                "    </xs:element>\n" +
                "  </xs:schema>\n" +
                "</NewDataSet>";

        String xml5 = "<NewDataSet>\n" +
                "  <xs:schema id=\"NewDataSet\" xmlns=\"\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:msdata=\"urn:schemas-microsoft-com:xml-msdata\">\n" +
                "    <xs:element name=\"NewDataSet\" msdata:IsDataSet=\"true\" msdata:UseCurrentLocale=\"true\">\n" +
                "      <xs:complexType>\n" +
                "        <xs:choice minOccurs=\"0\" maxOccurs=\"unbounded\">\n" +
                "          <xs:element name=\"table1\">\n" +
                "            <xs:complexType>\n" +
                "              <xs:sequence>\n" +
                "                <xs:element name=\"ID\" type=\"xs:string\" minOccurs=\"0\" />\n" +
                "                <xs:element name=\"TenderName\" type=\"xs:string\" minOccurs=\"0\" />\n" +
                "                <xs:element name=\"Personnels\" type=\"xs:string\" minOccurs=\"0\" />\n" +
                "                <xs:element name=\"PublishStartTime\" type=\"xs:dateTime\" minOccurs=\"0\" />\n" +
                "                <xs:element name=\"PublishEndTime\" type=\"xs:dateTime\" minOccurs=\"0\" />\n" +
                "                <xs:element name=\"WinPrice\" type=\"xs:double\" minOccurs=\"0\" />\n" +
                "                <xs:element name=\"EnterpriseName\" type=\"xs:string\" minOccurs=\"0\" />\n" +
                "              </xs:sequence>\n" +
                "            </xs:complexType>\n" +
                "          </xs:element>\n" +
                "        </xs:choice>\n" +
                "      </xs:complexType>\n" +
                "    </xs:element>\n" +
                "  </xs:schema>\n" +
                "  <table1>\n" +
                "    <ID>3D1BDD34-0716-459D-B68A-2ACCDBEDCADF</ID>\n" +
                "    <TenderName>上仓路等绿地养护项目</TenderName>\n" +
                "    <PublishStartTime>2016-03-01T00:00:00+08:00</PublishStartTime>\n" +
                "    <PublishEndTime>2616-03-03T00:00:00+08:00</PublishEndTime>\n" +
                "    <WinPrice>559.6008</WinPrice>\n" +
                "    <EnterpriseName>浙江歆源园林有限公司</EnterpriseName>\n" +
                "  </table1>\n" +
                "</NewDataSet>";
        org.json.JSONObject string = xml2Json(xml5);
//        var a = string.get("DATAAREA");
//        Map m = JSONObject.parseObject(a.toString());
//        Map b = (Map) m.get("MATERIALS");
//        System.out.println(a);
//        System.out.println(b);
//        System.out.println(string);
        System.out.println(string.toString());
//        String addressJson = JsonUtils.xml2Json(xml2).toString();
//        Map<String, Object> jsonObject = JSONObject.parseObject(addressJson);
//        Map<String, Object> str = (Map<String, Object>) jsonObject.get("DATAAREA");
//        Map<String, Object> addresss = (Map<String, Object>) str.get("ACCEPT_ADDRESSS");
//        try {
//            com.alibaba.fastjson.JSONArray address = (JSONArray) addresss.get("ACCEPT_ADDRESS");
//            StringBuilder stringBuilder = new StringBuilder();
//            for (int i = 0; i < address.size(); i++) {
//                //address.get(int i)是获取json格式”[]”中的值，i是下标
//                Map<String, Object> map = (Map<String, Object>) address.get(i);
//                if (map.get("ADDRESS") != null) {
//                    stringBuilder.append( map.get("ADDRESS")).append(" ");
//                }
//                if (map.get("PHONE") != null) {
//                    stringBuilder.append( map.get("PHONE")).append(" ");
//                }
//                if (map.get("ACCEPT_TIMEDESC") != null) {
//                    stringBuilder.append( map.get("ACCEPT_TIMEDESC")).append(" ");
//                }
//            }
//            addressInfo = stringBuilder.toString();
//        } catch (RuntimeException e) {
//            Map<String,Object> address = (Map<String,Object>) addresss.get("ACCEPT_ADDRESS");
//            StringBuilder stringBuilder = new StringBuilder();
//            if(address.get("ADDRESS")!=null){
//                stringBuilder.append( address.get("ADDRESS")).append(" ");
//            }
//            if(address.get("PHONE")!=null){
//                stringBuilder.append( address.get("PHONE")).append(" ");
//            }
//            if(address.get("ACCEPT_TIMEDESC")!=null){
//                stringBuilder.append( address.get("ACCEPT_TIMEDESC")).append(" ");
//            }
//            addressInfo = stringBuilder.toString();
//        }
//        System.out.println(addressInfo);
//    }
    }
}
