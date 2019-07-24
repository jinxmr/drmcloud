package com.ddl.web.system.user.service;

import com.ddl.web.system.user.domain.SysUser;
import com.ddl.web.system.user.mapper.SysUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户 业务层处理
 * 
 * @author school
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private SysUserMapper userMapper;

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    /**
     * 登录
     * @param loginName 登陆名
     * @param password 密码
     * @return
     */
    @Override
    public SysUser selectByLoginNameAndPassword(String loginName, String password) {
        Map<String, Object> query = new HashMap<>();
        query.put("loginName", loginName);
        query.put("password", password);
        SysUser user = userMapper.selectByLoginNameAndPassword(query);
        return user;
    }
}
