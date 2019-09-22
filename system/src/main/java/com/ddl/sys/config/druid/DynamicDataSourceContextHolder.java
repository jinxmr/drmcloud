package com.ddl.sys.config.druid;

public class DynamicDataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder=new ThreadLocal<>();
    //设置数据源名称
    public static void setDB(String dbType){
        contextHolder.set(dbType);
    }
    //获取数据源名称
    public static String getDB(){
        return contextHolder.get();
    }
    //清除数据源名
    public static void clearDB(){
        contextHolder.remove();
    }
}