package com.knowledge.service;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.knowledge.core.base.BaseMapper;
import com.knowledge.core.base.BaseService;
import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.MaterialListOutput;
import com.knowledge.domain.output.MinimumParticleOutput;
import com.knowledge.mapper.jpa.AnswerRepository;
import com.knowledge.mapper.jpa.MaterialListRepository;
import com.knowledge.mapper.jpa.MinimumParticleRepository;
import com.knowledge.mapper.mybatis.AnswerMapper;
import com.knowledge.mapper.mybatis.MaterialListMapper;
import com.knowledge.mapper.mybatis.MinimumParticleMapper;
import com.knowledge.model.Answer;
import com.knowledge.model.MaterialList;
import com.knowledge.model.MaterialsWithBLOBs;
import com.knowledge.model.MinimumParticle;
import org.hibernate.validator.constraints.EAN;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.transaction.Transactional;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
@Service
public class MinimumParticletService extends BaseService<MinimumParticleOutput, MinimumParticle,Integer>{

    @Autowired
    private MinimumParticleRepository repository;

    @Autowired
    private MinimumParticleMapper minimumParticleMapper;
    @Autowired
    private MaterialListMapper materialListMapper;
    @Autowired
    private MaterialListRepository materialListRepository;
    @Autowired
    private QltQlsxService qltQlsxService;

    @Override
    public BaseMapper<MinimumParticle, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<MinimumParticleOutput> getMybatisBaseMapper() {
        return minimumParticleMapper;
    }

    public boolean isRepeat(Integer id, MinimumParticle minimumParticle) {
        minimumParticle.setId(id);
        minimumParticle.setAmputated(0);
        List<MinimumParticleOutput> list = minimumParticleMapper.isRepeat(minimumParticle);
        if(list!=null&&list.size()>0){
            return true;
        }else{
            return  false;
        }
    }

    @Override
    public int add(MinimumParticle minimumParticle) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        setProperty(minimumParticle, "createdUserId", getUsers().getId());
        setProperty(minimumParticle, "createdUserName", getUsers().getUsername());
        setProperty(minimumParticle, "createdDateTime", new Date());
        setProperty(minimumParticle, "lastUpdateUserId", getUsers().getId());
        setProperty(minimumParticle, "lastUpdateUserName", getUsers().getUsername());
        setProperty(minimumParticle, "lastUpdateDateTime", new Date());
        setProperty(minimumParticle, "amputated", 0);
        MinimumParticle returnParticle = getMapper().save(minimumParticle);

