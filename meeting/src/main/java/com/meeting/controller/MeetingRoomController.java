package com.meeting.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.meeting.core.base.BaseController;
import com.meeting.core.base.BaseService;
import com.meeting.domain.output.MeetingApplyOutput;
import com.meeting.domain.output.MeetingRoomOutput;
import com.meeting.model.MeetingRoom;
import com.meeting.service.MeetingApplyService;
import com.meeting.service.MeetingRoomService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/meetingroom")
public class MeetingRoomController extends BaseController<MeetingRoomOutput, MeetingRoom,Integer> {

    @Autowired
    private MeetingRoomService meetingRoomService;

    @Autowired
    private MeetingApplyService meetingApplyService;
    @Override
    public BaseService<MeetingRoomOutput, MeetingRoom, Integer> getService() {
        return meetingRoomService;
    }

    @Override
    @ApiOperation("新增或修改会议室信息")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id, @Validated @RequestBody @ApiParam(name="职务信息",value="传入json格式",required=true) MeetingRoom meetingRoom) throws Exception {
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        if(meetingRoom.getIpList()!=null&&meetingRoom.getIpList().size()>0){
            StringBuilder a=new StringBuilder();
            for(String s:meetingRoom.getIpList()){
                a.append(s+",");
                if(!s.matches(regex)){
                    return ResponseResult.error("会议室ip格式错误");
                }
                if(id==null){
                    if(meetingRoomService.getByIp(s).size()>0){
                        return ResponseResult.error("会议室ip重复");
                    }
                }else {
                    MeetingRoom meetingRoom1=meetingRoomService.getById(id);
                    if(meetingRoom1.getIp()==null||meetingRoom1.getIp().indexOf(s)==-1){
                        if(meetingRoomService.getByIp(s).size()>0){
                            return ResponseResult.error("会议室ip重复");
                        }
                    }
                }
            }
            meetingRoom.setIp(a.toString().substring(0,a.toString().length()-1));
        }else {
            meetingRoom.setIp("");
        }
        if(id==null){
            meetingRoom.setState(0);
            meetingRoom.setDisplayOrder(0);
            if(meetingRoomService.getByName(meetingRoom.getName()).size()>0){
                return ResponseResult.error("会议室名称重复");
            }
        }else {
            MeetingRoom meetingRoom1=meetingRoomService.getById(id);
            if(!meetingRoom1.getName().equals(meetingRoom.getName())){
                if(meetingRoomService.getByName(meetingRoom.getName()).size()>0){
                    return ResponseResult.error("会议室名称重复");
                }
            }
        }
        return super.formPost(id,meetingRoom);
    }


    @Override
    @ApiOperation("删除会议室信息")
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException, ParseException {
        if (idList == null || idList.length() <= 0) {
            return ResponseResult.error(PARAM_EORRO);
        }else{
            if(idList.split(",").length>1){
                return ResponseResult.error("会议室只能单个删除");
            }else{
              List<MeetingApplyOutput> list=meetingApplyService.getByRoomId(Integer.parseInt(idList));
              if(list!=null&&list.size()>0){
                  return ResponseResult.error("会议室下还有会议,不能删除");
              }
            }
        }
        return super.logicDelete(idList);
    }

    @Override
    @ApiOperation("根据id获取单个会议室")
    @GetMapping(value = "get")
    public ResponseResult get(Integer id) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        return super.selectById(id);
    }


    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的会议室")
    @ApiImplicitParams({@ApiImplicitParam(name="name",value="会议室名称",required=false,dataType="string", paramType = "query")})
    public ResponseResult findPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return super.selectPageList(pageData);
    }

    @GetMapping(value = "findByTime")
    public ResponseResult selectByTime(HttpServletRequest request){
        return  ResponseResult.success(meetingRoomService.getByTime(new PageData(request)));
    }


    /**
     * 导出会议室
     *
     * @param
     * @return
     */
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ResponseResult export(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str = meetingRoomService.export(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
