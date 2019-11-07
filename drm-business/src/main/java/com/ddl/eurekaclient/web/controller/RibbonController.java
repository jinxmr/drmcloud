package com.ddl.eurekaclient.web.controller;

import com.ddl.eurekaclient.web.service.RibbonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("ribbon")
public class RibbonController {

    @Autowired
    private RibbonService ribbonService;

    @GetMapping("test")
    @ResponseBody
    public String ribbonTest(Integer userId) {
        String res = ribbonService.helloService(userId);
        return res;
    }
}
