package com.ddl.web.system.controller;

import com.ddl.model.AjaxResult;
import com.ddl.utils.Md5Utils;
import com.ddl.web.system.user.domain.SysUser;
import com.ddl.web.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("back")
public class LoginController {

    @Autowired
    private IUserService userService;

    @GetMapping("toLogin")
    public String toLogin(){
        return "login";
    }

    @ResponseBody
    @PostMapping("dologin")
    public AjaxResult dologin(HttpServletRequest request, HttpServletResponse response, Model model,
                              @RequestParam String userName, @RequestParam String password) {
        String md5Str = Md5Utils.getMD5Str(password);
        SysUser user = userService.selectByLoginNameAndPassword(userName, md5Str);
        return null == user ? AjaxResult.error("密码错误") : AjaxResult.success();
    }

    @GetMapping("index")
    public String index(Model model,HttpServletRequest request) {

        return "index";
    }
}
