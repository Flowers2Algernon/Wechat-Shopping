package com.cskaoyan.market.controller.wx;

import com.cskaoyan.market.db.domain.MarketCategory;
import com.cskaoyan.market.service.wx.WxCatalogService;
import com.cskaoyan.market.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: jyc
 * @Date: 2024/5/21 19:51
 */
@RestController
@RequestMapping("/wx/catalog")
public class WxCatalogController {
    @Autowired
    WxCatalogService wxCatalogService;

    @GetMapping("index")
    public Object index(){
        Map<String,Object> data = new HashMap<>();
        List<MarketCategory> marketCategoryList = wxCatalogService.getList();
        MarketCategory currentCategory = wxCatalogService.getCurrent(1005000);
        List<MarketCategory> currentSubList = wxCatalogService.getCurrentSub(1005000);
        data.put("categoryList",marketCategoryList);
        data.put("currentCategory",currentCategory);
        data.put("currentSubCategory",currentSubList);

        return ResponseUtil.ok(data);
    }

    @GetMapping("current")
    public Object current(Integer id){
        Map<String,Object> data = new HashMap<>();
        MarketCategory currentCategory = wxCatalogService.getCurrent(id);
        List<MarketCategory> currentSubList = wxCatalogService.getCurrentSub(id);
        data.put("currentCategory",currentCategory);
        data.put("currentSubCategory",currentSubList);
        return ResponseUtil.ok(data);
    }
}
