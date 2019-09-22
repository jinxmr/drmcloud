package com.ddl.sys.web.system.user.service;

import com.ddl.sys.model.ZTree;
import com.ddl.sys.utils.TreeUtils;
import com.ddl.sys.web.system.user.domain.SysMenu;
import com.ddl.sys.web.system.user.domain.SysMenuCriteria;
import com.ddl.sys.web.system.user.domain.SysUser;
import com.ddl.sys.web.system.user.mapper.SysMenuMapper;
import com.ddl.sys.web.system.user.mapper.SysRoleMenuMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectPermsByUserId(Integer userId) {
        List<String> perms = sysMenuMapper.selectPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户查询菜单
     *
     * @param user 用户信息
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenusByUser(SysUser user) {
        List<SysMenu> menus = new LinkedList<SysMenu>();
        // 管理员显示所有菜单信息
        if (user.isAdmin()) {
            menus = sysMenuMapper.selectMenuNormalAll();
        } else {
            menus = sysMenuMapper.selectMenusByUserId(user.getId());
        }
        return TreeUtils.getChildPerms(menus, 0);
    }

    /**
     * 根据ID查询
     *
     * @param id 菜单ID
     * @return
     */
    @Override
    public SysMenu selectMenuById(Integer id) {
        SysMenu sysMenu = sysMenuMapper.selectByPrimaryKey(id);
        return sysMenu;
    }

    /**
     * 列表信息
     *
     * @param menu 菜单信息
     * @return
     */
    @Override
    public List<SysMenu> selectMenuList(SysMenu menu) {
        SysMenuCriteria menuCriteria = new SysMenuCriteria();
        SysMenuCriteria.Criteria query = menuCriteria.createCriteria();
        if (StringUtils.isNotEmpty(menu.getMenuName())) {
            query.andMenuNameLike("%" + menu.getMenuName() + "%");
        }
        List<SysMenu> sysMenuList = sysMenuMapper.selectByExample(menuCriteria);
        return sysMenuList;
    }

    /**
     * 插入
     *
     * @param menu 菜单信息
     * @return
     */
    @Override
    public int insertMenu(SysMenu menu) {
        int res = sysMenuMapper.insert(menu);
        return res;
    }

    /**
     * 修改
     *
     * @param menu 菜单信息
     * @return
     */
    @Override
    public int updateMenu(SysMenu menu) {
        int res = sysMenuMapper.updateByPrimaryKeySelective(menu);
        return res;
    }

    /**
     * 删除
     *
     * @param ids 需要删除的数据ID
     * @return
     */
    @Override
    public int deleteMenuByIds(String ids) {
        String[] idArr = ids.split(",");
        List<Integer> idList = com.ddl.sys.utils.StringUtils.arrToList(idArr);
        SysMenuCriteria menuCriteria = new SysMenuCriteria();
        SysMenuCriteria.Criteria query = menuCriteria.createCriteria();
        query.andIdIn(idList);
        int res = sysMenuMapper.deleteByExample(menuCriteria);
        return res;
    }

    /**
     * 查询菜单树
     *
     * @return 菜单集合
     */
    @Override
    public List<ZTree> selectMenuZTreeList(Integer roleId) {
        List<SysMenu> menuList = sysMenuMapper.selectByRoleId(roleId);

        //处理成zTree树
        List<ZTree> zTreeList = new ArrayList<>();
        ZTree zTree = null;
        for(SysMenu menu : menuList) {
            zTree = new ZTree();
            zTree.setId(menu.getId());
            zTree.setPId(menu.getParentId());
            zTree.setName(menu.getMenuName());
            if(null != menu.getRoleId()) {
                zTree.setChecked(true);
            }
            zTreeList.add(zTree);
        }
        return zTreeList;
    }
}
