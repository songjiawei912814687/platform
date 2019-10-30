package com.feedback.service;

import com.common.model.PageData;
import com.common.utils.ExportExcel;
import com.feedback.core.base.BaseMapper;
import com.feedback.core.base.BaseService;
import com.feedback.core.base.MybatisBaseMapper;
import com.feedback.domain.output.DeptSatisfactionOutput;
import com.feedback.domain.output.FeedbackInfoOutput;
import com.feedback.domain.output.WindowSatisfactionStatisticsOutput;
import com.feedback.mapper.jpa.FeedbackInfoRepository;
import com.feedback.mapper.mybatis.FeedbackInfoMapper;
import com.feedback.model.FeedbackInfo;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;


@Service
public class WindowSatisfactionStatisticsService extends BaseService<FeedbackInfoOutput, FeedbackInfo,Integer> {

    @Autowired
    private FeedbackInfoMapper feedbackInfoMapper;

    @Autowired
    private FeedbackInfoRepository feedbackInfoRepository;

    @Override
    public BaseMapper<FeedbackInfo, Integer> getMapper() {
        return feedbackInfoRepository;
    }

    @Override
    public MybatisBaseMapper<FeedbackInfoOutput> getMybatisBaseMapper() {
        return feedbackInfoMapper;
    }

    public List<WindowSatisfactionStatisticsOutput> getWindowSatisfactionStatistics(PageData pageData){
        pageData.put("userId",getUsers().getId().toString());
        if(getUsers().getAdministratorLevel()!=9){
            if(getUsers().getUserType()==0){
                pageData.put("employeeNo",getUsers().getUsername());
            }else {
                pageData.put("orgId",getUsers().getOrganizationId().toString());
            }
        }
       List<FeedbackInfoOutput> list=feedbackInfoMapper.selectCount(pageData);
        List<WindowSatisfactionStatisticsOutput> list1=new ArrayList<>();
       if(list!=null&&list.size()>0){
           for(FeedbackInfoOutput output:list){
               WindowSatisfactionStatisticsOutput output1=new WindowSatisfactionStatisticsOutput();
              String a=null;
              String b=null;
              Integer sum=output.getRunOneTimes()+output.getRunManyTimes();
               NumberFormat numberFormat = NumberFormat.getInstance();
               numberFormat.setMaximumFractionDigits(0);
               a= numberFormat.format((double)output.getSatisfactionNumber()/(double)(sum)*100);
               b= numberFormat.format((double)output.getRunOneTimes()/(double)(sum)*100);
               output1.setCount(sum);
               output1.setOrganizationName(output.getOrganizationName());
               output1.setEmployeesName(output.getEmployeesName());
               output1.setEmployeesNo(output.getEmployeesNo());
               output1.setSatisfactoryNumbers(output.getSatisfactionNumber());
               output1.setRunManyTimes(output.getRunManyTimes());
               output1.setRunOneTimes(output.getRunOneTimes());
               output1.setUnsatisfactoryNumbers(output.getUnSatisfactionNumber());
               output1.setSatisfactionRate(a+"%");
               output1.setRealizationRate(b+"%");
               list1.add(output1);

           }
       }
       return list1;
    }


    /**
     *
     * @param response
     * @param request
     * @return
     */
    public String windowSatisfactionStatisticsExport(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "窗口满意率统计报表";
        String excelName = "窗口满意率统计报表";
        String[] rowsName = new String[]{"序号","工号","部门","姓名","满意数","不满意数","满意率","跑一次","跑多次","实现率"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objects = null;
        PageData pageData=new PageData(request);
        List<WindowSatisfactionStatisticsOutput> list=getWindowSatisfactionStatistics(pageData);
        if(list.size()>0){
            int i=1;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            for(WindowSatisfactionStatisticsOutput windowSatisfactionStatisticsOutput:list){
                objects = new Object[rowsName.length];
                objects[0]=i;
                objects[1]=windowSatisfactionStatisticsOutput.getEmployeesNo();
                objects[2]=windowSatisfactionStatisticsOutput.getOrganizationName();
                objects[3]=windowSatisfactionStatisticsOutput.getEmployeesName();
                objects[4]=windowSatisfactionStatisticsOutput.getSatisfactoryNumbers();
                objects[5]=windowSatisfactionStatisticsOutput.getUnsatisfactoryNumbers();
                objects[6]=windowSatisfactionStatisticsOutput.getSatisfactionRate();
                objects[7]=windowSatisfactionStatisticsOutput.getRunOneTimes();
                objects[8]=windowSatisfactionStatisticsOutput.getRunManyTimes();
                objects[9]=windowSatisfactionStatisticsOutput.getRealizationRate();
                dataList.add(objects);
                i++;
            }
        }
        ExportExcel excel = new ExportExcel(title, rowsName, dataList, excelName);
        return excel.export(response,request);
    }




    public String getPathById(Integer id){
        return feedbackInfoMapper.selectPathById(id);
    }

//    public List<FeedbackInfoOutput> selectUnstatisOrRunMany(PageData pageData){
//        feedbackInfoMapper.selectUnstatisOrRunMany(pageData);
//    }

}
