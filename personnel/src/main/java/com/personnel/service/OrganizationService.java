package com.personnel.service;

import com.common.Enum.MessageTypeEnum;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.ExportExcel;
import com.common.utils.HttpRequestUtil;
import com.personnel.core.base.BaseMapper;
import com.personnel.core.base.BaseService;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.OrganizationOutput;
import com.personnel.domian.output.OrganizationZTree;
import com.personnel.mapper.jpa.EmployeesRepository;
import com.personnel.mapper.jpa.OrganizationRepository;
import com.personnel.mapper.mybatis.*;
import com.personnel.model.Employees;
import com.personnel.model.Organization;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class OrganizationService extends BaseService<OrganizationOutput, Organization,Integer> {

    @Autowired
    private OrganizationRepository repository;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private EmployeesRepository employeesRepository;

    @Autowired
    private EmployeesMapper employeesMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private WindowMapper windowMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    @Lazy
    private FoodSystemService foodSystemService;

    @Override
    public BaseMapper<Organization, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<OrganizationOutput> getMybatisBaseMapper() {
        return organizationMapper;
    }

    public List<Organization> getByName(Organization organization) {
        List<Organization> list = organizationMapper.selectByName(organization.getName());
        return list;
    }

    public List<Organization> getByNo(Organization organization) {
        Organization org = organizationMapper.selectOrNoByOrId(organization.getId());
        PageData pageData = new PageData();
        pageData.put("organizationNo",organization.getOrganizationNo());
        pageData.put("orgId",organization.getLinkedId());
        pageData.put("id",organization.getId());
        List<Organization> list = organizationMapper.noIsRepeat(pageData);
        return list;
    }

    public boolean isRepeat(Integer id, String name) {
        Organization org = repository.getById(id);
        if (org.getName().equals(name)) {
            return true;
        }
        return false;
    }

    //所有的组织数据（用于列表）
    public List<OrganizationOutput> Assembly(List<Organization> all) {
        if (all == null) {
            return null;
        }
        all.sort((x, y) -> Double.compare(x.getDisplayOrder(), y.getDisplayOrder()));
        ArrayList<OrganizationOutput> parentArray = new ArrayList<>();
        for (Organization o : all) {
            if (o.getParentId() == 0) {
                parentArray.add(new OrganizationOutput(o));
            }
        }
        return getTreeData(all, parentArray);
    }


    public List<OrganizationOutput> getTreeData(List<Organization> list, List<OrganizationOutput> parents) {
        for (OrganizationOutput parentOrg : parents) {
            List<OrganizationOutput> childs = new ArrayList<>();

            for (Organization o : list) {
                if (parentOrg.getId().equals(o.getParentId())) {
                    childs.add(new OrganizationOutput(o));
                }
            }
            parentOrg.setChildren(childs.size() == 0 ? null : childs);
            if (childs.size() > 0) {
                getTreeData(list, childs);
            }
        }
        return parents;
    }

    //所有的组织数据（用户树形下拉框类ztree）
    public List<OrganizationZTree> ToZtree(List<Organization> all, Integer parentId) {

        List<OrganizationZTree> parentArray = new ArrayList<>();
        for (Organization o : all) {
            if (o.getParentId() == parentId) {
                parentArray.add(new OrganizationZTree(o));
            }
        }
        if (parentArray.size() <= 0) {
            this.ToZtree(all, ++parentId);
        }
        return getTree(all, parentArray);
    }

    public List<OrganizationZTree> getTree(List<Organization> list, List<OrganizationZTree> parents) {
        for (OrganizationZTree parentOrg : parents) {
            List<OrganizationZTree> childs = new ArrayList<>();
            for (Organization o : list) {
                if (parentOrg.getKey().equals(o.getParentId())) {
                    childs.add(new OrganizationZTree(o));
                }
            }
            parentOrg.setChildren(childs.size() == 0 ? null : childs);
            if (childs.size() > 0) {
                getTree(list, childs);
            }
        }
        return parents;
    }

    //单个更新
    public int updateOne(Integer id, Organization organization) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
//        setProperty(organization, "lastUpdateUserId", getUsers().getId());
//        setProperty(organization, "lastUpdateUserName", getUsers().getUsername());
        setProperty(organization, "lastUpdateDateTime", new Date());
        Organization t1 = getMapper().getById(id);
        String[] igre = getNotNullProperties(organization);
        t1 = copyProperties(t1, organization, igre);
        if (getMapper().save(t1) != null) {
            return SUCCESS;
        }
        return ERROR;
    }

    //更新当前对象及子对象的path
    public int updateObj(Organization byId, Organization organization) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        //修改path XXXXXX,id 迭代修改该对象及子元素的path 修改方法
        //1 先获得父级元素的path 修改对象的path
        Organization parentOrg = repository.getById(organization.getParentId());
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
        Organization t1 = getMapper().getById(byId.getId());
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
        List<Organization> list = repository.findByParentIdAndAmputated(id, 0);
        Organization byId = repository.getById(id);
        this.updatePath(list, byId);
    }

    private void updatePath(List<Organization> list, Organization byId) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        for (Organization o : list) {
            o.setPath(byId.getPath() + "," + o.getId());
            List<Organization> childs = repository.findByParentId(o.getId());
            this.update(o.getId(), o);
            if (childs != null && childs.size() > 0) {
                updatePath(childs, o);
            }
        }
    }

    //假删除（组织机构只进行单个删除）
    @Override
    public int logicDelete(String id) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        Organization t = getMapper().getById(Integer.valueOf(id));
        if (t == null) {
            return SUCCESS;
        } else {
            //判断是否有子元素
            List<Organization> parents = repository.findByParentIdAndAmputated(Integer.valueOf(id), 0);
            if (parents != null && parents.size() > 0) {
                return 2;
            }
            setProperty(t, "amputated", 1);
            int result = this.updateOne(Integer.valueOf(id), t);
            return result;
        }
    }

    @Override
    @Transactional
    public int add(Organization organization) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        setProperty(organization, "createdUserId", getUsers().getId());
        setProperty(organization, "createdUserName", getUsers().getUsername());
        setProperty(organization, "createdDateTime", new Date());
        setProperty(organization, "lastUpdateUserId", getUsers().getId());
        setProperty(organization, "lastUpdateUserName", getUsers().getUsername());
        setProperty(organization, "lastUpdateDateTime", new Date());
        setProperty(organization, "amputated", 0);
