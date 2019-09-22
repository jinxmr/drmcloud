package com.ddl.sys.web.system.user.controller;

import com.ddl.sys.model.AjaxResult;
import com.ddl.sys.model.TableDataInfo;
import com.ddl.sys.web.system.controller.BaseController;
import com.ddl.sys.web.system.user.domain.SysRole;
import com.ddl.sys.web.system.user.service.SysRoleService;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色 信息操作处理
 *
 * @author drm
 * @date 2019-09-02
 */
@Controller
@RequestMapping("back/sysRole" )
public class RoleController extends BaseController {

    @Autowired
    private SysRoleService roleService;

    @RequiresPermissions("system:role:view" )
    @GetMapping()
    public String role() {
        return "system/role/role" ;
    }

    /**
     * 查询角色列表
     */
    @RequiresPermissions("system:role:list" )
    @GetMapping("/list" )
    @ResponseBody
    public TableDataInfo list(SysRole role, @RequestParam(required=false,defaultValue="1")Integer pageNum,
                              @RequestParam(required=false,defaultValue="10")Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<SysRole> list = roleService.selectRoleList(role);
        TableDataInfo dataTable = getDataTable(list);
        return dataTable;
    }

    /**
     * 新增角色
     */
    @GetMapping("/add" )
    public String add() {
        return "system/role/add" ;
    }

    /**
     * 新增保存角色
     */
    @RequiresPermissions("system:role:add" )
    @PostMapping("/add" )
    @ResponseBody
    public AjaxResult addSave(SysRole role) {
        int res = 0;
        try {
            res = roleService.insertRole(role);
        } catch (RuntimeException r) {
            return AjaxResult.error(r.getMessage());
        }
        return toAjax(res);
    }

    /**
     * 修改角色
     */
    @GetMapping("/edit/{id}" )
    public String edit(@PathVariable("id" ) Integer id, ModelMap mmap) {
        SysRole role =roleService.selectRoleById(id);
        mmap.put("role" , role);
        return "system/role/edit" ;
    }

    /**
     * 修改保存角色
     */
    @RequiresPermissions("system:role:edit" )
    @PostMapping("/edit" )
    @ResponseBody
    public AjaxResult editSave(SysRole role) {
        int res = 0;
        try {
            res = roleService.updateRole(role);
        } catch (RuntimeException r) {
            return AjaxResult.error(r.getMessage());
        }
        return toAjax(res);
    }

    /**
     * 删除角色
     */
    @RequiresPermissions("system:role:remove" )
    @PostMapping("/remove" )
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(roleService.deleteRoleByIds(ids));
    }

    /**
     * 跳转到菜单树
     * @return
     */
    @RequestMapping("toZTree/{dataId}")
    public String toZTree(@PathVariable("dataId") Integer dataId, Model model) {
        model.addAttribute("dataId", dataId);
        return "system/role/assignPerm";
    }

    /**
     * 授权操作
     * @param roleId
     * @param menuIds
     * @return
     */
    @PostMapping("editRole")
    @ResponseBody
    public AjaxResult editRole(Integer roleId, String menuIds) {

        int res = roleService.updateRolePerm(roleId, menuIds);
        return toAjax(res);
    }
}
