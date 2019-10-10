package com.ddl.sys.web.system.user.controller;

import com.ddl.sys.model.AjaxResult;
import com.ddl.sys.model.TableDataInfo;
import com.ddl.sys.model.ZTree;
import com.ddl.sys.web.system.controller.BaseController;
import com.ddl.sys.web.system.user.domain.SysMenu;
import com.ddl.sys.web.system.user.service.SysMenuService;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单 信息操作处理
 *
 * @author drm
 * @date 2019-09-02
 */
@Controller
@RequestMapping("/system/menu" )
public class MenuController extends BaseController {
    private String prefix = "system/menu" ;

    @Autowired
    private SysMenuService menuService;

    @RequiresPermissions("system:menu:view" )
    @GetMapping()
    public String menu() {
        return prefix + "/menu" ;
    }

    /**
     * 查询菜单列表
     */
    @RequiresPermissions("system:menu:list" )
    @PostMapping("/list" )
    @ResponseBody
    public TableDataInfo list(SysMenu menu, @RequestParam(required=false,defaultValue="1")Integer pageNum,
                              @RequestParam(required=false,defaultValue="10")Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<SysMenu> list = menuService.selectMenuList(menu);
        TableDataInfo dataTable = getDataTable(list);
        return dataTable;
    }

    /**
     * 新增菜单
     */
    @GetMapping("/add" )
    public String add() {
        return prefix + "/add" ;
    }

    /**
     * 新增保存菜单
     */
    @RequiresPermissions("system:menu:add" )
    @PostMapping("/add" )
    @ResponseBody
    public AjaxResult addSave(SysMenu menu) {
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @GetMapping("/edit/{id}" )
    public String edit(@PathVariable("id" ) Integer id, ModelMap mmap) {
        SysMenu menu =menuService.selectMenuById(id);
        mmap.put("menu" , menu);
        return prefix + "/edit" ;
    }

    /**
     * 修改保存菜单
     */
    @RequiresPermissions("system:menu:edit" )
    @PostMapping("/edit" )
    @ResponseBody
    public AjaxResult editSave(SysMenu menu) {
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     */
    @RequiresPermissions("system:menu:remove" )
    @PostMapping("/remove" )
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(menuService.deleteMenuByIds(ids));
    }


    /**
     * 查询菜单树结构
     * @return 返回zTree
     */
    @GetMapping("zTreeList")
    @ResponseBody
    public AjaxResult zTreeList(Integer roleId) {

        List<ZTree> zTreeList = menuService.selectMenuZTreeList(roleId);
        return AjaxResult.success("", zTreeList);
    }
}
