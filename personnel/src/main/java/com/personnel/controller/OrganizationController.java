package com.personnel.controller;

import com.common.Enum.IsSyncQueueNameEnum;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.github.pagehelper.PageInfo;
import com.personnel.core.base.BaseController;
import com.personnel.core.base.BaseService;
import com.personnel.domian.output.OrganizationOutput;
import com.personnel.domian.output.OrganizationZTree;
import com.personnel.model.Organization;
import com.personnel.service.EmployeesService;
import com.personnel.service.FoodSystemService;
import com.personnel.service.OrganizationService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/organization")
@Api(value = "组织机构controller",description = "组织操作",tags = {"组织操作接口"})
public class OrganizationController extends BaseController<OrganizationOutput, Organization,Integer> {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private EmployeesService employeesService;

    @Autowired
    @Lazy
    private FoodSystemService foodSystemService;

    @Override
    public BaseService<OrganizationOutput,Organization, Integer> getService() {
        return organizationService;
    }

    @Override
    @Transactional
    @ApiOperation("新增或修改组织信息")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id, @Validated @RequestBody @ApiParam(name="组织信息",value="传入json格式",required=true) Organization organization){
        if(id==null&&organizationService.getUsers().getAdministratorLevel()!=9){
            return ResponseResult.error("抱歉，当前账户无权限新增组织");
        }
        organization.setId(id);
        try {
            if(organization.getParentId()==null||organization.getParentId().equals("")){
                organization.setParentId(0);
            }
            organization.setType(0);
            List<Organization> list=organizationService.getByName(organization);
            if(id==null){
                if(list!=null&&list.size()>0){
                    return ResponseResult.error("组织名称重复");
                }
                String organizationNo=organizationService.getMaxNo();
                if(organizationNo==null){
                    organizationNo="G0000001";
                }else {
                    String a=organizationNo.substring(1,organizationNo.length());
                    organizationNo ="G"+String.format("%07d",Integer.parseInt(a)+1);
                }
                organization.setOrganizationNo(organizationNo);
                //判断父级是否有子组织，若无子组织则将复制父级组织，名称为父级组织名称+"成员"，并将父级组织下的成员转移到该组织下
                if(!organizationService.copyInfo(organization)){
                    return ResponseResult.error(SYS_EORRO);
                }
                int result = organizationService.add(organization);
                if (result > 0) {
                    return ResponseResult.error(SYS_EORRO);
                }
                //当系统新增组织成功后，1.将组织下发到餐盘系统
                foodSystemService.addFoodOrganization(organization);
                return ResponseResult.success();
            }else {
                if(!organizationService.isRepeat(id,organization.getName())){
                    if(list!=null&&list.size()>0){
                        return ResponseResult.error("组织名称重复");
                    }
                }
                Organization byId = getService().getById(id);
                //更新时判断parentId是否发生改变
                if(!byId.getParentId().equals(organization.getParentId())){
                    //parentId不能为子组织
                    if(organizationService.isChild(organization)){
                        return ResponseResult.error("父级不能为自己的子级组织");
                    }
                    if(byId.getLinkedId()!=null&&!"".equals(byId.getLinkedId())){
                        return ResponseResult.error("系统复制的组织无法更改上级组织");
                    }
                    //判断父级是否有子组织，若无子组织则将复制父级组织，名称为父级组织名称+"成员"，并将父级组织下的成员转移到该组织下
                    if(!organizationService.copyInfo(organization)){
                        return ResponseResult.error(SYS_EORRO);
                    }
                    int update = organizationService.updateObj(byId,organization);
                    if (update > 0) {
                        return ResponseResult.error(SYS_EORRO);
                    }
                    //当系统编辑组织成功后，将组织下发到餐盘系统
                    foodSystemService.addFoodOrganization(organization);
                    if(organizationService.SynchronizeInfo(organization)){
                        return ResponseResult.success();
                    }else{
                        return ResponseResult.error(SYS_EORRO);
                    }
                }else{
                    //当系统编辑组织成功后，将组织下发到餐盘系统
                    foodSystemService.addFoodOrganization(organization);
                    //同步更新复制的组织信息
                    if(organizationService.SynchronizeInfo(organization)){
                        return super.formPost(id,organization);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return ResponseResult.error(SYS_EORRO);
    }


    @Override
    @GetMapping(value = "logicDelete")
    //假删除，删除之前判断是否有子元素，组织数据只支持单个删除
    public ResponseResult logicDelete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException,MethodArgumentNotValidException {
        PageData pageData=new PageData();
        if (idList == null || idList.length() <= 0) {
            return ResponseResult.error(PARAM_EORRO);
        }else{
            Organization organization=organizationService.getById(Integer.valueOf(idList.split(",")[0]));
            if(organization!=null){
              pageData.put("path",organization.getPath());
              if(employeesService.selectByPath(pageData).size()>0){
                  return ResponseResult.error("该组织机构下还有成员，不能删除");
              }
            }
            if(idList.split(",").length>1){
                return ResponseResult.error("组织数据只能单个删除，请选择单条数据后做删除");
            }else{
                int result = organizationService.logicDelete(idList.split(",")[0]);
                if (result == 1) {
                    return ResponseResult.error(SYS_EORRO);
                }else if(result == 2){
                    return  ResponseResult.error("该组织有子组织不能删除，请先删除该组织下的组织数据");
                }else{
                    Integer foodId = foodSystemService.deleteFoodOrganization(organization.getOrganizationNo());
                    if(foodId==null){
                        return ResponseResult.error("餐盘系统组织删除失败");
                    }
                    return ResponseResult.success("系统组织删除成功");
                }
            }
        }
    }

    @Override
    @ApiOperation("根据id获取单个职务")
    @GetMapping(value = "get")
    public ResponseResult get(Integer id) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        if(id == null){
            return ResponseResult.error(PARAM_EORRO);
        }
        var organizationOutput = organizationService.selectById(id);
        return ResponseResult.success(organizationOutput);
    }

    @ApiOperation("获取所有组织")
    @GetMapping(value = "getAll")
    public ResponseResult getAll(HttpServletRequest request) {
        List<Organization> all = organizationService.getAllOrg();
        PageData pageData = new PageData(request);
        if(pageData.containsKey("parentId")){
            all =  all.stream().filter(Organization -> Organization.getParentId() == 0).collect(Collectors.toList());
        }

        List<OrganizationOutput> outputs= organizationService.Assembly(all);
        return ResponseResult.success(outputs);
    }

    @ApiOperation("获取权限内的组织")
    @GetMapping(value = "getAllWithinAuthority")
    public ResponseResult getAllWithinAuthority() {
        List<Organization> all = organizationService.getAllWithinAuthority();
        List<OrganizationOutput> outputs= organizationService.Assembly(all);
        return ResponseResult.success(outputs);
    }



    @ApiOperation("获取微信需要所有组织")
    @GetMapping(value = "getAllWechatOrga")
    public ResponseResult getAllWechatOrga(){
        List<Organization> all = organizationService.getAll();
        if(all.size() == 0){
            return ResponseResult.error("查询出的组织为空");
        }
        return ResponseResult.success(all);
    }

    @ApiOperation("查询组织")
    @GetMapping(value = "getList")
    @ApiImplicitParams({
            @ApiImplicitParam(name="name",value="组织机构名称",required=false,dataType="string", paramType = "query")})
    public ResponseResult getList(HttpServletRequest request) {
        List<Organization> all = organizationService.getList(new PageData(request));
        List<OrganizationOutput> outputs= organizationService.Assembly(all);
        return ResponseResult.success(outputs);
    }

    @ApiOperation("带权限的查询组织")
    @GetMapping(value = "getListWithinAuthority")
    @ApiImplicitParams({
            @ApiImplicitParam(name="name",value="组织机构名称",required=false,dataType="string", paramType = "query")})
    public ResponseResult getListWithinAuthority(HttpServletRequest request) {
        PageData pageData = new PageData(request);
        List<Organization> all = organizationService.getListWithinAuthority(pageData);
        List<OrganizationOutput> outputs= organizationService.Assembly(all);
        return ResponseResult.success(outputs);
    }

    @ApiOperation("获取组织树数据")
    @GetMapping(value = "getZtree")
    public ResponseResult getZtree(HttpServletRequest request) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        List<Organization> all = organizationService.getTree(request);
        if(all == null){
            return ResponseResult.success(null);
        }
        all.sort((x, y) -> Double.compare(x.getDisplayOrder(), y.getDisplayOrder()));
        List<OrganizationZTree> outputs= organizationService.ToZtree(all,0);
        return ResponseResult.success(outputs);
    }

    @ApiOperation("获取有权限的组织树数据")
    @GetMapping(value = "getZtreeWithinAuthority")
    public ResponseResult getZtreeWithinAuthority(HttpServletRequest request) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        List<Organization> all = organizationService.getListWithinAuthority(new PageData(request));
        if(all==null){
            return ResponseResult.success(null);
        }
        all = all.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Organization::getId))), ArrayList::new));
        if(all == null){
            return ResponseResult.success(null);
        }
        all.sort((x, y) -> Double.compare(x.getDisplayOrder(), y.getDisplayOrder()));
        List<OrganizationZTree> outputs= organizationService.ToZtree(all,0);
        return ResponseResult.success(outputs);
    }


    @ApiOperation("获看成员")
    @GetMapping(value = "getTeam")
    @ApiImplicitParams({
            @ApiImplicitParam(name="organizationId",value="组织机构ID",required=false,dataType="int", paramType = "query")})
    public ResponseResult getTeam(HttpServletRequest request)  {
        PageData pageData=new PageData(request);
        if(!pageData.containsKey("organizationId")||pageData.get("organizationId")==null||"".equals(pageData.getString("organizationId"))){
            return  ResponseResult.error(PARAM_EORRO);
        }
        return ResponseResult.success(new PageInfo<>(employeesService.selectByOrgId(pageData)));
    }


    /**
     * 导出组织机构信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ResponseResult export(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str = organizationService.export(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping(value = "updateSyncState")
    public ResponseResult updateSyncState(Integer id){
        OrganizationOutput organizationOutput = organizationService.selectById(id);
        if(organizationOutput==null){
            return ResponseResult.error(SYS_EORRO);
        }
        if(IsSyncQueueNameEnum.BEING_SYNC_QUEUE.getCode().equals(organizationOutput.getIsSyncQueue()) ||
                IsSyncQueueNameEnum.IS_SYNC_QUEUE.getCode().equals(organizationOutput.getIsSyncQueue())){
            return ResponseResult.error("正在下发或者已经下发完成，请勿重复操作");
        }
        Integer result = organizationService.updateSyncState(id);
        if(result<0){
            return ResponseResult.error("组织下发到取号叫号系统失败");
        }
        return ResponseResult.success();
    }




    /**查询部门号码**/
    @GetMapping("getOrganizationMobile")
    public ResponseResult getOrganizationMobile(Integer organizationId){
        List<Organization> organizationList = organizationService.getOrganizationMobile(organizationId);
        if(organizationList == null){
            return ResponseResult.error("该组织没有号码");
        }
        return ResponseResult.success(organizationList);
    }

    /**初始化组织数据（将半山腰有人员的组织copy一份并更换员工所在组织）**/
    @GetMapping("InitOrgData")
    public void InitOrgData(){
        organizationService.InitOrgData();
    }
}
