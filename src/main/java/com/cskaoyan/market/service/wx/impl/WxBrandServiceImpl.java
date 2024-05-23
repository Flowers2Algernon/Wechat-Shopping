package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.db.domain.*;
import com.cskaoyan.market.db.mapper.MarketBrandMapper;
import com.cskaoyan.market.db.mapper.MarketCategoryMapper;
import com.cskaoyan.market.db.mapper.MarketGoodsMapper;
import com.cskaoyan.market.service.wx.WxBrandService;
import com.cskaoyan.market.util.ResponseUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WxBrandServiceImpl implements WxBrandService {
    @Autowired
    MarketBrandMapper marketBrandMapper;
    @Autowired
    MarketCategoryMapper marketCategoryMapper;
    @Autowired
    MarketGoodsMapper marketGoodsMapper;
    @Override
    public Object detail(Integer id) {
        MarketBrandExample brandExample = new MarketBrandExample();
        MarketBrandExample.Criteria criteria = brandExample.createCriteria();
        criteria.andIdEqualTo(id);
        List<MarketBrand> marketBrands = marketBrandMapper.selectByExample(brandExample);
        return ResponseUtil.ok(marketBrands.get(0));
    }

    @Override
    public Object list(Integer page, Integer limit) {
        Map<String,Object> map = new HashMap<>();
        MarketBrandExample brandExample = new MarketBrandExample();
        PageHelper.startPage(page,limit);
        List<MarketBrand> marketBrands = marketBrandMapper.selectByExample(brandExample);

            Page page1 = (Page) marketBrands;
            map.put("total", page1.getTotal());
            map.put("page", page1.getPageNum());
            map.put("limit", page1.getPageSize());
            map.put("pages", page1.getPages());

        map.put("list",marketBrands);
        return ResponseUtil.ok(map);
    }
}
