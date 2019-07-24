package com.ddl.config.target;

import com.ddl.config.druid.DynamicDataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DynamicDataSourceAspect {
    @Before("@annotation(targetDataSource)")
    public void beforeSwitchDS(JoinPoint point, TargetDataSource targetDataSource){
        DynamicDataSourceContextHolder.setDB(targetDataSource.value());
    }
    @After("@annotation(targetDataSource)")
    public void afterSwitchDS(JoinPoint point,TargetDataSource targetDataSource){
        DynamicDataSourceContextHolder.clearDB();
    }
}