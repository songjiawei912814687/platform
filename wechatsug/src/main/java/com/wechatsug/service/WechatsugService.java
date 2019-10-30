package com.wechatsug.service;

import com.common.Enum.MessageTypeEnum;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.HttpRequestUtil;
import com.wechatsug.config.RedisComponent;
import com.wechatsug.domain.output.OrganizationOutput;
import com.wechatsug.domain.output.SuggesstionsOutput;
import com.wechatsug.domain.output.WindowOutput;
import com.wechatsug.enums.IsTimingEnum;
import com.wechatsug.enums.SendMessageEnum;
import com.wechatsug.mapper.mybatis.OrganizationMapper;
import com.wechatsug.mapper.mybatis.SuggestionsMapper;
import com.wechatsug.mapper.mybatis.WindowMapper;
import com.wechatsug.mapper.repository.CheckMessageRepository;
import com.wechatsug.mapper.repository.OrganizationRepository;
import com.wechatsug.mapper.repository.SMSSendRepository;
import com.wechatsug.mapper.repository.SuggesstionsRepository;
import com.wechatsug.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-03  00:48
 * @modified by:
 */
@RestController
@RequestMapping
@Transactional
public class WechatsugService {

    @Autowired
    private SuggestionsMapper suggestionsMapper;
    @Autowired
    private SuggesstionsRepository suggesstionsRepository;
    @Autowired
    private WindowMapper windowMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private SMSSendRepository smsSendRepository;

    @Autowired
    private RedisComponent redisComponent;


    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final Logger log = LoggerFactory.getLogger(WechatsugService.class);

    public SuggesstionsOutput isSameSuggesstion(Suggestions suggestions) {
        suggestions.setAmputated(0);
        suggestions.setDealState(AppConsts.DealState_No);
        List<SuggesstionsOutput> list = suggestionsMapper.selectByMainInfo(suggestions);
        if(list!=null&&list.size()>0){
            return list.get(0);
        }else{
            return  null;
        }
    }

    public Integer selectByOrgName(String orgName){
        Integer orId = suggestionsMapper.selectOrIdByOrName(orgName);
        if(orId == null){
            return -1;
        }
        return orId;
    }

    public Integer selectReplayId(Integer orgId){
        Organization organization = organizationMapper.selectOrNoByOrId(orgId);
        if(organization.getDepartmentalManager()==null){
            return null;
        }
        return organization.getDepartmentalManager();
    }

    public Integer selectByWindowName(Integer orgId,String windowNo){
        Integer windowId =suggestionsMapper.selectByWindowName(orgId,windowNo);
        if(windowId == null){
            return -1;
        }
        return windowId;
    }

    public List<Organization> getAll(){
        List<Organization> all = organizationRepository.findAll();
        if(all == null||all.size() == 0){
            return null;
        }
        return all;
    }

    public List<OrganizationOutput> Assembly(List<Organization> all) {
        ArrayList<OrganizationOutput> parentArray = new ArrayList<>();
        if(all==null){
            return null;
        }
        for (Organization o:all) {
            if(o.getParentId()==0){
                parentArray.add(new OrganizationOutput(o));
            }
        }
        return getTreeData(all,parentArray);
    }

    private List<OrganizationOutput> getTreeData(List<Organization> list, List<OrganizationOutput> parents) {
        for (OrganizationOutput parentOrg:parents) {
            List<OrganizationOutput> childs = new ArrayList<>();

            for (Organization o:list) {
                if(parentOrg.getId().equals(o.getParentId())){
                    childs.add(new OrganizationOutput(o));
                }
            }
            parentOrg.setChildren(childs.size()==0?null:childs);
            if(childs.size()>0){
                getTreeData(list,childs)  ;
            }
        }
        return parents;
    }


    public SuggesstionsOutput selectByEmpNo(String empNo){
        SuggesstionsOutput suggesstionsOutput = suggestionsMapper.selectByEmpNo(empNo);
        if(suggesstionsOutput == null){
            return null;
        }
        return suggesstionsOutput;
    }

    public Organization selectByOrId(Integer orgId){
        Organization organization = organizationMapper.selectOrNoByOrId(orgId);
        if(organization == null){
            return null;
        }
        return organization;
    }

    public List<WindowOutput>selectAll(PageData pageData){
        List<WindowOutput> windowOutputList = windowMapper.selectAll(pageData);
        if(windowOutputList==null||windowOutputList.size()==0){
            return windowOutputList;
        }
        return windowOutputList;
    }


    public int add(Suggestions suggestions){
        Integer suggId = suggesstionsRepository.save(suggestions).getId();
        if(suggId < 0){
            return -1;
        }
        return suggId;
    }

    public Suggestions findById(Integer id){
        Suggestions suggesstionscurrent = suggesstionsRepository.findSuggestionsById(id);
        if(suggesstionscurrent == null){
            return null;
        }
        return suggesstionscurrent;
    }


    private void setValueToRedis(SMSSend sMSSend) {
        if (sMSSend.getIsTiming().equals(IsTimingEnum.YES.getCode())) {
            Date date = new Date();

            if (sMSSend.getTimingTime() != null) {
                long l = sMSSend.getTimingTime().getTime() - date.getTime();
                if (l < 0) {
                    redisComponent.sentinelSet("HZFT_MESSAGE:" + sMSSend.getId(), "", 5L);
                }
                l = l / 1000;

                redisComponent.sentinelSet("HZFT_MESSAGE:" + sMSSend.getId(), "", l);
            } else {
                redisComponent.sentinelSet("HZFT_MESSAGE:" + sMSSend.getId(), "", 5L);
            }
        } else {
            redisComponent.sentinelSet("HZFT_MESSAGE:" + sMSSend.getId(), "", 5L);
        }
    }


    public void send(Integer messageId) {

        var smsSend = smsSendRepository.findSMSSendById(messageId);
        if(smsSend == null){
            return;
        }
        Map<String, Object> map = new HashMap<>();
        String[] strs = new String[]{smsSend.getReceiveTelephone()};
        map.put("mobiles", strs);
        map.put("smsContent", smsSend.getDescription());
        ResponseResult responseResult = HttpRequestUtil.sendPostRequest("http://localhost:15000/yun/sendDSMS", map);
        System.out.println(responseResult.getMessage());
        System.out.println(responseResult.getCode());
        if (responseResult.getCode() == 200) {
            log.info("成功发出短信");
            smsSend.setState(SendMessageEnum.SUCCESS.getCode());
            smsSend.setTimingTime(new Date());
            smsSendRepository.save(smsSend);
        } else {
            log.error("发送短信失败");
            smsSend.setState(responseResult.getCode());
            smsSend.setTimingTime(new Date());
            smsSendRepository.save(smsSend);
        }
    }

    public static void main(String[] args) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,10);

        String date = sdf.format(calendar.getTime());
        System.out.println(date);
    }
}
