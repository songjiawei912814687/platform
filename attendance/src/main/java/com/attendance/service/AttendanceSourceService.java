package com.attendance.service;

import com.attendance.core.base.BaseMapper;
import com.attendance.core.base.BaseService;
import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.AttendanceSourceDataOutput;
import com.attendance.domian.output.EmployeesOutput;
import com.attendance.mapper.jpa.AttendanceDataRepository;
import com.attendance.mapper.jpa.AttendanceSourceRepository;
import com.attendance.mapper.mybatis.AttendanceSourceDataMapper;
import com.attendance.mapper.mybatis.EmployeesMapper;
import com.attendance.mapper.mybatis.OrganizationMapper;
import com.attendance.model.AttendanceData;
import com.attendance.model.AttendanceSourceData;
import com.attendance.model.Organization;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.HttpRequestUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author: Young
 * @description:
 * @date: Created in 17:04 2018/10/9
 * @modified by:
 */
@Service("attendanceSourceService")
public class AttendanceSourceService extends BaseService<AttendanceSourceDataOutput, AttendanceSourceData,Integer> {

        @Autowired
        private AttendanceSourceRepository attendanceSourceRepository;
        @Autowired
        private AttendanceDataRepository attendanceDataRepository;
        @Autowired
        private EmployeesMapper employeesMapper;
        @Autowired
        private OrganizationMapper organizationMapper;
        @Autowired
        private AttendanceSourceDataMapper attendanceSourceDataMapper;

        private final Logger log = LoggerFactory.getLogger(getClass());

        @Value("${haikang.url}")
        private String haikangUrl;
        @Value("${postgreUrl}")
        private String postgreUrl;
        @Value("${user}")
        private String user;
        @Value("${password}")
        private String password;
        @Value("${driver}")
        private String driver;

        @Override
        public BaseMapper<AttendanceSourceData, Integer> getMapper() {
            return attendanceSourceRepository;
        }

        @Override
        public MybatisBaseMapper<AttendanceSourceDataOutput> getMybatisBaseMapper() {
            return null;
        }

        private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        @Transactional
        public void addData(String startTimeString,String endTimeString) throws ParseException {
        Map<String,Object> map = getHikang(1,startTimeString,endTimeString);
        Integer total = (Integer) map.get("total");
        Integer pages = total%10==0?total/10:total/10+1;
        if(pages>=2){
            for(int i = 2;i<=pages;i++){
                getHikang(i,startTimeString,endTimeString);
            }
        }
    }

        private Map<String,Object> getHikang(Integer pageNo,String startTimeString,String endTimeString) throws ParseException {


            //得到开始时间和结束时间的毫秒数
            Long startTime = sdf.parse(startTimeString).getTime();
            Long endTime = sdf.parse(endTimeString).getTime();
            PageData pageData = new PageData();
            pageData.put("pageNo",pageNo);
            pageData.put("pageSize",pageData.getRows().toString());
            pageData.put("startTime",String.valueOf(startTime));
            pageData.put("endTime",String.valueOf(endTime));
            ResponseResult result = HttpRequestUtil.sendPostRequest(haikangUrl,pageData);
            Map<String, Object> dataList = (Map<String, Object>) result.getData();
            addDataIntoSheet(dataList);
            return dataList;
    }

        public void addDataIntoSheet(Map<String,Object> dataList) {
            List<AttendanceSourceData> attendanceSourceDataList = Lists.newArrayList();
            List<AttendanceData> attendanceDataList = Lists.newArrayList();
            var values = (ArrayList<Object>) dataList.get("list");
            for (int i = 0; i < values.size(); i++) {
                Map<String, Object> attendancemap = (Map<String, Object>) values.get(i);
                AttendanceSourceData attendanceSourceData = new AttendanceSourceData();
                String doorName = (String) attendancemap .get("doorName");
                String doorUuid = (String) attendancemap.get("doorUuid");
                String eventUuid = (String) attendancemap.get("eventUuid");
                Integer eventType = (Integer) attendancemap.get("eventType");
                Long eventTime = (Long) attendancemap.get("eventTime");
                String eventName = (String) attendancemap.get("eventName");
                Integer deviceType = (Integer) attendancemap.get("deviceType");
                String cardNo = (String) attendancemap.get("cardNo");
                String personName = (String) attendancemap.get("personName");
                String deptUuid = (String) attendancemap.get("deptUuid");
                String deptName = (String) attendancemap.get("deptName");
                if(cardNo!=null&&eventTime!=null) {
                    if (eventName.contains("指纹") || eventName.contains("人脸")) {
                        attendanceSourceData.setDoorName(doorName);
                        attendanceSourceData.setDoorUuid(doorUuid);
                        attendanceSourceData.setEventUuid(eventUuid);
                        attendanceSourceData.setEventType(eventType);
                        attendanceSourceData.setEventTime(new Date(eventTime));
                        attendanceSourceData.setEventName(eventName);
                        attendanceSourceData.setDeviceType(deviceType);
                        attendanceSourceData.setCardNo(cardNo);
                        attendanceSourceData.setPersonName(personName);
                        attendanceSourceData.setDeptUuid(deptUuid);
                        attendanceSourceData.setDeptName(deptName);
                        attendanceSourceData.setAmputated(0);

                        //根据工号和打卡时间去源数据表中查询，
                        Integer sourceCount = attendanceSourceDataMapper.selectSourceDateByCardNoAndEventTime(cardNo,new Date(eventTime));
                        //如果存在就查询下一条记录
                        if(sourceCount>0){
                            continue;
                        }
                        //如果不存在就添加到集合中
                        attendanceSourceDataList.add(attendanceSourceData);

                        //设置考勤数据对象属性
                        EmployeesOutput employees = employeesMapper.selectByEmployeesNo(cardNo);
                        if (employees != null) {
                            AttendanceData attendanceData = new AttendanceData();
                            attendanceData.setEmployeeId(employees.getId());
                            attendanceData.setOrganizationId(employees.getOrganizationId());
                            attendanceData.setJobsId(employees.getJobsId());
                            attendanceData.setAttendanceDeviceName(doorName);
                            attendanceData.setAuthentication(eventName);
                            attendanceData.setPunchTime(new Date(eventTime));

                            attendanceData.setCreatedUserId(1);
                            attendanceData.setCreatedUserName("attendanceDataJob");
                            attendanceData.setCreatedDateTime(new Date());
                            attendanceData.setLastUpdateUserId(1);
                            attendanceData.setLastUpdateDateTime(new Date());
                            attendanceData.setLastUpdateUserName("attendanceDataJob");
                            attendanceDataList.add(attendanceData);
                        }
                    }
                }
            }
            attendanceSourceRepository.saveAll(attendanceSourceDataList);
            attendanceDataRepository.saveAll(attendanceDataList);
        }

