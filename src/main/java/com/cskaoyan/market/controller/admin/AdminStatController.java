package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.annotation.RequiresPermissionsDesc;
import com.cskaoyan.market.service.admin.AdminStatService;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
  @Author: jyc
 * @Date: 2024/5/7 16:21
 * @Description: 统计图表
 */

@RestController
@RequestMapping("/admin/stat")
public class AdminStatController{

    @Autowired
    AdminStatService adminStatService;

    @RequiresPermissions("admin:stat:goods")
    @RequiresPermissionsDesc(menu = {"统计管理", "商品统计"}, button = "查询")
    @GetMapping("goods")
    public Object goodsStat() {
        Map<String, Object> data = new HashMap<>();
        List<String> columns = new ArrayList<>();
        List<Map<String,String>> goodsRows = adminStatService.getGoodsRows();

        columns.add("day");
        columns.add("orders");
        columns.add("products");
        columns.add("amount");
        data.put("columns", columns);
        data.put("rows",goodsRows);

        Object ok = ResponseUtil.ok(data);
        return ok;
    }

    @RequiresPermissions("admin:stat:order")
    @RequiresPermissionsDesc(menu = {"统计管理", "订单统计"}, button = "查询")
    @GetMapping("order")
    public Object orderStat() {
        Map<String, Object> data = new HashMap<>();
        List<String> columns = new ArrayList<>();
        List<Map<String,String>> orderRows = adminStatService.getOrderRows();

        columns.add("day");
        columns.add("orders");
        columns.add("customers");
        columns.add("amount");
        columns.add("pcr");
        data.put("columns", columns);
        data.put("rows",orderRows);

        Object ok = ResponseUtil.ok(data);
        return ok;
    }

    @RequiresPermissions("admin:stat:user")
    @RequiresPermissionsDesc(menu = {"统计管理", "用户统计"}, button = "查询")
    @GetMapping("user")
    public Object userStat() {
        Map<String, Object> data = new HashMap<>();
        List<String> columns = new ArrayList<>();
        List<Map<String,String>> userRows = adminStatService.getUserRows();

        columns.add("day");
        columns.add("users");
        data.put("columns", columns);
        data.put("rows",userRows);

        Object ok = ResponseUtil.ok(data);
        return ok;
    }
}