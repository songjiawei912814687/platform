package com.screen.mapper.mybatis;

import com.common.model.PageData;
import com.screen.core.base.MybatisBaseMapper;
import com.screen.domain.output.OrganizationOutput;
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
