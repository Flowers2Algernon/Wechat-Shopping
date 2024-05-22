package com.cskaoyan.market.controller;

import com.cskaoyan.market.db.domain.MarketLog;
import com.cskaoyan.market.service.AdminLogService;
import com.cskaoyan.market.service.impl.AdminLogServiceImpl;
import com.cskaoyan.market.util.JacksonUtil;
import com.cskaoyan.market.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("admin/log")
//@WebServlet("/admin/log/*")
public class AdminLogController{
    @Autowired
    AdminLogService adminLogService;
//    private AdminLogService adminLogService = new AdminLogServiceImpl();

    @GetMapping("list")
    private Object list(HttpServletRequest req, HttpServletResponse resp){
        String pageParam = req.getParameter("page");
        String limitParam = req.getParameter("limit");
        String username = req.getParameter("name");
        String sort = req.getParameter("sort");
        String order = req.getParameter("order");
        Integer limit = null;
        Integer page = null;
        try {
            limit = Integer.parseInt(limitParam);
            page= Integer.parseInt(pageParam);
        }catch (Exception e){
            return (ResponseUtil.badArgument());
        }
        List<MarketLog> list =adminLogService.list(page, limit, username, sort,order);
        Object o = ResponseUtil.okList(list);
        return o;
    }
}
