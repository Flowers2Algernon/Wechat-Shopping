package com.cskaoyan.market.service.impl;

import com.cskaoyan.market.db.domain.MarketCollect;
import com.cskaoyan.market.db.domain.MarketCollectExample;
import com.cskaoyan.market.db.mapper.MarketCollectMapper;
import com.cskaoyan.market.service.MarketCollectService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketCollectServiceImpl implements MarketCollectService {
    private final MarketCollectMapper marketCollectMapper;

    @Autowired
    public MarketCollectServiceImpl(MarketCollectMapper marketCollectMapper) {
        this.marketCollectMapper = marketCollectMapper;
    }

    @Override
    public List<MarketCollect> list(Integer limit, Integer page, String userId, String sort, String order, String valueId) {
        MarketCollectExample marketCollectExample = new MarketCollectExample();
        marketCollectExample.setOrderByClause(sort + " " + order);
        MarketCollectExample.Criteria criteria = marketCollectExample.createCriteria();
        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if (!StringUtils.isEmpty(valueId)) {
            criteria.andValueIdEqualTo(Integer.valueOf(valueId));
        }
        PageHelper.startPage(page, limit);
        return marketCollectMapper.selectByExampleSelective(marketCollectExample,
                MarketCollect.Column.id, MarketCollect.Column.userId, MarketCollect.Column.valueId, MarketCollect.Column.type,
                MarketCollect.Column.addTime, MarketCollect.Column.updateTime, MarketCollect.Column.deleted);
    }
}
