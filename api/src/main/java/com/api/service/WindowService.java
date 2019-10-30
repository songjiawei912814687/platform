package com.api.service;

import com.api.core.base.BaseMapper;
import com.api.core.base.BaseService;
import com.api.core.base.MybatisBaseMapper;
import com.api.domain.output.WindowOutput;
import com.api.mapper.jpa.WindowRepository;
import com.api.mapper.mybatis.OrganizationMapper;
import com.api.mapper.mybatis.WindowMapper;
import com.api.model.Window;
import com.common.model.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WindowService extends BaseService<WindowOutput, Window,Integer> {
    @Autowired
    private WindowRepository repository;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private WindowMapper windowMapper;
    @Override
    public BaseMapper<Window, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<WindowOutput> getMybatisBaseMapper() {
        return windowMapper;
    }

    public List<WindowOutput> getByOrganizationId(Integer organizationId){
        PageData pageData=new PageData();
        pageData.put("organizationId",organizationId);
        return windowMapper.selectByOrganizationId(pageData);
    }

    public int getCountByOrganizationId(Integer organizationId){
        int count=0;
        List<WindowOutput> list=getByOrganizationId(organizationId);
        if(list!=null&&list.size()>0){
            count=list.size();
        }
        return count;
    }

    public List<WindowOutput> getByOrganCode(List<String> list){
        var windows = windowMapper.selectByOrganCode(list);
        return windows;
    }


}
