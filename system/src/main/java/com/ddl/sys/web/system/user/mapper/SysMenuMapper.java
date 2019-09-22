package com.ddl.sys.web.system.user.mapper;

import com.ddl.sys.web.system.user.domain.SysMenu;
import com.ddl.sys.web.system.user.domain.SysMenuCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysMenuMapper {
    long countByExample(SysMenuCriteria example);

    int deleteByExample(SysMenuCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    List<SysMenu> selectByExample(SysMenuCriteria example);

    SysMenu selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SysMenu record, @Param("example") SysMenuCriteria example);

    int updateByExample(@Param("record") SysMenu record, @Param("example") SysMenuCriteria example);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    public List<String> selectPermsByUserId(Integer userId);

    /**
     * 查询系统正常显示菜单（不含按钮）
     *
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuNormalAll();

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    public List<SysMenu> selectMenusByUserId(Integer userId);

    /**
     * 根据角色ID关联查询菜单
     */
    public List<SysMenu> selectByRoleId(Integer roleId);
}