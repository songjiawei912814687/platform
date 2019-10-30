package com.wechatsug.controller;

import com.common.Enum.AttachmentEnum;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.HttpRequestUtil;
import com.wechatsug.domain.output.OrganizationOutput;
import com.wechatsug.domain.output.SuggesstionsOutput;
import com.wechatsug.domain.output.WindowOutput;
import com.wechatsug.mapper.repository.AttachmentRepository;
import com.wechatsug.model.AppConsts;
import com.wechatsug.model.Attachment;
import com.wechatsug.model.Organization;
import com.wechatsug.model.Suggestions;
import com.wechatsug.service.WechatsugService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-03  00:48
 * @modified by:
 */
@RestController
@RequestMapping("wechatsug")
public class WechatsugController {

    @Autowired
    private WechatsugService wechatsugService;
    @Autowired
    private AttachmentRepository attachmentRepository;

    /**不登录获取所有组织的方法*/
    @RequestMapping(value = "getAllOrganizaion", method = RequestMethod.GET)
    public ResponseResult getAllOrganizaion(){

        List<Organization> all = wechatsugService.getAll();
        if(all==null){
            return ResponseResult.error("没有获取到组织");
        }

        List<OrganizationOutput> outputs= wechatsugService.Assembly(all);
        if(outputs==null){
            return ResponseResult.error("未得到部门");
        }
        return ResponseResult.success(outputs);
    }

    @ApiOperation("获得部门下窗口")
    @GetMapping(value = "/getWindows")
    @ApiImplicitParams({
            @ApiImplicitParam(name="organizationId",value="部门id",required=false,dataType="int", paramType = "query")})
    public ResponseResult getWindows(HttpServletRequest request){
        PageData pageData=new PageData(request);
        Organization organization=null;
        if(pageData.containsKey("organizationId")){
            int organizationId=Integer.parseInt(pageData.getMap().get("organizationId"));
            organization=wechatsugService.selectByOrId(organizationId);
            if(organization!=null){
                pageData.put("path",organization.getPath()+",");
            }
            List<WindowOutput> windowOutputList = wechatsugService.selectAll(pageData);
            return ResponseResult.success(windowOutputList);
        }
        return ResponseResult.error("无数据");
    }

    @RequestMapping(value = "add",method = RequestMethod.POST)
    public ResponseResult add( @Validated @RequestBody @ApiParam(name="投诉建议",value="传入json格式",required=true) Suggestions suggestions){

            //新增时从数据库中查找是否存在手机号、投诉人姓名、投诉部门、投诉窗口相同且类型为短信、微信或者二次回复的数据，若存在则将投诉内容关联这个主键存入content表中
            suggestions.setDateResource(AppConsts.Resource_Wechat);
            //设置成未处理
            suggestions.setDealState(AppConsts.DealState_No);
            //设置成未发布
            suggestions.setPublish(AppConsts.Publish_No);
            //设置成未回复
            suggestions.setReplyType(AppConsts.Reply_Un);
            //设置成未逾期
            suggestions.setOutOfDate(AppConsts.OutOfDate_No);

            suggestions.setAmputated(0);
            suggestions.setCreatedDateTime(new Date());
            suggestions.setCreatedUserId(0);
            suggestions.setCreatedUserName("微信端投诉建议");
            suggestions.setLastUpdateDateTime(new Date());
            suggestions.setLastUpdateUserId(0);
            suggestions.setLastUpdateUserName("微信端投诉建议");

            Integer orgaId = wechatsugService.selectByOrgName(suggestions.getOrganizationName().trim());
            if(orgaId < 0){
                return ResponseResult.error("该部门没有找到");
            }
            suggestions.setOrganizationId(orgaId);

            //查询指定回复人的id
            Integer replayId = wechatsugService.selectReplayId(orgaId);
            if(replayId == null){
                suggestions.setReplyUserId(null);
            }
            //设置指定回复人的id
            suggestions.setReplyUserId(replayId);

            //如果上级组织name不为空
            if(suggestions.getUpperOrganizationName()!=null&&suggestions.getUpperOrganizationName()!=""){
                Integer upOrgaId = wechatsugService.selectByOrgName(suggestions.getUpperOrganizationName());
                if(upOrgaId<0){
                    return ResponseResult.error("该上级部门没有找到");
                }
                suggestions.setUpperOrganizationId(upOrgaId);
            }

            //根据投诉工号查询出员工的姓名和id
//            if(suggestions.getEmployeeNo()!=null&&suggestions.getEmployeeNo()!=""){
//                SuggesstionsOutput suggesstionsOutput = wechatsugService.selectByEmpNo(suggestions.getEmployeeNo().trim());
//                if(suggesstionsOutput == null){
//                    return ResponseResult.error("该员工编号没有查到对应的员工");
//                }
//                suggestions.setEmployeeId(suggesstionsOutput.getEmployeeId());
//                suggestions.setEmployeeName(suggesstionsOutput.getEmployeeName());
//            }
            Integer suggesstionId = wechatsugService.add(suggestions);
            if(suggesstionId<0){
                return ResponseResult.error("新增失败");
            }
            List<Attachment> attachmentList=suggestions.getAttachmentList();
            List<Attachment> attachmentInput=new ArrayList<>();
            if(attachmentList!=null&&attachmentList.size()>0){
                for(Attachment attachment:attachmentList){
                    attachment.setAmputated(0);
                    attachment.setCreatedUserId(0);
                    attachment.setCreatedUserName("微信上传的附件");
                    attachment.setCreatedDateTime(new Date());
                    attachment.setLastUpdateUserId(0);
                    attachment.setLastUpdateDateTime(new Date());
                    attachment.setLastUpdateUserName("微信上传的附件");
                    attachment.setResourcesId(suggesstionId);
                    attachment.setResourcesType(AttachmentEnum.COMPLAINT_TYPE.getCode());
                    attachment.setSourceFileName(attachment.getSuffix());
                    attachmentInput.add(attachment);
                }
//                PageData pageData = new PageData();
//                pageData.put("attachmentList",attachmentInput);
//                ResponseResult result = HttpRequestUtil.sendPostRequest("http://10.32.250.88:8770/attachment/attachmentApi",pageData);
//                if(result.getCode() != 200){
//                    return ResponseResult.error("上传附件失败");
//                }

                HashSet<String> objects = new HashSet<>();
                for (Attachment attachment:attachmentList) {
                    objects.add(attachment.getResourcesId() + "/" + attachment.getResourcesType());
                }
                Iterator<String> it = objects.iterator();
                while(it.hasNext()) {
                    String next = it.next();
                    String[] split = next.split("/");
                    attachmentRepository.deleteAllByResourcesIdAndResourcesType(Integer.valueOf(split[0]),Integer.valueOf(split[1]));
                }
                var result = attachmentRepository.saveAll(attachmentList).size();
                if(result<=0){
                    return ResponseResult.error("上传附件失败");
                }
            }
        return ResponseResult.success();
    }
}
