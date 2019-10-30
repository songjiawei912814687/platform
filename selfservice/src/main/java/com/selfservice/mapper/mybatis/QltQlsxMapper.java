package com.selfservice.mapper.mybatis;

import com.common.model.PageData;
import com.selfservice.core.base.BaseMapper;
import com.selfservice.core.base.MybatisBaseMapper;
import com.selfservice.domain.output.QltQlsxOutput;
import com.selfservice.model.QltQlsx;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface QltQlsxMapper {
    int deleteByPrimaryKey(String qlFullId);

    int insert(QltQlsxOutput record);

    int insertSelective(QltQlsxOutput record);

    QltQlsxOutput selectByPrimaryKey(String qlInnerCode);

    int updateByPrimaryKeySelective(QltQlsxOutput record);

    int updateByPrimaryKey(QltQlsx record);

    int selectCount();

    //根据组织编号来查
    List<QltQlsxOutput> selectByOrgaNo(String organo);

    //根据行业编码来查
    List<QltQlsxOutput> selectByHangyeType(String hangyeType);

    List<QltQlsxOutput> selectPage(PageData pageData);


    /**获取最小颗粒**/
    List<QltQlsxOutput> selectHotQlName();


    /**获取行业主题名称**/
    List<QltQlsxOutput> selectHangName();

    /**获取最小颗粒列表**/
    List<QltQlsxOutput> selectMini(PageData pageData);


    /**获取组织名称*/
    List<QltQlsxOutput> selectDepart();

    List<QltQlsxOutput> selectPage1(PageData pageData);
}
