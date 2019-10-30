package com.api.service;

import com.alibaba.fastjson.JSONArray;
import com.api.largerScreen.Service1Locator;
import com.api.largerScreen.Service1Soap_PortType;
import com.api.mapper.jpa.NoticeBoardRepository;
import com.api.mapper.mybatis.NoticeBoardMapper;
import com.api.model.NoticeBoard;
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
 * @description:
 * @date: Created in 2018-12-24  10:27
 * @modified by:
 */
@Service
public class NoticeBoardService  {

    @Autowired
    private NoticeBoardRepository repository;
    @Autowired
    private NoticeBoardMapper noticeBoardMapper;

    @Value("${jianyi.token}")
    private String token;

    public int getNoticeBoard() throws ServiceException, RemoteException {
        Service1Locator locator = new Service1Locator();
        Service1Soap_PortType service = locator.getService1Soap();
        String str6 = service.getPublishInfo(null,token);

        try{
            str6 = JsonUtils.xml2Json(str6).toString();
            Map<String,Object> jsonObject=com.alibaba.fastjson.JSONObject.parseObject(str6);
            Map<String,Object> str = (Map<String, Object>) jsonObject.get("NewDataSet");

            try{
                Map<String,Object> table1= (Map<String, Object>) str.get("table1");
                if(table1 == null){
                    return -1;
                }

                String enterpriseName = null;

                String startTime = null;
                NoticeBoard noticeBoard = new NoticeBoard();
                if(table1.get("EnterpriseName")!=null){
                    enterpriseName = (String) table1.get("EnterpriseName");
                }

                if(table1.get("EnterpriseCode")!=null){
                    var enterpriseCode = table1.get("EnterpriseCode");
                    noticeBoard.setEnterpriseCode(String.valueOf(enterpriseCode));
                }


                if(table1.get("PublishTime")!=null){
                    String publishTime = (String) table1.get("PublishTime");
                    startTime = DealDateFormatUtil.dealDate(publishTime);
                }


                noticeBoard.setEnterpriseName(enterpriseName);

                noticeBoard.setPublishTime(startTime);
                noticeBoard.setCreatedDateTime(new Date());

                Integer result = repository.save(noticeBoard).getId();

                if(result < 0){
                    return -1;
                }
                return result;
            }catch (ClassCastException e){

                List<NoticeBoard> noticeBoardList = Lists.newArrayList();
                JSONArray table1 = (JSONArray) str.get("table1");
                for(int i=0;i<table1.size();i++){
                    Map<String,Object> tableItem= (Map<String, Object>) table1.get(i);
                    String enterpriseName =null;
                    String startTime = null;
                    NoticeBoard noticeBoard = new NoticeBoard();
                    if(tableItem.get("EnterpriseName")!=null){
                         enterpriseName = (String) tableItem.get("EnterpriseName");
                    }
                    if(tableItem.get("EnterpriseCode")!=null){
                         var enterpriseCode = tableItem.get("EnterpriseCode");
                        noticeBoard.setEnterpriseCode(String.valueOf(enterpriseCode));
                    }
                    if(tableItem.get("PublishTime")!=null){
                        String publishTime = (String) tableItem.get("PublishTime");
                        startTime = DealDateFormatUtil.dealDate(publishTime);
                    }


                    noticeBoard.setEnterpriseName(enterpriseName);

                    noticeBoard.setPublishTime(startTime);
                    noticeBoard.setCreatedDateTime(new Date());

                    noticeBoardList.add(noticeBoard);
                }

                Integer result = repository.saveAll(noticeBoardList).size();
                if(result==0){
                    return -1;
                }
                return 1;
            }

        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public List<NoticeBoard> findPageList(HttpServletRequest request){

        PageData pageData = new PageData(request);
        PageHelper.startPage(pageData.getPageIndex(),pageData.getRows());
        List<NoticeBoard> noticeBoardList = noticeBoardMapper.selectAll(pageData);
        if(noticeBoardList == null){
            return null;
        }
        return noticeBoardList;
    }
}
