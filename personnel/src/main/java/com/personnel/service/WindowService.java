package com.personnel.service;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.HttpRequestUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.personnel.core.base.BaseMapper;
import com.personnel.core.base.BaseService;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.WindowOutput;
import com.personnel.mapper.jpa.QueueWindowRepository;
import com.personnel.mapper.jpa.WindowRepository;
import com.personnel.mapper.mybatis.OrganizationMapper;
import com.personnel.mapper.mybatis.WindowMapper;
import com.personnel.model.Organization;
import com.personnel.model.QueueWindow;
import com.personnel.model.Window;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

@Service
public class WindowService extends BaseService<WindowOutput, Window,Integer> {

    private static final Logger log = LoggerFactory.getLogger(WindowService.class);

    @Autowired
    private WindowRepository windowRepository;
    @Autowired
    private WindowMapper windowMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private QueueWindowRepository queueWindowRepository;

    @Value("${queue.evaluatorAddress}")
    private String evaluatorAddress;
    @Value("${queue.windowUrl}")
    private String windowUrl;

    @Override
    public BaseMapper<Window, Integer> getMapper() {
        return windowRepository;
    }

    @Override
    public MybatisBaseMapper<WindowOutput> getMybatisBaseMapper() {
        return windowMapper;
    }

    public List<Window> getByName(String name) {
        return windowRepository.findByName(name);
    }

    public boolean isRepeat(Integer id, String name) {
        Window window = windowRepository.getById(id);
        if (window.getName().equals(name)) {
            return true;
        }
        return false;
    }

