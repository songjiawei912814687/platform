package com.api.mapper.mybatis;

import com.api.domain.output.QltQlsxOutput;
import com.api.model.QltQlsx;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface QltQlsxMapper {
    int deleteByPrimaryKey(String id);

    int insert(QltQlsxOutput record);

    int insertSelective(QltQlsxOutput record);

    QltQlsxOutput selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(QltQlsxOutput record);

    int updateByPrimaryKey(QltQlsx record);

    int selectCount();

    List<QltQlsxOutput> selectPage(PageData pageData);


    /**查询出所有的热门事项*/
    List<QltQlsxOutput> selectByHot();

}