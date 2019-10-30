package com.api.service;

import com.alibaba.fastjson.JSONArray;
import com.api.largerScreen.Service1Locator;
import com.api.largerScreen.Service1Soap_PortType;
import com.api.mapper.jpa.SmallProjectResultRepository;
import com.api.mapper.mybatis.SmallProjectResultMapper;
import com.api.model.SmallProjectResult;
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
 * @description:  2.4	获取小额工程成交结果公示接口设计
 * @date: Created in 2018-12-13  09:54
 * @modified by:
 */
@Service
public class SmallProjectResultService {

    @Autowired
    private SmallProjectResultRepository repository;
    @Autowired
    private SmallProjectResultMapper smallProjectResultMapper;

    @Value("${jianyi.token}")
    private String token;

    public int getSmallProjectResult() throws ServiceException, RemoteException {
        Service1Locator locator = new Service1Locator();
        Service1Soap_PortType service = locator.getService1Soap();
        String str4 = service.getSmallProjectAfficheWinBidList(null,token);

        try {
            str4 = JsonUtils.xml2Json(str4).toString();
            Map<String, Object> jsonObject = com.alibaba.fastjson.JSONObject.parseObject(str4);
            Map<String, Object> str = (Map<String, Object>) jsonObject.get("NewDataSet");

            try {
                Map<String, Object> table1 = (Map<String, Object>) str.get("table1");
                if (table1 == null) {
                    return -1;
                }
                String id = (String) table1.get("ID");
                String projectName = (String) table1.get("ProjectName");
                String getUnit = (String) table1.get("GetUnit");
                BigDecimal bidCost = new BigDecimal( table1.get("BidCost").toString());

                String publishStartTime = (String) table1.get("PublishStartTime");
                String publishEndTime = (String) table1.get("PublishEndTime");

                String startTime = DealDateFormatUtil.dealDate(publishStartTime);
                String endTime = DealDateFormatUtil.dealDate(publishEndTime);

                SmallProjectResult smallProjectResult = new SmallProjectResult();
                smallProjectResult.setId(id);
                smallProjectResult.setProjectName(projectName);
                smallProjectResult.setGetUnit(getUnit);
                smallProjectResult.setBidCost(bidCost);
                smallProjectResult.setPublishStartTime(startTime);
                smallProjectResult.setPublishEndTime(endTime);
                smallProjectResult.setCreatedDateTime(new Date());
                String result = repository.save(smallProjectResult).getId();
                if (result == null) {
                    return -1;
                }
                return 1;
            }catch (ClassCastException e){
                List<SmallProjectResult> smallProjectResultList = Lists.newArrayList();
                JSONArray tableItems = (JSONArray) str.get("table1");
                if (tableItems == null) {
                    return -1;
                }

                for(int i=0;i<tableItems.size();i++){
                    Map<String, Object> table1 = (Map<String, Object>) tableItems.get(i);
                    String id = (String) table1.get("ID");
                    String projectName = (String) table1.get("ProjectName");
                    String getUnit = (String) table1.get("GetUnit");
//                    BigDecimal bidCost = (BigDecimal) table1.get("BidCost");
                    BigDecimal bidCost = new BigDecimal( table1.get("BidCost").toString());
                    String publishStartTime = (String) table1.get("PublishStartTime");
                    String publishEndTime = (String) table1.get("PublishEndTime");

                    String startTime = DealDateFormatUtil.dealDate(publishStartTime);
                    String endTime = DealDateFormatUtil.dealDate(publishEndTime);

                    SmallProjectResult smallProjectResult = new SmallProjectResult();
                    smallProjectResult.setId(id);
                    smallProjectResult.setProjectName(projectName);
                    smallProjectResult.setGetUnit(getUnit);
                    smallProjectResult.setBidCost(bidCost);
                    smallProjectResult.setPublishStartTime(startTime);
                    smallProjectResult.setPublishEndTime(endTime);
                    smallProjectResult.setCreatedDateTime(new Date());

                    smallProjectResultList.add(smallProjectResult);
                }

                Integer result  = repository.saveAll(smallProjectResultList).size();
                if (result == null) {
                    return -1;
                }
                return 1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    public List<SmallProjectResult> findPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        PageHelper.startPage(pageData.getPageIndex(),pageData.getRows());

        List<SmallProjectResult> smallProjectResultList = smallProjectResultMapper.selectAll(pageData);
        if(smallProjectResultList == null){
            return null;
        }
        return smallProjectResultList;
    }
}
