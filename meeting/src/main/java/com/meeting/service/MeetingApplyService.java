package com.meeting.service;

import com.common.Enum.MessageTypeEnum;
import com.common.model.PageData;
import com.common.request.ServiceCall;
import com.common.utils.GetMessageTemplate;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.meeting.core.base.BaseMapper;
import com.meeting.core.base.BaseService;
import com.meeting.core.base.MybatisBaseMapper;
import com.meeting.domain.output.MeetingApplyOutput;
import com.meeting.domain.output.MeetingRoomOutput;
import com.meeting.domain.output.Member;
import com.meeting.mapper.jpa.MeetingApplyRepository;
import com.meeting.mapper.mybatis.AttachmentMapper;
import com.meeting.mapper.mybatis.MeetingApplyMapper;
import com.meeting.mapper.mybatis.MeetingRoomMapper;
import com.meeting.model.Attachment;
import com.meeting.model.MeetingApply;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class MeetingApplyService extends BaseService<MeetingApplyOutput, MeetingApply, Integer> {
    @Autowired
    private MeetingApplyRepository repository;

    @Autowired
    private MeetingApplyMapper meetingApplyMapper;
    @Autowired
    private AttachmentMapper attachmentMapper;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private MeetingRoomMapper meetingRoomMapper;

    @Override
    public BaseMapper<MeetingApply, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<MeetingApplyOutput> getMybatisBaseMapper() {
        return meetingApplyMapper;
    }


    public String namelist(List<Integer> ids) {
        List<Member> s = meetingApplyMapper.selectByids(ids);
        StringBuilder sb = new StringBuilder();
        if (s.size() > 0) {
            for (Member member : s) {
                sb.append(member.getId() + "," + member.getName() + ";");
            }
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    @Override
    public MeetingApplyOutput getById(Integer id) {
        MeetingApplyOutput meetingApplyOutput = meetingApplyMapper.selectByPrimaryKey(id);
        if(meetingApplyOutput==null){
            return null;
        }
        List<Member> members = new ArrayList<>();
        //与会人员
        if (meetingApplyOutput.getAttendantsName() != null) {
            String[] strings = meetingApplyOutput.getAttendantsName().split(";");
            if (strings.length > 0) {
                for (String s : strings) {
                    Member member = new Member();
                    String[] strings1 = s.split(",");
                    member.setId(Integer.parseInt(strings1[0]));
                    member.setName(strings1[1]);
                    members.add(member);
                }
            }
        }
        //退休老干部
        if(StringUtils.isNotBlank(meetingApplyOutput.getRetirees())){
            String[] ids = meetingApplyOutput.getRetirees().split(",");
            List<Integer> idlist = new ArrayList<Integer>();
            for (String s : ids) {
                idlist.add(Integer.parseInt(s));
            }
            meetingApplyOutput.setAddressBookList(addressBookList(idlist));
        }
        if (meetingApplyOutput.getCreatedUserId().equals(getUsers().getId()) || meetingApplyOutput.getEmployeesId().equals(getUsers().getId())) {
            meetingApplyOutput.setIsCreator(1);
        } else {
            meetingApplyOutput.setIsCreator(0);
        }
        meetingApplyOutput.setMemberList(members);
        List<Attachment> attachmentList = attachmentMapper.selectByApplyId(id);
        meetingApplyOutput.setAttachmentList(attachmentList);
        return meetingApplyOutput;
    }

    public List<MeetingApplyOutput> getByDate(PageData pageData) {
        List<MeetingApplyOutput> pageList = meetingApplyMapper.selectAll(pageData);
        return pageList;
    }

    public List<MeetingApplyOutput> getMyMeetingApply(PageData pageData) {
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        Page<MeetingApplyOutput> pageList = meetingApplyMapper.selectMyMeetingApply(pageData);
        return pageList;
    }

    public List<MeetingApplyOutput> getByRoomId(PageData pageData) {
        List<MeetingApplyOutput> pageList = meetingApplyMapper.selectByRoomId(pageData);
        return pageList;
    }

    public String findTemplateByKey(String type) {
        PageData pageData = new PageData();
        pageData.put("type", type);
        var result = ServiceCall.getResult(loadBalancerClient, "/smstemplate/findByType", "message", pageData);
        if (result.getCode() != 200) {
            return null;
        }
        return (String) result.getData();
    }

    private String getContent(String type, MeetingApplyOutput meetingApply, String name) {
        String content = findTemplateByKey(type);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tp = simpleDateFormat.format(meetingApply.getAppointmentDate());
        String[] strings = tp.split("-");
        if (content != null) {
            var map = GetMessageTemplate.getKey(content);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                switch (entry.getKey()) {
                    case "n"://年
                        entry.setValue(strings[0]);
                        break;
                    case "y"://月
                        entry.setValue(strings[1]);
                        break;
                    case "r"://日
                        entry.setValue(strings[2]);
                        break;
                    case "d"://点
                        entry.setValue(meetingApply.getRealStartTime()==null?meetingApply.getTimes():meetingApply.getRealStartTime());
                        break;
                    case "hymc"://会议名称
                        entry.setValue(meetingApply.getTheme());
                        break;
                    case "hys"://会议室名称
                        entry.setValue(meetingApply.getRoomName());
                        break;
                    case "yhry"://与会人员
                        entry.setValue(name);
                        break;
                    case "sqr"://申请人
                        entry.setValue(name);
                        break;
                    case "zbdw"://主办单位
                        entry.setValue(meetingApply.getOrganizationName());
                        break;
                        default:
                }
            }
            String rContent = GetMessageTemplate.getContent(content, map);
            String des = meetingApply.getDescription();
            if(StringUtils.isNotBlank(des)){
                rContent = GetMessageTemplate.getContent(content, map) + meetingApply.getDescription();
            }
            return rContent;
        }
        return null;
    }

    public void sendMassage(MeetingApplyOutput meetingApply, String type) {
        PageData pageData = new PageData();

        //通知与会人员
        if (meetingApply.getAttendants() != null && !meetingApply.getAttendants().equals("")) {
            String[] ids = meetingApply.getAttendants().split(",");
            List<Integer> idlist = new ArrayList<Integer>();
            for (String s : ids) {
                idlist.add(Integer.parseInt(s));
            }
            List<Member> members = members(idlist);
            if (!CollectionUtils.isEmpty(members)) {
                for (Member member : members) {
                    List<String> list = new ArrayList<>();
                    list.add(member.getPhoneNumber() + "/" + member.getName());
                    String description=getContent(type, meetingApply, member.getName());
                    if(description==null||description.equals("")){
                        continue;
                    }
                    pageData.put("description", getContent(type, meetingApply, member.getName()));
                    pageData.put("isTiming", 0);
                    pageData.put("type", MessageTypeEnum.MEET_REMINDER.getCode());
                    pageData.put("sendList", list);
                    try {
                        var r = ServiceCall.postResult(loadBalancerClient, "/smsSend/form", "message", pageData);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }
        //通知退休老干部
        if (meetingApply.getRetirees() != null && !meetingApply.getRetirees().equals("")) {
            String[] ids = meetingApply.getRetirees().split(",");
            List<Integer> idlist = new ArrayList<Integer>();
            for (String s : ids) {
                idlist.add(Integer.parseInt(s));
            }
            List<Member> members = addressBookList(idlist);
            if (members != null && members.size() > 0) {
                for (Member member : members) {
                    List<String> list = new ArrayList<>();
                    list.add(member.getPhoneNumber() + "/" + member.getName());
                    String description=getContent(type, meetingApply, member.getName());
                    if(description==null||description.equals("")){
                        continue;
                    }
                    pageData.put("description", getContent(type, meetingApply, member.getName()));
                    pageData.put("isTiming", 0);
                    pageData.put("type", MessageTypeEnum.MEET_REMINDER.getCode());
                    pageData.put("sendList", list);
                    try {
                        var r = ServiceCall.postResult(loadBalancerClient, "/smsSend/form", "message", pageData);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

//    @Async
    public void sendMassages(MeetingApplyOutput meetingApply, String type1, String type2) {
        PageData pageData = new PageData();

        MeetingRoomOutput meetingRoom=meetingRoomMapper.selectByPrimaryKey(meetingApply.getMeetingRoomId());
        if (meetingApply.getIsNeedTea() != null && meetingApply.getIsNeedTea() == 1) {
            pageData.put("key", "ConferencePhone");
            List<String> list = new ArrayList<>();
//            result = ServiceCall.getResult(loadBalancerClient, "/config/getByKey", "bigdata", pageData).getData();
//            String value = JsonUtils.getMap(result, "configValue").get("configValue");
//            String configNo = JsonUtils.getMap(result, "configNo").get("configNo");
            if(meetingRoom.getTeaPhone()!=null&&!meetingRoom.getTeaPhone().equals("")
                    && meetingRoom.getTeaName()!=null && !meetingRoom.getTeaName().equals("")){
                list.add(meetingRoom.getTeaPhone() + "/" + meetingRoom.getTeaName());
                pageData.clear();
                String description= getContent(type1, meetingApply, meetingRoom.getTeaName());
                if(description!=null&&!description.equals("")){
                    pageData.put("description",description);
                    pageData.put("isTiming", 0);
                    pageData.put("type", MessageTypeEnum.MEET_REMINDER.getCode());
                    pageData.put("sendList", list);

                    try {
                        var r = ServiceCall.postResult(loadBalancerClient, "/smsSend/form", "message", pageData);
                    } catch (Exception e) {

                    }
                }
            }


        }
        if (meetingApply.getIsNeedNeetwork() != null && meetingApply.getIsNeedNeetwork() == 1) {
            pageData.clear();
            pageData.put("key", "ITPhone");
            List<String> list = new ArrayList<>();
//            result = ServiceCall.getResult(loadBalancerClient, "/config/getByKey", "bigdata", pageData).getData();
//            String value = JsonUtils.getMap(result, "configValue").get("configValue");
//            String configNo = JsonUtils.getMap(result, "configNo").get("configNo");
            if(StringUtils.isNotBlank(meetingRoom.getItName())&&StringUtils.isNotBlank(meetingRoom.getItPhone())){
                list.add(meetingRoom.getItPhone() + "/" + meetingRoom.getItName());
                pageData.clear();
                String description= getContent(type2, meetingApply, meetingRoom.getItName());
                if(StringUtils.isNotBlank(description)){
                    pageData.put("description",description);
                    pageData.put("isTiming", 0);
                    pageData.put("type", MessageTypeEnum.MEET_REMINDER.getCode());
                    pageData.put("sendList", list);

                    try {
                         ServiceCall.postResult(loadBalancerClient, "/smsSend/form", "message", pageData);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            }

        }

    }
//    @Async
    public void sendMassageToSqr(MeetingApplyOutput meetingApply, String type) {
        PageData pageData = new PageData();
        List<String> list = new ArrayList<>();
        list.add(meetingApply.getPhoneNumber() + "/" + meetingApply.getEmployeesName());
        String description=getContent(type, meetingApply, meetingApply.getEmployeesName());
        if(description!=null&&!description.equals("")){
            pageData.put("description", getContent(type, meetingApply, meetingApply.getEmployeesName()));
            pageData.put("isTiming", 0);
            pageData.put("type", MessageTypeEnum.MEET_REMINDER.getCode());
            pageData.put("sendList", list);
            try {
                var r = ServiceCall.postResult(loadBalancerClient, "/smsSend/form", "message", pageData);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }


    public List<Member> members(List<Integer> ids) {
        List<Member> string = meetingApplyMapper.selectByids(ids);
        return string;
    }

    public List<Member> addressBookList(List<Integer> ids) {
        List<Member> string = meetingApplyMapper.selectByIds(ids);
        return string;
    }

    public List<MeetingApplyOutput> getByRoomId(Integer id) {
        List<MeetingApplyOutput> pageList = meetingApplyMapper.selectOutputByRoomId(id);
        return pageList;
    }



    public String  getRetireesName(List<Integer> ids) {
        List<Member> s = meetingApplyMapper.selectByIds(ids);
        StringBuilder sb = new StringBuilder();
        if (s.size() > 0) {
            for (Member member : s) {
                sb.append(member.getId() + "," + member.getName() + ";");
            }
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }
}