        if (returnParticle!= null) {
            //更新path
            //1、根据parentid获得父级元素的path
             MinimumParticle minimumParticle1 = repository.getById(minimumParticle.getParentId());
            //2、path和id做拼接后更新到元素
            if(minimumParticle1==null){
                returnParticle.setPath("0,"+returnParticle.getId());
            }else{
                returnParticle.setPath(minimumParticle1.getPath()+","+returnParticle.getId());
                returnParticle.setOrgId(minimumParticle1.getOrgId());
            }

            if(this.update(returnParticle.getId(),returnParticle)>0){
                if(minimumParticle.getSelectAll()!=null&&1==minimumParticle.getSelectAll()){
                    this.addAllMaterialList(minimumParticle);
                }
                return SUCCESS;
            }else{
                return ERROR;
            }
        }
        return ERROR;
    }

    //更新当前对象及子对象的path
    public int updateObj(MinimumParticle byId,MinimumParticle minimumParticle) throws IllegalAccessException, IntrospectionException, InvocationTargetException ,MethodArgumentNotValidException{
        //修改path XXXXXX,id 迭代修改该对象及子元素的path 修改方法
        //1 先获得父级元素的path 修改对象的path
        MinimumParticle parentParticle = repository.getById(minimumParticle.getParentId());
        String changToPath = "";
        if(parentParticle==null){
            changToPath = "0,"+byId.getId();
        }else{
            changToPath = parentParticle.getPath()+","+byId.getId();
        }

        minimumParticle.setPath(changToPath);
        minimumParticle.setOrgId(byId.getOrgId());
        setProperty(minimumParticle, "lastUpdateUserId", getUsers().getId());
        setProperty(minimumParticle, "lastUpdateUserName", getUsers().getUsername());
        setProperty(minimumParticle, "lastUpdateDateTime", new Date());
        MinimumParticle t1 = getMapper().getById(byId.getId());
        String[] igre = getNotNullProperties(minimumParticle);
        t1 = copyProperties(t1,minimumParticle,igre);

        if (getMapper().save(t1) != null) {
            updateChildPath(minimumParticle.getId());
            return SUCCESS;
        }
        return ERROR;
    }

    public void updateChildPath(Integer id) throws IllegalAccessException, IntrospectionException, InvocationTargetException ,MethodArgumentNotValidException{
        //获得二级元素
        List<MinimumParticle> list = repository.findByParentIdAndAmputated(id,0);
        MinimumParticle byId = repository.getById(id);
        this.updatePath(list,byId);
    }

    private void updatePath(List<MinimumParticle> list, MinimumParticle byId) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        for (MinimumParticle o:list) {
            o.setPath(byId.getPath()+","+o.getId());
            List<MinimumParticle> childs = repository.findByParentId(o.getId());
            this.update(o.getId(),o);
            if(childs!=null&&childs.size()>0){
                updatePath(childs,o);
            }
        }
    }

    //最小颗粒搜索
    public List<MinimumParticleOutput> getList(PageData pageData) {
        //1、先获得根据权力事项名称和情况分类查找后的结果集
        List<MinimumParticleOutput> returnList = new ArrayList<>();
        MinimumParticleOutput minimumParticleOutput = new MinimumParticleOutput();
//        minimumParticleOutput.setUserId(pageData.GetParameter("userId"));
//        minimumParticleOutput.setEmployeeId(pageData.GetParameter("employeeId"));
        minimumParticleOutput.setOrganizationId(pageData.get("organizationId").toString());
        minimumParticleOutput.setQlName(pageData.GetParameter("qlName"));
        minimumParticleOutput.setHappeningType(pageData.GetParameter("happeningType"));
//        minimumParticleOutput.setAcpInstitution(pageData.GetParameter("acpInstitution"));
//        minimumParticleOutput.setOuoCode(qltQlsxService.selectOUORGCODE(Integer.parseInt(pageData.GetParameter("orgId"))));
        List<MinimumParticleOutput>  list ;
        list  =minimumParticleMapper.getByQlNameAndHappeningType(minimumParticleOutput);
        //2、循环遍历根据path获得所有的元素
        StringBuffer path = new StringBuffer("");
        for (MinimumParticle o:list) {
            path.append(o.getPath()+",");
        }
        if(path.length()==0){
            return null;
        }else{
            String pahtval = path.toString().substring(0,path.length()-1);
            String[] split = pahtval.split(",");
            HashSet<String> objects = new HashSet<>();
            for (String str:split) {
                    objects.add(str);
            }
            Object[] objects1 = objects.toArray();
            StringBuilder stringBuilder = new StringBuilder("");
            for (int i=0;i<objects1.length;i++) {
                stringBuilder.append(objects1[i]+",");
                if((i>0&&i%900==0)||i==objects1.length-1){
                    String substring = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
                    List<MinimumParticleOutput> minimumParticles = minimumParticleMapper.getByPath(substring);
                    returnList.addAll(minimumParticles);
                    stringBuilder = new StringBuilder("");
                }
            }
        }
        return  returnList;
    }

    //所有的组织数据（用于列表）
    public List<MinimumParticleOutput> Assembly(List<MinimumParticleOutput> all) {
        ArrayList<MinimumParticleOutput> parentArray = new ArrayList<>();
        if(all==null){
            return null;
        }
        for (MinimumParticle o:all) {
            if(o.getParentId()==0){
                MinimumParticleOutput minimumParticleOutput = new MinimumParticleOutput();
                BeanUtils.copyProperties(o,minimumParticleOutput);
                parentArray.add(minimumParticleOutput);
            }
        }
        return getTreeData(all,parentArray);
    }

    public List<MinimumParticleOutput> getTreeData(List<MinimumParticleOutput> list, List<MinimumParticleOutput> parents) {
        for (MinimumParticleOutput parent:parents) {
            List<MinimumParticleOutput> childs = new ArrayList<>();
            for (MinimumParticle o:list) {
                if(parent.getId().equals(o.getParentId())){
                    MinimumParticleOutput minimumParticleOutput = new MinimumParticleOutput();
                    BeanUtils.copyProperties(o,minimumParticleOutput);
                    childs.add(minimumParticleOutput);
                }
            }
            parent.setChildren(childs.size()==0?null:childs);
            if(childs.size()>0){
                getTreeData(list,childs)  ;
            }
        }
        return parents;
    }


    //假删除（最小颗粒只进行单个删除）
    @Override
    public int logicDelete(String id) throws IllegalAccessException, IntrospectionException, InvocationTargetException,MethodArgumentNotValidException {
        MinimumParticle t =  getMapper().getById(Integer.valueOf(id));
        if(t==null){
            return SUCCESS;
        }else{
            //判断是否有子元素
            List<MinimumParticle>  parents = repository.findByParentIdAndAmputated(Integer.valueOf(id),0);
            if(parents!=null&&parents.size()>0){
                return 2;
            }
            setProperty(t, "amputated", 3);
            int result = this.update(Integer.valueOf(id),t);
            return result;
        }
    }

    public void addAllMaterialList(MinimumParticle minimumParticle) {
        MaterialList materialList1 = new MaterialList();
        materialList1.setMinimumParticleId(minimumParticle.getParentId());
        ArrayList<MaterialList> materialLists = new ArrayList<>();
        List<MaterialListOutput> parentMaterialLIst = materialListMapper.getParentMaterialLIst(materialList1);
        for (MaterialListOutput materialsOutput:parentMaterialLIst) {
            MaterialList materialList = new MaterialList();
            BeanUtils.copyProperties(materialsOutput,materialList);
            materialList.setMinimumParticleId(minimumParticle.getId());
            materialList.setId(null);
            materialLists.add(materialList);
        }
        materialListRepository.saveAll(materialLists);
    }

    public List<MinimumParticleOutput> getParList() {
        //1、先获得根据权力事项名称和情况分类查找后的结果集
        List<MinimumParticleOutput> returnList = new ArrayList<>();
        Integer orgId = this.getUsers().getOrganizationId();
        List<MinimumParticleOutput>  list ;
        if(this.getUsers().getAdministratorLevel()==9){
            orgId = null;
        }
        list = minimumParticleMapper.selectParList(orgId);


        //2、循环遍历根据path获得所有的元素
        StringBuffer path = new StringBuffer("");
        for (MinimumParticle o:list) {
            path.append(o.getPath()+",");
        }
        if(path.length()==0){
            return null;
        }else{
            String pahtval = path.toString().substring(0,path.length()-1);
            String[] split = pahtval.split(",");
            HashSet<String> objects = new HashSet<>();
            for (String str:split) {
                objects.add(str);
            }
            Object[] objects1 = objects.toArray();
            StringBuilder stringBuilder = new StringBuilder("");
            for (int i=0;i<objects1.length;i++) {
                stringBuilder.append(objects1[i]+",");
                if((i>0&&i%900==0)||i==objects1.length-1){
                    String substring = stringBuilder.toString().substring(0, stringBuilder.length() - 1);
                    List<MinimumParticleOutput> minimumParticles = minimumParticleMapper.getByPath(substring);
                    returnList.addAll(minimumParticles);
                    stringBuilder = new StringBuilder("");
                }
            }
        }
        return  returnList;
    }

    public ResponseResult publish(Integer id) {
        MinimumParticleOutput minimumParticleOutput = this.selectById(id);
        Integer publishState = minimumParticleOutput.getAmputated()==0?1:0;

        if(minimumParticleOutput.getParentId()==0||publishState==1){
            List<MinimumParticleOutput> sonEmlemrnt = minimumParticleMapper.getSonEmlemrnt(id);
            return  this.updateState(sonEmlemrnt,publishState);
        }
        //子办事情形发布则所有的父办事情形都要发布
        if(minimumParticleOutput.getParentId()!=0&&publishState==0){
            List<MinimumParticleOutput> byPath = minimumParticleMapper.getByPath(minimumParticleOutput.getPath());
            return this.updateState(byPath,publishState);
        }
        return ResponseResult.success();
    }

    public ResponseResult updateState(List<MinimumParticleOutput> list,Integer publishState){
        MinimumParticle minimumParticle = new MinimumParticle();
        for (MinimumParticleOutput o:list
        ) {
            minimumParticle.setId(o.getId());
            minimumParticle.setAmputated(publishState);
            if(minimumParticleMapper.updateByPrimaryKeySelective(minimumParticle)<0){
                return ResponseResult.error("后台错误");
            }
        }
        return ResponseResult.success();
    }


}
