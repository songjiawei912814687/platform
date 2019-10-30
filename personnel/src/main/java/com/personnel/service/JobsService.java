package com.personnel.service;

import com.common.model.PageData;
import com.common.utils.ExportExcel;
import com.personnel.core.base.BaseMapper;
import com.personnel.core.base.BaseService;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.JobsOutput;
import com.personnel.mapper.jpa.JobsRepository;
import com.personnel.mapper.mybatis.JobsMapper;
import com.personnel.model.Jobs;
import com.personnel.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobsService extends BaseService<JobsOutput, Jobs,Integer> {

    @Autowired
    private JobsRepository repository;

    @Autowired
    private JobsMapper jobsMapper;
    @Override
    public BaseMapper<Jobs, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<JobsOutput> getMybatisBaseMapper() {
        return jobsMapper;
    }

    public List<Jobs> getByName(String name){
        return jobsMapper.selectByName(name);
    }

    public boolean isRepeat(Integer id,String name){
        Jobs jobs=repository.getById(id);
        if(jobs.getName().equals(name)){
            return true;
        }
        return false;
    }
    public String export(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "职务信息";
        String excelName = "职务信息";
        String[] rowsName = new String[]{"序号","职务","职责"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        PageData pageData=new PageData(request);
        List<JobsOutput> jobs=selectAll(false,pageData);
        if(jobs.size()>0){
            int i=1;
            for(JobsOutput jobsOutput:jobs){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=jobsOutput.getName();
                objs[2]=jobsOutput.getResponsibilities();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }





}
