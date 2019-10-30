package com.api.service;

import com.alibaba.fastjson.JSONArray;
import com.api.largerScreen.Service1Locator;
import com.api.largerScreen.Service1Soap_PortType;
import com.api.mapper.jpa.LighthouseRepository;
import com.api.mapper.mybatis.LighthouseMapper;
import com.api.model.Lighthouse;
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
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: young
 * @project_name: svn
 * @description: 曝光台接口
 * @date: Created in 2018-12-24  10:08
 * @modified by:
 */
@Service
public class LighthouseService  {

    @Autowired
    private LighthouseRepository repository;
    @Autowired
    private LighthouseMapper lighthouseMapper;

    @Value("${jianyi.token}")
    private String token;

    public int getLightHouse() throws ServiceException, RemoteException {
        Service1Locator locator = new Service1Locator();
        Service1Soap_PortType service = locator.getService1Soap();
        String str5 = service.getExposureNews(null,token);
        try {
            str5 = JsonUtils.xml2Json(str5).toString();
            Map<String,Object> jsonObject=com.alibaba.fastjson.JSONObject.parseObject(str5);
            Map<String,Object> str = (Map<String, Object>) jsonObject.get("NewDataSet");
            try {
                Map<String, Object> table1 = (Map<String, Object>) str.get("table1");
                if (table1 == null) {
                    return -1;
                }
                Lighthouse lighthouse = new Lighthouse();
                String classValue = (String) table1.get("ClassValue");
                String title = (String) table1.get("title");

                String publishStartTime = (String) table1.get("PublishStartTime");
                String startTime = DealDateFormatUtil.dealDate(publishStartTime);

                lighthouse.setClassValue(classValue);
                lighthouse.setTitle(title);
                lighthouse.setPublishStartTime(startTime);
                lighthouse.setCreatedDateTime(new Date());

                Integer result = repository.save(lighthouse).getId();
                if (result < 0) {
                    return -1;
                }
                return result;
            }catch (ClassCastException e){
                JSONArray tableItems = (JSONArray) str.get("table1");

                List<Lighthouse> lighthouseList = Lists.newArrayList();
                if (tableItems == null) {
                    return -1;
                }
                for(int i=0;i<tableItems.size();i++){
                    Map<String, Object> table1 = (Map<String, Object>) tableItems.get(i);

                    Lighthouse lighthouse = new Lighthouse();
                    String classValue = (String) table1.get("ClassValue");
                    String title = (String) table1.get("title");

                    String publishStartTime = (String) table1.get("PublishStartTime");
                    String startTime = DealDateFormatUtil.dealDate(publishStartTime);

                    lighthouse.setClassValue(classValue);
                    lighthouse.setTitle(title);
                    lighthouse.setPublishStartTime(startTime);
                    lighthouse.setCreatedDateTime(new Date());

                    lighthouseList.add(lighthouse);
                }

                Integer result = repository.saveAll(lighthouseList).size();
                if(result<0){
                    return -1;
                }
                return 1;
            }
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public List<Lighthouse> findPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        PageHelper.startPage(pageData.getPageIndex(),pageData.getRows());

        List<Lighthouse> lighthouseList = lighthouseMapper.selectAll(pageData);
        if(lighthouseList == null){
            return null;

        }
        return lighthouseList;
    }
}
