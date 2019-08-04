package com.ddl.web.system.user.service;

import com.ddl.web.system.user.domain.SysMenu;
import com.ddl.web.system.user.domain.SysUser;

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
}
