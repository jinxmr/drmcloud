package com.ddl.web.system.user.service;

import com.ddl.web.system.user.domain.SysRole;

import java.util.List;
import java.util.Set;

public interface SysRoleService {

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectRoleKeys(Integer userId);

    /**
     * 查询角色信息
     *
     * @param id 角色ID
     * @return 角色信息
     */
    public SysRole selectRoleById(Integer id);

    /**
     * 查询角色列表
     *
     * @param role 角色信息
     * @return 角色集合
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * 新增角色
     *
     * @param role 角色信息
     * @return 结果
     */
    public int insertRole(SysRole role);

    /**
     * 修改角色
     *
     * @param role 角色信息
     * @return 结果
     */
    public int updateRole(SysRole role);

    /**
     * 删除角色信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoleByIds(String ids);

    /**
     * 授权操作
     * @param roleId
     * @param menuIds
     * @return
     */
    public int updateRolePerm(Integer roleId, String menuIds);

    /**
     * 根据用户ID查询所有角色
     * @param userId
     * @return
     */
    List<SysRole> selectRoleListByUserId(Integer userId);
}
