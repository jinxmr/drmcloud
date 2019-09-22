package com.ddl.sys.config.shiro;

import com.ddl.sys.web.system.user.domain.SysUser;
import com.ddl.sys.web.system.user.service.IUserService;
import com.ddl.sys.web.system.user.service.SysMenuService;
import com.ddl.sys.web.system.user.service.SysRoleService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义Realm 处理登录 权限
 *
 * @author ruoyi
 */
public class MyShiroRealm extends AuthorizingRealm {
    private static final Logger log = LoggerFactory.getLogger(MyShiroRealm.class);

    @Autowired
    private SysMenuService menuService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private IUserService userService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUser user = (SysUser) principals.getPrimaryPrincipal();

        // 角色列表
        Set<String> roles = new HashSet<String>();
        // 功能列表
        Set<String> menus = new HashSet<String>();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            info.addRole("admin");
            info.addStringPermission("*:*:*");
        } else {
            roles = roleService.selectRoleKeys(user.getId());
            menus = menuService.selectPermsByUserId(user.getId());
            // 角色加入AuthorizationInfo认证对象
            info.setRoles(roles);
            // 权限加入AuthorizationInfo认证对象
            info.setStringPermissions(menus);
        }
        return info;
    }

    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        //获取userName
        String username = upToken.getUsername();
        String password = String.valueOf(upToken.getPassword());
        SysUser user = userService.login(username, password);
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());    //password是指从数据库获取的password
        return info;
    }

    /**
     * 清理缓存权限
     */
    public void clearCachedAuthorizationInfo() {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }

}