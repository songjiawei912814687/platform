package com.common.request;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

public class Audit {

    public static boolean apply(LoadBalancerClient loadBalancerClient, Integer resourceId, Integer applicantId, Integer approvalType) {
        PageData pageData = new PageData();
        pageData.put("resourceId", resourceId.toString());
        pageData.put("applicantId", applicantId.toString());
        pageData.put("approvalType", approvalType.toString());
        pageData.put("successivelyLevel", "0");
        var result = ServiceCall.getResult(loadBalancerClient, "/auditNew/applyNew", "bigdata", pageData);
        if (result == null) {
            return false;
        }
        if (result.getCode() == 200) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean applyApproveRule(LoadBalancerClient loadBalancerClient, Integer resourceId, Integer applicantId, Integer approvalType,Integer successivelyLevel) {
        PageData pageData = new PageData();
        pageData.put("resourceId", resourceId.toString());
        pageData.put("applicantId", applicantId.toString());
        pageData.put("approvalType", approvalType.toString());
        pageData.put("successivelyLevel", successivelyLevel.toString());
        var result = ServiceCall.getResult(loadBalancerClient, "/auditNew/applyNewApproveRule", "bigdata", pageData);
        if (result == null) {
            return false;
        }
        if (result.getCode() == 200) {
            return true;
        } else {
            return false;
        }
    }


    public  static boolean deleteByResourceId(LoadBalancerClient loadBalancerClient,String id,Integer approvalType){
        PageData pageData = new PageData();
        pageData.put("id", id);
        pageData.put("approvalType", approvalType.toString());

        ResponseResult result = ServiceCall.getResult(loadBalancerClient,"/auditNew/deleteByResourceId","bigdata",pageData);
        if(result.getCode()==200){
            return true;
        }
        return false;
    }
}
