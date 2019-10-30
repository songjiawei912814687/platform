package com.api.service;

import com.alibaba.fastjson.JSONArray;
import com.api.domain.output.*;
import com.api.mapper.jpa.AppointmentNumberRepository;
import com.api.mapper.mybatis.AppointmentNumberMapper;
import com.api.model.AppointmentNumber;
import com.common.response.ResponseResult;
import com.common.utils.HttpRequestUtil;
import com.google.common.collect.Lists;
import lombok.Cleanup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-29  08:56
 * @modified by:
 */
@Service
public class AppointmentNumberService {

    private static final Logger log = LoggerFactory.getLogger(AppointmentNumberService.class);

    @Autowired
    private AppointmentNumberRepository appointmentNumberRepository;
    @Autowired
    private AppointmentNumberMapper appointmentNumberMapper;
    @Value("${companyCode}")
    private String companyCode;
    @Value("${queHost}")
    private String queHost;

    @Value("${wechatqueHost}")
    private String wechatqueHost;

    @Value("${getDeptsUrl}")
    private String getDeptsUrl;

    @Value("${getBookableGroupsUrl}")
    private String getBookableGroupsUrl;

    @Value("${getBookableDateByDeptUrl}")
    private String getBookableDateByDeptUrl;

    @Value("${getAppointmentSummaryUrl}")
    private String getAppointmentSummaryUrl;

    @Value("${appointmentUrl}")
    private String appointmentUrl;

    @Value("${cancelAppointmentUrl}")
    private String cancelAppointmentUrl;

    @Value("${sqlserver.className}")
    private String className;

    @Value("${sqlserver.url}")
    private String url;

    @Value("${sqlserver.user}")
    private String user;

    @Value("${sqlserver.password}")
    private String password;

    @Value("${insertSqlserver.className}")
    private String insertClassName;

    @Value("${insertSqlserver.url}")
    private String insertUrl;

    @Value("${insertSqlserver.user}")
    private String insertUser;

    @Value("${insertSqlserver.password}")
    private String insertPassword;

    public List<GetDeptsOutput> getDepts() {


        String param = "companyCode=" + companyCode + "&outputJson=1";
        String data = HttpRequestUtil.sendPost(wechatqueHost + getDeptsUrl, param);
        if (data != null) {

            Map<String, Object> jsonObject = com.alibaba.fastjson.JSONObject.parseObject(data);

            JSONArray jsonArray = (JSONArray) jsonObject.get("attachObjectEx");
            List<GetDeptsOutput> getDeptsOutputList = Lists.newArrayList();
            Outer:for (int i = 0; i < jsonArray.size(); i++) {
                Map<String, Object> map = (Map<String, Object>) jsonArray.get(i);
                Integer id = (Integer) map.get("id");

                String deptCode = (String) map.get("code");
                String name = (String) map.get("name");
                if ("G0000004".equals(deptCode)) {
                    continue Outer;
                }
                //去请求窗口数据如果没有窗口不需要添加
                String paramGroups = "deptCode=" + deptCode + "&outputJson=1";
                String GroupsData = HttpRequestUtil.sendPost(wechatqueHost + getBookableGroupsUrl, paramGroups);

                if (GroupsData != null) {
                    Map<String, Object> jsonObject1 = com.alibaba.fastjson.JSONObject.parseObject(GroupsData);

                    JSONArray jsonArray1 = (JSONArray) jsonObject1.get("attachObjectEx");
                    if(jsonArray1.size()==0){
                        continue Outer;
                    }
                }
                GetDeptsOutput getDeptsOutput = new GetDeptsOutput();
                getDeptsOutput.setId(id);
                getDeptsOutput.setCode(deptCode);

                getDeptsOutput.setName(name);

                getDeptsOutputList.add(getDeptsOutput);
            }
            return getDeptsOutputList;
        }
        return null;
    }

