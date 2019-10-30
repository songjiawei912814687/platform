package com.api.service;

import com.alibaba.fastjson.JSONArray;
import com.api.largerScreen.Service1Locator;
import com.api.largerScreen.Service1Soap_PortType;
import com.api.mapper.jpa.SmallProjectTransRepository;
import com.api.mapper.mybatis.SmallProjectTransMapper;
import com.api.model.SmallProjectTrans;
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
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: young
 * @project_name: svn
 * @description: 小额工程交易公告
 * @date: Created in 2018-12-07  17:46
 * @modified by:
 */
@Service
public class SmallProjectTransService  {

    @Autowired
    private SmallProjectTransRepository repository;
    @Autowired
    private SmallProjectTransMapper smallProjectTransMapper;

    @Value("${jianyi.token}")
    private String token;

    public int getSmallProjectTrans() throws ServiceException, IOException {
        Service1Locator locator = new Service1Locator();
        Service1Soap_PortType service = locator.getService1Soap();
        String str3 = service.getSmallProjectAfficheList(null,token);
        try {
            str3 = JsonUtils.xml2Json(str3).toString();
            Map<String, Object> jsonObject = com.alibaba.fastjson.JSONObject.parseObject(str3);
            Map<String, Object> str = (Map<String, Object>) jsonObject.get("NewDataSet");

            try {
                Map<String, Object> table1 = (Map<String, Object>) str.get("table1");
                if (table1 == null) {
                    return -1;
                }

                SmallProjectTrans smallProjectTrans = new SmallProjectTrans();

                String id = (String) table1.get("ID");
                String projectName = (String) table1.get("ProjectName");
                String tenderUnit = (String) table1.get("TenderUnit");
                BigDecimal budgtaryPrice = (BigDecimal) table1.get("BudgtaryPrice");

                String bidTime = (String) table1.get("BidTime");
                String publishEndTime = (String) table1.get("PublishEndTime");

                String startTime = DealDateFormatUtil.dealDate(bidTime);
                String endTime = DealDateFormatUtil.dealDate(publishEndTime);


                smallProjectTrans.setId(id);
                smallProjectTrans.setProjectName(projectName);
                smallProjectTrans.setTenderUnit(tenderUnit);
                smallProjectTrans.setBudgtaryPrice(budgtaryPrice);
                smallProjectTrans.setBidTime(startTime);
                smallProjectTrans.setPublishEndTime(endTime);
                smallProjectTrans.setCreatedDateTime(new Date());
                String result = repository.save(smallProjectTrans).getId();
                if (result == null) {
                    return -1;
                }
                return 1;
            }catch (ClassCastException e){

                List<SmallProjectTrans> smallProjectTransList = Lists.newArrayList();
                JSONArray tableItems = (JSONArray) str.get("table1");
                if (tableItems == null) {
                    return -1;
                }
                for(int i=0;i<tableItems.size();i++){
                    Map<String, Object> table1 = (Map<String, Object>) tableItems.get(i);
                    SmallProjectTrans smallProjectTrans = new SmallProjectTrans();

                    String id = (String) table1.get("ID");
                    String projectName = (String) table1.get("ProjectName");
                    String tenderUnit = null;
                    if(table1.get("TenderUnit") != null){
                        tenderUnit = (String) table1.get("TenderUnit");
                        smallProjectTrans.setTenderUnit(tenderUnit);
                    }

//                    BigDecimal budgtaryPrice = (BigDecimal) table1.get("BudgtaryPrice");
                    BigDecimal budgtaryPrice = new BigDecimal( table1.get("BudgtaryPrice").toString());

                    String startTime = null;
                    String endTime = null;
                    if(table1.get("BidTime")!=null){
                        String bidTime = (String) table1.get("BidTime");
                        startTime = DealDateFormatUtil.dealDate(bidTime);
                    }
                    if(table1.get("PublishEndTime")!=null){
                        String publishEndTime = (String) table1.get("PublishEndTime");
                        endTime = DealDateFormatUtil.dealDate(publishEndTime);
                    }

                    smallProjectTrans.setId(id);
                    smallProjectTrans.setProjectName(projectName);
                    smallProjectTrans.setBudgtaryPrice(budgtaryPrice);
                    smallProjectTrans.setBidTime(startTime);
                    smallProjectTrans.setPublishEndTime(endTime);
                    smallProjectTrans.setCreatedDateTime(new Date());

                    smallProjectTransList.add(smallProjectTrans);
                }

                Integer result = repository.saveAll(smallProjectTransList).size();
                if(result<0){
                    return -1;
                }
                return 1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public List<SmallProjectTrans> findPageList(HttpServletRequest request){
        PageData pageDate = new PageData(request);
        PageHelper.startPage(pageDate.getPageIndex(),pageDate.getRows());

        List<SmallProjectTrans> smallProjectTransList = smallProjectTransMapper.selectAll(pageDate);
        if(smallProjectTransList == null){
            return null;
        }
        return smallProjectTransList;
    }
}
