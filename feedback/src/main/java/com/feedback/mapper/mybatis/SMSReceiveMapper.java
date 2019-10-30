package com.feedback.mapper.mybatis;


import com.common.model.PageData;
import com.feedback.core.base.MybatisBaseMapper;
import com.feedback.domain.output.SMSReceiveOutPut;
import com.feedback.model.SMSReceive;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SMSReceiveMapper extends MybatisBaseMapper<SMSReceiveOutPut> {
    int deleteByPrimaryKey(Integer id);

    int insert(SMSReceive record);

    int insertSelective(SMSReceive record);

    @Override
    SMSReceiveOutPut selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SMSReceive record);

    int updateByPrimaryKey(SMSReceive record);

    /**根据手机号码和类型来查看回复的短信内容*/
    SMSReceiveOutPut selectByPhone(@Param("phone") String phone, @Param("type") Integer type);

    /**根据手机号码和类型来查看回复的短信内容以及回复的时间需要大于投诉建议的最后跟新时间*/
    SMSReceiveOutPut selectVisit(PageData pageData);


    /**
     * 根据sendId查询回复的短信
     * @param sendId 发送短信的id
     * @return
     */
    SMSReceiveOutPut selectBySendId(Integer sendId);

}
