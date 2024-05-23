package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.service.admin.AdminDashboardService;
import com.cskaoyan.market.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: jyc
 * @Date: 2024/5/6 17:22
 * Dashboard接口，实现主页数量的显示
 * http://localhost:8083/admin/dashboard
 */
@RestController
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {

    @Autowired
    AdminDashboardService adminDashboardService;

    @GetMapping
    public Object dashBoard(){
        Map<String, String> data = new HashMap<>();
        int goodsTotal = adminDashboardService.getGoodsTotal();
        int userTotal = adminDashboardService.getUserTotal();
        int productTotal = adminDashboardService.getProductTotal();
        int orderTotal = adminDashboardService.getOrderTotal();

        data.put("goodsTotal", Integer.toString(goodsTotal));
        data.put("userTotal", Integer.toString(userTotal));
        data.put("productTotal", Integer.toString(productTotal));
        data.put("orderTotal", Integer.toString(orderTotal));

        Object ok = ResponseUtil.ok(data);
        return ok;
    }
}
