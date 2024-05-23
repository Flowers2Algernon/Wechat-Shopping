package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.annotation.RequiresPermissionsDesc;
import com.cskaoyan.market.db.domain.MarketLog;
import com.cskaoyan.market.service.admin.AdminLogService;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("admin/log")
//@WebServlet("/admin/log/*")
public class AdminLogController{
    @Autowired
    AdminLogService adminLogService;
//    private AdminLogService adminLogService = new AdminLogServiceImpl();
    @RequiresPermissions("admin:log:list")
    @RequiresPermissionsDesc(menu = {"系统管理","操作日志"},button = "查询")
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
