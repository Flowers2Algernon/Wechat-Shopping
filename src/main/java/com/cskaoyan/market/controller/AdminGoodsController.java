package com.cskaoyan.market.controller;

import com.cskaoyan.market.bo.AddGoodsBo;
import com.cskaoyan.market.db.domain.MarketGoods;
import com.cskaoyan.market.service.MarketGoodsService;
import com.cskaoyan.market.service.impl.MarketGoodsServiceImpl;
import com.cskaoyan.market.util.JacksonUtil;
import com.cskaoyan.market.util.ResponseUtil;
import com.cskaoyan.market.vo.CatAndBrandVo;
import com.cskaoyan.market.vo.MarketGoodsVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AdminGoodsController
 * @Description: TODO
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/3/16 9:47
 * @Version V1.0
 **/
@RestController
@RequestMapping("/admin/goods")
public class AdminGoodsController {

    @Autowired
    MarketGoodsService marketGoodsService;

    @PostMapping("delete")
    public Object deleted(@RequestBody MarketGoods goods)  {
        String id = String.valueOf(goods.getId());
        marketGoodsService.delete(Integer.valueOf(id));
        return ResponseUtil.ok();
    }

    @GetMapping("detail")
    public Object detail(Integer id) {
        MarketGoodsVo marketGoods = marketGoodsService.detail(id);
        return ResponseUtil.ok(marketGoods);
    }

    //返回商城商品的品牌和类目信息
    @GetMapping("catAndBrand")
    public Object catAndBrand() {
        Map<String, List<CatAndBrandVo>> catAndBrands = marketGoodsService.catAndBrand();
        return ResponseUtil.ok(catAndBrands);
    }

    @GetMapping("list")
    public Object list(String page,String limit,String goodsSn,String name,String sort,String order,String goodsId) {
        Integer pageNumber = null;
        Integer limitNumber = null;
        try {
            pageNumber = Integer.parseInt(page);
            limitNumber = Integer.parseInt(limit);
        } catch (Exception e) {
            return ResponseUtil.badArgument();
        }
        List<MarketGoods> goodsList = marketGoodsService.list(pageNumber, limitNumber, goodsSn, goodsId, name, sort, order);
        return ResponseUtil.okList(goodsList);
    }

    //更新商品
    @PostMapping("update")
    public Object update(@RequestBody AddGoodsBo addGoodsBo){
        boolean result = marketGoodsService.updated(addGoodsBo);
        return ResponseUtil.ok(result);
    }

    //1.接收请求参数---抓包，格式 位于请求体  json字符串  2,存储到数据库   3.返回结果
    @PostMapping("create")
    public Object create(@RequestBody AddGoodsBo addGoodsBo) {
        marketGoodsService.insertOne(addGoodsBo.getGoods(), addGoodsBo.getSpecifications(), addGoodsBo.getProducts(), addGoodsBo.getAttributes());
        return ResponseUtil.ok();
    }
}