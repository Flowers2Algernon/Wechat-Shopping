package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.annotation.RequiresPermissionsDesc;
import com.cskaoyan.market.db.domain.MarketBrand;
import com.cskaoyan.market.service.admin.BrandService;
import com.cskaoyan.market.util.ResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/brand")
public class AdminBrandController {
    @Autowired
    BrandService brandService;

    @RequiresPermissions("admin:brand:list")
    @RequiresPermissionsDesc(menu = {"商场管理","品牌管理"},button = "查询")
    @GetMapping("list")
    @ResponseBody
    public Object list(String page, String limit, String id, String name, String sort, String order){
        Integer page1 = null;
        Integer limit1 = null;
        try {
            page1 = Integer.parseInt(page);
            limit1 = Integer.parseInt(limit);
        }catch (Exception e){
            return ResponseUtil.badArgument();
        }
        List<MarketBrand> brandList = brandService.list(page1, limit1, id, name, sort, order);
        return ResponseUtil.okList(brandList);

    }

    @RequiresPermissions("admin:brand:delete")
    @RequiresPermissionsDesc(menu = {"商场管理","品牌管理"},button = "删除")
    @PostMapping("delete")
    public Object delete(@RequestBody MarketBrand marketBrand){

        brandService.delete(marketBrand);
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:brand:update")
    @RequiresPermissionsDesc(menu = {"商场管理","品牌管理"},button = "编辑")
    @PostMapping("update")
    public Object update(@RequestBody MarketBrand marketBrand) throws JsonProcessingException {

        MarketBrand updateMarketBrand = brandService.update(marketBrand);
        return ResponseUtil.ok(updateMarketBrand);
    }

    @RequiresPermissions("admin:brand:create")
    @RequiresPermissionsDesc(menu = {"商场管理","品牌管理"},button = "添加")
    @PostMapping("create")
    @ResponseBody
    public Object create(@RequestBody MarketBrand marketBrand) throws JsonProcessingException {

        MarketBrand insertOneMarketBrand = brandService.insertOne(marketBrand);
        return ResponseUtil.ok(insertOneMarketBrand);

    }

}
