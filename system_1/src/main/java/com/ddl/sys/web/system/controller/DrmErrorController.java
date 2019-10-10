package com.ddl.sys.web.system.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DrmErrorController implements ErrorController {
    /**
     * 异常的分别处理--------------------------------》》》》》》》》》》》》
     *
     * @param request
     * @return
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        //获取statusCode:401,404,500
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        switch (statusCode) {
            case 400:
                return "error/400";
            case 500:
                return "error/500";
            case 404:
                return "error/404";
            case 403:
                return "error/403";
            default:
                return "error/unknownError";
        }

    }

    @Override
    public String getErrorPath() {
        return "error/unknownError";
    }
}