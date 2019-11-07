package com.ddl.sys;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.ddl.sys.web.system.*.mapper")
@EnableCaching
@RestController
@RefreshScope
public class SystemApplication {

    public static void main(String[] args) {
        System.out.println("SpringCloud");
        SpringApplication.run(SystemApplication.class, args);
    }

    @PostConstruct
    void setDefaultTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

    @Value("${foo}")
    private String foo;

    @RequestMapping("/hi")
    public String foo() {
        return foo;
    }
}
