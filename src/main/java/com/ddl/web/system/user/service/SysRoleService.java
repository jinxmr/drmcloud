package com.ddl.web.system.user.service;

import java.util.Set;

public interface SysRoleService {

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public Set<String> selectRoleKeys(Integer userId);
}
