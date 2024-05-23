package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.annotation.RequiresPermissionsDesc;
import com.cskaoyan.market.db.domain.MarketSystem;
import com.cskaoyan.market.service.admin.MarketConfigService;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: jyc
 * @Date: 2024/5/6 20:08
 * 配置管理部分。商场配置，运费配置，订单配置，小程序配置。
 */
@RestController
@RequestMapping("/admin/config")
public class AdminConfigController {

    @Autowired
    MarketConfigService configService ;

    @RequiresPermissions("admin:config:wx")
    @RequiresPermissionsDesc(menu = {"配置管理", "小程序配置"}, button = "详情")
    @GetMapping("wx")
    public Object queryWx() {
        String index_new = configService.queryMarketConfigByName("market_wx_index_new").getKeyValue();
        String index_topic = configService.queryMarketConfigByName("market_wx_index_topic").getKeyValue();
        String share = configService.queryMarketConfigByName("market_wx_share").getKeyValue();
        String index_brand = configService.queryMarketConfigByName("market_wx_index_brand").getKeyValue();
        String catlog_goods = configService.queryMarketConfigByName("market_wx_catlog_goods").getKeyValue();
        String catlog_list = configService.queryMarketConfigByName("market_wx_catlog_list").getKeyValue();
        String index_hot = configService.queryMarketConfigByName("market_wx_index_hot").getKeyValue();

        Map<String, String> data = new HashMap<>();

        data.put("market_wx_index_new", index_new);
        data.put("market_wx_index_topic", index_topic);
        data.put("market_wx_share", share);
        data.put("market_wx_index_brand", index_brand);
        data.put("market_wx_catlog_goods", catlog_goods);
        data.put("market_wx_catlog_list", catlog_list);
        data.put("market_wx_index_hot", index_hot);
        Object ok = ResponseUtil.ok(data);
        return ok;
    }

    @RequiresPermissions("admin:config:order")
    @RequiresPermissionsDesc(menu = {"配置管理", "订单配置"}, button = "详情")
    @GetMapping("order")
    public Object queryOrder() {
        String unconfirm = configService.queryMarketConfigByName("market_order_unconfirm").getKeyValue();
        String unpaid = configService.queryMarketConfigByName("market_order_unpaid").getKeyValue();
        String comment = configService.queryMarketConfigByName("market_order_comment").getKeyValue();

        Map<String,String> data = new HashMap<>();
        data.put("market_order_unconfirm",unconfirm);
        data.put("market_order_unpaid",unpaid);
        data.put("market_order_comment",comment);

        Object ok = ResponseUtil.ok(data);
        return ok;
    }

    @RequiresPermissions("admin:config:express")
    @RequiresPermissionsDesc(menu = {"配置管理", "运费配置"}, button = "详情")
    @GetMapping("express")
    public Object queryExpress() {
        String min = configService.queryMarketConfigByName("market_express_freight_min").getKeyValue();
        String value = configService.queryMarketConfigByName("market_express_freight_value").getKeyValue();

        Map<String, String> data = new HashMap<>();

        data.put("market_express_freight_min", min);
        data.put("market_express_freight_value", value);

        Object ok = ResponseUtil.ok(data);
        return ok;
    }

    @RequiresPermissions("admin:config:mall")
    @RequiresPermissionsDesc(menu = {"配置管理", "商场配置"}, button = "详情")
    @GetMapping("mall")
    public Object queryMall() {

        String longitude = configService.queryMarketConfigByName("market_mall_longitude").getKeyValue();
        String latitude = configService.queryMarketConfigByName("market_mall_latitude").getKeyValue();
        String address = configService.queryMarketConfigByName("market_mall_address").getKeyValue();
        String qq = configService.queryMarketConfigByName("market_mall_qq").getKeyValue();
        String phone = configService.queryMarketConfigByName("market_mall_phone").getKeyValue();
        String name = configService.queryMarketConfigByName("market_mall_name").getKeyValue();

        Map<String, String> data = new HashMap<>();

        data.put("market_mall_longitude", longitude);
        data.put("market_mall_latitude", latitude);
        data.put("market_mall_address", address);
        data.put("market_mall_qq", qq);
        data.put("market_mall_phone", phone);
        data.put("market_mall_name", name);
        Object ok = ResponseUtil.ok(data);
        return ok;
    }


