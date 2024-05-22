package com.cskaoyan.market.service.impl;

import com.cskaoyan.market.db.domain.MarketFootprint;
import com.cskaoyan.market.db.domain.MarketFootprintExample;
import com.cskaoyan.market.db.mapper.MarketFootprintMapper;
import com.cskaoyan.market.service.MarketFootprintService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketFootprintServiceImpl implements MarketFootprintService {
    private final MarketFootprintMapper marketFootprintMapper;

    @Autowired
    public MarketFootprintServiceImpl(MarketFootprintMapper marketFootprintMapper) {
        this.marketFootprintMapper = marketFootprintMapper;
    }

    @Override
    public List<MarketFootprint> list(Integer limit, Integer page, String userId, String sort, String order, String goodsId) {
        MarketFootprintExample marketFootprintExample = new MarketFootprintExample();
        marketFootprintExample.setOrderByClause(sort + " " + order);
        MarketFootprintExample.Criteria criteria = marketFootprintExample.createCriteria();
        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if (!StringUtils.isEmpty(goodsId)) {
            criteria.andGoodsIdEqualTo(Integer.valueOf(goodsId));
        }
        PageHelper.startPage(page, limit);
        return marketFootprintMapper.selectByExampleSelective(marketFootprintExample,
                MarketFootprint.Column.id, MarketFootprint.Column.userId, MarketFootprint.Column.goodsId,
                MarketFootprint.Column.addTime, MarketFootprint.Column.updateTime, MarketFootprint.Column.deleted);
    }
}
