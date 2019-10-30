package com.knowledge.service;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.ExportExcel;
import com.common.utils.HttpRequestUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.knowledge.core.base.BaseMapper;
import com.knowledge.core.base.BaseService;
import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.MinimumParticleOutput;
import com.knowledge.domain.output.QltQlsxOutput;
import com.knowledge.mapper.jpa.MaterialListRepository;
import com.knowledge.mapper.jpa.MinimumParticleRepository;
import com.knowledge.mapper.jpa.QltQlsxRepository;
import com.knowledge.mapper.mybatis.MaterialsMapper;
import com.knowledge.mapper.mybatis.MinimumParticleMapper;
import com.knowledge.mapper.mybatis.QltQlsxMapper;
import com.knowledge.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author: Young
 * @description:
 * @date: Created in 13:44 2018/11/13
 * @modified by:
 */
@Configuration
@Service
public class QltQlsxService  extends BaseService<QltQlsxOutput, QltQlsx,String> {

    @Override
    public BaseMapper<QltQlsx, String> getMapper() {
        return qltQlsxRepository;
    }
    @Override
    public MybatisBaseMapper<QltQlsxOutput> getMybatisBaseMapper() {
        return qltQlsxMapper;
    }

    @Autowired
    private QltQlsxMapper qltQlsxMapper;
    @Autowired
    private QltQlsxRepository qltQlsxRepository;
    @Autowired
    private MaterialListRepository materialListRepository;
    @Autowired
    private MinimumParticleRepository minimumParticleRepository;
    @Autowired
    private MaterialsMapper materialsMapper;
    @Autowired
    private MinimumParticleMapper minimumParticleMapper;


    public ResponseResult selectById(String  id){
        QltQlsxOutput qltQlsxOutput = qltQlsxMapper.selectByPrimaryKey(id);
        if(qltQlsxOutput==null){
            return ResponseResult.error("没有查询到权力事项");
        }
        return ResponseResult.success(qltQlsxOutput);
    }

    /**跟新热门事项状态**/
    public ResponseResult updateHotState(String id,Integer state) {
        QltQlsx qltQlsx = qltQlsxRepository.findById(id).orElse(null);
        if(qltQlsx == null){
            return ResponseResult.error("跟新热门事项状态失败");
        }
        qltQlsx.setHot(state);
        qltQlsx = qltQlsxRepository.save(qltQlsx);
        if(qltQlsx!=null){
            return ResponseResult.success(qltQlsx);
        }
        return ResponseResult.error("跟新热门事项状态失败");
    }

    /**跟新最小颗粒状态**/
    public ResponseResult updateParticlesState(String id, Integer state){
        QltQlsx qltQlsx = qltQlsxRepository.findById(id).orElse(null);
        if(qltQlsx==null){
            return ResponseResult.error("跟新最小颗粒状态失败");
        }
        qltQlsx.setParticles(state);
        qltQlsx = qltQlsxRepository.save(qltQlsx);
        if(qltQlsx!=null){
            return ResponseResult.success(qltQlsx);
        }
        return ResponseResult.error("跟新最小颗粒状态失败");
    }

    @Async
    @Transactional(rollbackFor = Exception.class)
    public void UpdateOrAddRelationDateByhot(String id, Integer state,Users users) {
        //根据权利编码id查询出删除状态为2的最小颗粒
        List<MinimumParticle> minimumParticleList = minimumParticleRepository.findByQlInnerCodeAndAmputatedAndParentId(id,2,0);

        //根据权利编码id查询出权力事项
        QltQlsxOutput qltQlsxOutput = qltQlsxMapper.selectByPrimaryKey(id);

       // 0不是热门事项 1是热门事项
        if(state==0){
            if(!CollectionUtils.isEmpty(minimumParticleList)){
                for(MinimumParticle minimumParticleItem : minimumParticleList){
                    //删除状态为2的最小颗粒
                    minimumParticleRepository.delete(minimumParticleItem);
                    //删除状态为2的最小颗粒id的材料清单
                    materialListRepository.deleteByMinimumParticleId(minimumParticleItem.getId());
                }
            }

        }else if(state == 1) {
            if(CollectionUtils.isEmpty(minimumParticleList)){
                //插入一条最小颗粒数据
                MinimumParticle save = saveMinimumParticle(id,qltQlsxOutput,2,users);
                //插入材料清单
                saveMaterialList(id,qltQlsxOutput,users,save,0);
            }
        }
    }

