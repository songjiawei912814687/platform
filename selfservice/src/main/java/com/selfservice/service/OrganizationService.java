package com.selfservice.service;

import com.common.model.PageData;
import com.selfservice.core.base.BaseMapper;
import com.selfservice.core.base.BaseService;
import com.selfservice.core.base.MybatisBaseMapper;
import com.selfservice.domain.output.OrganizationOutput;
import com.selfservice.mapper.jpa.OrganizationRepository;
import com.selfservice.mapper.mybatis.OrganizationMapper;
import com.selfservice.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizationService extends BaseService<OrganizationOutput, Organization,Integer> {

    @Autowired
    private OrganizationRepository repository;
    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public BaseMapper<Organization, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<OrganizationOutput> getMybatisBaseMapper() {
        return organizationMapper;
    }

    @Override
    public List<OrganizationOutput> selectAll(boolean isPage, PageData pageData){
        return super.selectAll(isPage,pageData);
    }

    public List<Organization> getByName(Organization organization){
        List<Organization> byNameAndIsDelete = repository.findByNameAndAmputated(organization.getName(), organization.getAmputated());
        return  byNameAndIsDelete;
    }

    //所有的组织数据（用于列表）
    public List<OrganizationOutput> Assembly(List<Organization> all) {
        ArrayList<OrganizationOutput> parentArray = new ArrayList<>();
        if(all==null){
            return null;
        }
        for (Organization o:all) {
            if(o.getParentId()==0){
                parentArray.add(new OrganizationOutput(o));
            }
        }
        return getTreeData(all,parentArray);
    }
    public List<OrganizationOutput> getTreeData(List<Organization> list, List<OrganizationOutput> parents) {
        for (OrganizationOutput parentOrg:parents) {
            List<OrganizationOutput> childs = new ArrayList<>();

            for (Organization o:list) {
                if(parentOrg.getId().equals(o.getParentId())){
                    childs.add(new OrganizationOutput(o));
                }
            }
            parentOrg.setChildren(childs.size()==0?null:childs);
            if(childs.size()>0){
                getTreeData(list,childs)  ;
            }
        }
        return parents;
    }


    //组织名称搜索
    public List<Organization> getList(PageData pageData) {
        //1、先获得根据名称查找后的结果集
        List<Organization> returnList = new ArrayList<>();
        Organization organization = new Organization();
        organization.setName("%"+pageData.GetParameter("name")+"%");
        List<Organization>  list  =organizationMapper.getByName(organization);
        //2、循环遍历根据path获得所有的元素
        StringBuffer path = new StringBuffer("");
        for (Organization o:list) {
            path.append(o.getPath()+",");

        }
        if(path.length()==0){
            return null;
        }else{
            String pahtval = path.toString().substring(0,path.length()-1);
            List<Organization> organizations = organizationMapper.getByPath(pahtval);
            returnList.addAll(organizations);
        }
        return  returnList;
    }

}
