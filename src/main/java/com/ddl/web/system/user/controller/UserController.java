package com.ddl.web.system.user.controller;

import java.util.List;

import com.ddl.model.AjaxResult;
import com.ddl.model.TableDataInfo;
import com.ddl.web.enums.BusinessType;
import com.ddl.web.system.controller.BaseController;
import com.ddl.web.system.user.domain.SysUser;
import com.ddl.web.system.user.service.IUserService;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 用户 信息操作处理
 *
 * @author drm
 * @date 2019-09-02
 */
@Controller
@RequestMapping("back/sysUser" )
public class UserController extends BaseController {

    @Autowired
    private IUserService userService;

    @RequiresPermissions("system:user:view" )
    @GetMapping("")
    public String user() {
        return "system/user/user" ;
    }

    /**
     * 查询用户列表
     */
    @RequiresPermissions("system:user:list" )
    @GetMapping("/list" )
    @ResponseBody
    public TableDataInfo list(SysUser user, @RequestParam(required=false,defaultValue="1")Integer pageNum,
                              @RequestParam(required=false,defaultValue="10")Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> list = userService.selectUserList(user);
        TableDataInfo dataTable = getDataTable(list);
        return dataTable;
    }

    /**
     * 新增用户
     */
    @GetMapping("/add" )
    public String add() {
        return "system/user/add" ;
    }

    /**
     * 新增保存用户
     */
    @RequiresPermissions("system:user:add" )
    @PostMapping("/add" )
    @ResponseBody
    public AjaxResult addSave(SysUser user) {
        return toAjax(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @GetMapping("/edit/{id}" )
    public String edit(@PathVariable("id" ) Integer id, ModelMap mmap) {
        SysUser user =userService.selectUserById(id);
        mmap.put("user" , user);
        return "system/user/edit" ;
    }

    /**
     * 修改保存用户
     */
    @RequiresPermissions("system:user:edit" )
    @PostMapping("/edit" )
    @ResponseBody
    public AjaxResult editSave(SysUser user) {
        return toAjax(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @RequiresPermissions("system:user:remove" )
    @PostMapping("/remove" )
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(userService.deleteUserByIds(ids));
    }

}