    @Async
    public void UpdateOrAddRelationDateByMini(String id, Integer state, Users users) {
        //查询删除状态为0的
        List<MinimumParticle> minimumParticleList = minimumParticleRepository.findByQlInnerCodeAndAmputated(id,0);

        //根据权利编码id查询出权力事项
        QltQlsxOutput qltQlsxOutput = qltQlsxMapper.selectByPrimaryKey(id);

        // 0不是最小颗粒 1是最小颗粒
        if(state==0){
            if(!CollectionUtils.isEmpty(minimumParticleList)){
                for(MinimumParticle minimumParticleItem:minimumParticleList){
                    //找到本身即下级的所有办事情形
                    List<MinimumParticleOutput> sonEmlemrnt = minimumParticleMapper.getSonEmlemrnt(minimumParticleItem.getId());
                    MinimumParticle minimumParticle = new MinimumParticle();
                    for (MinimumParticleOutput o:sonEmlemrnt) {

                        minimumParticle.setId(o.getId());
                        minimumParticle.setAmputated(3);
                        minimumParticleMapper.updateByPrimaryKeySelective(minimumParticle);
                    }
                }
            }
        }else if(state == 1) {
            if(CollectionUtils.isEmpty(minimumParticleList)){
                //插入一条最小颗粒数据
                MinimumParticle save = saveMinimumParticle(id,qltQlsxOutput,0,users);
                //插入材料清单
                saveMaterialList(id,qltQlsxOutput,users,save,1);
            }
        }
    }

