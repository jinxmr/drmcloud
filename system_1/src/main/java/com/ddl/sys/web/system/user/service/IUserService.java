package com.ddl.sys.web.system.user.service;



import com.ddl.sys.web.system.user.domain.SysUser;

import java.util.List;

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
    public SysUser selectUserById(Integer userId);

    /**
     * 登录
     */
    public SysUser login(String username, String password);

    /**
     * 查询用户列表
     *
     * @param user 用户信息
     * @return 用户集合
     */
    public List<SysUser> selectUserList(SysUser user);

    /**
     * 新增用户
     *
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(SysUser user, String roleIds);

    /**
     * 修改用户
     *
     * @param user 用户信息
     * @return 结果
     */
    public int updateUser(SysUser user, String roleIds);

    /**
     * 删除用户信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteUserByIds(String ids);
}
