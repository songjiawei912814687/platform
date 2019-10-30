package com.screen.service;

import com.common.model.PageData;
import com.screen.core.base.BaseMapper;
import com.screen.core.base.BaseService;
import com.screen.core.base.MybatisBaseMapper;
import com.screen.domain.output.EmployeesOutput;
import com.screen.domain.output.FeedbackInfoOutput;
import com.screen.domain.output.WindowEmployeesOutput;
import com.screen.mapper.jpa.WindowEmployeeRepository;
import com.screen.mapper.mybatis.EmployeesMapper;
import com.screen.mapper.mybatis.FeedbackInfoMapper;
import com.screen.mapper.mybatis.WindowEmployeesMapper;
import com.screen.mapper.mybatis.WindowMapper;
import com.screen.model.WindowEmployees;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class WindowEmployeesService extends BaseService<WindowEmployeesOutput, WindowEmployees,Integer> {
    @Autowired
    private WindowEmployeeRepository windowEmployeeRepository;
    @Autowired
    private WindowEmployeesMapper windowEmployeesMapper;
    @Autowired
    private EmployeesMapper employeesMapper;
    @Autowired
    private FeedbackInfoMapper feedbackInfoMapper;
    @Autowired
    private WindowMapper windowMapper;



    @Override
    public BaseMapper<WindowEmployees, Integer> getMapper() {
        return windowEmployeeRepository;
    }

    @Override
    public MybatisBaseMapper<WindowEmployeesOutput> getMybatisBaseMapper() {
        return windowEmployeesMapper;
    }

    public void addWindowEmployees() throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        windowEmployeesMapper.delete();
        PageData pageData=new PageData();
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String tp = simpleDateFormat.format(date);
        String[] strings=tp.split("-");
        pageData.put("year",strings[0]);
        pageData.put("month",strings[1]);
        List<EmployeesOutput> list=employeesMapper.selectByWindowState(pageData);
        if(list!=null&&list.size()>0){
           for(EmployeesOutput employeesOutput:list){
               WindowEmployees windowEmployees=new WindowEmployees();
               windowEmployees.setAmputated(0);
               windowEmployees.setEmployeeId(employeesOutput.getId());
               windowEmployees.setEmployeeNo(employeesOutput.getEmployeeNo());
               windowEmployees.setIcon(employeesOutput.getIcon());
               windowEmployees.setIsStar(employeesOutput.getIsStar()==null?0:employeesOutput.getIsStar());
               windowEmployees.setName(employeesOutput.getName());
               windowEmployees.setSatisfaction("100%");
               windowEmployees.setOfficePhone(employeesOutput.getOfficePhone());
               windowEmployees.setPhoneNumber(employeesOutput.getPhoneNumber());
               windowEmployees.setOrganizationId(employeesOutput.getOrganizationId());
               windowEmployees.setWindowId(employeesOutput.getWindowId());
               super.add(windowEmployees);
               log.info("新增成功");
           }
        }
    }

    public void updateWindowEmployee() throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        PageData pageData=new PageData();
        List<WindowEmployeesOutput> list=windowEmployeesMapper.selectAll(pageData);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        String tp = simpleDateFormat.format(cal.getTime());
        pageData.put("time",tp);
        List<FeedbackInfoOutput> list1=feedbackInfoMapper.selectAll(pageData);
        for(WindowEmployeesOutput windowEmployeesOutput:list){
            WindowEmployees windowEmployees=new WindowEmployees();
            windowEmployees.setId(windowEmployeesOutput.getId());
            List<FeedbackInfoOutput> list2=new ArrayList<>();
            if(list1!=null&&list1.size()>0){
                for(FeedbackInfoOutput feedbackInfoOutput:list1){
                    if(feedbackInfoOutput.getEmployeesNo().equals(windowEmployeesOutput.getEmployeeNo())){
                       list2.add(feedbackInfoOutput);
                    }
                }
            }
            if(list2!=null&&list2.size()>0){
                int a=0;//满意数
                int b=0;//不满意数
                for(FeedbackInfoOutput feedbackInfoOutput:list2){
                     if(feedbackInfoOutput.getSatisfaction()!=null&&feedbackInfoOutput.getSatisfaction()==0){
                         a++;
                     }else {
                         b++;
                     }
                }
                int c=a+b;//总数
                if(c>0){
                    NumberFormat numberFormat = NumberFormat.getInstance();
                    numberFormat.setMaximumFractionDigits(0);
                    String s = numberFormat.format((double)a/(double)(c)*100);
                    windowEmployees.setSatisfaction(s+"%");
                }
            }
            super.update(windowEmployees.getId(),windowEmployees);
            log.info("跟新成功");
        }
    }


    public List<WindowEmployeesOutput> getByOrganization(Integer organizationId){
        return windowEmployeesMapper.selectOrganizationId(organizationId);
    }

//    public List<FeedbackInfoOutput> getFeedbackInfoByOrganizationId(Integer organizationId){
//        PageData pageData=new PageData();
//        pageData.put("organizationId",""+organizationId);
//        List<FeedbackInfoOutput> list=feedbackInfoMapper.selectDissatisfaction(pageData);
//        return list;
//    }

//
//    public String getFeedbackInfoCountByOrganizationId(Integer organizationId){
//        List<FeedbackInfoOutput> list=feedbackInfoMapper.selectByOrganizationId(organizationId);
//        String s="100%";
//        if(list!=null&&list.size()>0){
//            int a=0;
//            int b=0;
//            int c=0;
//            for(FeedbackInfoOutput feedbackInfoOutput:list){
//                if(feedbackInfoOutput.getSatisfaction()!=null&&feedbackInfoOutput.getSatisfaction()==1){
//                    b++;
//                }else {
//                    a++;
//                }
//            }
//            if(c>0){
//                NumberFormat numberFormat = NumberFormat.getInstance();
//                numberFormat.setMaximumFractionDigits(0);
//                s = numberFormat.format((double)a/(double)(c)*100);
//            }
//        }
//        return s;
//    }
//    public int getTodayCount(Integer organizationId){
//        List<FeedbackInfoOutput> list=feedbackInfoMapper.selectTodayByOrganizationId(organizationId);
//        int a=0;
//        if(list!=null&&list.size()>0){
//            a=list.size();
//        }
//        return a;
//    }

//    public List<WindowCountOutput> getWindowTodayCount(Integer organizationId){
//        PageData pageData=new PageData();
//        pageData.put("organizationId",""+organizationId);
//        List<WindowOutput> list=windowMapper.selectByOrganizationId(pageData);
//        List<FeedbackInfoOutput> list1=feedbackInfoMapper.selectTodayByOrganizationId(organizationId);
//        List<WindowCountOutput> outputs=new ArrayList<WindowCountOutput>();
//        if(list!=null&&list.size()>0){
//            for(WindowOutput windowOutput:list){
//                WindowCountOutput windowCountOutput=new WindowCountOutput();
//                windowCountOutput.setWindowNo(windowOutput.getWindowNo());
//                int a=0;
//                if(list1!=null&&list1.size()>0){
//                    for(FeedbackInfoOutput feedbackInfoOutput:list1){
//                        if(feedbackInfoOutput.getWindowNo().equals(windowOutput.getWindowNo())){
//                            a++;
//                        }
//                    }
//                }
//                windowCountOutput.setCount(a);
//                outputs.add(windowCountOutput);
//            }
//        }
//        return outputs;
//    }


}
