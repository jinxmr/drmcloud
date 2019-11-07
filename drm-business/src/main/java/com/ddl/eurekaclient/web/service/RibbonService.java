package com.ddl.eurekaclient.web.service;

import com.ddl.eurekaclient.web.service.Impl.RibbonServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "drm-system", fallback = RibbonServiceImpl.class)
public interface RibbonService {

    @RequestMapping(value = "/test/hello",method = RequestMethod.GET)
    String helloService(@RequestParam(value = "userId") Integer userId);
}