    @Async
    public void saveMaterialList(String id,QltQlsxOutput qltQlsxOutput,Users users,MinimumParticle save,Integer isMini){
        List<MaterialsWithBLOBs> materialsOutputs = materialsMapper.selectByQlInnerCode(id);
        List<MaterialList> materialLists = new ArrayList<>();
        for (MaterialsWithBLOBs materialsOutput : materialsOutputs) {
            MaterialList materialList = new MaterialList();
            //组织机构id可能为空
            materialList.setOrgId(qltQlsxOutput.getOrgId());
            materialList.setMaterialForm(materialsOutput.getFormat());
            materialList.setMeteriaOrganization(materialsOutput.getMeteriaOrganization());
            materialList.setMinimumParticleId(save.getId());
            materialList.setName(materialsOutput.getName());
            materialList.setNecessarilyDescription(materialsOutput.getNecessarilyDescription());
            materialList.setRemark(materialsOutput.getRemark());
            materialList.setRequestDetail(materialsOutput.getRequestDetail());
            materialList.setEmptyTableFileName(materialsOutput.getEmptyTableFileName());
            materialList.setEmptyTableFileUrl(materialsOutput.getEmptyTableFileUrl());
            materialList.setExampleTableFileUrl(materialsOutput.getExampleTableFileUrl());
            materialList.setExampleTableFileName(materialsOutput.getExampleTableFileName());
            materialList.setAmputated(0);
            //设置成是否最小颗粒的材料清单
            materialList.setIsMini(isMini);
            materialList.setCreatedDateTime(new Date());
            materialList.setCreatedUserId(users.getId());
            materialList.setCreatedUserName(users.getUsername());
            materialList.setLastUpdateDateTime(new Date());
            materialList.setLastUpdateUserId(users.getId());
            materialList.setLastUpdateUserName(users.getUsername());
            if (materialList.getMaterialForm() == null) {
                materialList.setMaterialForm(" ");
            }
            materialLists.add(materialList);
        }
                for (MaterialList materialList : materialLists) {
                    if (materialList.getEmptyTableFileName() != null && !"".equals(materialList.getEmptyTableFileName()) && materialList.getEmptyTableFileUrl() != null && !"".equals(materialList.getEmptyTableFileUrl()) && materialList.getEmptyTableFileName().indexOf(".doc") >= 0 && materialList.getEmptyTableFileUrl().indexOf("http") >= 0) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("url", materialList.getEmptyTableFileUrl());
                        map.put("fileName", materialList.getEmptyTableFileName());
                        ResponseResult result = HttpRequestUtil.sendPostRequest("http://10.32.250.84:8762/changeToPdf", map);
//                        ResponseResult result = HttpRequestUtil.sendPostRequest("http://118.31.3.79:8762/changeToPdf", map);
//                        ResponseResult result = HttpRequestUtil.sendPostRequest("http://192.168.2.130:8762/changeToPdf", map);
                        if (result.isSuccess()) {
                            materialList.setEmptyTableFileUrl(result.getData().toString());
                            if (materialList.getEmptyTableFileName().indexOf(".docx") >= 0) {
                                materialList.setEmptyTableFileName(materialList.getEmptyTableFileName().replace(".docx", "pdf"));
                            } else {
                                materialList.setEmptyTableFileName(materialList.getEmptyTableFileName().replace(".doc", "pdf"));
                            }
                        }
                    }
                    if (materialList.getExampleTableFileName() != null && !"".equals(materialList.getExampleTableFileName()) && materialList.getExampleTableFileUrl() != null && !"".equals(materialList.getExampleTableFileUrl()) && materialList.getExampleTableFileName().indexOf(".doc") >= 0 && materialList.getExampleTableFileUrl().indexOf("http") >= 0) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("url", materialList.getExampleTableFileUrl());
                        map.put("fileName", materialList.getExampleTableFileName());
                        ResponseResult result = HttpRequestUtil.sendPostRequest("http://10.32.250.84:8762/changeToPdf", map);
//                        ResponseResult result = HttpRequestUtil.sendPostRequest("http://118.31.3.79:8762/changeToPdf", map);
//                        ResponseResult result = HttpRequestUtil.sendPostRequest("http://192.168.2.130:8762/changeToPdf", map);
                        if (result.isSuccess()) {
                            materialList.setExampleTableFileUrl(result.getData().toString());
                            if (materialList.getExampleTableFileName().indexOf(".docx") >= 0) {
                                materialList.setExampleTableFileName(materialList.getExampleTableFileName().replace(".docx", "pdf"));
                            } else {
                                materialList.setExampleTableFileName(materialList.getExampleTableFileName().replace(".doc", "pdf"));
                            }
                        }
                    }
                }
        materialListRepository.saveAll(materialLists);
    }

    @Async
    public MinimumParticle saveMinimumParticle(String id,QltQlsxOutput qltQlsxOutput,Integer amputated,Users users){
        MinimumParticle minimumParticle = new MinimumParticle();

        minimumParticle.setHappeningType(qltQlsxOutput.getQlName());
        //组织机构id可能为空
        minimumParticle.setOrgId(qltQlsxOutput.getOrgId());
        //插入到最小颗粒中状态2
        minimumParticle.setAmputated(amputated);
        minimumParticle.setQlInnerCode(id);
        minimumParticle.setQlsxId(qltQlsxOutput.getQlFullId());
        minimumParticle.setCreatedDateTime(new Date());
        minimumParticle.setCreatedUserId(users.getId());
        minimumParticle.setCreatedUserName(users.getUsername());
        minimumParticle.setLastUpdateDateTime(new Date());
        minimumParticle.setLastUpdateUserId(users.getId());
        minimumParticle.setLastUpdateUserName(users.getUsername());
        minimumParticle.setProcess(" ");
        MinimumParticle save = minimumParticleRepository.save(minimumParticle);
        minimumParticle.setPath("0," + minimumParticle.getId());
        minimumParticle.setParentId(0);
        minimumParticleRepository.save(minimumParticle);
        return save;
    }

    /**跟新材料清单*/
    public void updateMaterialList(QltQlsxOutput qltQlsxOutput1,List<MaterialList> materialListList){
        for(MaterialList materialListItem: materialListList){
            MaterialList materialList = new MaterialList();
            BeanUtils.copyProperties(materialListItem,materialList);
            materialList.setOrgId(qltQlsxOutput1.getOrgId());
            materialListRepository.save(materialList);
        }
    }

    /**跟新最小颗粒*/
    public void updateMinimumParticle(QltQlsxOutput qltQlsxOutput1, List<MinimumParticleOutput> sonList,Integer amputated){
        for (MinimumParticleOutput minimumParticleOutput:sonList) {
            MinimumParticle minimumParticle1 = new MinimumParticle();
            minimumParticle1.setId(minimumParticleOutput.getId());
            minimumParticle1.setAmputated(amputated);
            minimumParticle1.setOrgId(qltQlsxOutput1.getOrgId());
            minimumParticleMapper.updateByPrimaryKeySelective(minimumParticle1);
        }
    }


    /**编辑申请的详细方式**/
    public ResponseResult updateApplicationDetail(QltQlsx qltQlsx) {
        QltQlsx qltQlsxOutput = qltQlsxRepository.findById(qltQlsx.getQlInnerCode()).orElse(null);
        if(qltQlsxOutput==null){
            return ResponseResult.error("未找到该事项");
        }
        qltQlsxOutput.setOnlineApplication(qltQlsx.getOnlineApplication());
        qltQlsxOutput.setWindowApplication(qltQlsx.getWindowApplication());
//        qltQlsxOutput.setApplyType(qltQlsx.getApplyType());
        /*if(qltQlsxOutput.getApplyType()!=null&&!"".equals(qltQlsxOutput.getApplyType())){
            if(qltQlsxOutput.getApplyType().indexOf("1;")>=0||qltQlsxOutput.getApplyType().indexOf("1；")>=0){
                qltQlsxOutput.setOnlineApplication(qltQlsx.getOnlineApplication());
            }
            if(qltQlsxOutput.getApplyType().indexOf("2;")>=0||qltQlsxOutput.getApplyType().indexOf("2；")>=0){
                qltQlsxOutput.setWindowApplication(qltQlsx.getWindowApplication());
            }
        }*/
        QltQlsx result = qltQlsxRepository.save(qltQlsxOutput);
        if(result!=null){
            return ResponseResult.success(qltQlsxOutput);
        }
        return ResponseResult.error("修改办理方式失败");
    }


    public ResponseResult findPageList(PageData pageData){
        PageHelper.startPage(pageData.getPageIndex(),pageData.getRows());
        List<QltQlsxOutput> qltQlsxList = qltQlsxMapper.selectPage(pageData);
        PageInfo pageInfo = new PageInfo(qltQlsxList);
        return ResponseResult.success(pageInfo);
    }


    public ResponseResult selectCount(){
        int count = qltQlsxMapper.selectCount();
        if(count==0) {
           return ResponseResult.error("权力事项数据为空");
        }
        return  ResponseResult.success(count);
    }

    public ResponseResult exportExcel(HttpServletResponse response, HttpServletRequest request) {

        String title = "总表";
        String excelName = "总表";
        List<Object[]> dataList = new ArrayList<>();
        Object[] objs = null;
        PageData pageData=new PageData(request);
        String[]rowsName = {"序列","权力名称","权力编码","办件类型","涉及内容","适用对象","权力事项类型",
                "权力来源","受理机构","联办机构","责任科室","事项审查类型","申请方式","联系电话","办公地址",
                "咨询电话","监督投诉电话","审批结果","办事者到达办事现场次数","承诺期限","法定期限","热门事项","最小颗粒"};
        List<QltQlsxOutput> list = qltQlsxMapper.selectPage(pageData);

        for(int i=0;i<list.size();i++){
            QltQlsxOutput qltQlsxOutput = list.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = qltQlsxOutput.getQlName();
            objs[2] = qltQlsxOutput.getQlFullId();
            objs[3] = qltQlsxOutput.getBjtypeName();
            objs[4] = qltQlsxOutput.getContentInvolve();
            objs[5] = qltQlsxOutput.getApplicableObject();
            objs[6] = qltQlsxOutput.getQlKindName();
            objs[7] = qltQlsxOutput.getItemsourceName();
            objs[8] = qltQlsxOutput.getAcpInstitution();
            objs[9] = qltQlsxOutput.getDecInstitution();
            objs[10] = qltQlsxOutput.getLeadDept();
            objs[11] = qltQlsxOutput.getShixiangsctypeName();
            objs[12] = qltQlsxOutput.getApplyTypeName();
            objs[13] = qltQlsxOutput.getApplyTypeTel();
            objs[14] = qltQlsxOutput.getAddressInfo();
            objs[15] = qltQlsxOutput.getLinkTel();
            objs[16] = qltQlsxOutput.getSuperviseTel();
            objs[17] = qltQlsxOutput.getBanjianFinishfiles();
            objs[18] = qltQlsxOutput.getApplyerminCount();
            objs[19] = qltQlsxOutput.getPromiseDay();
            objs[20] = qltQlsxOutput.getAnticipateDay();
            objs[21] = qltQlsxOutput.getHotName();
            objs[22] = qltQlsxOutput.getParticlesName();
            dataList.add(objs);
        }
        ExportExcel exportExcel = new ExportExcel(title,rowsName,dataList,excelName);
        try {
            String excelpath =  exportExcel.export(response, request);
            return ResponseResult.success(excelpath);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    /**查看附件*/
    public ResponseResult findMaterialInfo(String qlInnerCode){
        //如果为空则用户可以查看办事指南
        QltQlsxOutput qltQlsxOutput = qltQlsxMapper.selectByPrimaryKey(qlInnerCode);
        if(qltQlsxOutput==null){
            return ResponseResult.error("没查到该事项");
        }
        List<MaterialsWithBLOBs> materialsOutputList = materialsMapper.selectByQlInnerCode(qltQlsxOutput.getQlInnerCode());
        if (materialsOutputList.size() == 0) {
            return ResponseResult.success("申请材料信息为空");
        }
        return ResponseResult.success(materialsOutputList);
    }

    public List<String> findAllAcceptInstitution() {
        //返回所有受理机构的列表
        List<String> acceptInstitutions =  qltQlsxMapper.selectAllAccInstitution();
        return acceptInstitutions;

    }

    public String selectOUORGCODE(Integer orgId){
        String OUORGCODE = qltQlsxMapper.selectOuoCode(orgId);
        return OUORGCODE;
    }

}
