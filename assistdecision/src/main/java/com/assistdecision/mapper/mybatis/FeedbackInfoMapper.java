package com.assistdecision.mapper.mybatis;

import com.assistdecision.core.base.MybatisBaseMapper;
import com.assistdecision.domain.output.FeedbackInfoOutput;
import com.common.model.PageData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedbackInfoMapper extends MybatisBaseMapper<FeedbackInfoOutput> {

    List<FeedbackInfoOutput> selectBusiness();

    /**
     * 每月总办件量
     * @param date
     * @return
     */
    FeedbackInfoOutput selectWindowDo(String date);


    /**
     * 查看热门事项
     * @param date
     * @return
     */
    List<FeedbackInfoOutput> selectHotDo(String date);

    /**
     * 查询出各部门已评价总数
     * @return
     */
    List<FeedbackInfoOutput> selectAllCount();

    /**
     * 根据组织id查询出满意数量
     * @param orgaId
     * @return
     */
    Integer selectSatisByOrgaId(Integer orgaId);

    Integer selectSatisByPageData(PageData pageData);

    /**
     * 根据组织id查询出跑一次数量
     * @param orgaId
     * @return
     */
    Integer selectCompByOrgaId(Integer orgaId);

    List<FeedbackInfoOutput> selectAllCountAndDept(PageData pageData);

    Integer selectUnSatisByPageData(PageData pageData);

    /**
     * 根据某个部门查询出下面各个事项的各个情况的件数
     * @param orgId
     * @return
     */
    int selectCompAndsatisByOrgaId(@Param("orgId") Integer orgId,
                                                  @Param("matters") String matter,
                                                        @Param("content")String content);

    List<FeedbackInfoOutput> selectAllMaters(PageData pageData);
}