package com.ddl.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {
    //从配置文件配置数据源
    @Primary
    @Bean(name="datasource1")
    @ConfigurationProperties("spring.datasource.db1")
    public DataSource dataSource1(){
        return new DruidDataSource();
    }
    //从配置文件配置数据源
    @Bean(name="datasource2")
    @ConfigurationProperties("spring.datasource.db2")
    public DataSource dataSource2(){
        return new DruidDataSource();
    }
    //动态数据源 进行数据源切换
    @Bean(name="dynamicDataSource")
    public DataSource dynamicDataSource(){
        DynamicDataSource dynamicDatasource=new DynamicDataSource();
        //设置默认数据源
        dynamicDatasource.setDefaultTargetDataSource(dataSource1());
        //配置多数据源
        Map<Object,Object> dsMap=new HashMap<>();
        dsMap.put("datasource1",dataSource1());
        dsMap.put("datasource2",dataSource2());
        //将多数据源放到数据源池中
        dynamicDatasource.setTargetDataSources(dsMap);
        return dynamicDatasource;
    }
}