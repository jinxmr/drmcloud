package com.ddl.web.system.user.controller;

import com.ddl.config.target.TargetDataSource;
import com.ddl.web.system.user.domain.User;
import com.ddl.web.system.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("test")
public class testController {

    @Autowired
    private IUserService userService;

    @GetMapping()
    @ResponseBody
    public String test() {
        User user = userService.selectUserById(1L);
        return user.toString();
    }

    @GetMapping("test1")
    @TargetDataSource("datasource2")
    @ResponseBody
    public String test1() {
        User user = userService.selectUserById(1L);
        return user.toString();
    }

    @GetMapping("toLogin")
    public String toLogin(){
        return "login";
    }
}
