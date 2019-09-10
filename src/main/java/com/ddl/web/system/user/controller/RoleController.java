package com.ddl.web.system.user.controller;

import java.util.List;

import com.ddl.model.AjaxResult;
import com.ddl.model.TableDataInfo;
import com.ddl.web.enums.BusinessType;
import com.ddl.web.system.controller.BaseController;
import com.ddl.web.system.user.domain.SysRole;
import com.ddl.web.system.user.service.SysRoleService;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 角色 信息操作处理
 *
 * @author drm
 * @date 2019-09-02
 */
@Controller
@RequestMapping("/system/role" )
public class RoleController extends BaseController {
    private String prefix = "system/role" ;

    @Autowired
    private SysRoleService roleService;

    @RequiresPermissions("system:role:view" )
    @GetMapping()
    public String role() {
        return prefix + "/role" ;
    }

    /**
     * 查询角色列表
     */
    @RequiresPermissions("system:role:list" )
    @PostMapping("/list" )
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
        return prefix + "/add" ;
    }

    /**
     * 新增保存角色
     */
    @RequiresPermissions("system:role:add" )
    @PostMapping("/add" )
    @ResponseBody
    public AjaxResult addSave(SysRole role) {
        return toAjax(roleService.insertRole(role));
    }

    /**
     * 修改角色
     */
    @GetMapping("/edit/{id}" )
    public String edit(@PathVariable("id" ) Integer id, ModelMap mmap) {
        SysRole role =roleService.selectRoleById(id);
        mmap.put("role" , role);
        return prefix + "/edit" ;
    }

    /**
     * 修改保存角色
     */
    @RequiresPermissions("system:role:edit" )
    @PostMapping("/edit" )
    @ResponseBody
    public AjaxResult editSave(SysRole role) {
        return toAjax(roleService.updateRole(role));
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

}