package com.message.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.message.core.base.BaseController;
import com.message.core.base.BaseService;
import com.message.mapper.jpa.EmpTelRepository;
import com.message.model.EmpTel;
import com.message.service.EmpTelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value = "/msg")
@Api(value = "批量发短信controller",description = "批量发短信操作",tags = {"批量发短信操作接口"})
public class EmpTelController extends BaseController<EmpTel, EmpTel,Integer> {

    @Autowired
    private EmpTelService empTelService;
    @Autowired
    private EmpTelRepository repository;


    /**
     *
     * @param files
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "importMobilephone", method = RequestMethod.POST)
    public ResponseResult importemployees(MultipartFile files) throws Exception {
        ResponseResult result = new ResponseResult();
        if (files == null ) {
            result.setMessage("上传文件为空");
            result.setSuccess(false);
            result.setCode(500);
            return result;
        }
        String a=empTelService.checkedFile(files);
        if(!a.equals("操作成功")){
            result.setSuccess(false);
            result.setCode(500);
            result.setMessage(a);
            return result;
        }
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("导入成功");
        return result;
    }


    @RequestMapping(value = "selectAlls", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name="keyword",value="接收人姓名",required=false,dataType="string", paramType = "query")})
    public ResponseResult selectAlls(HttpServletRequest request) {
        PageData pageData = new PageData(request);
        return empTelService.selectPageInfo(pageData);

    }


    @Override
    public BaseService<EmpTel, EmpTel, Integer> getService() {
        return null;
    }
}
