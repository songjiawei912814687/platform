package com.personnel.service;

import com.common.Enum.MessageTypeEnum;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.ExportExcel;
import com.common.utils.HttpRequestUtil;
import com.common.utils.Img2Base64Util;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.personnel.core.base.BaseMapper;
import com.personnel.core.base.BaseService;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.input.EmployeesInput;
import com.personnel.domian.output.EmployeesOutput;
import com.personnel.mapper.jpa.EmployeesRepository;
import com.personnel.mapper.jpa.QueueEmployeesRepository;
import com.personnel.mapper.jpa.QueueLogRepository;
import com.personnel.mapper.jpa.StationEmployeesRepository;
import com.personnel.mapper.mybatis.EmployeesMapper;
import com.personnel.mapper.mybatis.JobsMapper;
import com.personnel.mapper.mybatis.OrganizationMapper;
import com.personnel.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("employeesService")
public class EmployeesService extends BaseService<EmployeesOutput, Employees, Integer> {

    @Autowired
    private EmployeesRepository repository;

    @Autowired
    private EmployeesMapper employeesMapper;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PersonService personService;

    @Autowired
    private PlateService plateService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WindowService windowService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private JobsService jobsService;
    @Autowired
    private JobsMapper jobsMapper;
    @Autowired
    private QueueEmployeesRepository queueEmployeesRepository;
    @Autowired
    private FoodSystemService foodSystemService;
    @Autowired
    private QueueLogRepository queueLogRepository;
    @Autowired
    private StationEmployeesRepository stationEmployeesRepository;

    @Value("${queue.password}")
    private String password;
    @Value("${queue.rated}")
    private String rated;
    @Value("${queue.avatarUrlPrefix}")
    private String avatarUrlPrefix;
    @Value("${queue.empUrl}")
    private String empUrl;

    @Override
    public BaseMapper<Employees, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<EmployeesOutput> getMybatisBaseMapper() {
        return employeesMapper;
    }

    public String getMaxNo(Integer orgId){
        Organization org = organizationMapper.selectOrNoByOrId(orgId);
        String[] strs = org.getPath().split(",");
        int firstLetter = 9;
        for(int i = strs.length-1;i>=0;i--) {
            Organization o = organizationMapper.selectOrNoByOrId(Integer.valueOf(strs[i]));
            if (o!=null&&o.getFirstLetter() != null) {
                firstLetter = o.getFirstLetter();
                break;

            }
        }
        Integer small = null;
        Integer big = null;
        if(firstLetter==0){
            big = 10000;
        }else{
            small = Integer.valueOf(firstLetter+"0000");
            int firstLetter1 = firstLetter+1;
            big = Integer.valueOf(firstLetter1+"0000");
        }
        PageData pageData = new PageData();
        pageData.put("small",small);
        pageData.put("big",big);
        String s = employeesMapper.selectMaxNo(pageData);
        String t = null;
        if(s==null){
            t=firstLetter+"0001";
        }else {
            t = String.format("%05d",Integer.parseInt(s)+1);
        }
        return t;
    }

    public boolean isCantChange(Integer id, Employees employees) {
        Employees employees1 = repository.getById(id);
        if (employees1 != null) {
            if (employees.getIdCard().equals(employees1.getIdCard())
                    && employees.getEmployeeNo().equals(employees1.getEmployeeNo())
                    && employees.getSex().equals(employees1.getSex())) {
                return true;
            }
        }
        return false;
    }

    public ResponseResult selectAll() {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        return ResponseResult.success(repository.findAll(sort));
    }

