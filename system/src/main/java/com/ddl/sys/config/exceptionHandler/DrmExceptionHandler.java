package com.ddl.sys.config.exceptionHandler;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class DrmExceptionHandler {

    @ExceptionHandler(value = UnauthorizedException.class)
    public String errorHandler(HttpServletRequest reqest,
                               HttpServletResponse response, Exception e) throws Exception {

        return "error/403";
    }

}