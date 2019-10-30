package com.attendance.core.security;

import com.attendance.domian.output.ActionOutput;
import com.attendance.domian.output.LoginOutput;
import com.attendance.domian.output.MenuOutput;
import com.attendance.domian.output.UsersOutput;
import com.attendance.mapper.mybatis.UsersMapper;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.attendance.config.RedisComponent;
import com.attendance.model.Action;
import com.attendance.model.Menu;
import com.attendance.model.UserRole;
import com.attendance.model.Users;
import com.attendance.service.ActionService;
import com.attendance.service.MenuService;
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

import static java.util.stream.Collectors.toList;


@Component
public class FuryAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private Long time = 60L * 60 * 12;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ActionService actionService;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private ObjectMapper objectMapper; // Json转化工具

    @Autowired
    private RedisComponent redisComponent;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        SecurityContextHolder securityContextHolder = new SecurityContextHolder();
        LoginOutput loginOutput = new LoginOutput();
        Users users = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //身份验证
        boolean flag = SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        List<UserRole> list = users.getRoles();
        //获取ip地址
        WebAuthenticationDetails map = (WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        String IP = map.getRemoteAddress();

        var userOutput = usersMapper.selectByUserName(users.getUsername());
        List<Integer> list1 = list.stream().map(UserRole::getRoleId).collect(toList());
        List<ActionOutput> actions = null;
        List<MenuOutput> menus = null;
        if (userOutput.getAdministratorLevel() == 9) {
            menus = menuService.selectAll(false, null);
            actions = actionService.selectAll(false, null);
        } else {
            if (list1 != null && list1.size() > 0) {
                menus = menuService.getByIdIn(list1);
                actions = actionService.getByIdIn(list1);
            }
        }

        /**
         * 定位权限的第一个菜单
         */
        if(menus == null || menus.size() <= 0){
            var str = objectMapper.writeValueAsString(ResponseResult.error("对不起您未开通此系统权限，请联系管理员"));
            var out = response.getOutputStream();
            out.write(str.getBytes("UTF-8"));
            return;
        }
        menus = menus.stream().filter(MenuOutput -> MenuOutput.getDisPlayOrderBy() != null && MenuOutput.getDisPlayOrderBy() >= 1000 ).sorted(Comparator.comparingInt(MenuOutput::getDisPlayOrderBy)).collect(toList());

        var i = 0;

        var path = menus.get(i).getPath();
        try{
            while (path == null){
                path = menus.get(++i).getPath();
            }
        }catch (Exception e){
            var str = objectMapper.writeValueAsString(ResponseResult.error("对不起您未开通此系统权限，请联系管理员"));
            var out = response.getOutputStream();
            out.write(str.getBytes("UTF-8"));
            return;
        }

        loginOutput.setPath(path);
        //放入菜单
        loginOutput.setMenuOutputs(menus);
        if (actions == null || actions.size() <= 0) {
            loginOutput.setActions(null);
        } else {
            loginOutput.setActions(getActionOutput(actions));
        }
        loginOutput.setUserName(users.getUsername());
        loginOutput.setEmployeesName(userOutput.getEmployeesName());
        loginOutput.setJobName(userOutput.getJobsName());

        loginOutput.setJobId(userOutput.getJobId());
        loginOutput.setEmployeeId(userOutput.getEmployeeId());

        if (userOutput.getUserType() > 0) {
            loginOutput.setOrganizationName(userOutput.getOrganName());
            loginOutput.setOrganizationId(userOutput.getOrganizationId());
        } else {
            loginOutput.setOrganId(userOutput.getOrganId());
            loginOutput.setOrganName(userOutput.getOrganizationName());
            loginOutput.setIcon(userOutput.getIcon());

        }

        if (flag) {
            ResponseResult.success(loginOutput);
        } else {
            ResponseResult.error("");
        }

        var cookieName = "tbdkso";
        var cookieValue = UUID.randomUUID().toString().replace("-", "");
        StringBuilder sb = new StringBuilder();
        sb.append(users.getUsername()).append("-" + users.getPassword());
        var maxValue = 60 * 60 * 12;

        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/");
        if (maxValue != 0) {
            cookie.setMaxAge(maxValue);
        } else {
            cookie.setMaxAge(maxValue);
        }
        cookie.setHttpOnly(true);
        response.addCookie(cookie);


        try {
            redisComponent.set(cookieValue, sb.toString(), time);
            System.out.println(redisComponent.get(cookieValue));
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e);
        }
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        var str = objectMapper.writeValueAsString(ResponseResult.success(loginOutput));
        var out = response.getOutputStream();
        out.write(str.getBytes("UTF-8"));
    }


    private List<MenuOutput> getMenuOutput(List<MenuOutput> menus) {
        List<MenuOutput> out = new ArrayList<>();
        List<MenuOutput> firstMenu = menus.stream().filter(Menu -> Menu.getParentId() == 0).sorted(Comparator.comparingInt(Menu::getDisPlayOrderBy)).collect(toList());
        out.addAll(firstMenu);
        for (MenuOutput menuOutput : out) {
            if (menuOutput.getHasChild() == 1) {
                List<MenuOutput> list = menus.stream().filter(Menu -> Menu.getParentId().equals(menuOutput.getId())).collect(toList());
                menuOutput.setChildren(list);
                getMenuOutput(list);
            } else {
                continue;
            }
        }
        return out;
    }


    private Map<String, List<String>> getActionOutput(List<ActionOutput> actions) {
        Map<String, List<String>> map = new HashMap<>();
        if (actions.size() <= 0) {
            return null;
        }
        List<Action> action = actions.stream().filter(Action -> Action.getCode().indexOf("A_ATTENDANCE") >= 0).collect(toList());
        List<String> idList = action.stream().map(Action::getCode).collect(toList());
        for (var str : idList) {
            map.put(str, new ArrayList<>());
        }

        return map;
    }


}
