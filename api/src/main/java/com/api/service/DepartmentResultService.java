package com.api.service;

import com.api.domain.output.PerformanceIndexOutput;
import com.api.mapper.jpa.ConfigRepository;
import com.api.mapper.mybatis.AppraisalPlanMapper;
import com.api.model.Config;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class DepartmentResultService {

    @Autowired
    private AppraisalPlanMapper appraisalPlanMapper;

    @Autowired
    private ConfigRepository configRepository;
    //展示上月参与考核的各部门考核得分，按照得分倒序分页展示，实际效果是上下滚动。接口做成分页形式
    public ResponseResult performanceIndex(PageData pageData) {
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);

        Config config = configRepository.getById(117);
        if(config==null){
            return ResponseResult.success();
        }
        Integer year ;
        Integer month ;
        //state0为有效
        if(config.getState().equals(0)){
            String value = config.getConfigValue();
            String [] str = value.split("-");
            year = Integer.valueOf(str[0]);
            month = Integer.valueOf(str[1]);
        }else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH,-1);

            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH)+1;
        }
        pageData.put("year",year);
        pageData.put("month",month);
        return ResponseResult.success(new PageInfo<>(appraisalPlanMapper.selectPage(pageData)));
    }
}
