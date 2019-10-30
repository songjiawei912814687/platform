package com.screen.service;


import com.common.response.ResponseResult;
import com.common.utils.BigDecimalUtil;
import com.google.common.collect.Lists;
import com.screen.domain.output.HotDoOutput;
import com.screen.mapper.mybatis.FeedbackInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: Young
 * @description: 热门事项办件
 * @date: Created in 15:45 2018/11/20
 * @modified by:
 */
@Service
public class HotDoService {

    @Autowired
    private FeedbackInfoMapper feedbackInfoMapper;

    public ResponseResult findPageList(){

        List<HotDoOutput> hotList = null;
        try {
            List<HotDoOutput> hotDoOutputList = feedbackInfoMapper.selectHotDo();
            if(hotDoOutputList.size() == 0 || hotDoOutputList == null){
                return ResponseResult.success(0);
            }
            hotList = Lists.newArrayList();
            for(int i=0;i<hotDoOutputList.size();i++){
                HotDoOutput hotDoOutput = new HotDoOutput();
                hotDoOutput.setCount(hotDoOutputList.get(i).getCount());
                hotDoOutput.setQlNames(hotDoOutputList.get(i).getQlNames());
                if(i==0){
                    hotDoOutput.setPoint(new BigDecimal(100));
                    hotList.add(hotDoOutput);
                }else {
                    BigDecimal point = BigDecimalUtil.div(hotDoOutputList.get(i).getCount().doubleValue(),
                            hotDoOutputList.get(0).getCount().doubleValue());
                    BigDecimal allPoint = BigDecimalUtil.mul(point.doubleValue(),100);
                    hotDoOutput.setPoint(allPoint);
                    hotList.add(hotDoOutput);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.success(hotList);
    }




}
