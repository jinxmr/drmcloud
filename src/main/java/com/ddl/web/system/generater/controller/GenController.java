package com.ddl.web.system.generater.controller;

import com.ddl.model.TableDataInfo;
import com.ddl.web.system.controller.BaseController;
import com.ddl.web.system.generater.domain.TableInfo;
import com.ddl.web.system.generater.service.IGenService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 代码生成 操作处理
 *
 * @author jxm
 */
@Controller
@RequestMapping("/back/tool/gen")
@Slf4j
public class GenController extends BaseController {
    @Autowired
    private IGenService genService;

    @RequiresPermissions("tool:gen:view")
    @GetMapping()
    public String gen() {
        return "system/gen/gen";
    }

    @RequiresPermissions("tool:gen:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TableInfo tableInfo, @PathVariable("pageNum")Integer pageNum, @PathVariable("pageSize")Integer pageSize) {
        PageHelper.startPage(0, 10);
        List<TableInfo> list = genService.selectTableList(tableInfo);
        return getDataTable(list);
    }

    /**
     * 生成代码
     */
    @RequiresPermissions("tool:gen:code")
    @GetMapping("/genCode/{tableName}")
    public void genCode(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
        byte[] data = genService.generatorCode(tableName);
        genCode(response, data);
    }

    /**
     * 生成zip文件
     *
     * @param response
     * @param data
     * @throws IOException
     */
    private void genCode(HttpServletResponse response, byte[] data) throws IOException {
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"drm.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}
