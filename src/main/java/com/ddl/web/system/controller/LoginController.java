package com.ddl.web.system.controller;

import com.ddl.model.AjaxResult;
import com.ddl.utils.Md5Utils;
import com.ddl.utils.ShiroUtils;
import com.ddl.web.system.user.domain.SysMenu;
import com.ddl.web.system.user.domain.SysUser;
import com.ddl.web.system.user.service.IUserService;
import com.ddl.web.system.user.service.SysMenuService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("back")
public class LoginController {

    @Autowired
    private IUserService userService;

    @Autowired
    private SysMenuService selectMenusByUser;

    @GetMapping("login")
    public String toLogin() {
        return "login";
    }

    @ResponseBody
    @PostMapping("dologin")
    public AjaxResult dologin(HttpServletRequest request, HttpServletResponse response, Model model,
                              @RequestParam String userName, @RequestParam String password) {

        UsernamePasswordToken token = new UsernamePasswordToken(userName, password, false);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return AjaxResult.success();
        } catch (Exception e) {
            String msg = "用户名或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return AjaxResult.error(msg);
        }
    }

    @GetMapping("index")
    public String index(Model model, HttpServletRequest request) {
        SysUser sysUser = ShiroUtils.getSysUser();

        // 根据用户id取出菜单
        List<SysMenu> menus = selectMenusByUser.selectMenusByUser(sysUser);
        model.addAttribute("menus", menus);
        model.addAttribute("user", sysUser);
        return "index";
    }
}