    public Integer addOrUpdateQueueWindow(Window window) {

        Integer orgaId = window.getOrganizationId();
        //根据组织id查询出组织编号
        Organization organization = organizationMapper.selectOrNoByOrId(orgaId);
        if(organization == null){
            return null;
        }
        QueueWindow queueWindow = new QueueWindow();

        //窗口编号
        queueWindow.setCode(window.getWindowNo());
        //窗口编号
        queueWindow.setName(window.getName());
        //设置组织编号
        queueWindow.setDeptCode(organization.getOrganizationNo());
        queueWindow.setEvaluatorAddress(evaluatorAddress);
        //设置成未下发状态
        queueWindow.setState(0);
        //设置成可用状态0
        queueWindow.setEnabled(0);

        queueWindow.setCreatedDateTime(new Date());
        queueWindow.setCreatedUserId(0);
        queueWindow.setCreatedUserName("排队叫号");
        queueWindow.setLastUpdateDateTime(new Date());
        queueWindow.setLastUpdateUserId(0);
        queueWindow.setLastUpdateUserName("排队叫号");
        Integer result = queueWindowRepository.save(queueWindow).getId();
        if (result < 0) {
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * 定时每60秒去调用一次
     */
    public void createOrUpdateCounter() {
        //查询出未下发的集合
        List<QueueWindow> queueWindowList = queueWindowRepository.findAllByState(0);
        if (queueWindowList.size() == 0) {
            log.info("没有窗口数据可以下放到排队叫号系统中");
            return;
        }
        try {
            for (QueueWindow queueWindow : queueWindowList) {

                String code = queueWindow.getCode();
                String name = queueWindow.getName();
                String deptCode = queueWindow.getDeptCode();
                String evaluatorAddress = queueWindow.getEvaluatorAddress();
                String homeUrl = "";
                Boolean enabled = queueWindow.getEnabled()==0?true:false;
                String param = "code="+code+"&name="+name+"&deptCode=" + deptCode + "&evaluatorAddress=" + evaluatorAddress +
                        "&homeUrl="+homeUrl+"&enabled="+enabled+"&outputJson=1";
                String result = HttpRequestUtil.sendPost(windowUrl, param);
                if (!result.contains("0")) {
                    log.error("下发窗口失败:{}",result);
                    continue;
                }
                //设置成已经下发完成的状态
                queueWindow.setState(1);
                if(queueWindowRepository.save(queueWindow).getId()>0){
                    log.info("窗口数据下放到排队叫号系统中操作成功");
                }else {
                    log.info("窗口数据下放到排队叫号系统中操作失败");
                }
            }
            return;
        } catch (Exception e) {
            log.error("窗口下放排队叫号系统出现异常");
        }
    }


    @Transactional
    public ResponseResult checkedFile(MultipartFile file) throws IOException, IntrospectionException, IllegalAccessException, InvocationTargetException {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase().trim();
        if (!suffixName.equals(".xls") && !suffixName.equals(".xlsx")) {
            return ResponseResult.error("数据表格式不正确");
        }
        int index = fileName.lastIndexOf("\\");
        if (index != -1) {
            fileName = fileName.substring(index + 1);
        }
        int hashCode = fileName.hashCode();
        //把hash值转换为十六进制
        String hex = Integer.toHexString(hashCode);
        String pathHeader = "C:";
        StringBuilder sb = new StringBuilder();
        sb.append("/" + hex.charAt(0));
        sb.append("/" + hex.charAt(1));
        sb.append("/" + System.currentTimeMillis() + "_" + fileName);
        String pathEnd = sb.toString();
        File fileInfo = new File(pathHeader + pathEnd);
        if (!fileInfo.getParentFile().exists()) {
            fileInfo.getParentFile().mkdirs();
        }
        file.transferTo(fileInfo);
        Workbook wb = null;
        try {
            wb = new HSSFWorkbook(new FileInputStream(pathHeader + pathEnd));
        } catch (Exception e) {
            wb = new XSSFWorkbook(new FileInputStream(pathHeader + pathEnd));
        }
        return importWindow(wb);
    }

    private ResponseResult importWindow(Workbook wb) throws InvocationTargetException, IntrospectionException, IllegalAccessException {
        // 得到第一个sheet
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        int rows = sheet.getPhysicalNumberOfRows();
        int totalCells = 0;
        // 得到Excel的列数(前提是有行数)
        if (rows > 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }

        List<Window> windowList = Lists.newArrayList();
        for (int r = 1; r < rows; r++) {
            Row row = sheet.getRow(r);
            Window window = new Window();
            if (row == null) {
                continue;
            }
            // 循环Excel的列
            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String value = String.valueOf(cell.getStringCellValue()).trim();
                    if (!value.equals("") && value != null) {
                        if (c == 0) {
                            //设置窗口名称
                            window.setName(value);
                        } else if (c == 1) {
                            //设置群众号码
                            window.setWindowNo(value);
                        } else {
                            //组织名称精确匹配查找
                            Organization organization = organizationMapper.selectByOrgaName(value);
                            if (organization == null) {
                                return ResponseResult.error("第" + r + "行，第3列组织名称填写错误");
                            }
                            window.setOrganizationId(organization.getId());
                        }
                    }
                }
            }
            setProperty(window, "createdUserId", getUsers().getId());
            setProperty(window, "createdUserName", getUsers().getUsername());
            setProperty(window, "createdDateTime", new Date());
            setProperty(window, "lastUpdateUserId", getUsers().getId());
            setProperty(window, "lastUpdateUserName", getUsers().getUsername());
            setProperty(window, "lastUpdateDateTime", new Date());

            windowList.add(window);
        }

        Integer size = windowRepository.saveAll(windowList).size();
        if(size == 0){
            return ResponseResult.error("导入失败");
        }
        return ResponseResult.success("导入成功");
    }

    public boolean verificationOrg(Window window) {
        List<Organization> list =  organizationMapper.selectByParentId(window.getOrganizationId());
        if(list!=null&&list.size()>0){
            return false;
        }else{
            return true;
        }
    }

    public Page<WindowOutput> selectPageListWithinAuthority(PageData pageData) {
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        Page<WindowOutput> pageList = null;
        if(getUsers().getAdministratorLevel()==9){
            pageList = windowMapper.selectPageListWithinAuthority(pageData);
        }else{
            pageData.put("userId",getUsers().getId());
            if(getUsers().getUserType()==1){
                pageData.put("orgId",getUsers().getOrganizationId());
            }
            pageList = windowMapper.selectPageListWithinAuthority(pageData);
        }
        return pageList;
    }
}
