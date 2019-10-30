package com.meeting.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.meeting.core.base.BaseController;
import com.meeting.core.base.BaseService;
import com.meeting.domain.output.PitOutput;
import com.meeting.model.Pit;
import com.meeting.service.PitService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/pit")
public class PitController extends BaseController<PitOutput, Pit,Integer> {

    @Autowired
    private PitService pitService;
    @Override
    public BaseService<PitOutput, Pit, Integer> getService() {
        return pitService;
    }

    @Override
    @ApiOperation("新增或修改会议室信息")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id, @RequestBody Pit pit) throws Exception {
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        if(pit.getIpList()!=null&&pit.getIpList().size()>0){
            StringBuilder a=new StringBuilder();
            for(String s:pit.getIpList()){
                a.append(s+",");
                if(!s.matches(regex)){
                    return ResponseResult.error("ip格式错误");
                }
                if(id==null){
                    if(pitService.getByIp(s).size()>0){
                        return ResponseResult.error("ip重复");
                    }
                }else {
                    Pit pit1=pitService.getById(id);
                    if(pit1.getIp().indexOf(s)==-1){
                        if(pitService.getByIp(s).size()>0){
                            return ResponseResult.error("ip重复");
                        }
                    }
                }
            }
            pit.setIp(a.toString().substring(0,a.toString().length()-1));
        }else {
            return ResponseResult.error("ip不能为空");
        }
        if(id==null){
            pit.setDisplayOrder(0);
            if(pitService.getByName(pit.getName()).size()>0){
                return ResponseResult.error("名称重复");
            }
        }else {
            Pit pit1=pitService.getById(id);
            if(!pit1.getName().equals(pit1.getName())){
                if(pitService.getByName(pit.getName()).size()>0){
                    return ResponseResult.error("名称重复");
                }
            }
        }
        return super.formPost(id,pit);
    }


    @Override
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException, ParseException {
        PageData pageData=new PageData();
        if (idList == null || idList.length() <= 0) {
            return ResponseResult.error(PARAM_EORRO);
        }else{
            if(idList.split(",").length>1){
                return ResponseResult.error("只能单个删除");
            }
        }
        return super.logicDelete(idList);
    }

    @Override
    @GetMapping(value = "get")
    public ResponseResult get(Integer id) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        return super.selectById(id);
    }



    @ApiOperation("获取分页")
    @GetMapping(value = "findPageList")
    @ApiImplicitParams({@ApiImplicitParam(name="name",value="名称",required=false,dataType="string", paramType = "query")})
    public ResponseResult findPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return super.selectPageList(pageData);
    }


}
