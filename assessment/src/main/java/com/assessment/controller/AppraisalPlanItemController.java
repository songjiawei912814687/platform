package com.assessment.controller;

import com.assessment.core.base.BaseController;
import com.assessment.core.base.BaseService;
import com.assessment.domian.input.AppraisalPlanItemAttachmentInput;
import com.assessment.domian.input.AppraisalPlanItemInput;
import com.assessment.domian.input.FormDescrptionInput;
import com.assessment.domian.input.PlanItemAdditionInput;
import com.assessment.domian.output.AppraisalPlanItemOutput;
import com.assessment.model.AppraisalPlan;
import com.assessment.model.AppraisalPlanItem;
import com.assessment.service.AppraisalPlanItemService;
import com.assessment.service.AppraisalPlanService;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/appraisalplanitem")
@Api(value = "考核计划明细controller",description = "考核计划明细操作",tags = {"考核计划明细接口"})
public class AppraisalPlanItemController extends BaseController<AppraisalPlanItemOutput, AppraisalPlanItem,Integer> {
    @Autowired
    private AppraisalPlanItemService appraisalPlanItemService;
    @Override
    public BaseService<AppraisalPlanItemOutput, AppraisalPlanItem, Integer> getService() {
        return appraisalPlanItemService;
    }

    @Override
    @ApiOperation("修改考核计划明细")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id, @Validated @RequestBody @ApiParam(name="考核明细",value="传入json格式",required=true) AppraisalPlanItem appraisalPlanItem) throws Exception {
        if(id==null||appraisalPlanItem.getQuantity()==null||"".equals(appraisalPlanItem.getQuantity())){
            return ResponseResult.error(PARAM_EORRO);
        }
        return  appraisalPlanItemService.updatePlanAndDetail(id,appraisalPlanItem);
    }

    @ApiOperation("修改考核计划明细")
    @RequestMapping(value = "formOrgScore", method = RequestMethod.POST)
    public ResponseResult formOrgScore(@RequestBody @ApiParam(name="考核明细",value="传入json格式",required=true) AppraisalPlanItemInput appraisalPlanItemInput) throws Exception {
        if(appraisalPlanItemInput.getAppraisalPlanItem().getId()==null||appraisalPlanItemInput.getAppraisalPlanItem().getQuantity()==null){
            return ResponseResult.error(PARAM_EORRO);
        }
        return  appraisalPlanItemService.updatePlanAndDetail(appraisalPlanItemInput.getAppraisalPlanItem().getId(),appraisalPlanItemInput.getAppraisalPlanItem());
    }

    @ApiOperation("修改考核计划状态")
    @RequestMapping(value = "updateState", method = RequestMethod.POST)
    public ResponseResult updateState(Integer id,Integer state) throws Exception {
        if(id==null||state==null||"".equals(id)||"".equals(state)){
            return ResponseResult.error(PARAM_EORRO);
        }
        return  appraisalPlanItemService.updateState(id,state);

    }

    @ApiOperation("新增或修改评分说明")
    @RequestMapping(value = "formDescrption", method = RequestMethod.POST)
    public ResponseResult formDescrption(Integer id, String description) throws Exception {
        if(id==null||id.equals("")){
            return ResponseResult.error(PARAM_EORRO);
        }
        AppraisalPlanItem appraisalPlanItem1=getService().getById(id);
        appraisalPlanItem1.setRatingDescription(description);
        return  super.formPost(id,appraisalPlanItem1);
    }

    @ApiOperation("新增或修改评分说明")
    @RequestMapping(value = "formDescrptionInput", method = RequestMethod.POST)
    public ResponseResult formDescrptionInput(@RequestBody FormDescrptionInput formDescrptionInput) throws Exception {
        if(formDescrptionInput.getId()==null){
            return ResponseResult.error(PARAM_EORRO);
        }
        AppraisalPlanItem appraisalPlanItem1=getService().getById(formDescrptionInput.getId());
        appraisalPlanItem1.setRatingDescription(formDescrptionInput.getDescription());
        return  super.formPost(formDescrptionInput.getId(),appraisalPlanItem1);
    }

    @GetMapping(value = "findByPlanId")
    @ApiOperation("获取考核明细列表")
    public ResponseResult selectPageList(Integer planId){
        if(planId==null
                ||planId.equals("获取考核明细列表失败")){
            return ResponseResult.error("");
        }
        return ResponseResult.success(appraisalPlanItemService.getByPlanId(planId));
    }

    /**
     * 导出员工考核明细信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "employeesPlanItemExport", method = RequestMethod.GET)
    public ResponseResult employeesPlanItemExport(HttpServletResponse response, HttpServletRequest request) {
        try {
            PageData pageData=new PageData(request);
            if(pageData.getMap().get("planId")==null
                    ||pageData.getMap().get("planId").equals("")){
                return ResponseResult.error("导出考核明细失败失败");
            }
            String str = appraisalPlanItemService.EmployeesPlanItemExport(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 导出部门考核明细信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "departmentPlanItemExport", method = RequestMethod.GET)
    public ResponseResult departmentPlanItemExport(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str = appraisalPlanItemService.departmentPlanItemExport(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @ApiOperation("保存考核明细附件")
    @RequestMapping(value = "saveAddtition", method = RequestMethod.POST)
    public ResponseResult saveAddtition(Integer id,  @RequestBody @ApiParam(name="考核明细",value="传入json格式",required=true) PlanItemAdditionInput planItemAdditionInput) throws Exception {
        if(id==null){
            return ResponseResult.error(PARAM_EORRO);
        }
        return  appraisalPlanItemService.saveAddtition(id,planItemAdditionInput);
    }

    @ApiOperation("投诉建议系统远程保存考核明细附件")
    @RequestMapping(value = "saveAppraisalPlanItemAttachment", method = RequestMethod.POST)
    public ResponseResult saveAppraisalPlanItemAttachment( @RequestBody AppraisalPlanItemAttachmentInput appraisalPlanItemAttachmentInput) throws Exception {
        if(appraisalPlanItemAttachmentInput.getPlanItemId()==null){
            return ResponseResult.error(PARAM_EORRO);
        }
        return  appraisalPlanItemService.saveAppraisalPlanItemAttachment(appraisalPlanItemAttachmentInput);
    }

    @ApiOperation("获得考核明细附件")
    @RequestMapping(value = "getAdditionById", method = RequestMethod.GET)
    public ResponseResult getAdditionById(Integer id) throws Exception {
        if(id==null){
            return ResponseResult.error(PARAM_EORRO);
        }
        return  appraisalPlanItemService.getAdditionById(id);
    }


    /**
     * 导出人均办件量
     *
     * @param
     * @return
     */
    @RequestMapping(value = "banjianExport", method = RequestMethod.GET)
    public ResponseResult banjianExport(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str = appraisalPlanItemService.banjianExport(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 导出“一件事”联办率
     *
     * @param
     * @return
     */
    @RequestMapping(value = "liangbanExport", method = RequestMethod.GET)
    public ResponseResult liangbanExport(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str = appraisalPlanItemService.liangbanExport(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 导出五办实现率
     *
     * @param
     * @return
     */
    @RequestMapping(value = "wubanshixianExport", method = RequestMethod.GET)
    public ResponseResult wubanshixianExport(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str = appraisalPlanItemService.wubanshixianExport(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
