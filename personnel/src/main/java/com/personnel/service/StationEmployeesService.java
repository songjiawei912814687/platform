package com.personnel.service;

import com.alibaba.fastjson.JSONObject;
import com.personnel.mapper.jpa.StationEmployeesRepository;
import com.personnel.model.StationEmployees;
import com.personnel.util.PostUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StationEmployeesService {
    @Autowired
    StationEmployeesRepository stationEmployeesRepository;

    public void saveAcc(){
        String url = "http://smrz.zjtax.gov.cn:8082/taxService/service/taxNsfwCommonApp!doService";
        String charset = "GBK";
        List<StationEmployees> list = stationEmployeesRepository.findAllByState(0);
        for(StationEmployees stationEmployees:list){
            Map<String,String> params = new HashMap<String,String>();
            params.put("domain.ywId","nsfw.saveacc");
            Map<String,String> parmJson = new HashMap<String,String>();
            parmJson.put("bid","nsfw.saveacc");
            parmJson.put("ver","1.0");
            parmJson.put("govhallid","133011002");//由税务局配置
            parmJson.put("ID",stationEmployees.getId().toString());
            parmJson.put("Acccode",stationEmployees.getEmployeeNo());
            parmJson.put("Accname",stationEmployees.getName());
            parmJson.put("yxbz",stationEmployees.getAmputated()==0 ? "Y":"N");
            String parm = JSONObject.toJSONString(parmJson);
            params.put("domain.ParmJson", parm);
            String res = PostUtil.doPost(url,params,charset,true);
            System.out.println("------------------------------------"+res);
            //把数据中间表更新为已上传
            stationEmployees.setState(1);
            stationEmployeesRepository.save(stationEmployees);
        }
    }
}
