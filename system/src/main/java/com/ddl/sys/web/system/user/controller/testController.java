package com.ddl.sys.web.system.user.controller;

import com.ddl.sys.config.target.TargetDataSource;
import com.ddl.sys.web.system.user.domain.SysUser;
import com.ddl.sys.web.system.user.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("back1")
public class testController {

    @Autowired
    private IUserService userService;

    @GetMapping("sysUser1")
    @RequiresPermissions("sys:user:view")
    @ResponseBody
    public String test() {
        SysUser user = userService.selectUserById(1);
        return user.toString();
    }

    @GetMapping("test1")
    @TargetDataSource("datasource2")
    @ResponseBody
    public String test1() {
        SysUser user = userService.selectUserById(1);
        return user.toString();
    }

    @GetMapping("hello")
    @ResponseBody
    public String test2(String name) {
        return "hello," + name;
    }
}