//        String t=this.getMaxNo();
//        if(t==null){
//            t="00000001";
//        }else {
//            t = String.format("%08d",Integer.parseInt(t)+1);
//        }
//        organization.setOrganizationNo(t);
        Organization returnOrg = getMapper().save(organization);
        if (returnOrg != null) {
            //更新path
            //1、根据parentid获得父级元素的path
            Organization organization1 = repository.getById(organization.getParentId());
            //2、path和id做拼接后更新到元素
            if (organization1 == null) {
                returnOrg.setPath("0," + returnOrg.getId());
            } else {
                returnOrg.setPath(organization1.getPath() + "," + returnOrg.getId());
            }
            if (this.updateOne(returnOrg.getId(), returnOrg) > 0) {
                return ERROR;
            } else {
                return SUCCESS;
            }
        }
        return ERROR;
    }

    public List<Organization> getTree(HttpServletRequest request) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        PageData pageData = new PageData(request);
        Organization o = null;
        List<Organization> organs = new ArrayList<>();
        if (pageData.containsKey("permissions")) {
            if (!isPermissions()) {
                return null;
            }
            var organId = getUsers().getOrganizationId();
            if (organId == null) {
                return null;
            }
            o = this.getById(organId);
            if (o == null || o.getPath() == null || "".equals(o.getPath())) {
                return null;
            }
            organs = organizationMapper.getByLikePath(o.getPath());
        } else {
            organs = repository.findByAmputated(0);

        }
        return organs;
    }

    private boolean isPermissions() {
        var user = getUsers();
        if (user.getUserType() == 1) {//如果是部门的账号
            return true;
        }
        var roleList = roleMapper.findByUserId(user.getId());
        if (roleList == null || roleList.size() <= 0) {
            return false;
        }
        for (var r : roleList) {
            if (r.getDefaultPermissions() != null) {
                if (r.getDefaultPermissions().equals(1)) {//开启默认权限
                    return true;
                }
            }
        }
        return false;
    }

    //组织名称搜索
    public List<Organization> getList(PageData pageData) {
        //1、先获得根据名称查找后的结果集
        List<Organization> returnList = new ArrayList<>();
        Organization organization = new Organization();
        organization.setName("%" + pageData.GetParameter("name") + "%");
        List<Organization> list = organizationMapper.getByName(organization);
        //2、循环遍历根据path获得所有的元素
        StringBuffer path = new StringBuffer("");
        for (Organization o : list) {
            path.append(o.getPath() + ",");

        }
        if (path.length() == 0) {
            return null;
        } else {
            String pahtval = path.toString().substring(0, path.length() - 1);
            String[] split = pahtval.split(",");
            HashSet<String> strings = new HashSet<>();
            for (String str:split) {
                strings.add(str);
            }
            Object[] objects1 = strings.toArray();
            StringBuilder stringBuilder = new StringBuilder("");
            for (int i=0;i<objects1.length;i++) {
                stringBuilder.append(objects1[i]+",");
                if((i>0&&i%900==0)||i==objects1.length-1){
                    String substring = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
                    List<Organization> organizations = organizationMapper.getByPath(substring);
                    returnList.addAll(organizations);
                    stringBuilder = new StringBuilder("");
                }
            }
        }
        return returnList;
    }

    //组织名称搜索
    public List<Organization> getListWithinAuthority(PageData pageData) {
        //1、先获得根据名称查找后的结果集
        List<Organization> returnList = new ArrayList<>();
        List<Organization> list = null;
        if(getUsers().getAdministratorLevel()==9){
            Organization organization = new Organization();
            if(pageData.GetParameter("name")==null){
                organization.setName("%%");
            }else{
                organization.setName("%" + pageData.GetParameter("name") + "%");
            }

            list = organizationMapper.getByName(organization);
        }else{
            pageData.put("userId",getUsers().getId());
            if(getUsers().getUserType()==1){//部门账号为部门本身和部门账号权限内的组织
                Integer i  = getUsers().getOrganizationId();
                Organization organizationOutput = organizationMapper.selectOrNoByOrId(getUsers().getOrganizationId());
                pageData.put("path",organizationOutput.getPath());
                pageData.put("linkedId",organizationOutput.getId());
            }
            list = organizationMapper.getByNameWithinAuthority(pageData);
        }
        //2、循环遍历根据path获得所有的元素
        StringBuffer path = new StringBuffer("");
        for (Organization o : list) {
            path.append(o.getPath() + ",");

        }
        if (path.length() == 0) {
            return null;
        } else {
            String pahtval = path.toString().substring(0, path.length() - 1);
            String[] split = pahtval.split(",");
            HashSet<String> strings = new HashSet<>();
            for (String str:split) {
                strings.add(str);
            }
            Object[] objects1 = strings.toArray();
            StringBuilder stringBuilder = new StringBuilder("");
            for (int i=0;i<objects1.length;i++) {
                stringBuilder.append(objects1[i]+",");
                if((i>0&&i%900==0)||i==objects1.length-1){
                    String substring = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
                    List<Organization> organizations = organizationMapper.getByPath(substring);
                    returnList.addAll(organizations);
                    stringBuilder = new StringBuilder("");
                }
            }
        }
        return returnList;
    }

    @Override
    public OrganizationOutput selectById(Integer id) {
        if (id == null) {
            return null;
        }
        Organization organization = repository.findById(id).get();
        Organization ParentName = repository.getById(organization.getParentId());
        Employees leadership = employeesRepository.getById(organization.getLeadership());
        Employees departmentalManager = employeesRepository.getById(organization.getDepartmentalManager());
        OrganizationOutput organizationOutput = new OrganizationOutput(organization, ParentName == null ? "-" : ParentName.getName(), leadership == null ? "-" : leadership.getName(), departmentalManager == null ? "-" : departmentalManager.getName(),organizationMapper.selectByRuleConfigId(organization.getAttendanceRuleConfigId()));
        System.out.println("看看output所有属性");
        return organizationOutput;
    }

    public Organization getByname(String name) {
        List<Organization> list = organizationMapper.selectByName(name);
        if (list != null && list.size() > 0) {
            return organizationMapper.selectByName(name).get(0);
        }
        return null;

    }

    public Integer updateSyncState(Integer id){
        Organization organization = organizationMapper.selectOrNoByOrId(id);
        if(organization==null){
            return ERROR;
        }
        if(organization.getIsSyncQueue()==1){
            return SUCCESS;
        }
        //设置成下发状态
        organization.setIsSyncQueue(1);
        Integer orgId = repository.save(organization).getId();
        if(orgId==null){
            return ERROR;
        }
        //todo 新增取号叫号下发组织中间表，需要设置自增的主键
        return SUCCESS;
    }

    public String export(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "组织机构信息";
        String excelName = "组织机构信息";
        String[] rowsName = new String[]{"序号", "组织机构名称", "上级组织机构", "组织机构编号", "组织机构编码"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        PageData pageData = new PageData(request);
        List<Organization> organizationList = organizationMapper.selectAllOrg();
        if (organizationList.size() > 0) {
            int i = 1;
            for (Organization organization : organizationList) {
                objs = new Object[rowsName.length];
                objs[0] = i;
                objs[1] = organization.getName();
                objs[2] = "";
                if (getById(organization.getParentId()) != null) {
                    objs[2] = getById(organization.getParentId()).getName();
                }
                objs[3] = organization.getOrganizationNo();
                objs[4] = organization.getOrganizationCode();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }

    public String getMaxNo() {
        String organizationNo = organizationMapper.selectMaxNo();
        return organizationNo;
    }

    public boolean hasOrganManager(Integer empId) {
        var list = repository.findAllByDepartmentalManager(empId);
        if (list.size() > 0) {
            return true;
        }
        return false;
    }

    public List<Organization> getAllOrg() {
        List<Organization> list = organizationMapper.selectAllOrg();
        return list;
    }


    public List<Organization> getOrganizationMobile(Integer organizationId) {
        List<Organization> organizationList = organizationMapper.selectOrganizationMobile(organizationId);
        if (organizationList == null || organizationList.size() == 0) {
            return null;
        }
        return organizationList;
    }

    public void sendMeassage() {
        List<Organization> list = organizationMapper.selectOrganizationPath();
        for (Organization output : list) {
            List<String> list3 = new ArrayList<>();
            list3.add(output.getPhoneNumber() + "/" + output.getName());
            if (output.getPhoneNumber() == null) {
                continue;
            }
            String description = output.getName() + "您好： 大数据平台的地址是：http://10.32.250.84（请使用谷歌浏览器打开）您部门的账号是：" + output.getName() + " 您的初始密码是：123456  请尽快修改您的初始密码 ";
            if (description == null || description.equals("")) {
                continue;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("description", description);
            map.put("isTiming", 0);
            map.put("type", MessageTypeEnum.APPROVAL.getCode());
            map.put("sendList", list3);
            try {
                ResponseResult result = HttpRequestUtil.sendPostRequest("http://127.0.0.1:8775/smsSend/form", map);
            } catch (Exception e) {

            }
        }

    }

    public List<Organization> getAllWithinAuthority() {
        if(getUsers().getAdministratorLevel()==9){
            return  organizationMapper.selectAllOrg();
        }
        PageData pageData = new PageData();
        pageData.put("userId",getUsers().getId());
        if(getUsers().getUserType()==1){//部门账号为部门本身和部门账号权限内的组织
            Organization organizationOutput = organizationMapper.selectOrNoByOrId(getUsers().getOrganizationId());
            pageData.put("path",organizationOutput.getPath());
            pageData.put("linkedId",organizationOutput.getId());
        }
        return  organizationMapper.getByUserId(pageData);
    }

    @Transactional
    public boolean copyInfo(Organization organization) {
        if(organization.getParentId() <= 0){
            return true;
        }
        //根据组织的父级的id查看该组织下是否有子组织
        List<Organization> list = null;
        if(organization.getParentId()!=0){
            list = organizationMapper.getByLikePath(","+organization.getParentId()+",");
        }
        if(list==null||list.size()==0){
            //若无子组织复制父级组织
            Organization organization1 = new Organization();
            Organization organizationOutput = organizationMapper.selectOrNoByOrId(organization.getParentId());
            BeanUtils.copyProperties(organizationOutput,organization1);
            organization1.setId(null);
            organization1.setName(organizationOutput.getName()+"成员");
            organization1.setParentId(organizationOutput.getId());
            organization1.setLinkedId(organizationOutput.getId());
            organization1.setFirstLetter(organizationOutput.getFirstLetter());
            Organization save = repository.save(organization1);
            save.setPath(organizationOutput.getPath()+","+save.getId());
            repository.save(save);
            if(save==null){
                return false;
            }
            //将原父级组织下的人员的组织id更换为copy的组织的id下
            PageData pageData = new PageData();
            pageData.put("oldOrgId",organization.getParentId());
            pageData.put("newOrgId",save.getId());
            employeesMapper.updateOrgId(pageData);
            usersMapper.updateOrgId(pageData);
            //更新窗口组织机构信息
            windowMapper.updateOrgId(pageData);
            //更新考勤数据组织机构信息
            windowMapper.updateAttendanceDataOrgId(pageData);
            //更新考勤日报表组织机构信息
            windowMapper.updateAttendanceDataStaticOrgId(pageData);
            //更新加班表组织机构信息
            windowMapper.updateOverTimeApplication(pageData);
            //更新调休表组织机构信息
            windowMapper.updateOffApplication(pageData);
            //更新请假申请表组织机构信息
            windowMapper.updateLeaveApplication(pageData);
            //更新咨询问题表组织机构信息
            windowMapper.updateQuestion(pageData);
            //更新考核申述问题表组织机构信息
            windowMapper.updateComplain(pageData);
            //更新员工加减分记录
            windowMapper.updateEmployeeRecord(pageData);
            //更新考核计划
            windowMapper.updatePlan(pageData);
            //更新部门考核报表
            windowMapper.updateDepartSheet(pageData);
            //更新反馈信息
            windowMapper.updateFeedBackInfo(pageData);
            //更新投诉建议(投诉部门和被投诉部门)
            windowMapper.updateSuggestion(pageData);
            windowMapper.updateSuggestion1(pageData);
            foodSystemService.addFoodOrganization(save);
        }
        return true;
    }

    public boolean SynchronizeInfo(Organization organization) {
        Organization organization1 = organizationMapper.selectOrNoByOrId(organization.getId());
        if(organization1!=null){
            if(organization1.getLinkedId()!=null&&!"".equals(organization1.getLinkedId())){
                //当前对象是复制的组织，需要更新被复制的组织的信息
                Organization byId = repository.getById(organization1.getLinkedId());
                byId.setOrganizationCode(organization.getOrganizationCode());
                byId.setAssessmentState(organization.getAssessmentState());
                byId.setPhoneNumber(organization.getPhoneNumber());
                byId.setOfficePhone(organization.getOfficePhone());
                byId.setFax(organization.getFax());
                byId.setDisplayOrder(organization.getDisplayOrder());
                byId.setLeadership(organization.getLeadership());
                byId.setDepartmentalManager(organization.getDepartmentalManager());
                byId.setAddress(organization.getAddress());
                byId.setFirstLetter(organization.getFirstLetter());
                Organization save = repository.save(byId);
                if(save!=null&&save.getId()<0){
                    return false;
                }
            }else{
                Organization org = repository.findByLinkedId(organization.getId());
                if(org!=null){
                    //当前对象是被复制的组织，需要更新复制的组织信息
                    Organization byId = repository.getById(org.getId());
                    byId.setName(organization.getName()+"成员");
                    byId.setOrganizationCode(organization.getOrganizationCode());
                    byId.setAssessmentState(organization.getAssessmentState());
                    byId.setPhoneNumber(organization.getPhoneNumber());
                    byId.setOfficePhone(organization.getOfficePhone());
                    byId.setFax(organization.getFax());
                    byId.setDisplayOrder(organization.getDisplayOrder());
                    byId.setLeadership(organization.getLeadership());
                    byId.setDepartmentalManager(organization.getDepartmentalManager());
                    byId.setAddress(organization.getAddress());
                    byId.setFirstLetter(organization.getFirstLetter());
                    Organization save = repository.save(byId);
                    if(save!=null&&save.getId()<0){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isChild(Organization organization) {
        Organization organization1 = organizationMapper.selectOrNoByOrId(organization.getParentId());//获得要更改的上级组织的path
        if(organization1 == null || organization1.getPath() == null){
            return false;
        }
        Organization organization2 = organizationMapper.selectOrNoByOrId(organization.getId());//获得当前的组织的path
        String f = organization1.getPath().trim();
        String ff = organization2.getPath().trim();
        if(f.indexOf(ff)==0){
            return true;
        }
        return false;
    }

    @Transactional
    public void InitOrgData() {
        List<Organization> list = organizationMapper.findInitDatas();
        for (Organization o :list) {
            Organization organization = organizationMapper.selectOrNoByOrId(o.getId());
            Organization organization1 = new Organization();
            BeanUtils.copyProperties(organization,organization1);
            organization1.setId(null);
            organization1.setName(organization.getName()+"成员");
            organization1.setParentId(organization.getId());
            organization1.setLinkedId(organization.getId());
            organization1.setFirstLetter(organization.getFirstLetter());
            Organization save = repository.save(organization1);
            save.setPath(organization.getPath()+","+save.getId());
            repository.save(save);
            if(save==null){
                return ;
            }
            //将原父级组织下的人员的组织id更换为copy的组织的id下
            PageData pageData = new PageData();
            pageData.put("oldOrgId",organization.getId());
            pageData.put("newOrgId",save.getId());
            employeesMapper.updateOrgId(pageData);
            usersMapper.updateOrgId(pageData);
            //更新窗口组织机构信息
            windowMapper.updateOrgId(pageData);
            //更新考勤数据组织机构信息
            windowMapper.updateAttendanceDataOrgId(pageData);
            //更新考勤日报表组织机构信息
            windowMapper.updateAttendanceDataStaticOrgId(pageData);
            //更新加班表组织机构信息
            windowMapper.updateOverTimeApplication(pageData);
            //更新调休表组织机构信息
            windowMapper.updateOffApplication(pageData);
            //更新请假申请表组织机构信息
            windowMapper.updateLeaveApplication(pageData);
            //更新咨询问题表组织机构信息
            windowMapper.updateQuestion(pageData);
            //更新考核申述问题表组织机构信息
            windowMapper.updateComplain(pageData);
            //更新员工加减分记录
            windowMapper.updateEmployeeRecord(pageData);
            //更新考核计划
            windowMapper.updatePlan(pageData);
            //更新部门考核报表
            windowMapper.updateDepartSheet(pageData);
            //更新反馈信息
            windowMapper.updateFeedBackInfo(pageData);
            //更新投诉建议(投诉部门和被投诉部门)
            windowMapper.updateSuggestion(pageData);
            windowMapper.updateSuggestion1(pageData);
            foodSystemService.addFoodOrganization(save);
        }
    }

    public Organization getByLinkedId(Integer linkedId){
        List<Organization> organization=organizationMapper.selectByLinkedId(linkedId);
        if(organization!=null&&organization.size()>0){
            return organization.get(0);
        }
        return null;
    }
}
