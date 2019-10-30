package com.api.service;

import com.api.core.base.BaseMapper;
import com.api.core.base.BaseService;
import com.api.core.base.MybatisBaseMapper;
import com.api.domain.output.ScreenConfigOutput;
import com.api.mapper.jpa.ScreenConfigRepository;
import com.api.mapper.mybatis.ScreenConfigMapper;
import com.api.model.ScreenConfig;
import com.api.model.ScreenConfigZTree;
import com.common.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;
/**
 * @author: Young
 * @description:
 * @date: Created in 18:14 2018/10/30
 * @modified by:
 */
@Service
public class ScreenConfigService extends BaseService<ScreenConfigOutput, ScreenConfig,Integer> {
    @Autowired
    private ScreenConfigMapper screenConfigMapper;

    @Autowired
    private ScreenConfigRepository ScreenConfigRepository;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Override
    public BaseMapper<ScreenConfig, Integer> getMapper() {
        return ScreenConfigRepository;
    }

    @Override
    public MybatisBaseMapper<ScreenConfigOutput> getMybatisBaseMapper() {
        return screenConfigMapper;
    }


    public ScreenConfig getByKey(String key){
        return ScreenConfigRepository.findByConfigKey(key);
    }

    public List<ScreenConfig> getListByKey(String key){
        var config = getByKey(key);
        return ScreenConfigRepository.findByParentId(config.getId());
    }

    public ResponseResult getPledgeData() {
        var ruleMap = this.getListByKey("fwcn");
        return  ResponseResult.success(ruleMap);
    }



    public ResponseResult getMobileService() {
        var ruleMap = this.getListByKey("ydbs");
        return  ResponseResult.success(ruleMap);
    }


    public List<ScreenConfig> getListByParentId(Integer id) {
        return ScreenConfigRepository.findByParentId(id);
    }

    public int updateParentConfig(Integer id, Integer hisChild) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        var parentConfig = getById(id);
        parentConfig.setHisChild(hisChild);
        return super.add(parentConfig);
    }

    public List<ScreenConfigOutput> findAll(){
        return screenConfigMapper.findAll();
    }

    public List<ScreenConfigOutput> findTree(){
        var configs = findAll();
        return getConfigOutput(configs,0);
    }

    public List<ScreenConfigOutput> getConfigOutput(List<ScreenConfigOutput> menus,Integer parentId) {
        List<ScreenConfigOutput> out = new ArrayList<>();
        List<ScreenConfigOutput> firstMenu = menus.stream().filter(Menu -> Menu.getParentId() == parentId).sorted(Comparator.comparingInt(ScreenConfig::getDisPlayOrderBy)).collect(toList());
        out.addAll(firstMenu);
        for (ScreenConfigOutput screenConfigOutput : out) {
            if (screenConfigOutput.getHisChild() != null && screenConfigOutput.getHisChild() == 1) {
                List<ScreenConfigOutput> list = menus.stream().filter(Menu -> Menu.getParentId() .equals(screenConfigOutput.getId())).collect(toList());
                screenConfigOutput.setChildren(list);
                getConfigOutput(menus,screenConfigOutput.getId());
            } else {
                continue;
            }
        }
        return out;
    }

    //所有的组织数据（用户树形下拉框类ztree）
    public List<ScreenConfigZTree> toZtree(List<ScreenConfigOutput> all) {
        List<ScreenConfigZTree> parentArray = new ArrayList<>();
        for (var o:all) {
            if(o.getParentId()==0){
                parentArray.add(new ScreenConfigZTree(o));
            }
        }
        return getTree(all,parentArray);
    }
    public List<ScreenConfigZTree> getTree(List<ScreenConfigOutput> list, List<ScreenConfigZTree> parents) {
        for (ScreenConfigZTree parentOrg:parents) {
            List<ScreenConfigZTree> childs = new ArrayList<>();
            for (ScreenConfigOutput o:list) {
                if(parentOrg.getKey().equals(o.getParentId())){
                    childs.add(new ScreenConfigZTree(o));
                }
            }
            parentOrg.setChildren(childs.size()==0?null:childs);
            if(childs.size()>0){
                getTree(list,childs)  ;
            }
        }
        return parents;
    }




}
