package com.ddl.sys.web.system.controller;

import com.ddl.sys.config.socket.WebSocketServer;
import com.ddl.sys.model.AjaxResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequestMapping("back/socket")
public class NewMessageSendController {
    //页面请求
    @GetMapping("ask/{cid}")
    public ModelAndView socket(@PathVariable String cid) {
        ModelAndView mav=new ModelAndView("/socket");
        mav.addObject("cid", cid);
        return mav;
    }
    //推送数据接口
    @ResponseBody
    @RequestMapping("push/{cid}")
    public AjaxResult pushToWeb(@PathVariable String cid, String message) {
        try {
            WebSocketServer.sendInfo(message,cid);
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.error(cid+"#"+e.getMessage());
        }
        return AjaxResult.success(cid);
    }
}
