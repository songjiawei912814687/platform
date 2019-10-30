package com.meeting.service;

import com.common.model.PageData;
import com.meeting.core.base.BaseMapper;
import com.meeting.core.base.BaseService;
import com.meeting.core.base.MybatisBaseMapper;
import com.meeting.domain.output.Member;
import com.meeting.domain.output.PitApplyOutput;
import com.meeting.mapper.jpa.PitApplyRepository;
import com.meeting.mapper.mybatis.AttachmentMapper;
import com.meeting.mapper.mybatis.MeetingApplyMapper;
import com.meeting.mapper.mybatis.PitApplyMapper;
import com.meeting.model.Attachment;
import com.meeting.model.PitApply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PitApplyService extends BaseService<PitApplyOutput, PitApply,Integer> {
    @Autowired
    private PitApplyRepository repository;

    @Autowired
    private PitApplyMapper pitApplyMapper;

    @Autowired
    private MeetingApplyMapper meetingApplyMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;
    @Override
    public BaseMapper<PitApply, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<PitApplyOutput> getMybatisBaseMapper() {
        return pitApplyMapper;
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

    public PitApplyOutput getById(Integer id) {
        PitApplyOutput pitApplyOutput = pitApplyMapper.selectByPrimaryKey(id);
        if(pitApplyOutput==null){
            return null;
        }
        List<Member> members = new ArrayList<>();
        //与会人员
        if (pitApplyOutput.getAttendantsName() != null) {
            String[] strings = pitApplyOutput.getAttendantsName().split(";");
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
        if (pitApplyOutput.getCreatedUserId().equals(getUsers().getId()) || pitApplyOutput.getEmployeesId().equals(getUsers().getId())) {
            pitApplyOutput.setIsCreator(1);
        } else {
            pitApplyOutput.setIsCreator(0);
        }
        pitApplyOutput.setMemberList(members);
        List<Attachment> attachmentList = attachmentMapper.selectByPitId(id);
        pitApplyOutput.setAttachmentList(attachmentList);
        return pitApplyOutput;
    }


    public List<PitApplyOutput> getByRoomId(PageData pageData) {
        List<PitApplyOutput> pageList = pitApplyMapper.selectByRoomId(pageData);
        return pageList;
    }

    public List<PitApplyOutput> getByDate(PageData pageData) {
        List<PitApplyOutput> pageList = pitApplyMapper.selectAll(pageData);
        return pageList;
    }
}
