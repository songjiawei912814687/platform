package com.knowledge.controller;

import com.common.Enum.AttachmentEnum;
import com.common.model.PageData;
import com.common.request.ServiceCall;
import com.common.response.ResponseResult;
import com.knowledge.core.base.BaseController;
import com.knowledge.core.base.BaseService;
import com.knowledge.domain.output.MaterialListOutput;
import com.knowledge.model.Answer;
import com.knowledge.model.Attachment;
import com.knowledge.model.MaterialList;
import com.knowledge.service.AnswerService;
import com.knowledge.service.MaterialListService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("material_list")
@Api(value = "材料清单controller",description = "材料清单操作",tags = {"材料清单操作接口"})
public class MaterialListController extends BaseController<MaterialListOutput,MaterialList,Integer> {
    @Autowired
    private MaterialListService materialListService;
    @Override
    public BaseService<MaterialListOutput, MaterialList, Integer> getService() {
        return materialListService;
    }

    @Override
    @ApiOperation("新增或修改材料清单")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    @Transactional
    public ResponseResult formPost(Integer id, @Validated @RequestBody @ApiParam(name="指标信息",value="传入json格式",required=true) MaterialList materialList) throws Exception {
        //验证示例表和空白表是否存在
       /* if(materialList.getBlankAttachmentList()==null||materialList.getExampleAttachmentList()==null||materialList.getBlankAttachmentList().size()==0||materialList.getExampleAttachmentList().size()==0){
            return ResponseResult.error("示例表和空白表必须上传");
        }*/
        //同一个最小颗粒下不能有两个重名的材料清单
        materialList.setId(id);
        materialList.setIsMini(1);
        if(materialListService.isRepeat(materialList)){
            return ResponseResult.error("当前最小颗粒下已经存在该名称的材料清单");
        }
        if (materialList == null) {
            return ResponseResult.error(PARAM_EORRO);
        }
        int result = 0;
        List<Attachment> attachmentList=materialList.getExampleAttachmentList();
        List<Attachment> blankAttachmentList=materialList.getBlankAttachmentList();
        if(attachmentList==null||attachmentList.size()==0){
            materialList.setExampleTableFileName(null);
            materialList.setExampleTableFileUrl(null);
        }else{
            materialList.setExampleTableFileName(attachmentList.get(0).getFileName());
            materialList.setExampleTableFileUrl(attachmentList.get(0).getUrl());
        }
        if(blankAttachmentList==null||blankAttachmentList.size()==0){
            materialList.setEmptyTableFileName(null);
            materialList.setEmptyTableFileUrl(null);
        }else{
            materialList.setEmptyTableFileName(blankAttachmentList.get(0).getFileName());
            materialList.setEmptyTableFileUrl(blankAttachmentList.get(0).getUrl());
        }
        if (id == null) {
            result = materialListService.add(materialList);
        } else {
            result = materialListService.update(id, materialList);
        }
        if(result>0){
            /*List<Attachment> attachments=new ArrayList<Attachment>();
            if(attachmentList!=null&&attachmentList.size()>0){
                for(Attachment attachment:attachmentList){
                    attachment.setResourcesId(result);
                    attachment.setResourcesType(AttachmentEnum.EXAMPLE_SHEET.getCode());
                    attachment.setSourceFileName(attachment.getSuffix());
                    attachments.add(attachment);
                }
            }
            if(blankAttachmentList!=null&&blankAttachmentList.size()>0){
                for(Attachment attachment:blankAttachmentList){
                    attachment.setResourcesId(result);
                    attachment.setResourcesType(AttachmentEnum.BLANK_SHEET.getCode());
                    attachment.setSourceFileName(attachment.getSuffix());
                    attachments.add(attachment);
                }
            }
            if(attachments!=null&&attachments.size()>0){
                PageData pageData = new PageData();
                pageData.put("attachmentList",attachments);
                if(!ServiceCall.postResult(loadBalancerClient,"attachment/attachmentApi","bigdata",pageData).isSuccess()){
                    return ResponseResult.error("上传附件失败");
                }
            }*/

        }else {
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success();
    }

    @Override
    @ApiOperation("删除材料清单")
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String id) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        return super.logicDelete(id);
    }

    @Override
    @GetMapping(value = "get")
    @ApiOperation(value="根据id获取单个材料清单")
    public ResponseResult get(Integer id) {
        {
            MaterialListOutput materialListOutput = materialListService.selectById(id);
            Attachment attachment = new Attachment();
            Attachment attachment1 = new Attachment();
            ArrayList<Attachment> list = new ArrayList<>();
            ArrayList<Attachment> list1 = new ArrayList<>();
            if(materialListOutput.getEmptyTableFileName()!=null&&!"".equals(materialListOutput.getEmptyTableFileName())&&materialListOutput.getEmptyTableFileUrl()!=null&&!"".equals(materialListOutput.getEmptyTableFileUrl())){
                attachment.setFileName(materialListOutput.getEmptyTableFileName());
                attachment.setUrl(materialListOutput.getEmptyTableFileUrl());
                list.add(attachment);
                materialListOutput.setBlankAttachmentList(list);
            }else{
                materialListOutput.setBlankAttachmentList(null);
            }

            if(materialListOutput.getExampleTableFileName()!=null&&!"".equals(materialListOutput.getExampleTableFileName())&&materialListOutput.getExampleTableFileUrl()!=null&&!"".equals(materialListOutput.getExampleTableFileUrl())){
                attachment1.setFileName(materialListOutput.getExampleTableFileName());
                attachment1.setUrl(materialListOutput.getExampleTableFileUrl());
                list1.add(attachment1);
                materialListOutput.setExampleAttachmentList(list1);
            }else{
                materialListOutput.setExampleAttachmentList(null);
            }
            return ResponseResult.success(materialListOutput);
        }
    }

    @GetMapping(value = "getParentMaterialLIst")
    @ApiOperation(value="根据id获取单个材料清单")
    @ApiImplicitParams({@ApiImplicitParam(name="name",value="材料名称",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="minimumParticleId",value="最小颗粒id",required=true,dataType="int", paramType = "query")})
    public ResponseResult getParentMaterialLIst(HttpServletRequest request){
        PageData pageData = new PageData(request);
        if(pageData.get("minimumParticleId")==null||"".equals(pageData.get("minimumParticleId"))){
            return ResponseResult.error(PARAM_EORRO);
        }
        return ResponseResult.success(materialListService.getParentMaterialLIst(pageData)) ;
    }

    //批量保存材料清单
    @ApiOperation("/批量保存材料清单")
    @RequestMapping(value = "addMaterialInBatch", method = RequestMethod.POST)
    @ApiImplicitParams({@ApiImplicitParam(name="materialList",value="材料id",required=true,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="id",value="最小颗粒id",required=true,dataType="int", paramType = "query")})
    public ResponseResult addMaterialInBatch(Integer id, String materialList) throws Exception {
        if(id==null||materialList==null||"".equals(id)||"".equals(materialList)){
            return ResponseResult.error(PARAM_EORRO);
        }else{
            return materialListService.addInBatch(id,materialList);
        }
    }

    @GetMapping("updatematerList")
    public ResponseResult updatematerList(){
        materialListService.updatematerList();
        return ResponseResult.success();
    }

    @GetMapping(value = "findPageList")
    @ApiImplicitParams({@ApiImplicitParam(name="name",value="材料名称",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="minimumParticleId",value="最小颗粒id",required=true,dataType="int", paramType = "query")})
    public ResponseResult selectPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        if(pageData.get("minimumParticleId")==null||"".equals(pageData.get("minimumParticleId"))){
            return ResponseResult.error(PARAM_EORRO);
        }
        ResponseResult result = super.selectPageList(pageData);
        return result;

    }
}
