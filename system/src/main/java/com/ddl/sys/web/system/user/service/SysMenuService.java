package com.ddl.sys.web.system.user.service;


import com.ddl.sys.model.ZTree;
import com.ddl.sys.web.system.user.domain.SysMenu;
import com.ddl.sys.web.system.user.domain.SysUser;

import java.util.List;
import java.util.Set;

public interface SysMenuService {

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectPermsByUserId(Integer userId);

    /**
     * 根据用户ID查询菜单
     *
     * @param user 用户信息
     * @return 菜单列表
     */
    public List<SysMenu> selectMenusByUser(SysUser user);

    /**
     * 查询菜单信息
     *
     * @param id 菜单ID
     * @return 菜单信息
     */
    public SysMenu selectMenuById(Integer id);

    /**
     * 查询菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单集合
     */
    public List<SysMenu> selectMenuList(SysMenu menu);

    /**
     * 新增菜单
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int insertMenu(SysMenu menu);

    /**
     * 修改菜单
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public int updateMenu(SysMenu menu);

    /**
     * 删除菜单信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteMenuByIds(String ids);

    /**
     * 查询菜单树
     *
     * @return 菜单集合
     */
    public List<ZTree> selectMenuZTreeList(Integer roleId);
}
