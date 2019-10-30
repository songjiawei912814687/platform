package com.stamp.service;

import com.common.model.PageData;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.stamp.core.base.BaseRepository;
import com.stamp.core.base.BaseService;
import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.output.EmployeesOutput;
import com.stamp.domain.output.StampOrganOutput;
import com.stamp.mapper.jpa.StampOrganRepository;
import com.stamp.mapper.mybatis.EmployeesMapper;
import com.stamp.mapper.mybatis.StampOrganMapper;
import com.stamp.model.StampOrgan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-04-18  10:27
 * @modified by:
 */
@Service
public class StampOrganService extends BaseService<StampOrganOutput, StampOrgan,Integer> {

    @Autowired
    private StampOrganRepository stampOrganRepository;
    @Autowired
    private StampOrganMapper stampOrganMapper;
    @Autowired
    private EmployeesMapper employeesMapper;

    @Override
    public BaseRepository<StampOrgan, Integer> getMapper() {
        return stampOrganRepository;
    }

    @Override
    public MybatisBaseMapper<StampOrganOutput> getMybatisBaseMapper() {
        return stampOrganMapper;
    }

    public Boolean findByName(String name){
        StampOrgan stampOrganByName = stampOrganRepository.findByNameAndAmputated(name,0);
        if(stampOrganByName==null){
            return false;
        }
        return true;
    }


    public String getMaxNo() {
        String organizationNo = stampOrganMapper.selectMaxNo();
        return organizationNo;
    }

    public boolean isRepeat(Integer id, String name) {
        StampOrgan stampOrgan = stampOrganRepository.getById(id);
        if (name.equals(stampOrgan.getName())) {
            return true;
        }
        return false;
    }

    //查看是否有子集
    public boolean isChild(StampOrgan stampOrgan) {
        StampOrgan stampOrgan1 = stampOrganMapper.selectByPrimaryKey(stampOrgan.getParentId());//获得要更改的上级组织的path
        if(stampOrgan1 == null || stampOrgan1.getPath() == null){
            return false;
        }
        //获得当前的组织的path
        StampOrgan stampOrgan2 = stampOrganMapper.selectByPrimaryKey(stampOrgan.getId());
        String f = stampOrgan1.getPath().trim();
        String ff = stampOrgan2.getPath().trim();
        if(f.indexOf(ff)==0){
            return true;
        }
        return false;
    }

    public int updateObj(StampOrgan byId, StampOrgan organization) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        //修改path XXXXXX,id 迭代修改该对象及子元素的path 修改方法
        //1 先获得父级元素的path 修改对象的path
        StampOrgan parentOrg = stampOrganRepository.getById(organization.getParentId());
        String changToPath = "";
        if (parentOrg == null) {
            changToPath = "0," + byId.getId();
        } else {
            changToPath = parentOrg.getPath() + "," + byId.getId();
        }
        organization.setPath(changToPath);
        setProperty(organization, "lastUpdateUserId", getUsers().getId());
        setProperty(organization, "lastUpdateUserName", getUsers().getUsername());
        setProperty(organization, "lastUpdateDateTime", new Date());
        StampOrgan t1 = getMapper().getById(byId.getId());
        String[] igre = getNotNullProperties(organization);
        t1 = copyProperties(t1, organization, igre);
        if (getMapper().save(t1) != null) {
            updateChildPath(organization.getId());
            return SUCCESS;
        }
        return ERROR;
    }

    public void updateChildPath(Integer id) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        //获得二级元素
        List<StampOrgan> list = stampOrganRepository.findByParentIdAndAmputated(id, 0);
        StampOrgan byId = stampOrganRepository.getById(id);
        this.updatePath(list, byId);
    }

    private void updatePath(List<StampOrgan> list, StampOrgan byId) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        for (StampOrgan o : list) {
            o.setPath(byId.getPath() + "," + o.getId());
            List<StampOrgan> childs = stampOrganRepository.findByParentIdAndAmputated(o.getId(),0);
            this.update(o.getId(), o);
            if (childs.size() > 0) {
                updatePath(childs, o);
            }
        }
    }

    public List<EmployeesOutput> selectByPath(PageData pageData) {
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        Page<EmployeesOutput> pageList = employeesMapper.selectByPath(pageData);
        return pageList;
    }

    public List<StampOrgan> getAllOrg() {
        List<StampOrgan> list = stampOrganMapper.selectAllOrg();
        return list;
    }

    public List<StampOrganOutput> Assembly(List<StampOrgan> all) {
        all.sort((x, y) -> Double.compare(x.getDisplayOrder(), y.getDisplayOrder()));
        List<StampOrganOutput> parentArray = new ArrayList<>();
        for (StampOrgan stampOrgan : all) {
            if (stampOrgan.getParentId() == 0) {
                parentArray.add(new StampOrganOutput(stampOrgan));
            }
        }
        return getTreeData(all, parentArray);
    }


    public List<StampOrganOutput> getTreeData(List<StampOrgan> list, List<StampOrganOutput> parents) {
        for (StampOrganOutput parentOrg : parents) {
            List<StampOrganOutput> childs = new ArrayList<>();

            for (StampOrgan o : list) {
                if (parentOrg.getId().equals(o.getParentId())) {
                    childs.add(new StampOrganOutput(o));
                }
            }
            parentOrg.setChildren(childs.size() == 0 ? null : childs);
            if (childs.size() > 0) {
                getTreeData(list, childs);
            }
        }
        return parents;
    }

    public List<EmployeesOutput> selectByOrgId(PageData pageData){
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        Page<EmployeesOutput> pageList = employeesMapper.selectByOrgId(pageData);
        return pageList;
    }
}
