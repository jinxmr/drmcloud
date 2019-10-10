package com.ddl.sys.web.system.generater.controller;

import com.ddl.sys.model.TableDataInfo;
import com.ddl.sys.web.system.controller.BaseController;
import com.ddl.sys.web.system.generater.domain.TableInfo;
import com.ddl.sys.web.system.generater.service.IGenService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @GetMapping("/list")
    @ResponseBody
    public TableDataInfo list(TableDataInfo<TableInfo> res, TableInfo tableInfo) {
        PageHelper.startPage(res.getPage(), res.getLimit());
        List<TableInfo> list = genService.selectTableList(tableInfo);
        TableDataInfo dataTable = getDataTable(list);
        return dataTable;
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
     * 批量生成代码
     */
    @RequiresPermissions("tool:gen:code")
    @GetMapping("/batchGenCode")
    @ResponseBody
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException
    {
        String[] tableNames = tables.split(",");
        byte[] data = genService.generatorCode(tableNames);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"drm.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
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
