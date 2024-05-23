package com.cskaoyan.market.service.admin.impl;

import com.cskaoyan.market.db.domain.MarketGoodsExample;
import com.cskaoyan.market.db.domain.MarketGoodsProductExample;
import com.cskaoyan.market.db.domain.MarketOrderExample;
import com.cskaoyan.market.db.domain.MarketUserExample;
import com.cskaoyan.market.db.mapper.MarketGoodsMapper;
import com.cskaoyan.market.db.mapper.MarketGoodsProductMapper;
import com.cskaoyan.market.db.mapper.MarketOrderMapper;
import com.cskaoyan.market.db.mapper.MarketUserMapper;
import com.cskaoyan.market.service.admin.AdminDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: jyc
 * @Date: 2024/5/10 14:26
 */
@Service
public class AdminDashBoardIServiceImpl implements AdminDashboardService {
    @Autowired
    MarketUserMapper marketUserMapper;

    @Autowired
    MarketGoodsProductMapper marketGoodsProductMapper;

    @Autowired
    MarketOrderMapper marketOrderMapper;

    @Autowired
    MarketGoodsMapper marketGoodsMapper;

    @Override
    public int getGoodsTotal() {

        int l = (int) marketGoodsMapper.countByExample(new MarketGoodsExample());
        return l;
    }

    @Override
    public int getUserTotal() {
        int l = (int)marketUserMapper.countByExample(new MarketUserExample());
        return l;
    }

    @Override
    public int getProductTotal() {
        long l = marketGoodsProductMapper.countByExample(new MarketGoodsProductExample());
        return (int)l;
    }

    @Override
    public int getOrderTotal() {
        long l = marketOrderMapper.countByExample(new MarketOrderExample());
        return (int)l;
    }
}