    @RequiresPermissions("admin:config:wx")
    @RequiresPermissionsDesc(menu = {"配置管理", "小程序配置"}, button = "编辑")
    @PostMapping("wx")
    public Object updateWx(@RequestBody Map<String,String> map) {

        String index_new = map.get("market_wx_index_new");
        String index_topic = map.get("market_wx_index_topic");
        String share = map.get("market_wx_share");
        String index_brand = map.get("market_wx_index_brand");
        String catlog_goods = map.get("market_wx_catlog_goods");
        String catlog_list = map.get("market_wx_catlog_list");
        String index_hot = map.get("market_wx_index_hot");

        MarketSystem marketSystem_index_new = new MarketSystem();
        MarketSystem marketSystem_index_topic = new MarketSystem();
        MarketSystem marketSystem_share = new MarketSystem();
        MarketSystem marketSystem_index_brand = new MarketSystem();
        MarketSystem marketSystem_catlog_goods = new MarketSystem();
        MarketSystem marketSystem_catlog_list = new MarketSystem();
        MarketSystem marketSystem_index_hot = new MarketSystem();

        marketSystem_index_new.setId(2);
        marketSystem_index_new.setKeyValue(index_new);
        marketSystem_index_topic.setId(18);
        marketSystem_index_topic.setKeyValue(index_topic);
        marketSystem_share.setId(5);
        marketSystem_share.setKeyValue(share);
        marketSystem_index_brand.setId(17);
        marketSystem_index_brand.setKeyValue(index_brand);
        marketSystem_catlog_goods.setId(12);
        marketSystem_catlog_goods.setKeyValue(catlog_goods);
        marketSystem_catlog_list.setId(15);
        marketSystem_catlog_list.setKeyValue(catlog_list);
        marketSystem_index_hot.setId(10);
        marketSystem_index_hot.setKeyValue(index_hot);

        configService.updateById(marketSystem_index_new);
        configService.updateById(marketSystem_index_topic);
        configService.updateById(marketSystem_share);
        configService.updateById(marketSystem_index_brand);
        configService.updateById(marketSystem_catlog_goods);
        configService.updateById(marketSystem_catlog_list);
        configService.updateById(marketSystem_index_hot);
        Object ok = ResponseUtil.ok();
        return ok;
    }

    @RequiresPermissions("admin:config:order")
    @RequiresPermissionsDesc(menu = {"配置管理", "订单配置"}, button = "编辑")
    @PostMapping("order")
    public Object updateOrder(@RequestBody Map<String,String>map){
        String unconfirm = map.get("market_order_unconfirm");
        String unpaid = map.get("market_order_unpaid");
        String comment = map.get("market_order_comment");
        MarketSystem marketSystem_unconfirm = new MarketSystem();
        MarketSystem marketSystem_unpaid = new MarketSystem();
        MarketSystem marketSystem_comment = new MarketSystem();
        marketSystem_unconfirm.setId(4);
        marketSystem_unconfirm.setKeyValue(unconfirm);
        marketSystem_unpaid.setId(1);
        marketSystem_unpaid.setKeyValue(unpaid);
        marketSystem_comment.setId(11);
        marketSystem_comment.setKeyValue(comment);

        configService.updateById(marketSystem_unconfirm);
        configService.updateById(marketSystem_unpaid);
        configService.updateById(marketSystem_comment);
        Object ok = ResponseUtil.ok();
        return ok;
    }

    @RequiresPermissions("admin:config:express")
    @RequiresPermissionsDesc(menu = {"配置管理", "运费配置"}, button = "编辑")
    @PostMapping("express")
    public Object updateExpress(@RequestBody Map<String,String>map) {
        String min = map.get("market_express_freight_min");
        String value = map.get("market_express_freight_value");
        MarketSystem marketSystem_min = new MarketSystem();
        MarketSystem marketSystem_value = new MarketSystem();
        marketSystem_min.setId(6);
        marketSystem_min.setKeyValue(min);
        marketSystem_value.setId(8);
        marketSystem_value.setKeyValue(value);
        configService.updateById(marketSystem_min);
        configService.updateById(marketSystem_value);
        Object ok = ResponseUtil.ok();
        return ok;
    }

    @RequiresPermissions("admin:config:mall")
    @RequiresPermissionsDesc(menu = {"配置管理", "商场配置"}, button = "编辑")
    @PostMapping("mall")
    public Object updateMall(@RequestBody Map<String,String>map) {
        String longitude = map.get("market_mall_longitude");
        String latitude = map.get("market_mall_latitude");
        String address = map.get("market_mall_address");
        String qq = map.get("market_mall_qq");
        String phone = map.get("market_mall_phone");
        String name = map.get("market_mall_name");

        // 每条数据都是一个MarketSystem
        MarketSystem marketSystem_longitude = new MarketSystem();
        MarketSystem marketSystem_latitude = new MarketSystem();
        MarketSystem marketSystem_address = new MarketSystem();
        MarketSystem marketSystem_qq = new MarketSystem();
        MarketSystem marketSystem_phone = new MarketSystem();
        MarketSystem marketSystem_name = new MarketSystem();
        // 这里用KeyName查找也可以，不过要构造MarketSystemExample
        marketSystem_longitude.setId(13);
        marketSystem_longitude.setKeyValue(longitude);
        marketSystem_latitude.setId(3);
        marketSystem_latitude.setKeyValue(latitude);
        marketSystem_address.setId(16);
        marketSystem_address.setKeyValue(address);
        marketSystem_qq.setId(9);
        marketSystem_qq.setKeyValue(qq);
        marketSystem_phone.setId(14);
        marketSystem_phone.setKeyValue(phone);
        marketSystem_name.setId(7);
        marketSystem_name.setKeyValue(name);

        configService.updateById(marketSystem_longitude);
        configService.updateById(marketSystem_latitude);
        configService.updateById(marketSystem_address);
        configService.updateById(marketSystem_qq);
        configService.updateById(marketSystem_phone);
        configService.updateById(marketSystem_name);

        Object ok = ResponseUtil.ok();
        return ok;
    }
}
