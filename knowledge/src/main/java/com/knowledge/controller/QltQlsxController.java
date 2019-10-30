package com.knowledge.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.knowledge.core.base.BaseController;
import com.knowledge.core.base.BaseService;
import com.knowledge.domain.output.QltQlsxOutput;
import com.knowledge.model.QltQlsx;
import com.knowledge.model.Users;
import com.knowledge.service.QltQlsxService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author: Young
 * @description:
 * @date: Created in 13:45 2018/11/13
 * @modified by:
 */
@RestController
@RequestMapping("qlt_qlsx")
public class QltQlsxController  extends BaseController<QltQlsxOutput, QltQlsx,String> {

    @Override
    public BaseService<QltQlsxOutput, QltQlsx, String> getService() {
        return qltQlsxService;
    }

    @Autowired
    private QltQlsxService qltQlsxService;

    @ApiOperation("编辑权力事项（网上申请或窗口申请的方式）")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(@Validated @RequestBody @ApiParam(name="指标信息",value="传入json格式",required=true) QltQlsx qltQlsx) {
        if(StringUtils.isBlank(qltQlsx.getQlInnerCode())){
            return ResponseResult.error("未选择权力事项");
        }
        return  qltQlsxService.updateApplicationDetail(qltQlsx);
    }

    /**
     * 查询单条记录
     **/
    @GetMapping(value = "selectById")
    public ResponseResult selectById(String id) {
        return qltQlsxService.selectById(id);
    }

    /**
     * 根新热门事项状态
     **/
    @GetMapping(value = "update_hot_state")
    public ResponseResult updateHotState(@RequestParam("id") String id, @RequestParam("state") Integer state) {
        //1.跟新热门事项状态
        ResponseResult result = qltQlsxService.updateHotState(id, state);
        Users users = qltQlsxService.getUsers();
        //操作最小颗粒
        qltQlsxService.UpdateOrAddRelationDateByhot(id,state,users);
        return result;
    }

    /**
     * 跟新最小颗粒状态
     **/
    @GetMapping(value = "update_particles_state")
    public ResponseResult updateParticlesState(@RequestParam("id") String id, @RequestParam("state") Integer state) {
        //1.跟新最小颗粒
        ResponseResult result = qltQlsxService.updateParticlesState(id, state);
        Users users = qltQlsxService.getUsers();
        qltQlsxService.UpdateOrAddRelationDateByMini(id,state,users);
        return result;
    }

    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的权力事项内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "qlName", value = "权力名称",  dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "contentInvolve", value = "涉及内容",  dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "hot", value = "热门事项", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "particles", value = "最小颗粒",  dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "organizationId", value = "受理机构", dataType = "int", paramType = "query")
    })
    public ResponseResult findPageList(HttpServletRequest request) {


//        pageData.put("userId",this.getService().getUsers().getId().toString());
//        if(this.getService().getUsers().getAdministratorLevel()!=9){
//            if(this.getService().getUsers().getUserType()==0){
//                pageData.put("employeeId",this.getService().getUsers().getEmployeeId());
//            }else if(this.getService().getUsers().getUserType()==1){
//                pageData.put("orgId",this.getService().getUsers().getOrganizationId());
//
//                String ouoCode = qltQlsxService.selectOUORGCODE(this.getService().getUsers().getOrganizationId());
//                if(StringUtils.isBlank(ouoCode)){
//                    return qltQlsxService.findAll(pageData);
//                }
//            }
//        }
//        String ouoCode = qltQlsxService.selectOUORGCODE(this.getService().getUsers().getOrganizationId());
//        if(StringUtils.isBlank(ouoCode)){
//            return qltQlsxService.findAll(pageData);
//        }

        PageData pageData = new PageData(request);
        return super.selectPageList(pageData);
    }

    /**查询所有受理机构*/
    @GetMapping(value = "findAllAcceptInstitution")
    public ResponseResult findAllAcceptInstitution(){
       return ResponseResult.success(qltQlsxService.findAllAcceptInstitution());
    }

    @GetMapping(value = "findMinPageList")
    @ApiOperation("获取分页的最小颗粒权力事项内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "qlName", value = "权力名称",  dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "contentInvolve", value = "涉及内容",  dataType = "string", paramType = "query")
    })
    public ResponseResult findMinPageList(HttpServletRequest request) {
        PageData pageData = new PageData(request);
        pageData.put("particles",1);
        return super.selectPageList(pageData);
    }


    /**
     * 查询权力事项表中共多少条数据
     **/
    @GetMapping("select_count")
    public ResponseResult selectCount() {
        return qltQlsxService.selectCount();
    }

    /**
     * 导出
     **/
    @GetMapping(value = "/exportExcel")
    @ApiOperation("导出到excel功能")
    public ResponseResult exportExcel(HttpServletResponse response, HttpServletRequest request) throws IOException {
        return qltQlsxService.exportExcel(response, request);
    }


    /**查看附件*/
    @GetMapping(value = "find_materialInfo")
    @ApiOperation("查看附件功能")
    public ResponseResult findMaterialInfo(String qlInnerCode){
        return qltQlsxService.findMaterialInfo(qlInnerCode);
    }
}
