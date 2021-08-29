package com.zlst.demo.controller;


import com.zlst.demo.polo.AdminMenu;
import com.zlst.demo.polo.AdminPermission;
import com.zlst.demo.polo.User;
import com.zlst.demo.result.Result;
import com.zlst.demo.service.AdminPermissionService;
import com.zlst.demo.service.UserService;
import com.zlst.demo.util.ResultFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@CrossOrigin
@Controller
public class LoginController {
    @Autowired
    AdminPermissionService adminPermissionService;
    @Autowired
    UserService userService;

//    @CrossOrigin
//    @PostMapping(value = "/api/login")
//    @ResponseBody
//    public Result login(@RequestBody User requestUser) {
//        String username = requestUser.getUsername();
//        username = HtmlUtils.htmlEscape(username);
//        User user;
////根据用户名密码查询出来唯一一个用户
//        user = userService.get(username, requestUser.getPassword());
//        if (user == null) {
//            return ResultFactory.buildErrorResult();
//        } else {
//            return ResultFactory.buildSuccessResult();
//        }
//    }
    @PostMapping(value = "/api/login")
    @ResponseBody
    public Result login(@RequestBody User requestUser,
                        HttpServletResponse httpServletResponse) {
        String username = requestUser.getUsername();
        Subject subject = SecurityUtils.getSubject();
//        subject.getSession().setTimeout(10000);
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, requestUser.getPassword());
        usernamePasswordToken.setRememberMe(true);
//        Cookie cookie = new Cookie("taken",username);
//        httpServletResponse.addCookie(cookie);
        try {
            subject.login(usernamePasswordToken);
//            列出所有权限和角色，并查询是否存在，存在便添加到结果
            List<AdminPermission> permissionURLs = adminPermissionService.getAllPermission(username);
            Set<String> roles = adminPermissionService.listRoelNameByUser(username);
//            Map<String,Object>  result = new HashMap<>();
//            result.put("permission",permissionURLs);
//            result.put("role",roles);
            return ResultFactory.buildSuccessResult(roles);
        } catch (AuthenticationException e) {
            String message = "账号密码错误";
            return ResultFactory.buildFailResult(message);
        }
    }


    @ResponseBody
    @GetMapping("api/logout")
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        String message = "成功登出";
        return ResultFactory.buildSuccessResult(message);
    }

    //注册方法
    @PostMapping("api/register")
    @ResponseBody
    public Result register(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);

        boolean exist = userService.isExist(username);
        if (exist) {
            String message = "用户名已被使用";
            return ResultFactory.buildFailResult(message);
        }

        // 生成盐,默认长度 16 位
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        // 设置 hash 算法迭代次数
        int times = 2;
        // 得到 hash 后的密码
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();
        // 存储用户信息，包括 salt 与 hash 后的密码
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userService.add(user);

        return ResultFactory.buildSuccessResult(user);
    }
//    列出菜单menu的请求
    @GetMapping("/api/menu")
    @ResponseBody
    public Result getMenu(){
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        List<AdminMenu> adminMenus = adminPermissionService.getAllMenu(username);
        return ResultFactory.buildSuccessResult(adminMenus);
    }
}
