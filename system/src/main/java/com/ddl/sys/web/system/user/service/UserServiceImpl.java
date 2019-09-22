package com.ddl.sys.web.system.user.service;

import com.ddl.sys.utils.PasswordEncryptionUtil;
import com.ddl.sys.utils.StringUtils;
import com.ddl.sys.web.enums.UserDictEnums;
import com.ddl.sys.web.system.user.domain.SysUser;
import com.ddl.sys.web.system.user.domain.SysUserCriteria;
import com.ddl.sys.web.system.user.domain.SysUserRole;
import com.ddl.sys.web.system.user.domain.SysUserRoleCriteria;
import com.ddl.sys.web.system.user.mapper.SysUserMapper;
import com.ddl.sys.web.system.user.mapper.SysUserRoleMapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 用户 业务层处理
 *
 * @author school
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Value("${initialize.password}")
    private String initPassword;

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
        return user.getPassword().equals(PasswordEncryptionUtil.encryptPassword(newPassword, user.getLoginName()));
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
        userCriteria.setOrderByClause("ID DESC");
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
    @Transactional
    public int insertUser(SysUser user, String roleIds) {
        verify(user);
        user.setCreateDate(new Date()); //创建时间
        user.setDelFlag(UserDictEnums.OK.getCode());    //是否删除
        user.setPassword(PasswordEncryptionUtil.encryptPassword(initPassword, user.getLoginName()));
        int res = userMapper.insert(user);

        //批量插入用户角色
        batchInsertUserRole(user, roleIds);
        return res;
    }

    /**
     * 验证登录账号 手机号 邮箱是否有重复
     * @param user
     */
    private void verify(SysUser user) {
        Integer id = user.getId();

        //登录账号校验
        SysUserCriteria criteria = new SysUserCriteria();
        SysUserCriteria.Criteria query = criteria.createCriteria();
        query.andLoginNameEqualTo(user.getLoginName());
        if(null != id) {    //修改校验
            query.andIdNotEqualTo(id);
        }
        List<SysUser> users = userMapper.selectByExample(criteria);
        if (null != users && users.size() > 0) {
            throw new RuntimeException("登录账号重复");
        }

        //手机号校验
        criteria = new SysUserCriteria();
        query = criteria.createCriteria();
        query.andMobileEqualTo(user.getMobile());
        if(null != id) {    //修改校验
            query.andIdNotEqualTo(id);
        }
        List<SysUser> users_1 = userMapper.selectByExample(criteria);
        if (null != users_1 && users_1.size() > 0) {
            throw new RuntimeException("手机号已被注册");
        }

        //Email校验
        criteria = new SysUserCriteria();
        query = criteria.createCriteria();
        query.andEmailEqualTo(user.getEmail());
        if(null != id) {    //修改校验
            query.andIdNotEqualTo(id);
        }
        List<SysUser> users_2 = userMapper.selectByExample(criteria);
        if (null != users_2 && users_2.size() > 0) {
            throw new RuntimeException("邮箱已被注册");
        }
    }

    /**
     * 用户修改
     *
     * @param user 用户信息
     * @return
     */
    @Override
    @Transactional
    public int updateUser(SysUser user, String roleIds) {

        verify(user);
        int res = userMapper.updateByPrimaryKeySelective(user);

        //删除该用户的所有角色
        SysUserRoleCriteria userRoleCriteria = new SysUserRoleCriteria();
        SysUserRoleCriteria.Criteria delQuery = userRoleCriteria.createCriteria();
        delQuery.andUserIdEqualTo(user.getId());
        userRoleMapper.deleteByExample(userRoleCriteria);
        //批量插入用户角色
        batchInsertUserRole(user, roleIds);
        return res;
    }

    /**
     * 批量插入用户角色 新增/修改时
     * @param user
     * @param roleIds
     */
    private void batchInsertUserRole(SysUser user, String roleIds) {
        if (StringUtils.isNotEmpty(roleIds)) {
            String[] roleIdArr = roleIds.split(",");
            List<SysUserRole> userRoleList = new ArrayList<>();
            SysUserRole userRole = null;
            for (String roleId : roleIdArr) {
                userRole = new SysUserRole();
                userRole.setRoleId(Integer.valueOf(roleId));
                userRole.setUserId(user.getId());
                userRoleList.add(userRole);
            }
            userRoleMapper.batchInsert(userRoleList);
        }
    }

    /**
     * 删除用户
     *
     * @param ids 需要删除的数据ID
     * @return
     */
    @Override
    public int deleteUserByIds(String ids) {
        //删除用户
        SysUserCriteria userCriteria = new SysUserCriteria();
        SysUserCriteria.Criteria query = userCriteria.createCriteria();
        String[] idArr = ids.split(",");
        List<Integer> idList = StringUtils.arrToList(idArr);
        query.andIdIn(idList);
        int res = userMapper.deleteByExample(userCriteria);

        //删除用户绑定的权限
        SysUserRoleCriteria userRoleCriteria = new SysUserRoleCriteria();
        SysUserRoleCriteria.Criteria userRoleQuery = userRoleCriteria.createCriteria();
        userRoleQuery.andUserIdIn(idList);
        userRoleMapper.deleteByExample(userRoleCriteria);
        return res;
    }
}
