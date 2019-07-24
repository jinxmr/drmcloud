package com.ddl.web.system.user.mapper;

import com.ddl.web.system.user.domain.User;

import java.util.List;

/**
 * 用户表 数据层
 * 
 * @author school
 */
public interface UserMapper
{
    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public User selectUserById(Long userId);
}
