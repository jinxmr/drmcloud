package com.ddl.sys.web.system.user.service;


import com.ddl.sys.utils.ShiroUtils;
import com.ddl.sys.utils.StringUtils;
import com.ddl.sys.web.enums.UserDictEnums;
import com.ddl.sys.web.system.user.domain.*;
import com.ddl.sys.web.system.user.mapper.SysRoleMapper;
import com.ddl.sys.web.system.user.mapper.SysRoleMenuMapper;
import com.ddl.sys.web.system.user.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

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
        query.andStatusEqualTo(String.valueOf(UserDictEnums.OK.getCode()));
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
        verify(role);
        SysUser user = ShiroUtils.getSysUser();
        role.setCreateBy(user.getUserName());
        role.setCreateTime(new Date());
        int res = sysRoleMapper.insertSelective(role);
        return res;
    }

    private void verify(SysRole role) {
        //查询角色名称 权限标识是否重复
        SysRoleCriteria roleCriteria = new SysRoleCriteria();
        SysRoleCriteria.Criteria query = roleCriteria.createCriteria();
        //现根据角色名称查询 重复则抛异常
        query.andRoleNameEqualTo(role.getRoleName());
        List<SysRole> roles_1 = sysRoleMapper.selectByExample(roleCriteria);
        if(null != roles_1 && roles_1.size()>0) {
            throw new RuntimeException("角色名称重复");
        }

        roleCriteria = new SysRoleCriteria();
        query = roleCriteria.createCriteria();
        //现根据角色名称查询 重复则抛异常
        query.andRoleNameEqualTo(role.getRoleName());
        List<SysRole> roles_2 = sysRoleMapper.selectByExample(roleCriteria);
        if(null != roles_1 && roles_1.size()>0) {
            throw new RuntimeException("权限标识重复重复");
        }
    }

    /**
     * 修改角色信息
     * @param role 角色信息
     * @return
     */
    @Override
    public int updateRole(SysRole role) {
        verify(role);
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
        if (res > 0) {
            //删除roleMenu中间表数据
            SysRoleMenuCriteria roleMenuCriteria = new SysRoleMenuCriteria();
            SysRoleMenuCriteria.Criteria roleMenuQuery = roleMenuCriteria.createCriteria();
            roleMenuQuery.andRoleIdIn(idList);
            sysRoleMenuMapper.deleteByExample(roleMenuCriteria);

            //删除userRole中间表数据
            SysUserRoleCriteria userRoleCriteria = new SysUserRoleCriteria();
            SysUserRoleCriteria.Criteria userRoleQuery = userRoleCriteria.createCriteria();
            userRoleQuery.andRoleIdIn(idList);
            sysUserRoleMapper.deleteByExample(userRoleCriteria);
        }
        return res;
    }

    /**
     * 授权操作
     * @param roleId
     * @param menuIds
     * @return
     */
    @Override
    @Transactional
    public int updateRolePerm(Integer roleId, String menuIds) {

        //先删除在新增
        SysRoleMenuCriteria roleMenuCriteria = new SysRoleMenuCriteria();
        SysRoleMenuCriteria.Criteria roleMenuQuery = roleMenuCriteria.createCriteria();
        roleMenuQuery.andRoleIdEqualTo(roleId);
        sysRoleMenuMapper.deleteByExample(roleMenuCriteria);

        String[] menuIdArr = menuIds.split(",");
        List<SysRoleMenu> roleMenuList = new ArrayList<>();
        SysRoleMenu roleMenu = null;
        for(String menuId : menuIdArr) {
            roleMenu = new SysRoleMenu();
            roleMenu.setMenuId(Integer.valueOf(menuId));
            roleMenu.setRoleId(roleId);
            roleMenuList.add(roleMenu);
        }
        int res = sysRoleMenuMapper.batchInsert(roleMenuList);
        return res;
    }

    /**
     * 根据用户ID查询所有角色
     * @param userId
     * @return
     */
    @Override
    public List<SysRole> selectRoleListByUserId(Integer userId) {

        //查询所有角色
        SysRoleCriteria roleCriteria = new SysRoleCriteria();
        SysRoleCriteria.Criteria query = roleCriteria.createCriteria();
        String.valueOf(UserDictEnums.OK.getCode());
        List<SysRole> roles = sysRoleMapper.selectByExample(roleCriteria);

        //根据用户Id查询被选中的角色
        List<SysRole> checkedRoles = sysRoleMapper.selectRolesByUserId(userId);
        for(SysRole role : roles) {
            for(SysRole role_1 : checkedRoles) {
                if(role.getId() == role_1.getId()) {    //被选中
                    role.setChecked(true);
                }
            }

        }

        return roles;
    }
}