    @Transactional
    public boolean updateState(Employees employees, Person person) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        if (update(employees.getId(), employees) > 0) {
            return personService.add(person) > 0;

        } else {
            return false;
        }
    }

    public boolean checkCondition(Integer orgaId) {

        Organization organization = organizationMapper.selectOrNoByOrId(orgaId);
        Integer result = organizationMapper.selectCheckCondition(organization.getOrganizationNo());
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 把员工信息数据存储或更新到员工信息中间表
     * @param employees
     */
    public void saveStationEmployees(Employees employees,Integer state){
        StationEmployees stationEmployees = new StationEmployees();
        stationEmployees.setAmputated(employees.getAmputated());
        stationEmployees.setEmployeeNo(employees.getEmployeeNo());
        stationEmployees.setId(employees.getId());
        stationEmployees.setName(employees.getName());
        if(state != null){
            stationEmployees.setState(state);
        }else{
            stationEmployees.setState(0);
        }
        stationEmployeesRepository.save(stationEmployees);
    }

    /**
     * 删除员工信息中间表的数据
     * @param employees
     */
    public void deleteStationEmployees(Employees employees){
        StationEmployees stationEmployees = new StationEmployees();
        stationEmployees.setAmputated(employees.getAmputated());
        stationEmployees.setEmployeeNo(employees.getEmployeeNo());
        stationEmployees.setId(employees.getId());
        stationEmployees.setName(employees.getName());
        stationEmployees.setState(0);
        stationEmployeesRepository.delete(stationEmployees);
    }

    /**
     * 根据id查找员工信息中间表的数据
     * @param id
     * @return
     */
    public StationEmployees selectStationEmployeesById(Integer id){
        return  stationEmployeesRepository.getById(id);
    }

    /**
     * 查询所有员工信息中间表未上传的数据
     */
    public List<StationEmployees> findAllStationEmployeesByState(){
        return stationEmployeesRepository.findAllByState(0);
    }

    /**
     * 下发到取号叫号中间表公共方法
     */

    public void addQueueEmployees(Employees employees) {
        Organization organization = organizationMapper.selectOrNoByOrId(employees.getOrganizationId());
        if (organization == null) {
            logger.error("没有找到组织");
            return;
        }
        QueueEmployees queueEmployees = new QueueEmployees();

        queueEmployees.setEmployeesId(employees.getId());
        queueEmployees.setCode(employees.getEmployeeNo());
        queueEmployees.setUserName(employees.getEmployeeNo());
        queueEmployees.setName(employees.getName());
        queueEmployees.setPassword(password);
        queueEmployees.setDeptCode(organization.getOrganizationNo());
        //处理星级，默认为0
        queueEmployees.setRated(Integer.valueOf(rated));
        //处理头像
        queueEmployees.setAvatar(avatarUrlPrefix + employees.getIcon());
        Jobs jobs = jobsMapper.selectByPrimaryKey(employees.getJobsId());
        queueEmployees.setJobTitle(jobs.getName());
        //设置成在职状态0是在职
        queueEmployees.setEnabled(0);
        //设置成未下发状态
        queueEmployees.setQueueState(0);

        queueEmployees.setCreatedDateTime(new Date());
        queueEmployees.setCreatedUserId(0);
        queueEmployees.setCreatedUserName("排队叫号");
        queueEmployees.setLastUpdateDateTime(new Date());
        queueEmployees.setLastUpdateUserId(0);
        queueEmployees.setLastUpdateUserName("排队叫号");
        Integer result = queueEmployeesRepository.save(queueEmployees).getId();
        if (result < 0) {
            logger.error("人员添加到排队叫号系统中间表失败");
            return;
        }
        logger.info("人员添加到排队叫号系统中间表成功");
        return;
    }

    @Async
    /**当人员审核通过下发*/
    public void queueUpdateState(EmployeesInput employeesInput) throws Exception {
        if (employeesInput.getId() == null || employeesInput.getId().equals("") || employeesInput.getState() == null || employeesInput.getState().equals("")) {
            return;
        }
        //如果是未审核的状态直接返回
        if (employeesInput.getState() == 3) {
            return;
        }
        Employees employees = this.getById(employeesInput.getId());
        if (employeesInput.getState() == 1) {
            employees.setWorkingState(employeesInput.getState());
            employees.setInductionDateTime(new Date());
        } else {
            if (logicDelete(employeesInput.getId().toString()) < 0) {
                return;
            }
        }
        addQueueEmployees(employees);
    }

    /**
     * 定时20秒拉取中间表需要放到排队叫号系统中的数据
     */
    public void createOrUpdateEmployee() {
        //去查询未下发的数据
        List<QueueEmployees> queueEmployeesList = queueEmployeesRepository.findAllByQueueState(0);
        if (queueEmployeesList.size() == 0) {
            logger.info("没有人员数据可以下发到取号叫号");
            return;
        }
        try {
            for (QueueEmployees queueEmployees : queueEmployeesList) {
                //获取员工工号
                String code = queueEmployees.getCode();
                if(code.equals("11643")){
                    System.out.println("开始");
                }
                //获取用户名同工号
                String userName = queueEmployees.getUserName();
                //员工姓名
                String name = queueEmployees.getName();
                //密码
                String password = queueEmployees.getPassword();
                //组织编号
                String orga = queueEmployees.getDeptCode();
                //星级
                String rated = queueEmployees.getRated().toString();
                //获取人员头像地址
                String iconPath = queueEmployees.getAvatar();

                //转化成base64位
                String avatar = Img2Base64Util.getImgStr(iconPath);
                avatar = avatar.replace("+", "%2B");
                String jobTitle = queueEmployees.getJobTitle();

                String bios = queueEmployees.getBios() == null ? "" : queueEmployees.getBios();
                //获取是否有效，0是有效1-是无效
                Boolean enable = queueEmployees.getEnabled() == 0 ? true : false;

                String param = "code=" + code + "&userName=" + userName + "&name=" + name + "&password=" + password + "&deptCode=" + orga +
                        "&rated=" + rated + "&avatar=" + avatar + "&jobTitle=" + jobTitle + "&bios=" + bios + "&enabled=" + enable + "&outputJson=1";
                String result = HttpRequestUtil.sendPost(empUrl, param);
                if (!result.contains("0")) {
                    //根据resourceId先去日志表查找数据，如果存在就跳出
                    List<QueueLog> queueLogList = queueLogRepository.findAllByResourceId(queueEmployees.getId());
                    if(queueLogList.size()>0){
                        logger.info("已有日志不需要打印");
                        continue;
                    }
                    QueueLog queueLog = new QueueLog();
                    queueLog.setCreatedDateTime(new Date());
                    queueLog.setLastUpdateDateTime(new Date());
                    queueLog.setMatters("取号叫号人员下发错误"+result);
                    queueLog.setResourceId(queueEmployees.getId());
                    queueLogRepository.save(queueLog).getId();
                    continue;
                }
                QueueLog queueLog = new QueueLog();
                queueLog.setCreatedDateTime(new Date());
                queueLog.setLastUpdateDateTime(new Date());
                queueLog.setMatters("取号叫号人员下发成功"+result);
                queueLog.setResourceId(queueEmployees.getId());
                queueLogRepository.save(queueLog).getId();
                //设置成已经下发状态
                queueEmployees.setQueueState(1);
                logger.info("人员下发到排队叫号系统中操作成功");
            }
            queueEmployeesRepository.saveAll(queueEmployeesList);
        } catch (Exception e) {
            logger.error("人员下发到取号叫号系统中下发失败");
        }
    }

    /**
     * 当人员编辑时重新下发 人员跟新的时候
     */
    @Async
    public void updateQue(Employees employees) {

        //查询出该中间表中的数据
        QueueEmployees queueEmployees = queueEmployeesRepository.findByCode(employees.getEmployeeNo());

        Organization organization = organizationMapper.selectOrNoByOrId(employees.getOrganizationId());
        if (organization == null) {
            return;
        }
        if (queueEmployees == null) {
            queueEmployees = new QueueEmployees();
            queueEmployees.setCreatedDateTime(new Date());
            queueEmployees.setCreatedUserId(0);
            queueEmployees.setCreatedUserName("排队叫号");
        }else {
            queueEmployees.setId(queueEmployees.getId());
            queueEmployees.setLastUpdateDateTime(new Date());
            queueEmployees.setLastUpdateUserId(0);
            queueEmployees.setLastUpdateUserName("排队叫号");
        }
        queueEmployees.setEmployeesId(employees.getId());
        queueEmployees.setCode(employees.getEmployeeNo());
        queueEmployees.setUserName(employees.getEmployeeNo());
        queueEmployees.setName(employees.getName());
        queueEmployees.setPassword(password);
        queueEmployees.setDeptCode(organization.getOrganizationNo());
        //处理星级，默认为100
        queueEmployees.setRated(Integer.valueOf(rated));
        //处理头像
        queueEmployees.setAvatar(avatarUrlPrefix + employees.getIcon());
        Jobs jobs = jobsMapper.selectByPrimaryKey(employees.getJobsId());
        if (jobs == null) {
            return;
        }
        queueEmployees.setJobTitle(jobs.getName());
        //设置成在职状态
        queueEmployees.setEnabled(0);
        //设置成未下发状态
        queueEmployees.setQueueState(0);

        //重新保存到下发的中间表
        Integer id = queueEmployeesRepository.saveAndFlush(queueEmployees).getId();
        if (id < 0) {
            return;
        }
        return;
    }

    /**
     * 删除人员后改变成enabled状态为1
     */
    @Async
    public void leave(Employees employees) {
        QueueEmployees queueEmployees = queueEmployeesRepository.findByCode(employees.getEmployeeNo());
        if (queueEmployees == null) {
            logger.error("没有找到取号叫号的中间表数据人员");
            return;
        }

        queueEmployees.setId(queueEmployees.getId());
        //设置离职状态1
        queueEmployees.setEnabled(1);
        //设置下发状态为未下发
        queueEmployees.setQueueState(0);
        //设置最后跟新时间
        queueEmployees.setLastUpdateDateTime(new Date());
        Integer id = queueEmployeesRepository.saveAndFlush(queueEmployees).getId();
        if (id <= 0) {
            logger.error("保存离职状态");
            return;
        }
        logger.info("保存离职状态成功");
        return;
    }

    public List<EmployeesOutput> selectByPath(PageData pageData) {
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        Page<EmployeesOutput> pageList = employeesMapper.selectByPath(pageData);
        return pageList;
    }


    public List<EmployeesOutput> selectByOrgId(PageData pageData){
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        Page<EmployeesOutput> pageList = employeesMapper.selectByOrgId(pageData);
        return pageList;
    }

    public EmployeesOutput getByEmployeeId(Integer id) {
        EmployeesOutput employeesOutput = employeesMapper.selectByPrimaryKey(id);
        if (employeesOutput != null) {
            if (employeesOutput.getPlateNo() != null && !employeesOutput.getPlateNo().equals("")) {
                String[] s = employeesOutput.getPlateNo().split(",");
                List<String> list = new ArrayList<String>();
                if (s != null && s.length > 0) {
                    for (String s1 : s) {
                        list.add(s1);
                    }
                }
                employeesOutput.setPlateNoList(list);
            }
        }
        return employeesOutput;
    }

    //上传excel到服务器
    public String checkedFile(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        if (!suffixName.equals(".xls") && !suffixName.equals(".xlsx")) {
            return "文件类型错误";
        }
        int index = fileName.lastIndexOf("\\");
        if (index != -1) {
            fileName = fileName.substring(index + 1);
        }
        int hashCode = fileName.hashCode();
        //把hash值转换为十六进制
        String hex = Integer.toHexString(hashCode);
        String pathHeader = "D:\\\\HZFY\\\\personInfo";
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
        return importemployees(wb);

    }

    //把excel数据导入到数据库中
    public String importemployees(Workbook wb) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException, IOException {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        int rows = sheet.getPhysicalNumberOfRows();
        int totalCells = 0;
        // 得到Excel的列数(前提是有行数)
        if (rows > 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<Employees> list = new ArrayList<Employees>();
        List<QueueEmployees> queueEmployeesList = new ArrayList<>();


        for (int r = 1; r < rows; r++) {
            Row row = sheet.getRow(r);
            Employees employees = new Employees();
            QueueEmployees queueEmployees = new QueueEmployees();
            if (row == null) {
                continue;
            }
            var a = row.getCell(0);
            var name = "";
            try {
                name = String.valueOf(a.getStringCellValue()).trim();
            } catch (NullPointerException e) {
                break;
            }

            if (name == null || "".equals(name)) {
                continue;
            }


            // 循环Excel的列
            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);

                if (null != cell) {
                    if (c == 15) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        String value = String.valueOf(cell.getStringCellValue()).trim();
                        if (employees.getPartyMemberState() < 2) {
                            if (value.equals("") || value == null) {
                                return "第" + r + "行入党时间错误";
                            }
                            if (0 == cell.getCellType()) {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    //用于转化为日期格式
                                    Date d = cell.getDateCellValue();
                                    employees.setInductionDateTime(d);
                                } else {
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                    try {
                                        Date date = simpleDateFormat.parse(String.valueOf(cell.getStringCellValue()));
                                        employees.setInductionDateTime(date);
                                    } catch (ParseException px) {
                                        px.printStackTrace();
                                        return "第" + r + "行入党时间错误";
                                    }
                                }
                            }
                        }

                    }
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String value = String.valueOf(cell.getStringCellValue()).trim();
                    if (!value.equals("") && value != null) {
                        if (c == 0) {
                            employees.setName(value.replace(" ",""));
                        } else if(c==1) {
                            if(!this.verificationOrg(null,value)){
                                return "第"+r+"行组织机构错误，必须将员工关联根组织";
                            }
                            Organization organization=organizationService.getByname(value);
                            if(organization!=null){
                                employees.setOrganizationId(organization.getId());
                                if(organization.getLinkedId()!=null){
                                    Organization organization1=organizationService.getByLinkedId(organization.getLinkedId());
                                    if(organization1!=null){
                                        employees.setOrganizationId(organization1.getId());
                                    }
                                }
                            }else {
                                return "第"+r+"行组织机构错误，系统中没有对应组织机构";
                            }
                        } else if(c==2){
                            List<Jobs> jobs=jobsService.getByName(value);
                            if(jobs!=null&&jobs.size()>0){
                                employees.setJobsId(jobs.get(0).getId());
                            }else {
                                return "第"+r+"行职务错误，系统中没有对应职务";
                            }
                        }
                        else if(c==3){
                            employees.setIdCard(value);
                            String s=value.substring(6,14);
                            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
                            try {
                                Date date=simpleDateFormat.parse(s);
                                employees.setDateBirth(date);
                            } catch(ParseException px) {
                                px.printStackTrace();
                               return "第"+r+"行身份证错误";
                            }
                        }else if(c==4){
                             if(value.equals("行政")){
                                 employees.setWindowState(2);
                             }else if(value.equals("窗口")){
                                 employees.setWindowState(1);

                             }else if(value.equals("后台")){
                                 employees.setWindowState(0);
                             }else if(value.equals("其他")){
                                 employees.setWindowState(3);
                             }else {
                                 return "第"+r+"行人员类型错误";
                             }
                        }else if(c==5){
                            employees.setWindowNo(value);
                        }else if(c==6){
                           employees.setOffice(value);
                        }else if(c==7){
                            if(value.equals("男")){
                                employees.setSex(0);
                            }else if (value.equals("女")){
                                employees.setSex(1);
                            }else {
                                return  "第"+r+"行，性别错误";
                            }
                        }else if(c==8){
                            if(value.equals("国家机关人员编制")){
                                employees.setUserCompile(0);
                            }else if(value.equals("国家事业单位人员编制")){
                                employees.setUserCompile(1);
                            }else if(value.equals("国家企业单位人员编制")){
                                employees.setUserCompile(2);
                            }else if(value.equals("编外人员")){
                                employees.setUserCompile(3);
                            } else {
                                return  "第"+r+"行，人员编制错误";
                            }
                        }else if(c==9){
                            if(value.equals("是")){
                                employees.setAttendanceState(0);
                            }else if(value.equals("否")){
                                employees.setAttendanceState(1);
                            }else {
                                return  "第"+r+"行，是否考勤错误，只能为是或否";
                            }
                        }else if(c==10){
                            if(value.equals("未婚")){
                                employees.setMaritalStatus(0);
                            }else if(value.equals("已婚")){
                                employees.setMaritalStatus(1);
                            }else if(value.equals("离异")){
                                employees.setMaritalStatus(2);
                            }else if(value.equals("丧偶")){
                                employees.setMaritalStatus(3);
                            }else {
                                return "第"+r+"行，婚姻状态错误";
                            }
                        }else if(c==11){
                            employees.setNational(value);
                        }else if(c==12){
                            if(value.equals("小学")){
                                employees.setRecordOfFormalSchooling(7);
                            } else if(value.equals("初中")){
                                employees.setRecordOfFormalSchooling(6);
                            }else if(value.equals("高中")){
                                employees.setRecordOfFormalSchooling(5);
                            }else if(value.equals("中专")){
                                employees.setRecordOfFormalSchooling(4);
                            }else if(value.equals("大专")){
                                employees.setRecordOfFormalSchooling(3);
                            }else if(value.equals("本科")){
                                employees.setRecordOfFormalSchooling(2);
                            }else if(value.equals("硕士")){
                                employees.setRecordOfFormalSchooling(1);
                            }else if(value.equals("博士")){
                                employees.setRecordOfFormalSchooling(0);
                            }else {
                                return "第"+r+"行，学历错误";
                            }
                        }else if(c==13){
                            if(value.equals("中共党员")){
                                employees.setPartyMemberState(1);
                            }else if(value.equals("中共预备党员")){
                                employees.setPartyMemberState(2);
                            }else if(value.equals("共青团员")){
                                employees.setPartyMemberState(3);
                            }else if(value.equals("民主党派")){
                                employees.setPartyMemberState(4);
                            }else if(value.equals("群众")){
                                employees.setPartyMemberState(5);
                            }else {
                                return "第"+r+"行，政治面貌错误";
                            }
                        }else if(c==14){
                            if(employees.getPartyMemberState()<2){
                                if(value.equals("机关支部")){
                                    employees.setPartyBranch(1);
                                }else if(value.equals("投资项目综合受理区支部")){
                                    employees.setPartyBranch(2);
                                }else if(value.equals("社会事务综合受理区支部")){
                                    employees.setPartyBranch(3);
                                }else if(value.equals("不动产登记综合受理区支部")){
                                    employees.setPartyBranch(4);
                                }else if(value.equals("公安业务受理区支部")){
                                    employees.setPartyBranch(5);
                                }else if(value.equals("商事登记综合受理区支部")){
                                    employees.setPartyBranch(6);
                                }else if(value.equals("新登分中心支部")){
                                    employees.setPartyBranch(7);
                                }else if(value.equals("场口分中心支部")){
                                    employees.setPartyBranch(8);
                                } else if(value.equals("中心机关支部")){
                                    employees.setPartyBranch(9);
                                } else if (value.equals("其他")) {
                                    employees.setPartyBranch(10);
                                } else {
                                    return "第"+r+"行，所在党支部错误";
                                }
                            }
                        }else if(c==16){
                            if(value.equals("是")){
                                employees.setReserveCadresState(1);
                            }else if(value.equals("否")){
                                employees.setReserveCadresState(0);
                            }else {
                                return "第"+r+"行，是否后备干部错误";
                            }
                        }else if(c==17){
                            employees.setPhoneNumber(value);
                        }else if(c==18){
                            employees.setOfficePhone(value);
                        }else if(c==19){
                            employees.setCitizenCards(value);
                        }else if(c==20){
                            employees.setBankCardNumber(value);
                        }
//                        else if(c==21){
//                            employees.setPlateNo(value);
//                        }else if(c==22){
//                            employees.setEmail(value);
//                            if(value!=null||!value.equals("")){
//                                if(!value.matches("^\\w+@(\\w+\\.)+\\w+$")){
//                                    return "第"+r+"行，邮箱格式错误";
//                                }
//                            }
//                        }
                    }
                }
            }
            list.add(employees);
        }
        if(!addEmployees(list)){
            return "系统错误";
        }
        return "操作成功";
    }

    @Transactional
    public  boolean addEmployees(List<Employees> employees) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException, IOException {
        for(Employees employees1:employees){
            employees1.setActivationId(0);
            employees1.setIcon("---");

            String t=getMaxNo(employees1.getOrganizationId());
            Organization organization=organizationService.getById(employees1.getOrganizationId());
            employees1.setEmployeeNo(t);
            employees1.setInductionDateTime(new Date());
            employees1.setWorkingState(1);
            PageData pageData=new PageData();
            pageData.put("idCard",employees1.getIdCard());
            List<EmployeesOutput> employeesOutputList=employeesMapper.selectByIdCard(pageData);

            if(employeesOutputList!=null&&employeesOutputList.size()>0){
                employees1.setEmployeeNo(employeesOutputList.get(0).getEmployeeNo());
                employees1.setId(employeesOutputList.get(0).getId());
                employees1.setIcon(employeesOutputList.get(0).getIcon());
                if(update(employees1.getId(),employees1)<0){
                    return false;
                }
            }else {
                int id=add(employees1);
                Person person=new Person();
                person.setEmployeeId(id);
                person.setName(employees1.getName());
                person.setPersonId(0);
                person.setState(0);
                person.setPhoneNo(employees1.getPhoneNumber());
                person.setGender(employees1.getSex());
                person.setPersonNo(employees1.getEmployeeNo());

                person.setDeptNo(organization.getOrganizationNo());
                person.setDeptName(organization.getName());

                person.setCreatedDateTime(new Date());
//                person.setLastUpdateDateTime(new Date());

                if (personService.add(person) < 0) {
                    return false;
                }
                var user = new Users();
                user.setEmployeeId(id);
                user.setUserType(0);
                user.setPassword(passwordEncoder.encode("123456"));
                user.setIsEnabled(0);
                user.setIsCredentialsNonExpired(0);
                user.setIsAccountNonLocked(0);
                user.setIsAccountNonExpired(0);
                user.setUsername(employees1.getEmployeeNo());
                user.setOrganizationId(employees1.getOrganizationId());
                user.setAdministratorLevel(1);
                var userId = userService.add(user);
                if (userId < 0) {
                    return false;
                } else {
                    userService.setDefaultRole(userId);//保存默认权限
                }
            }
            //下发中心窗口人员到取到叫号
            if ((employees1.getWindowState() == 1 || employees1.getWindowState() == 0) && this.checkCondition(employees1.getOrganizationId())){
                addQueueEmployees(employees1);
            }
        }
        return true;
    }

    //根据id查询一个实体类
    public Employees findById(Integer id){
        return repository.findEmployeesById(id);
    }


    //根据员工编号取出一个实体类
    public Employees findByEmpNo(String empNo){
        return repository.findEmployeesByEmployeeNo(empNo);
    }

    public List<EmployeesOutput> findByplateNo(PageData pageData){
        return employeesMapper.selectByPlateNo(pageData);
    }


    public String export(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "人员信息";
        String excelName = "人员信息";
        String[] rowsName = new String[]{"序号", "工号","姓名" , "组织机构名称", "职务", "入职时间", "状态","手机号码","市民卡号","市民卡物理地址","车牌号","人员类型","身份证号码","性别","所在党支部","婚姻状态","窗口号","人员编制","是否考勤","民族","学历","政治面貌","是否后备干部","办公电话","后台号"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        PageData pageData=new PageData(request);
        pageData.put("userId",getUsers().getId().toString());
        if(getUsers().getAdministratorLevel()!=9){
            if(getUsers().getUserType()==0){
                pageData.put("employeeId",getUsers().getEmployeeId().toString());
            }else {
                pageData.put("orgId",getUsers().getOrganizationId().toString());
            }
        }
        List<EmployeesOutput> employeesOutputList=selectAll(false,pageData);
        if(employeesOutputList.size()>0){
            int i=1;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            for(EmployeesOutput employeesOutput:employeesOutputList){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=employeesOutput.getEmployeeNo();
                objs[2]=employeesOutput.getName();
                objs[3]=employeesOutput.getOrganizationName();
                objs[4]=employeesOutput.getJobsName();
                objs[5]=formatter.format(employeesOutput.getInductionDateTime());
                objs[6]=employeesOutput.getStateName();
                objs[7]=employeesOutput.getPhoneNumber();
                objs[8]=employeesOutput.getCitizenCards();
                objs[9]=employeesOutput.getCitizenCardPhysicalAddress();
                objs[10]=employeesOutput.getPlateNo();
                objs[11]=employeesOutput.getWindowStateName();
                objs[12]=employeesOutput.getIdCard();
                objs[13]=employeesOutput.getSexName();
                objs[14]=employeesOutput.getPartyBranchName();
                objs[15]=employeesOutput.getMaritalStatusName();
                objs[16]=employeesOutput.getWindowName();
                objs[17]=employeesOutput.getUserCompileName();
                objs[18]=employeesOutput.getAttendanceStateName();
                objs[19]=employeesOutput.getNational();
                objs[20]=employeesOutput.getRecordOfFormalSchoolingName();
                objs[21]=employeesOutput.getPartyMemberStateName();
                objs[22]=employeesOutput.getReserveCadresStateName();
                objs[23]=employeesOutput.getOfficePhone();
                objs[24]=employeesOutput.getOffice();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }

    public ResponseResult selectByWindowId(Integer id) {
        List<EmployeesOutput> list =  employeesMapper.selectByWindowId(id);
        return  ResponseResult.success(list);
    }

    //上传excel到服务器
    public  String checkedFiles(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        if(!suffixName.equals(".xls")){
            return "文件类型错误";
        }
        int index = fileName.lastIndexOf("\\");
        if (index != -1) {
            fileName = fileName.substring(index + 1);
        }
        int hashCode = fileName.hashCode();
        //把hash值转换为十六进制
        String hex = Integer.toHexString(hashCode);
        String pathHeader = "D:\\HZFY\\personInfo";
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
        return importcitizenCardPhysicalAddress(wb);

    }

    //把excel数据导入到数据库中
    public  String  importcitizenCardPhysicalAddress(Workbook wb) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        int rows = sheet.getPhysicalNumberOfRows();
        int totalCells = 0;
        // 得到Excel的列数(前提是有行数)
        if (rows > 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<Employees> list=new ArrayList<Employees>();
       for (int r = 1; r < rows; r++){
           Row row = sheet.getRow(r);
           if (row == null) {
               continue;
           }
           var a = row.getCell(0);
           var name = "";
           try{
               name = String.valueOf(a.getStringCellValue()).trim();
           }catch (NullPointerException e){
               break;
           }
           if(name == null || "".equals(name)){
               continue;
           }
           Employees employees=new Employees();
           if (row == null) {
               continue;
           }
           for (int c = 0; c < totalCells; c++) {
               Cell cell = row.getCell(c);
               if(cell==null){
                   break;
               }
               cell.setCellType(Cell.CELL_TYPE_STRING);
               String value= String.valueOf(cell.getStringCellValue()).trim();
               EmployeesOutput employeesOutput=new EmployeesOutput();
               if(c==1){
                   if(value==null||value.equals("")){
                       return "第"+r+"行身份证不能为空";
                   }
                   PageData pageData=new PageData();
                   pageData.put("idCard",value);
                   List<EmployeesOutput> employeesOutputList=employeesMapper.selectByIdCard(pageData);
                   if(employeesOutputList==null||employeesOutputList.size()<0){
                       if(value==null||value.equals("")){
                           return "第"+r+"行身份证错误，未在系统中找到对应人";
                       }
                   }
                   employees.setId(employeesOutputList.get(0).getId());
               } else if(c==2){
                   if(value==null||value.equals("")){
                       return "第"+r+"行市民卡号错误，市民卡号不能为空";
                   }else {
                       employees.setCitizenCards(value);
                   }

               }else if(c==4){
                   if(value==null||value.equals("")){
                       return "第"+r+"行市民卡物理地址不能为空";
                   }else {
                       employees.setCitizenCardPhysicalAddress(value);
                   }
               }
           }
           list.add(employees);
       }
       if(list!=null&&list.size()>0){
           if(!updateEmployees(list)){
               return "系统错误";
           }
       }
        return "操作成功";
    }

    @Transactional
    public boolean updateEmployees(List<Employees> list) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        for(Employees employees:list){
            if(super.update(employees.getId(),employees)<=0){
                return false;
            }
        }
        return true;
    }

    //判断市民卡号是否重复
    public boolean isRepeatCitizenCards(Integer id,Employees employees) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        if(id!=null&&!id.equals("")){
            Employees employees1=getById(id);
            if(employees1!=null&&!employees1.equals("")){
                if(employees.getCitizenCards()!=null&&!employees.getCitizenCards().equals("")){
                    if(employees.getCitizenCards().equals(employees1.getCitizenCards())){
                        return false;
                    }
                }
            }
            if(employeesMapper.selectByCitizenCards(employees.getCitizenCards())!=null){
                return true;
            }
        }
        return false;
    }


    //判断身份证号是否重复
    public boolean isRepeatIdCard(Integer id,Employees employees) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        if(id!=null&&!id.equals("")){
            Employees employees1=getById(id);
            if(employees1!=null&&!employees1.equals("")){
                if(employees.getIdCard()!=null&&!employees.getIdCard().equals("")){
                    if(employees.getIdCard().equals(employees1.getIdCard())){
                        return false;
                    }
                }
            }
        }
        PageData pageData=new PageData();
        pageData.put("idCard",employees.getIdCard());
        List<EmployeesOutput> list=employeesMapper.selectByIdCards(pageData);
        if(list!=null&&list.size()>0){
            return true;
        }
        return false;
    }

    //判断手机号是否重复
    public boolean isRepeatPhoneNumber(Integer id,Employees employees) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        if(id!=null&&!id.equals("")){
            Employees employees1=getById(id);
            if(employees1!=null&&!employees1.equals("")){
                if(employees.getPhoneNumber()!=null&&!employees.getPhoneNumber().equals("")){
                    if(employees.getPhoneNumber().equals(employees1.getPhoneNumber())){
                        return false;
                    }
                }
            }
        }
        PageData pageData=new PageData();
        pageData.put("phoneNumber",employees.getPhoneNumber());
        List<EmployeesOutput> list=employeesMapper.selectByPhoneNumber(pageData);
        if(list!=null&&list.size()>0){
            return true;
        }
        return false;
    }
    /**
     * 获取组织机构编码相同的管理员列表
     * @param organId
     * @return
     */
    public List<EmployeesOutput> getByOrganCode(Integer organId){
        var list = employeesMapper.getByOrganCode(organId);
        return list;
    }


    public  void sendMessage(){
        List<EmployeesOutput> list=employeesMapper.selectAll(null);
        for(EmployeesOutput output:list){
            List<String> list3 = new ArrayList<>();
            list3.add(output.getPhoneNumber()+ "/" +output.getName());
            String description=output.getName()+"您好： 大数据平台的地址是：http://10.32.250.84  您的账号是："+output.getEmployeeNo()+" 您的初始密码是：123456  请尽快修改您的初始密码";
            if(description==null||description.equals("")){
                continue;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("description", description);
            map.put("isTiming", 0);
            map.put("type", MessageTypeEnum.APPROVAL.getCode());
            map.put("sendList",list3);
            try {
                ResponseResult result = HttpRequestUtil.sendPostRequest("http://127.0.0.1:8775/smsSend/form",map);
            } catch (Exception e) {

            }
        }

    }

    public Page<EmployeesOutput> selectPageListWithinAuthority(PageData pageData) {
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        Page<EmployeesOutput> pageList = null;
        pageData.put("userId",getUsers().getId());
        if(getUsers().getAdministratorLevel()==9){
            pageList = employeesMapper.selectPageListWithinAuthority(pageData);
        }else{
            if(getUsers().getUserType()==1){
                pageData.put("orgId",getUsers().getOrganizationId());
            }else{
                pageData.put("employeeId",getUsers().getEmployeeId());
            }
            pageList = employeesMapper.selectPageListWithinAuthority(pageData);
        }
        return pageList;
    }


    public Integer getUserIdByEmpId(Integer empId){
        return employeesMapper.selectUserIdByEmpId(empId);
    }

    public boolean verificationOrg(Integer organizationId, String organizationName) {
        if(organizationId!=null){
            List<Organization> list = organizationMapper.selectByParentId(organizationId);
            if(list!=null&&list.size()>0){
                return false;
            }else{
                return true;
            }
        }
        if(organizationName!=null){
            List<Organization> list = organizationMapper.selectByName(organizationName);
            List<Organization> lists = organizationMapper.selectByParentId(list.get(0).getId());
            if(lists!=null&&lists.size()>0){
                return false;
            }else{
                return true;
            }
        }
        return true;

    }

    /**临时下发新登人员*/
    public void QueueXindengPeople() {

        List<EmployeesOutput> employeesOutputList = employeesMapper.selectXindeng();
        List<QueueEmployees> queueEmployeesList = Lists.newArrayList();
        for(EmployeesOutput employeesOutput:employeesOutputList){

            QueueEmployees queueEmployees = new QueueEmployees();
            queueEmployees.setEmployeesId(employeesOutput.getId());
            queueEmployees.setCode(employeesOutput.getEmployeeNo());
            queueEmployees.setUserName(employeesOutput.getEmployeeNo());
            queueEmployees.setName(employeesOutput.getName());
            queueEmployees.setPassword(password);
            queueEmployees.setDeptCode(employeesOutput.getOrgNo());
            //处理星级，默认为100
            queueEmployees.setRated(Integer.valueOf(rated));
            //处理头像
            queueEmployees.setAvatar(avatarUrlPrefix + employeesOutput.getIcon());
            Jobs jobs = jobsMapper.selectByPrimaryKey(employeesOutput.getJobsId());
            queueEmployees.setJobTitle(jobs.getName());
            //设置成在职状态
            queueEmployees.setEnabled(0);
            //设置成未下发状态
            queueEmployees.setQueueState(0);

            queueEmployees.setCreatedDateTime(new Date());
            queueEmployees.setCreatedUserId(0);
            queueEmployees.setCreatedUserName("排队叫号");
            queueEmployees.setLastUpdateDateTime(new Date());
            queueEmployees.setLastUpdateUserId(0);
            queueEmployees.setLastUpdateUserName("排队叫号");

            queueEmployeesList.add(queueEmployees);
        }
        Integer size = queueEmployeesRepository.saveAll(queueEmployeesList).size();
        logger.info("新登分中心人数:{}",size);
    }

    /**临时下发场口人员*/
    public void QueueCKPeople() {

        List<EmployeesOutput> employeesOutputList = employeesMapper.selectCK();
        List<QueueEmployees> queueEmployeesList = Lists.newArrayList();
        for(EmployeesOutput employeesOutput:employeesOutputList){

            QueueEmployees queueEmployees = new QueueEmployees();
            queueEmployees.setEmployeesId(employeesOutput.getId());
            queueEmployees.setCode(employeesOutput.getEmployeeNo());
            queueEmployees.setUserName(employeesOutput.getEmployeeNo());
            queueEmployees.setName(employeesOutput.getName());
            queueEmployees.setPassword(password);
            queueEmployees.setDeptCode(employeesOutput.getOrgNo());
            //处理星级，默认为100
            queueEmployees.setRated(Integer.valueOf(rated));
            //处理头像
            queueEmployees.setAvatar(avatarUrlPrefix + employeesOutput.getIcon());
            Jobs jobs = jobsMapper.selectByPrimaryKey(employeesOutput.getJobsId());
            queueEmployees.setJobTitle(jobs.getName());
            //设置成在职状态
            queueEmployees.setEnabled(0);
            //设置成未下发状态
            queueEmployees.setQueueState(0);

            queueEmployees.setCreatedDateTime(new Date());
            queueEmployees.setCreatedUserId(0);
            queueEmployees.setCreatedUserName("排队叫号");
            queueEmployees.setLastUpdateDateTime(new Date());
            queueEmployees.setLastUpdateUserId(0);
            queueEmployees.setLastUpdateUserName("排队叫号");

            queueEmployeesList.add(queueEmployees);
        }
        Integer size = queueEmployeesRepository.saveAll(queueEmployeesList).size();
        logger.info("场口分中心人数:{}",size);
    }


}
