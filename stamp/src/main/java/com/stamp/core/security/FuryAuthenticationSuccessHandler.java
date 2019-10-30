package com.stamp.core.security;

import com.common.response.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stamp.config.RedisComponent;
import com.stamp.domain.output.ActionOutput;
import com.stamp.domain.output.LoginOutput;
import com.stamp.domain.output.MenuOutput;
import com.stamp.domain.output.UsersOutput;
import com.stamp.mapper.mybatis.UsersMapper;
import com.stamp.model.Action;
import com.stamp.model.Menu;
import com.stamp.model.UserRole;
import com.stamp.model.Users;
import com.stamp.service.ActionService;
import com.stamp.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @author: young
 * @project_name: platform
 * @description: 2.自定义登陆成功
 * @date: Created in 2019-04-12  09:45
 * @modified by:
 */
@Component
@Slf4j
public class FuryAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private Long time = 60*60*12L;
    @Autowired
    private MenuService menuService;
    @Autowired
    private ActionService actionService;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private ObjectMapper objectMapper;//JSON转换
    @Autowired
    private RedisComponent redisComponent;

    private static final Integer ADMINISTRATOR=9;
    private static final Integer USER_TYPE = 0;
    private static final Integer ORGAN_TYPE = 1;
    private static final String COOKIE_NAME = "tbdkso";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json;charset=utf-8");

        LoginOutput loginOutput = new LoginOutput();
        //获取users对象
        Users users = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //身份验证
        boolean flag = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        List<UserRole> userRoleList = users.getRoles();
//
        //获取用户对象
        UsersOutput usersOutput = usersMapper.selectByUserName(users.getUsername());
        List<Integer> roleIdList = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<ActionOutput> actionOutputList = null;
        List<MenuOutput> menuOutputList = null;
        //如果是管理员权限
        if(ADMINISTRATOR.equals(usersOutput.getAdministratorLevel())){
            menuOutputList = menuService.selectAll(false,null);
            actionOutputList = actionService.selectAll(false,null);
        }else {
            if(roleIdList.size()>0&&roleIdList!=null){
                //如果不是管理员权限
                menuOutputList = menuService.getByIdIn(roleIdList);
                actionOutputList = actionService.getByIdIn(roleIdList);
            }
        }

        //定位第一个按钮权限,如果没有权限直接返回
        if(menuOutputList.size()==0||menuOutputList==null){
            String str = objectMapper.writeValueAsString(ResponseResult.error("对不起您未开通此系统权限，请联系管理员在角色管理中配置权限"));
            var output = response.getOutputStream();
            output.write(str.getBytes("utf-8"));
            return;
        }

        menuOutputList = menuOutputList.stream()
                .filter(e->e.getDisPlayOrderBy()!=null&&e.getDisPlayOrderBy()>=1000)
                .sorted(Comparator.comparingInt(Menu::getDisPlayOrderBy)).collect(Collectors.toList());
        Integer i = 0;
        String path = menuOutputList.get(i).getPath();
        try {
            while (path==null){
                path = menuOutputList.get(++i).getPath();
            }
        } catch (Exception e) {
            String str = objectMapper.writeValueAsString(ResponseResult.error("对不起您未开通此系统权限，请联系管理员"));
            var output = response.getOutputStream();
            output.write(str.getBytes("utf-8"));
            return;
        }
        loginOutput.setPath(path);
        //放入菜单
        loginOutput.setMenuOutputs(menuOutputList);

        //查看，修改，删除等动作管理
        if(actionOutputList.size()==0||actionOutputList==null){
            loginOutput.setActions(null);
        }else {
            loginOutput.setActions(this.getActionOutput(actionOutputList));
        }

        loginOutput.setUserName(users.getUsername());
        loginOutput.setEmployeesName(usersOutput.getEmployeesName());
        loginOutput.setJobName(usersOutput.getJobsName());
        loginOutput.setJobId(usersOutput.getJobId());
        loginOutput.setEmployeeId(usersOutput.getEmployeeId());
        loginOutput.setUserTypeName(usersOutput.getUserTypeName());
        if(usersOutput.getUserType()>USER_TYPE){
            loginOutput.setOrganizationName(usersOutput.getOrganName());
            loginOutput.setOrganizationId(usersOutput.getOrganizationId());
        }else {
            loginOutput.setOrganId(usersOutput.getOrganId());
            loginOutput.setEmployeeNo(usersOutput.getEmployeeNo());
            loginOutput.setOrganName(usersOutput.getOrganizationName());
            loginOutput.setIcon(usersOutput.getIcon());
        }
        //如果身份验证成功，放入登陆信息
        if(flag){
            ResponseResult.success(loginOutput);
        }else {
            ResponseResult.error("");
        }

        String cookieName = COOKIE_NAME;
        String cookieValue = UUID.randomUUID().toString().replace("-","");
        StringBuffer sb = new StringBuffer();
        sb.append(users.getUsername()).append("-").append(users.getPassword());
        Integer maxValue = 60*60*60;

        //放入cookie
        Cookie cookie = new Cookie(cookieName,cookieValue);
        cookie.setPath("/");
        cookie.setMaxAge(maxValue);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        //放到redis
        try {
            redisComponent.set(cookieValue,sb.toString(),time);
            log.info("cookieValue:{}",redisComponent.get(cookieValue));
            response.flushBuffer();
        } catch (Exception e) {
            log.error("e:{}",e);
        }

        String str = objectMapper.writeValueAsString(ResponseResult.success(loginOutput));
        var out = response.getOutputStream();
        out.write(str.getBytes("UTF-8"));
    }

    private Map<String, List<String>> getActionOutput(List<ActionOutput> actions) {
        Map<String, List<String>> map = new HashMap<>();
        if (actions.size() == 0||actions==null) {
            return null;
        }
        List<Action> action = actions.stream().filter(Action -> Action.getCode().indexOf("M_STAMP") >= 0).collect(toList());
        List<String> idList = action.stream().map(Action::getCode).collect(toList());
        for (var str : idList) {
            map.put(str, new ArrayList<>());
        }

        return map;
    }

}