    public List<GetBookableGroupsOutput> getBookableGroups(String deptCode){


        List<GetBookableGroupsOutput> getBookableGroupsOutputList = Lists.newArrayList();

            String param = "deptCode="+deptCode+"&outputJson=1";
            try {
                String data =  HttpRequestUtil.sendPost(wechatqueHost+getBookableGroupsUrl,param);
                if(data!=null){
                    Map<String,Object> jsonObject=com.alibaba.fastjson.JSONObject.parseObject(data);

                    JSONArray jsonArray = (JSONArray) jsonObject.get("attachObjectEx");
                    for(int i=0;i<jsonArray.size();i++){
                        Map<String,Object> map = (Map<String,Object>) jsonArray.get(i);
                        Integer id = (Integer) map.get("id");
                        //窗口编号
                        String code = (String) map.get("code");
                        //业务窗口名称
                        String name = (String) map.get("name");

                        GetBookableGroupsOutput getBookableGroupsOutput = new GetBookableGroupsOutput();
                        getBookableGroupsOutput.setId(id);
                        getBookableGroupsOutput.setCode(code);
                        getBookableGroupsOutput.setName(name);
                        getBookableGroupsOutput.setDeptCode(deptCode);

                        getBookableGroupsOutputList.add(getBookableGroupsOutput);
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        return getBookableGroupsOutputList;
    }

    public List<GetBookableDateByDeptOutput> getBookableDateByDept(String deptCode){
        String param = "deptCode="+deptCode+"&outputJson=1";

        String data =  HttpRequestUtil.sendPost(wechatqueHost+getBookableDateByDeptUrl,param);

        if(data!=null) {
            List<GetBookableDateByDeptOutput>getBookableDateByDeptOutputs = Lists.newArrayList();
            Map<String, Object> jsonObject = com.alibaba.fastjson.JSONObject.parseObject(data);

            JSONArray jsonArray = (JSONArray) jsonObject.get("attachObjectEx");

            for(int i=0;i<jsonArray.size();i++) {
                String date = (String) jsonArray.get(i);
                GetBookableDateByDeptOutput getBookableDateByDeptOutput = new GetBookableDateByDeptOutput();

                getBookableDateByDeptOutput.setDate(date);
                getBookableDateByDeptOutputs.add(getBookableDateByDeptOutput);
            }
            return getBookableDateByDeptOutputs;
        }
        return null;
    }

    public List<AppointmentSummaryOutput> getAppointmentSummary(String appointDateTime, String deptCode, String groupCode) throws ParseException {

        String param ="appointDateTime="+appointDateTime+"&deptCode="+deptCode+"&groupCode="+groupCode+"&outputJson=1";

        String data =  HttpRequestUtil.sendPost(wechatqueHost+getAppointmentSummaryUrl,param);
        if(data!=null) {

            Map<String, Object> jsonObject = com.alibaba.fastjson.JSONObject.parseObject(data);

            JSONArray jsonArray = (JSONArray) jsonObject.get("attachObjectEx");

            List<AppointmentSummaryOutput> appointmentSummaryOutputs = Lists.newArrayList();
            if(jsonArray!=null){
                for(int i=0;i<jsonArray.size();i++) {

                    Map<String,Object> map = (Map<String,Object>) jsonArray.get(i);

                    AppointmentSummaryOutput appointmentSummaryOutput = new AppointmentSummaryOutput();
                    Integer id = (Integer) map.get("id");
                    String deptName= (String) map.get("dept_name");
                    String groupName = (String) map.get("group_name");
                    String beginTime = (String) map.get("begin_time");
                    String endTime = (String) map.get("end_time");
                    ////时间段内还可取号的号码数量
                    Integer availableEnqueueQuantity = (Integer) map.get("available_enqueue_quantity");
                    //时间段内可取号的最多号码数量
                    Integer maxEnqueueQuantity = (Integer) map.get("max_enqueue_quantity");
                    appointmentSummaryOutput.setId(id);
                    appointmentSummaryOutput.setDeptCode(deptCode);
                    appointmentSummaryOutput.setDeptName(deptName);
                    appointmentSummaryOutput.setGroupCode(groupCode);
                    appointmentSummaryOutput.setGroupName(groupName);
                    appointmentSummaryOutput.setBeginTime(beginTime);
                    appointmentSummaryOutput.setEndTime(endTime);
                    appointmentSummaryOutput.setAvailableEnqueueQuantity("还剩预约数:"+availableEnqueueQuantity);
                    appointmentSummaryOutput.setMaxEnqueueQuantity("最多可预约数:"+maxEnqueueQuantity);

                    appointmentSummaryOutputs.add(appointmentSummaryOutput);

                }
            }
            return appointmentSummaryOutputs;
        }
        return null;

    }


    public ResponseResult appointment(AppointmentNumber appointmentNumber){

        appointmentNumber.setSequence(0);
        appointmentNumber.setPriorityCode("C");
        appointmentNumber.setServiceEmpCode("");
        appointmentNumber.setSource("");

        String param = "sequence="+appointmentNumber.getSequence()+"&code="+appointmentNumber.getCode()+
                "&name="+appointmentNumber.getName()+"&mobile="+appointmentNumber.getMobile()+
                "&priorityCode="+appointmentNumber.getPriorityCode()+"&groupCode="+appointmentNumber.getGroupCode()+
                "&serviceEmpCode="+appointmentNumber.getServiceEmpCode()+"&appointDateTime="+appointmentNumber.getAppointDateTime()+
                "&source="+appointmentNumber.getSource()+"&outputJson=1";
        String desc="" ;
        try {
            String data =  HttpRequestUtil.sendPost(wechatqueHost+appointmentUrl,param);

            net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(data);

            Integer code =(Integer) object.get("code");
            if(code.equals(0)){

                Map<String,Object> attMap = (Map<String,Object>) object.get("attachObjectEx");
                Integer id =(Integer) attMap.get("id");

                appointmentNumber.setId(id);
                //创建属性
                appointmentNumber.setCreatedDateTime(new Date());
                appointmentNumber.setCreatedUserId(0);
                appointmentNumber.setCreatedUserName(appointmentNumber.getCode());
                //是否已经取消，未取消是0，取消为1
                appointmentNumber.setAmputated(0);

                id = appointmentNumberRepository.save(appointmentNumber).getId();
                if(id==0||id == null){
                    return ResponseResult.error("预约信息保存失败");
                }
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

                String appointDate = appointmentNumber.getAppointDateTime();
                String time = appointDate.split(" ")[1];
                Date time1 = sdf.parse(time);
                Date time2 = sdf.parse("12:00:00");
                if(time1.compareTo(time2)<0){
                     time = "上午";
                }else {
                    time = "下午";
                }
                //这个改下，洪宇斌您好，您预约了民政局结婚登记业务，请携带办理材料，
                // 并于2019-08-06日上午至富阳区行政服务中心（富阳区鹿山街道江连路27号）办理
                String date = appointDate.split(" ")[0];

                String groupName = appointmentNumber.getGroupName().contains("业务")? appointmentNumber.getGroupName():appointmentNumber.getGroupName()+"业务";

                Map<String, Object> map = new HashMap<>();
                String[] strs = new String[]{appointmentNumber.getMobile()};
                map.put("mobiles", strs);
                map.put("smsContent",
                        appointmentNumber.getName()+"您好，您预约了"
                                +groupName+"，请携带办理材料并于"+date+"日"+time+"至富阳区行政服务中心（富阳区鹿山街道江连路27号）办理。");
                ResponseResult sendResult = HttpRequestUtil.sendPostRequest("http://10.71.177.154:15000/yun/sendDSMS", map);
                System.out.println(sendResult);
                return ResponseResult.success(appointmentNumber);
            }
            desc = (String) object.get("description");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseResult.error(desc);
    }


    /**同步154上的预约数据**/
    public void synAppointment(String startTime,String endTime){
        startTime = "'"+startTime+"'";
        endTime = "'"+endTime+"'";

        String sql = "SELECT * FROM [queue].[queueing_list] qq\n" +
                "where qq.creation_time BETWEEN" +startTime+ "and" +endTime +"and finish_time is null";

        ResultSet re ;
        //1.从154服务器上拉取数据
        try {
            Class.forName(insertClassName);
            Connection conn= DriverManager.getConnection(insertUrl,insertUser,insertPassword);
            Statement statement = conn.createStatement();
            re = statement.executeQuery(sql);
            while (re.next()){

                Integer sortIndex = re.getInt("sort_index");
                Integer sequence = re.getInt("sequence");
                String code = re.getString("code");
                String name = re.getString("name");
                String mobile = re.getString("mobile");
                String priorityCode = re.getString("priority_code");
                String  groupCode = re.getString("group_code");
                String serviceEmpCode = re.getString("service_emp_code");
                Date appointDateTime = re.getTimestamp("creation_time");
                String source = "";

                String param = "sequence="+sequence+"&code="+code+
                        "&name="+name+"&mobile="+mobile+
                        "&priorityCode="+priorityCode+"&groupCode="+groupCode+
                        "&serviceEmpCode="+serviceEmpCode+"&appointDateTime="+appointDateTime+
                        "&source="+source+"&outputJson=1";

                //将预约信息保存到246服务器
                String data =  HttpRequestUtil.sendPost(queHost+appointmentUrl,param);

                net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(data);

                Integer statusCode =(Integer) object.get("code");
                if(!statusCode.equals(0)){
                    log.info("预约信息同步出现异常");
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**获取预约列表*/
    public List<AppointmentNumberOutput> getAppointmentList(String code, String phone){
        List<AppointmentNumberOutput> appointmentNumberOutputList =  appointmentNumberMapper.selectByCodeAndPhone(code,phone);
        if(appointmentNumberOutputList == null||appointmentNumberOutputList.size() == 0){
            return null;
        }
        return appointmentNumberOutputList;
    }

    /**取消预约*/
    public ResponseResult cancelAppointment(Integer id) {

        AppointmentNumberOutput appointmentNumberOutput = appointmentNumberMapper.selectByPrimaryKey(id);
        if(appointmentNumberOutput==null){
            return ResponseResult.error("取消预约失败");
        }

        if(appointmentNumberOutput.getAmputated() == 1){
            return ResponseResult.error("该预约已经取消");
        }
        String param = "id="+id +"&outputJson=1";
        String desc = "";
        try {
            String data = HttpRequestUtil.sendPost(wechatqueHost + cancelAppointmentUrl, param);
            net.sf.json.JSONObject object = net.sf.json.JSONObject.fromObject(data);

            Integer statusCode = (Integer) object.get("code");
            if (statusCode.equals(0)) {

                //当取消预约后将amptemp状态设置成1
                appointmentNumberOutput.setAmputated(1);
                appointmentNumberOutput.setLastUpdateUserName(appointmentNumberOutput.getCreatedUserName());
                appointmentNumberOutput.setLastUpdateDateTime(new Date());
                appointmentNumberOutput.setLastUpdateUserId(0);
                AppointmentNumber appointmentNumber = new AppointmentNumber();
                BeanUtils.copyProperties(appointmentNumberOutput,appointmentNumber);
                Integer result = appointmentNumberRepository.save(appointmentNumber).getId();
                if(result == 0){
                    return ResponseResult.error("取消预约失败");
                }
                return ResponseResult.success("取消预约成功");
            }
            desc = (String) object.get("description");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseResult.error(desc+"取消预约失败");
    }


    //获取参与排队的部门列表
    public void syscDepts() throws Exception{
        String selectSql  = "select code,name from [queue].[V_DEPT_QUEUEING_SUMMARY]";

        String insertSql = "INSERT into  [queue].[V_DEPT_QUEUEING_SUMMARY] (code,name) VALUES (?,?)";
//        String insertSql = "INSERT into  [queue].[group] values (" +
//                "code," +
//                "name," +
//                "alias_name," +
//                "prefix," +
//                "minimum_sequence," +
//                "maximum_sequence," +
//                "max_waiting_of_realtime," +
//                "wait_timeout_minute," +
//                "service_timeout_minute," +
//                "report_file," +
//                "number_of_copies," +
//                "priority_weights," +
//                "voice_formula," +
//                "sub_group_code," +
//                "super_code," +
//                "dept_code," +
//                "average_service_minute," +
//                "keywords," +
//                "area_code," +
//                "description," +
//                "enabled," +
//                "bookable," +
//                "queueable," +
//                "sequence_length," +
//                "readly_quantity," +
//                "group_type_codes," +
//                "sort_index," +
//                "predistribution_counter) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        @Cleanup ResultSet re = null;
        @Cleanup Connection conn = null;
        @Cleanup Statement statement=null;
        @Cleanup PreparedStatement preparedStatement = null;
        try {
            Class.forName(className);

            conn= DriverManager.getConnection(url,user,password);
            statement = conn.createStatement();
            re = statement.executeQuery(selectSql);

            while (re.next()){
               String code = re.getString("code");
               String name = re.getString("name");
                try {
                    Class.forName(insertClassName);
                    conn= DriverManager.getConnection(insertUrl,insertUser,insertPassword);
                    preparedStatement = conn.prepareStatement(insertSql);
                    preparedStatement.setString(1, code);
                    preparedStatement.setString(2, name);
                    preparedStatement.executeQuery();

                } catch (Exception e) {
                    continue;
                }
            }
        } catch (Exception e) {
            log.error("连接源部门数据表出现异常，请检查连接...");
        }
    }

    //获取参与排队的部门下的队列列表
    public void syscWindows() throws Exception{
        String selectSql  = "select * from [queue].[group] ";
//        String insertSql = "INSERT into  [queue].[group] (code,name) VALUES (?,?)";
        String insertSql = "INSERT into  [queue].[group](code,name,prefix," +
                "minimum_sequence,maximum_sequence,max_waiting_of_realtime," +
                "wait_timeout_minute,service_timeout_minute,report_file," +
                "number_of_copies,priority_weights,voice_formula,sub_group_code," +
                "super_code,dept_code,average_service_minute,keywords,area_code," +
                "description,enabled,bookable,queueable,sequence_length,readly_quantity," +
                "group_type_codes,sort_index,predistribution_counter) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        ResultSet re = null;
        Connection conn = null;
        Statement statement=null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(className);
            conn= DriverManager.getConnection(url,user,password);
            statement = conn.createStatement();
            re = statement.executeQuery(selectSql);
            while (re.next()){
                String code = re.getString("code");
                String name = re.getString("name");
                String prefix = re.getString("prefix");
                int minimum_sequence = re.getInt("minimum_sequence");
                int maximum_sequence = re.getInt("maximum_sequence");
                int max_waiting_of_realtime = re.getInt("max_waiting_of_realtime");
                int wait_timeout_minute = re.getInt("wait_timeout_minute");
                int service_timeout_minute = re.getInt("service_timeout_minute");
                String report_file = re.getString("report_file");
                int number_of_copies = re.getInt("number_of_copies");
                int priority_weights = re.getInt("priority_weights");
                String voice_formula = re.getString("voice_formula");
                String sub_group_code = re.getString("sub_group_code");
                String super_code = re.getString("super_code");
                String dept_code = re.getString("dept_code");
                int average_service_minute =re.getInt("average_service_minute");
                String keywords =re.getString("keywords");
                String area_code = re.getString("area_code");
                String description = re.getString("description");
                int enabled =re.getInt("enabled");
                int bookable =re.getInt("bookable");
                int queueable =re.getInt("queueable");
                int sequence_length =re.getInt("sequence_length");
                int readly_quantity =re.getInt("readly_quantity");
                String group_type_codes = re.getString("group_type_codes");
                int sort_index =re.getInt("sort_index");
                int predistribution_counter =re.getInt("predistribution_counter");
                try {
                    Class.forName(insertClassName);
                    conn= DriverManager.getConnection(insertUrl,insertUser,insertPassword);
                    preparedStatement = conn.prepareStatement(insertSql);
                    preparedStatement.setString(1, code);
                    preparedStatement.setString(2, name);
                    preparedStatement.setString(3, prefix);
                    preparedStatement.setInt(4, minimum_sequence);
                    preparedStatement.setInt(5, maximum_sequence);
                    preparedStatement.setInt(6, max_waiting_of_realtime);
                    preparedStatement.setInt(7, wait_timeout_minute);
                    preparedStatement.setInt(8, service_timeout_minute);
                    preparedStatement.setString(9, report_file);
                    preparedStatement.setInt(10, number_of_copies);
                    preparedStatement.setInt(11, priority_weights);
                    preparedStatement.setString(12, voice_formula);
                    preparedStatement.setString(13, sub_group_code);
                    preparedStatement.setString(14, super_code);
                    preparedStatement.setString(15, dept_code);
                    preparedStatement.setInt(16, average_service_minute);
                    preparedStatement.setString(17, keywords);
                    preparedStatement.setString(18, area_code);
                    preparedStatement.setString(19, description);
                    preparedStatement.setInt(20, enabled);
                    preparedStatement.setInt(21, bookable);
                    preparedStatement.setInt(22, queueable);
                    preparedStatement.setInt(23, sequence_length);
                    preparedStatement.setInt(24, readly_quantity);
                    preparedStatement.setString(25, group_type_codes);
                    preparedStatement.setInt(26, sort_index);
                    preparedStatement.setInt(27, predistribution_counter);

                    preparedStatement.executeUpdate();

                } catch (Exception e) {
                    continue;
                }
            }
        } catch (Exception e) {
            log.error("连接源部门下队列窗口数据表出现异常，请检查连接...");
        }
        finally {
            conn.close();
            preparedStatement.close();
            re.close();
            statement.close();
        }
    }

    //同步节假日表(工作日+节假日)
    public void syscWorkingDays() throws Exception{
        String selectSql  = "select * from [comm].[work_calendar] ";
        String insertSql = "INSERT into [comm].[work_calendar](year,month,day,workingday_or_holiday,enabled) VALUES (?,?,?,?,?)";
        ResultSet re = null;
        Connection conn = null;
        Statement statement=null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(className);
            conn= DriverManager.getConnection(url,user,password);
            statement = conn.createStatement();
            re = statement.executeQuery(selectSql);
            while (re.next()){
                int year = re.getInt("year");
                int month = re.getInt("month");
                int day = re.getInt("day");
                int workingday_or_holiday = re.getInt("workingday_or_holiday");
                int enabled = re.getInt("enabled");

                try {
                    Class.forName(insertClassName);
                    conn= DriverManager.getConnection(insertUrl,insertUser,insertPassword);
                    preparedStatement = conn.prepareStatement(insertSql);
                    preparedStatement.setInt(1, year);
                    preparedStatement.setInt(2, month);
                    preparedStatement.setInt(3, day);
                    preparedStatement.setInt(4, workingday_or_holiday);
                    preparedStatement.setInt(5, enabled);
                    preparedStatement.executeUpdate();

                } catch (Exception e) {
                    continue;
                }
            }
        } catch (Exception e) {
            log.error("连接可预约工作日数据表出现异常，请检查连接...");
        }
        finally {
            conn.close();
            preparedStatement.close();
            re.close();
            statement.close();
        }
    }

    //
    public void syscSettingNum() throws Exception{
        String selectSql  = "select [queue].[GetParameterValueByOwner]() ";
        String insertSql = "INSERT into [comm].[work_calendar](year,month,day,workingday_or_holiday,enabled) VALUES (?,?,?,?,?)";
        ResultSet re = null;
        Connection conn = null;
        Statement statement=null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(className);
            conn= DriverManager.getConnection(url,user,password);
            statement = conn.createStatement();
            re = statement.executeQuery(selectSql);
            while (re.next()){
                int year = re.getInt("year");
                int month = re.getInt("month");
                int day = re.getInt("day");
                int workingday_or_holiday = re.getInt("workingday_or_holiday");
                int enabled = re.getInt("enabled");

                try {
                    Class.forName(insertClassName);
                    conn= DriverManager.getConnection(insertUrl,insertUser,insertPassword);
                    preparedStatement = conn.prepareStatement(insertSql);
                    preparedStatement.setInt(1, year);
                    preparedStatement.setInt(2, month);
                    preparedStatement.setInt(3, day);
                    preparedStatement.setInt(4, workingday_or_holiday);
                    preparedStatement.setInt(5, enabled);
                    preparedStatement.executeUpdate();

                } catch (Exception e) {
                    continue;
                }
            }
        } catch (Exception e) {
            log.error("连接可预约工作日数据表出现异常，请检查连接...");
        }
        finally {
            conn.close();
            preparedStatement.close();
            re.close();
            statement.close();
        }
    }


}
