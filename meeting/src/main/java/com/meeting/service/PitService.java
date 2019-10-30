package com.meeting.service;

import com.common.model.PageData;
import com.meeting.core.base.BaseMapper;
import com.meeting.core.base.BaseService;
import com.meeting.core.base.MybatisBaseMapper;
import com.meeting.domain.output.PitOutput;
import com.meeting.mapper.jpa.PitRepository;
import com.meeting.mapper.mybatis.PitMapper;
import com.meeting.model.Pit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PitService extends BaseService<PitOutput, Pit,Integer> {
    @Autowired
    private PitRepository repository;

    @Autowired
    private PitMapper pitMapper;

    @Override
    public BaseMapper<Pit, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<PitOutput> getMybatisBaseMapper() {
        return pitMapper;
    }

    public List<PitOutput> getByIp(String ip){
        return pitMapper.selectByIp(ip);
    }

    public List<PitOutput> getByName(String name){
        return pitMapper.selectByName(name);
    }

    public List<PitOutput> getByApply(PageData pageData){
        List<PitOutput> pageList = pitMapper.selectAll(pageData);
        return pageList;
    }
}
