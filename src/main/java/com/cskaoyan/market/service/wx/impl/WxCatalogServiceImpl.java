package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.db.domain.MarketCategory;
import com.cskaoyan.market.db.domain.MarketCategoryExample;
import com.cskaoyan.market.db.mapper.MarketCategoryMapper;
import com.cskaoyan.market.service.wx.WxCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: jyc
 * @Date: 2024/5/21 19:56
 */
@Service
public class WxCatalogServiceImpl implements WxCatalogService {

    @Autowired
    MarketCategoryMapper mapper;

    @Override
    public List<MarketCategory> getList() {
        List<MarketCategory> marketCategoryList = mapper.selectByExample(new MarketCategoryExample());
        return marketCategoryList;
    }

    @Override
    public MarketCategory getCurrent(int id) {
        MarketCategory marketCategory = mapper.selectByPrimaryKey(id);
        return marketCategory;
    }

    @Override
    public List<MarketCategory> getCurrentSub(int pid) {
        MarketCategoryExample marketCategoryExample = new MarketCategoryExample();
        MarketCategoryExample.Criteria criteria = marketCategoryExample.createCriteria();
        criteria.andPidEqualTo(pid);
        List<MarketCategory> marketCategoryList = mapper.selectByExample(marketCategoryExample);
        return marketCategoryList;
    }
}
