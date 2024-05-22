package com.cskaoyan.market.service.impl;

import com.cskaoyan.market.db.domain.MarketGoodsExample;
import com.cskaoyan.market.db.domain.MarketGoodsProductExample;
import com.cskaoyan.market.db.domain.MarketOrderExample;
import com.cskaoyan.market.db.domain.MarketUserExample;
import com.cskaoyan.market.db.mapper.*;
import com.cskaoyan.market.service.AdminDashboardService;
import com.cskaoyan.market.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.error.Mark;

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
