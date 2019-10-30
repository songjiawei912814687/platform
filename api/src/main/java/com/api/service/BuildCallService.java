package com.api.service;

import com.alibaba.fastjson.JSONArray;
import com.api.largerScreen.Service1Locator;
import com.api.largerScreen.Service1Soap_PortType;
import com.api.mapper.jpa.BuildCallRepository;
import com.api.mapper.mybatis.BuildCallMapper;
import com.api.model.BuildCall;
import com.common.model.PageData;
import com.common.utils.DealDateFormatUtil;
import com.common.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: young
 * @project_name: svn
 * @description:  建筑工程招标service
 * @date: Created in 2018-12-07  16:38
 * @modified by:
 */
@Service
public class BuildCallService {

    @Autowired
    private BuildCallRepository buildCallRepository;
    @Autowired
    private BuildCallMapper buildCallMapper;

    @Value("${jianyi.token}")
    private String token;

    public int getBuildCall() throws ServiceException, RemoteException {

        Service1Locator locator = new Service1Locator();
        Service1Soap_PortType service = locator.getService1Soap();
        String str1 = service.getProjectAfficheList(null,token);
        try {
            //将xml转化成json
            str1 = JsonUtils.xml2Json(str1).toString();
            Map<String,Object> jsonObject=com.alibaba.fastjson.JSONObject.parseObject(str1);
            Map<String,Object> str = (Map<String, Object>) jsonObject.get("NewDataSet");

            try {
                Map<String, Object> table1 = (Map<String, Object>) str.get("table1");
                if (table1 == null) {
                    return -1;
                }
                //开始获取value
                String id = (String) table1.get("ID");
                String tenderName = (String) table1.get("TenderName");
                String tendererName = (String) table1.get("TendererName");
                BigDecimal thisBudget = new BigDecimal(table1.get("ThisBudget").toString());
                String openBidTime = (String) table1.get("OpenBidTime");
                String publishEndTime = (String) table1.get("PublishEndTime");
                //解析时间格式2016-09-03T00:00:00.000+08:00
                String startTime = DealDateFormatUtil.dealDate(openBidTime);
                String endTime = DealDateFormatUtil.dealDate(publishEndTime);

                //创建对象
                BuildCall buildCall = new BuildCall();
                buildCall.setId(id);
                buildCall.setTenderName(tenderName);
                buildCall.setTendererName(tendererName);
                buildCall.setThisBudget(thisBudget);
                buildCall.setOpenBidTime(startTime);
                buildCall.setPublishEndTime(endTime);
                buildCall.setCreatedDateTime(new Date());
                //保存
                String result = buildCallRepository.save(buildCall).getId();
                if (result == null) {
                    return -1;
                }
                return 1;
            } catch (ClassCastException e){
                JSONArray tableItems = (JSONArray) str.get("table1");
                if(tableItems == null){
                    return -1;
                }
                List<BuildCall> buildCallList = Lists.newArrayList();
                for(int i=0;i<tableItems.size();i++){
                    Map<String, Object> table1 = (Map<String, Object>) tableItems.get(i);
                    //开始获取value
                    String id = (String) table1.get("ID");
                    String tenderName = (String) table1.get("TenderName");
                    String tendererName = (String) table1.get("TendererName");
                    BigDecimal thisBudget = new BigDecimal(table1.get("ThisBudget").toString());
//                    BigDecimal thisBudget = (BigDecimal) table1.get("ThisBudget");
                    String openBidTime = (String) table1.get("OpenBidTime");
                    String publishEndTime = (String) table1.get("PublishEndTime");
                    //解析时间格式2016-09-03T00:00:00.000+08:00
                    String startTime = DealDateFormatUtil.dealDate(openBidTime);
                    String endTime = DealDateFormatUtil.dealDate(publishEndTime);

                    //创建对象
                    BuildCall buildCall = new BuildCall();
                    buildCall.setId(id);
                    buildCall.setTenderName(tenderName);
                    buildCall.setTendererName(tendererName);
                    buildCall.setThisBudget(thisBudget);
                    buildCall.setOpenBidTime(startTime);
                    buildCall.setPublishEndTime(endTime);
                    buildCall.setCreatedDateTime(new Date());

                    buildCallList.add(buildCall);
                }

                Integer result = buildCallRepository.saveAll(buildCallList).size();
                if(result<0){
                    return -1;
                }
                return 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    public List<BuildCall> findPageList(HttpServletRequest request){
        PageData pageData = new PageData();
        PageHelper.startPage(pageData.getRows(),pageData.getRows());
        List<BuildCall> buildCallList = buildCallMapper.selectAll(pageData);
        if(buildCallList == null || buildCallList.size() == 0){
            return null;
        }
        return buildCallList;
    }
}
