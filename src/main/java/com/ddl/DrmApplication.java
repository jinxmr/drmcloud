package com.ddl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ddl.web.system.user.mapper")
public class DrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(DrmApplication.class, args);
	}

}
