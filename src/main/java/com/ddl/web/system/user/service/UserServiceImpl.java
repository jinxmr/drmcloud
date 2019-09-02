package com.ddl.web.system.user.service;

import com.ddl.utils.StringUtils;
import com.ddl.web.enums.UserDictEnums;
import com.ddl.web.system.user.domain.SysUser;
import com.ddl.web.system.user.domain.SysUserCriteria;
import com.ddl.web.system.user.mapper.SysUserMapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
     *
     * @param username 登陆名
     */
    public SysUser login(String username, String password) {
        Map<String, Object> query = new HashMap<>();
        query.put("loginName", username);
        SysUser user = userMapper.selectByLoginNameAndPassword(query);
        if (null == user) {
            throw new UnknownAccountException("用户不存在");
        }
        //用户停用
        if (user.getStatus() == UserDictEnums.DISABLE.getCode()) {
            throw new DisabledAccountException("用户已停用，请联系管理员");
        }
        //用户密码错误
        if (!matches(user, password)) {
            throw new AuthenticationException("密码错误");
        }
        return user;
    }

    public boolean matches(SysUser user, String newPassword) {
        return user.getPassword().equals(encryptPassword(user.getLoginName(), newPassword, user.getLoginName()));
    }

    public String encryptPassword(String username, String password, String salt) {
        return new SimpleHash("md5", password,
                ByteSource.Util.bytes(salt), 2).toHex();
    }

    /**
     * 分页查询列表
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public List<SysUser> selectUserList(SysUser user) {
        SysUserCriteria userCriteria = new SysUserCriteria();
        SysUserCriteria.Criteria query = userCriteria.createCriteria();
        if (StringUtils.isNotEmpty(user.getLoginName())) {
            query.andLoginNameLike("%" + user.getLoginName() + "%");
        }
        if (StringUtils.isNotEmpty(user.getUserName())) {
            query.andUserNameLike("%" + user.getUserName() + "%");
        }
        List<SysUser> sysUsers = userMapper.selectByExample(userCriteria);
        return sysUsers;
    }

    /**
     * 用户插入
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public int insertUser(SysUser user) {
        int res = userMapper.insert(user);
        return res;
    }

    /**
     * 用户修改
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public int updateUser(SysUser user) {
        int res = userMapper.updateByPrimaryKeySelective(user);
        return res;
    }

    /**
     * 删除用户
     *
     * @param ids 需要删除的数据ID
     * @return
     */
    @Override
    public int deleteUserByIds(String ids) {
        SysUserCriteria userCriteria = new SysUserCriteria();
        SysUserCriteria.Criteria query = userCriteria.createCriteria();
        String[] idArr = ids.split(",");
        List<Integer> idList = StringUtils.arrToList(idArr);
        query.andIdIn(idList);
        int res = userMapper.deleteByExample(userCriteria);
        return res;
    }
}
