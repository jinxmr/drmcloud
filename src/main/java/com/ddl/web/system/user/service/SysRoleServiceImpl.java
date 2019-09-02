package com.ddl.web.system.user.service;


import com.ddl.utils.StringUtils;
import com.ddl.web.system.user.domain.SysRole;
import com.ddl.web.system.user.domain.SysRoleCriteria;
import com.ddl.web.system.user.mapper.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRoleKeys(Integer userId) {
        List<SysRole> perms = sysRoleMapper.selectRolesByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            if (null != perm) {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据ID查询
     * @param id 角色ID
     * @return
     */
    @Override
    public SysRole selectRoleById(Integer id) {
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(id);
        return sysRole;
    }

    /**
     * 查询角色列表
     * @param role 角色信息
     * @return
     */
    @Override
    public List<SysRole> selectRoleList(SysRole role) {
        SysRoleCriteria roleCriteria = new SysRoleCriteria();
        SysRoleCriteria.Criteria query = roleCriteria.createCriteria();
        if(StringUtils.isNotEmpty(role.getRoleName())) {
            query.andRoleNameLike("%" + role.getRoleName() + "%");
        }
        List<SysRole> sysRoleList = sysRoleMapper.selectByExample(roleCriteria);
        return sysRoleList;
    }

    /**
     * 插入角色
     * @param role 角色信息
     * @return
     */
    @Override
    public int insertRole(SysRole role) {
        int res = sysRoleMapper.insertSelective(role);
        return res;
    }

    /**
     * 修改角色信息
     * @param role 角色信息
     * @return
     */
    @Override
    public int updateRole(SysRole role) {
        int res = sysRoleMapper.updateByPrimaryKeySelective(role);
        return res;
    }

    /**
     * 删除角色
     * @param ids 需要删除的数据ID
     * @return
     */
    @Override
    public int deleteRoleByIds(String ids) {
        SysRoleCriteria roleCriteria = new SysRoleCriteria();
        SysRoleCriteria.Criteria query = roleCriteria.createCriteria();
        String[] idArr = ids.split(",");
        List<Integer> idList = StringUtils.arrToList(idArr);
        query.andIdIn(idList);
        int res = sysRoleMapper.deleteByExample(roleCriteria);
        return res;
    }
}
