package com.screenData.mapper.mybatis;

import com.common.model.PageData;
import com.screenData.core.base.MybatisBaseMapper;
import com.screenData.domain.output.OrganizationOutput;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface OrganizationMapper extends MybatisBaseMapper<OrganizationOutput> {

    String selectNameById(Integer id);

    List<OrganizationOutput> getByParentId(Integer parentId);

    List<Integer> selectChildById(Integer id);

    @Override
    List<OrganizationOutput> selectAll(PageData pageData);

    /**根据组织编号查询出组织对象*/
    OrganizationOutput selectByOrgaNo(String orgaNo);


    List<OrganizationOutput> selectByOrgaNoList(@Param("orgaNoList") List<String> orgaNoList);
}
