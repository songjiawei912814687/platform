package com.message.mapper.mybatis;

import com.common.model.PageData;
import com.message.core.base.MybatisBaseMapper;
import com.message.domain.output.SMSSendOutput;
import com.message.model.SMSSend;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SMSSendMapper extends MybatisBaseMapper<SMSSendOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(SMSSend record);

    int insertSelective(SMSSend record);

    SMSSendOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SMSSend record);

    int updateByPrimaryKey(SMSSend record);

    /**
     * 根据发送的号码查询出发送人的姓名和发送编号和业务类型,发送时间
     * @param number
     * @return
     */
    SMSSend selectByNumber(@Param("number") String number, @Param("sendTime") String sendTime);


    List<SMSSendOutput> selectNotPage(PageData pageData);
}
