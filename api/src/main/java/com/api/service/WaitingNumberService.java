package com.api.service;

import com.api.core.base.BaseMapper;
import com.api.core.base.BaseService;
import com.api.core.base.MybatisBaseMapper;
import com.api.domain.output.OrganizationOutput;
import com.api.domain.output.StationPeopleOutput;
import com.api.domain.output.WaitingNumberOutput;
import com.api.mapper.jpa.OrganizationRepository;
import com.api.mapper.mybatis.OrganizationMapper;
import com.api.mapper.mybatis.StationPeopleMapper;
import com.api.model.Organization;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


@Service
public class WaitingNumberService extends BaseService<OrganizationOutput, Organization,Integer> {

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private StationPeopleMapper stationPeopleMapper;

    @Override
    public BaseMapper<Organization, Integer> getMapper() {
        return organizationRepository ;
    }

    @Override
    public MybatisBaseMapper<OrganizationOutput> getMybatisBaseMapper(){return organizationMapper;}

    public List<WaitingNumberOutput> findPageList(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<WaitingNumberOutput> waitingNumberOutputs = null;
        try {
            Calendar calendar = Calendar.getInstance();
            //结束时间为当前时间
            String endDate = sdf.format(calendar.getTime());
            calendar.add(Calendar.MINUTE,-10);
            //开始时间为前30分钟
            String startDate = sdf.format(calendar.getTime());
            waitingNumberOutputs = Lists.newArrayList();
            //获取办件取号量前10名
            List<StationPeopleOutput> stationPeopleOutputList = stationPeopleMapper.selectByTakeTime(startDate,endDate);
            if(stationPeopleOutputList == null||stationPeopleOutputList.size()==0){
                List<String> orgaNameList =stationPeopleMapper.selectDeptName();
                for(String orgName:orgaNameList){
                    WaitingNumberOutput waitingNumberOutput = new WaitingNumberOutput();

                    waitingNumberOutput.setNumber(0);
                    waitingNumberOutput.setOrganizationName(orgName);
                    waitingNumberOutputs.add(waitingNumberOutput);
                }
                return waitingNumberOutputs;
            }

            for(StationPeopleOutput stationPeopleOutput:stationPeopleOutputList){
                WaitingNumberOutput waitingNumberOutput = new WaitingNumberOutput();
                //等待人数的算法应该是取号总数减去完成人数再减去正在办理的人数
                Integer waitPeople = stationPeopleOutput.getTakeCount()-stationPeopleOutput.getCompCount();
                waitingNumberOutput.setNumber(waitPeople);
                waitingNumberOutput.setOrganizationName(stationPeopleOutput.getOrgName());

                waitingNumberOutputs.add(waitingNumberOutput);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return waitingNumberOutputs;
    }

}
