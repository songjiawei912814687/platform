package com.stamp.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.github.pagehelper.PageInfo;
import com.stamp.core.base.BaseController;
import com.stamp.core.base.BaseService;
import com.stamp.domain.output.StampOrganOutput;
import com.stamp.model.StampOrgan;
import com.stamp.service.StampOrganService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-04-18  10:29
 * @modified by:
 */
@RestController
@RequestMapping("StampOrgan")
public class StampOrganController extends BaseController<StampOrganOutput, StampOrgan,Integer> {

    @Autowired
    private StampOrganService stampOrganService;


    @Override
    public BaseService<StampOrganOutput, StampOrgan, Integer> getService() {
        return stampOrganService;
    }

    @Override
    @PostMapping("formPost")
    public ResponseResult formPost(Integer id, @RequestBody @Valid StampOrgan stampOrgan) throws Exception {
        if (id == null && this.getService().getUsers().getAdministratorLevel() != 9) {
            return ResponseResult.error("抱歉，当前账户无权限新增组织");
        }
        stampOrgan.setId(id);

        if (stampOrgan.getParentId() == null) {
            stampOrgan.setParentId(0);
        }
        stampOrgan.setType(0);
        if(id==null) {
            if (stampOrganService.findByName(stampOrgan.getName())) {
                return ResponseResult.error("组织已经存在");
            }
            String organizationNo=stampOrganService.getMaxNo();
            if(organizationNo==null){
                organizationNo="G0000001";
            }else {
                String a=organizationNo.substring(1);
                organizationNo ="G"+String.format("%07d",Integer.parseInt(a)+1);
            }
            stampOrgan.setOrganizationNo(organizationNo);
            Integer result = stampOrganService.add(stampOrgan);
            if (result <=0 ) {
                return ResponseResult.error(SYS_EORRO);
            }
            return ResponseResult.success();
        }else {
            if(!stampOrganService.isRepeat(id,stampOrgan.getName())){
                if(stampOrganService.findByName(stampOrgan.getName())){
                    return ResponseResult.error("组织名称重复");
                }
            }
            StampOrgan byId = getService().getById(id);
            if(!byId.getParentId().equals(stampOrgan.getParentId())){
                //parentId不能为子组织
                if(stampOrganService.isChild(stampOrgan)){
                    return ResponseResult.error("父级不能为自己的子级组织");
                }
                Integer update = stampOrganService.updateObj(byId,stampOrgan);
                if(update<0){
                    return ResponseResult.error(SYS_EORRO);
                }
            }
            return super.formPost(id,stampOrgan);
        }
    }


    @Override
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {

        StampOrgan stampOrgan=stampOrganService.getById(Integer.valueOf(idList.split(",")[0]));
        if(stampOrgan==null){
            return ResponseResult.error("该组织不存在");
        }
        PageData pageData=new PageData();
        pageData.put("path",stampOrgan.getPath());
        if(stampOrganService.selectByPath(pageData).size()>0){
            return ResponseResult.error("该组织机构下还有成员，不能删除");
        }

        Integer result = stampOrganService.logicDelete(idList.split(",")[0]);
        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success();
    }

    @Override
    @ApiOperation("根据id获取单个组织")
    @GetMapping(value = "get")
    public ResponseResult get(Integer id) {
        if(id == null){
            return ResponseResult.error(PARAM_EORRO);
        }
        var organizationOutput = stampOrganService.selectById(id);
        return ResponseResult.success(organizationOutput);
    }

    @ApiOperation("获取所有组织")
    @GetMapping(value = "getAll")
    public ResponseResult getAll(HttpServletRequest request) {
        List<StampOrgan> all = stampOrganService.getAllOrg();
        if(all.size()==0){
            return ResponseResult.success();
        }
        PageData pageData = new PageData(request);
        if(pageData.containsKey("parentId")){
            all =  all.stream().filter(e -> e.getParentId() == 0).collect(Collectors.toList());
        }
        if(all.size()==0){
            return ResponseResult.success();
        }

        List<StampOrganOutput> outputs= stampOrganService.Assembly(all);
        return ResponseResult.success(outputs);
    }

    @ApiOperation("获看成员")
    @GetMapping(value = "getTeam")
    @ApiImplicitParams({
            @ApiImplicitParam(name="stampOrgId",value="组织机构ID",dataType="int", paramType = "query")})
    public ResponseResult getTeam(HttpServletRequest request)  {
        PageData pageData=new PageData(request);
        return ResponseResult.success(new PageInfo<>(stampOrganService.selectByOrgId(pageData)));
    }
}
