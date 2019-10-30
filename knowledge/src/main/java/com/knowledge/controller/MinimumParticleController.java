package com.knowledge.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.github.pagehelper.Page;
import com.knowledge.core.base.BaseController;
import com.knowledge.core.base.BaseService;
import com.knowledge.domain.output.MinimumParticleOutput;
import com.knowledge.model.Answer;
import com.knowledge.model.MinimumParticle;
import com.knowledge.service.AnswerService;
import com.knowledge.service.MinimumParticletService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("minimum_particle")
@Api(value = "最小颗粒controller",description = "最小颗粒操作",tags = {"最小颗粒操作接口"})
public class MinimumParticleController extends BaseController<MinimumParticleOutput, MinimumParticle,Integer> {
    @Autowired
    private MinimumParticletService minimumParticletService;
    @Override
    public BaseService<MinimumParticleOutput, MinimumParticle, Integer> getService() {
        return minimumParticletService;
    }


    @Override
    @ApiOperation("新增或修改最小颗粒")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id, @Validated @RequestBody @ApiParam(name="指标信息",value="传入json格式",required=true) MinimumParticle minimumParticle) throws Exception {
       if(minimumParticletService.isRepeat(id,minimumParticle)){
           return ResponseResult.error("办事情形名称不能重复");
       }

       if(minimumParticle.getParentId()==null){
           minimumParticle.setParentId(0);
       }
        if(id==null||"".equals(id)){//表添加
            int result = minimumParticletService.add(minimumParticle);
            if (result < 0) {
                return ResponseResult.error("请选择正确的上级最小颗粒");
            }
            return ResponseResult.success();
        }else {//表编辑
            //不能选自己作为自己的父级
            if(minimumParticle.getParentId()==id){
                return  ResponseResult.error("不能选自身作为父级");
            }else{
                MinimumParticle byId = getService().getById(minimumParticle.getParentId());
                if(byId!=null&&byId.getPath().indexOf(minimumParticle.getId().toString()+",")>=0){
                    return  ResponseResult.error("不能选则自己的子级作为自己的父级");
                }
            }
        }
            //更新时判断parentId是否发生改变
            MinimumParticle byId = getService().getById(id);
            if(!byId.getParentId().equals(minimumParticle.getParentId())){
                int update = minimumParticletService.updateObj(byId,minimumParticle);
                if (update < 0) {
                    return ResponseResult.error("请选择正确的上级最小颗粒");
                }
                return ResponseResult.success();
            }else{
                return super.formPost(id,minimumParticle);
            }
    }

    @Override
    @GetMapping(value = "get")
    @ApiOperation(value="根据id获取单个最小颗粒")
    public ResponseResult get(Integer id) {
        return super.selectById(id);
    }


    @ApiOperation("删除最小颗粒")
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        if (idList == null || idList.length() <= 0) {
            return ResponseResult.error(PARAM_EORRO);
        }else{
            if(idList.split(",").length>1){
                return ResponseResult.error("最小颗粒只能单个删除，请选择单条数据后做删除");
            }else{
                MinimumParticle minimumParticle=minimumParticletService.getById(Integer.valueOf(idList.split(",")[0]));
                if(minimumParticle.getParentId()==0){
                    return ResponseResult.error("最外成最小颗粒无法删除");
                }
                int result = minimumParticletService.logicDelete(idList.split(",")[0]);
                if (result == 1) {
                    return ResponseResult.error(SYS_EORRO);
                }else if(result == 2){
                    return  ResponseResult.error("该最小颗粒有子级不能删除，请先删除该最小颗粒下的数据");
                }else{
                    return ResponseResult.success();
                }
            }
        }
    }

    @ApiOperation("最小颗粒发布或取消发布")
    @GetMapping(value = "publish")
    public ResponseResult publish(Integer id) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        return  minimumParticletService.publish(id);
    }

    @GetMapping(value = "findPageList")
    @ApiImplicitParams({@ApiImplicitParam(name="qlName",value="权力名称",required=false,dataType="String", paramType = "query"),
            @ApiImplicitParam(name="happeningType",value="情况分类",required=false,dataType="String", paramType = "query")})
    public ResponseResult selectPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return super.selectPageList(pageData);
    }

    @ApiOperation("查询最小颗粒列表")
    @GetMapping(value = "getList")
    @ApiImplicitParams({@ApiImplicitParam(name="qlName",value="权力名称",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="happeningType",value="情况分类",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="organizationId",value="受理机构",dataType="String", paramType = "query")
    })
    public ResponseResult getList(HttpServletRequest request) {


//        pageData.put("userId",this.getService().getUsers().getId().toString());
////        if(this.getService().getUsers().getAdministratorLevel()!=9){
////            if(this.getService().getUsers().getUserType()==0){
////                pageData.put("employeeId",this.getService().getUsers().getEmployeeId().toString());
////            }
////        }
////        pageData.put("orgId",this.getService().getUsers().getOrganizationId().toString());
////        List<MinimumParticleOutput> all = minimumParticletService.getList(pageData);
////        List<MinimumParticleOutput> outputs= minimumParticletService.Assembly(all);


        List<MinimumParticleOutput> all = minimumParticletService.getList(new PageData(request));
        List<MinimumParticleOutput> outputs= minimumParticletService.Assembly(all);
        return ResponseResult.success(outputs);
    }

    @GetMapping(value = "getParList")
    public ResponseResult getParList(){
        List<MinimumParticleOutput> all = minimumParticletService.getParList();
        List<MinimumParticleOutput> outputs= minimumParticletService.Assembly(all);
        return ResponseResult.success(outputs);
    }
}
