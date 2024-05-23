package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.db.domain.MarketStorage;
import com.cskaoyan.market.db.domain.MarketStorageExample;
import com.cskaoyan.market.db.mapper.MarketStorageMapper;
import com.cskaoyan.market.service.wx.MarketStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketStorageServiceWxImpl implements MarketStorageService {
    @Autowired
    MarketStorageMapper marketStorageMapper;
    @Override
    public MarketStorage upload(MarketStorage marketStorage) {
        marketStorageMapper.insertSelective(marketStorage);
        MarketStorageExample marketStorageExample = new MarketStorageExample();
        MarketStorageExample.Criteria criteria = marketStorageExample.createCriteria();
        criteria.andUrlEqualTo(marketStorage.getUrl());
        MarketStorage marketStorage1 = marketStorageMapper.selectOneByExample(marketStorageExample);
        return marketStorage1;
    }
}
