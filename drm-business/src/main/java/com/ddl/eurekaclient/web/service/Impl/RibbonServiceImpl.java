package com.ddl.eurekaclient.web.service.Impl;

import com.ddl.eurekaclient.web.service.RibbonService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RibbonServiceImpl implements RibbonService {

    public String helloService(Integer userId) {
        return "hello,这是本地的";
    }
}
