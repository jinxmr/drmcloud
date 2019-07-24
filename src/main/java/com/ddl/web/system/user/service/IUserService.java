package com.ddl.web.system.user.service;

import com.ddl.web.system.user.domain.User;


/**
 * 用户 业务层
 * 
 * @author school
 */
public interface IUserService {
    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public User selectUserById(Long userId);
}
