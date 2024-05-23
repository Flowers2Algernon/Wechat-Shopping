package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.annotation.RequiresPermissionsDesc;
import com.cskaoyan.market.db.domain.MarketOrder;
import com.cskaoyan.market.service.admin.AdminOrderService;
import com.cskaoyan.market.util.JacksonUtil;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: jyc
 * @Date: 2024/5/8 20:01
 */
@RestController
@RequestMapping("/admin/order")
public class AdminOrderController {

    @Autowired
    AdminOrderService adminOrderService;

    @RequiresPermissions("admin:order:list")
    @RequiresPermissionsDesc(menu = {"商场管理","订单管理"},button = "查询")
    @GetMapping("list")
    public Object list(HttpServletRequest req, HttpServletResponse resp){
        String pageParam = req.getParameter("page");
        String limitParam = req.getParameter("limit");
        String sort = req.getParameter("sort");
        String order = req.getParameter("order");
        String orderId = req.getParameter("orderId");
        String[] orderStatusArray = req.getParameterValues("orderStatusArray");
        String start = req.getParameter("start");
        String end = req.getParameter("end");
        String userId = req.getParameter("userId");
        String orderSn = req.getParameter("orderSn");

        Integer page = null;
        Integer limit = null;
        try {
            page = Integer.parseInt(pageParam);
            limit = Integer.parseInt(limitParam);
        }catch (Exception e){
            return ResponseUtil.badArgument();
        }
        List<MarketOrder> marketOrders = adminOrderService.list(page,limit,sort,order,orderId,orderStatusArray,start,end,userId,orderSn);
        Object ok = ResponseUtil.okList(marketOrders);
        return ok;
    }


    @GetMapping("channel")
    public Object channel(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Map<String,String>> data = new ArrayList<>();
        Map<String,String> data0 = new HashMap<>();
        data0.put("code","ZTO");
        data0.put("name","中通快递");
        data.add(data0);
        Map<String,String> data1 = new HashMap<>();
        data1.put("code","YTO");
        data1.put("name","圆通速递");
        data.add(data1);
        Map<String,String> data2 = new HashMap<>();
        data2.put("code","RUBBISH");
        data2.put("name","韵达快递");
        data.add(data2);
        Map<String,String> data3 = new HashMap<>();
        data3.put("code","YZPY");
        data3.put("name","邮政快递包裹");
        data.add(data3);
        Map<String,String> data4 = new HashMap<>();
        data4.put("code","E8MS");
        data4.put("name","EMS");
        data.add(data4);
        Map<String,String> data5 = new HashMap<>();
        data5.put("code","DBL");
        data5.put("name","德邦快递");
        data.add(data5);
        Map<String,String> data6 = new HashMap<>();
        data6.put("code","FAST");
        data6.put("name","快捷快递");
        data.add(data6);
        Map<String,String> data7 = new HashMap<>();
        data7.put("code","ZJS");
        data7.put("name","宅急送");
        data.add(data7);
        Map<String,String> data8 = new HashMap<>();
        data8.put("code","TNT");
        data8.put("name","TNT快递");
        data.add(data8);
        Map<String,String> data9 = new HashMap<>();
        data9.put("code","UPS");
        data9.put("name","UPS");
        data.add(data9);
        Map<String,String> data10 = new HashMap<>();
        data10.put("code","DHL");
        data10.put("name","DHL");
        data.add(data10);
        Map<String,String> data11 = new HashMap<>();
        data11.put("code","FEDEX");
        data11.put("name","FEDEX联邦（国内件）");
        data.add(data11);
        Map<String,String> data12 = new HashMap<>();
        data12.put("code","FEDEX_GJ");
        data12.put("name","FEDEX联邦（国际件）");
        data.add(data12);
        Object ok = ResponseUtil.ok(data);
        return ok;
    }
    @RequiresPermissions("admin:order:detail")
    @RequiresPermissionsDesc(menu = {"商场管理","订单管理"},button = "详情")
    @GetMapping("detail")
    public Object detail(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        Map<String,Object> data = new HashMap<>();
        data = adminOrderService.detail(id);
        Object ok = ResponseUtil.ok(data);
        return ok;
    }

    @RequiresPermissions("admin:order:refund")
    @RequiresPermissionsDesc(menu = {"商场管理","订单管理"},button = "订单退款")
    @PostMapping("refund")
    public Object refund(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestBody = req.getReader().readLine();
        String orderId = JacksonUtil.parseString(requestBody, "orderId");
        String refundMoney = JacksonUtil.parseString(requestBody, "refundMoney");
        adminOrderService.refund(orderId,refundMoney);
        Object ok = ResponseUtil.ok();
        return ok;
    }
    @RequiresPermissions("admin:order:ship")
    @RequiresPermissionsDesc(menu = {"商场管理","订单管理"},button = "订单发货")
    @PostMapping("ship")
    public Object ship(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestBody = req.getReader().readLine();
        String orderId = JacksonUtil.parseString(requestBody, "orderId");
        String shipChannel = JacksonUtil.parseString(requestBody, "shipChannel");
        String shipSn = JacksonUtil.parseString(requestBody, "shipSn");
        adminOrderService.ship(orderId,shipChannel,shipSn);
        Object ok = ResponseUtil.ok();
        return ok;
    }

    @RequiresPermissions("admin:order:delete")
    @RequiresPermissionsDesc(menu = {"商场管理","订单管理"},button = "订单删除")
    @PostMapping("delete")
    public Object delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestBody = req.getReader().readLine();
        String orderId = JacksonUtil.parseString(requestBody, "orderId");
        adminOrderService.delete(orderId);
        return ResponseUtil.ok();
    }
    @RequiresPermissions("admin:order:reply")
    @RequiresPermissionsDesc(menu = {"商场管理","订单管理"},button = "订单商品回复")
    @PostMapping("reply")
    public Object reply(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestBody = req.getReader().readLine();
        String commentId = JacksonUtil.parseString(requestBody, "commentId");
        String content = JacksonUtil.parseString(requestBody, "content");
        boolean flag = adminOrderService.updateReply(commentId,content);
        if(flag){
            Object ok = ResponseUtil.ok();
            return ok;
        }else {
            Object fail = ResponseUtil.fail(622, "订单商品已回复");
            return fail;
        }
    }
}