        public ResponseResult getLDKDate(String startTime,String endTime){
         Connection connection = null;
         Statement statement = null;
         try {
             Class.forName(driver);
            connection = DriverManager.getConnection(postgreUrl,user,password);
            log.info("成功连接上pg数据库");
            String sql = "SELECT * FROM \"public\".\"v_fas_face_roster_alarm\" vf\n" +
                    "where vf.create_time BETWEEN  \'"  +startTime+ "\'"+
                    "and \'" +endTime+"\'";

            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                //查询出工号
                String empNo =  resultSet.getString("person_info_syscode");
                if(empNo == null||empNo == ""){
                    continue;
                }
                //查询出打卡时间
                Date punchTime = resultSet.getTimestamp("create_time");
                if(punchTime == null){
                    continue;
                }

                String attendanceTime = sdf.format(punchTime);
                //重要步骤根据员工工号和时间段的数据先去考勤元素表里面去查
                Integer attendanceCount  = attendanceSourceDataMapper.selectByEmpNoAndDate(empNo,startTime,endTime,attendanceTime);
                //如果有超过1条以上的数据,就查询下一条考勤记录
                if(attendanceCount>0){
                    continue;
                }
                //查询出员工数据
                EmployeesOutput employeesOutput = employeesMapper.selectByEmployeesNo(empNo);
                if(employeesOutput==null){
                    continue;
                }
                //查询组织对象
                Organization organization = organizationMapper.selectOrNoByOrId(employeesOutput.getOrganizationId());
                if(organization==null){
                    continue;
                }
                //保存考勤源数据
                Integer sourceId = this.saveAttendanceSource(punchTime,employeesOutput,organization);
                if(sourceId==null){
                    continue;
                }
                //保存考勤数据
                Integer attendanceId = this.saveAttendanceDate(punchTime,employeesOutput,organization);
                if(attendanceId == null){
                    continue;
                }
            }
         }catch (Exception e){
            log.error(e.getMessage());
         }finally {
             try{
                 statement.close();
                 connection.close();
             }catch (Exception e){
                 e.printStackTrace();
             }
         }
         return ResponseResult.success();
    }

    private Integer saveAttendanceSource(Date punchTime,EmployeesOutput employeesOutput,Organization organization){

         AttendanceSourceData attendanceSourceData = new AttendanceSourceData();
        attendanceSourceData.setDeptName(organization.getName());
        attendanceSourceData.setDoorName(organization.getName());
        attendanceSourceData.setCardNo(employeesOutput.getEmployeeNo());
        attendanceSourceData.setEventName("落地式考勤机人脸认证");
        attendanceSourceData.setEventTime(punchTime);
        attendanceSourceData.setEventType(196893);
        attendanceSourceData.setDeptUuid("LDK");
        attendanceSourceData.setEventUuid("LDK");
        attendanceSourceData.setPersonName(employeesOutput.getName());
        attendanceSourceData.setPersonId(employeesOutput.getId());
        attendanceSourceData.setAmputated(0);

        Integer sourceId = attendanceSourceRepository.save(attendanceSourceData).getId();
        if(sourceId<=0){
            return null;
        }
        return sourceId;
    }

    private Integer saveAttendanceDate(Date punchTime,EmployeesOutput employeesOutput,Organization organization){

        AttendanceData attendanceData = new AttendanceData();
        attendanceData.setEmployeeId(employeesOutput.getId());
        attendanceData.setOrganizationId(employeesOutput.getOrganizationId());
        attendanceData.setAttendanceDeviceName(organization.getName());
        attendanceData.setAuthentication(organization.getName());
        attendanceData.setPunchTime(punchTime);
        attendanceData.setDescription("落地式考勤机推送到考勤记录中");
        attendanceData.setJobsId(employeesOutput.getJobsId());
        attendanceData.setCreatedUserId(0);
        attendanceData.setCreatedUserName("落地式考勤机");
        attendanceData.setCreatedDateTime(new Date());
        attendanceData.setLastUpdateUserId(0);
        attendanceData.setLastUpdateDateTime(new Date());
        attendanceData.setLastUpdateUserName("落地式考勤机");
        Integer attendanceId = attendanceDataRepository.save(attendanceData).getId();
        if (attendanceId == null) {
            return null;
        }
        return attendanceId;
    }
}
