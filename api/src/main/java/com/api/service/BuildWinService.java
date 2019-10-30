package com.api.service;

import com.alibaba.fastjson.JSONArray;
import com.api.largerScreen.Service1Locator;
import com.api.largerScreen.Service1Soap_PortType;
import com.api.mapper.jpa.BuildWinRepository;
import com.api.mapper.mybatis.BuildWinMapper;
import com.api.model.BuildWin;
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
 * @description: 建设工程中标公告接口设计
 * @date: Created in 2018-12-07  17:18
 * @modified by:
 */
@Service
public class BuildWinService {

    @Autowired
    private BuildWinRepository buildWinRepository;
    @Autowired
    private BuildWinMapper buildWinMapper;

    @Value("${jianyi.token}")
    private String token;

    public int getBuildWin() throws ServiceException, RemoteException {

        Service1Locator locator = new Service1Locator();
        Service1Soap_PortType service = locator.getService1Soap();
        String str2 = service.getProjectAfficheWinBidList(null,token);
        try {
            str2 = JsonUtils.xml2Json(str2).toString();
            Map<String,Object> jsonObject=com.alibaba.fastjson.JSONObject.parseObject(str2);
            Map<String,Object> str = (Map<String, Object>) jsonObject.get("NewDataSet");

            try {
                Map<String, Object> table1 = (Map<String, Object>) str.get("table1");
                if (table1 == null) {
                    return -1;
                }
                String id = (String) table1.get("ID");
                String tenderName = (String) table1.get("TenderName");
                String personnels = (String) table1.get("Personnels");
                String publishStartTime = (String) table1.get("PublishStartTime");
                String publishEndTime = (String) table1.get("PublishEndTime");


                String startTime = DealDateFormatUtil.dealDate(publishStartTime);
                String endTime = DealDateFormatUtil.dealDate(publishEndTime);
                BigDecimal WinPrice =new  BigDecimal(table1.get("WinPrice").toString());
//                BigDecimal WinPrice = (BigDecimal) table1.get("WinPrice");
                String enterpriseName = (String) table1.get("EnterpriseName");

                BuildWin buildWin = new BuildWin();
                buildWin.setId(id);
                buildWin.setTenderName(tenderName);
                buildWin.setPersonnels(personnels);
                buildWin.setPublishStartTime(startTime);
                buildWin.setPublishEndTime(endTime);
                buildWin.setWinPrice(WinPrice);
                buildWin.setEnterpriseName(enterpriseName);
                buildWin.setCreatedDateTime(new Date());
                String result = buildWinRepository.save(buildWin).getId();
                if (result == null) {
                    return -1;
                }
                return 1;
            }catch (ClassCastException e){

                List<BuildWin> buildWinList = Lists.newArrayList();
                JSONArray tableItems = (JSONArray) str.get("table1");
                if (tableItems == null) {
                    return -1;
                }

                for(int i=0;i<tableItems.size();i++){
                    Map<String, Object> table1 = (Map<String, Object>) tableItems.get(i);
                    String id = (String) table1.get("ID");
                    String tenderName = (String) table1.get("TenderName");
                    String personnels = (String) table1.get("Personnels");
                    String publishStartTime = (String) table1.get("PublishStartTime");
                    String publishEndTime = (String) table1.get("PublishEndTime");


                    String startTime = DealDateFormatUtil.dealDate(publishStartTime);
                    String endTime = DealDateFormatUtil.dealDate(publishEndTime);

                    BigDecimal WinPrice =new  BigDecimal(table1.get("WinPrice").toString());
                    String enterpriseName = (String) table1.get("EnterpriseName");

                    BuildWin buildWin = new BuildWin();
                    buildWin.setId(id);
                    buildWin.setTenderName(tenderName);
                    buildWin.setPersonnels(personnels);
                    buildWin.setPublishStartTime(startTime);
                    buildWin.setPublishEndTime(endTime);
                    buildWin.setWinPrice(WinPrice);
                    buildWin.setEnterpriseName(enterpriseName);
                    buildWin.setCreatedDateTime(new Date());

                    buildWinList.add(buildWin);
                }

                Integer result = buildWinRepository.saveAll(buildWinList).size();
                if(result<0){
                    return -1;
                }
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public List<BuildWin> findPageList(HttpServletRequest request){

        PageData pageData = new PageData(request);
        PageHelper.startPage(pageData.getPageIndex(),pageData.getRows());
        List<BuildWin> buildWinList = buildWinMapper.selectAll(pageData);
        if(buildWinList == null){
            return null;
        }
        return buildWinList;
    }
}
