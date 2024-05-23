package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.annotation.RequiresPermissionsDesc;
import com.cskaoyan.market.bo.AddGoodsBo;
import com.cskaoyan.market.db.domain.MarketGoods;
import com.cskaoyan.market.service.admin.MarketGoodsService;
import com.cskaoyan.market.util.ResponseUtil;
import com.cskaoyan.market.vo.CatAndBrandVo;
import com.cskaoyan.market.vo.MarketGoodsVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequiresPermissions("admin:goods:delete")
    @RequiresPermissionsDesc(menu ={"商品管理","商品管理"},button ="删除")
    @PostMapping("delete")
    public Object deleted(@RequestBody MarketGoods goods)  {
        String id = String.valueOf(goods.getId());
        marketGoodsService.delete(Integer.valueOf(id));
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:goods:detail")
    @RequiresPermissionsDesc(menu ={"商品管理","商品管理"},button ="详情")
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
    @RequiresPermissions("admin:goods:list")
    @RequiresPermissionsDesc(menu ={"商品管理","商品管理"},button ="查询")
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

    @RequiresPermissions("admin:goods:update")
    @RequiresPermissionsDesc(menu ={"商品管理","商品管理"},button ="编辑")
    //更新商品
    @PostMapping("update")
    public Object update(@RequestBody AddGoodsBo addGoodsBo){
        boolean result = marketGoodsService.updated(addGoodsBo);
        return ResponseUtil.ok(result);
    }

    //1.接收请求参数---抓包，格式 位于请求体  json字符串  2,存储到数据库   3.返回结果
    @RequiresPermissions("admin:goods:create")
    @RequiresPermissionsDesc(menu ={"商品管理","商品管理"},button ="上架")
    @PostMapping("create")
    public Object create(@RequestBody AddGoodsBo addGoodsBo) {
        marketGoodsService.insertOne(addGoodsBo.getGoods(), addGoodsBo.getSpecifications(), addGoodsBo.getProducts(), addGoodsBo.getAttributes());
        return ResponseUtil.ok();
    }
}