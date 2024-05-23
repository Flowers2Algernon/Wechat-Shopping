package com.cskaoyan.market.service.admin.impl;

import com.cskaoyan.market.db.domain.MarketSearchHistory;
import com.cskaoyan.market.db.domain.MarketSearchHistoryExample;
import com.cskaoyan.market.db.mapper.MarketSearchHistoryMapper;
import com.cskaoyan.market.service.admin.MarketSearchHistoryService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketSearchHistoryServiceImpl implements MarketSearchHistoryService {
    private final MarketSearchHistoryMapper marketSearchHistoryMapper;

    @Autowired
    public MarketSearchHistoryServiceImpl(MarketSearchHistoryMapper marketSearchHistoryMapper) {
        this.marketSearchHistoryMapper = marketSearchHistoryMapper;
    }

    @Override
    public List<MarketSearchHistory> list(Integer limit, Integer page, String userId, String sort, String order, String keyword) {
        MarketSearchHistoryExample marketSearchHistoryExample = new MarketSearchHistoryExample();
        marketSearchHistoryExample.setOrderByClause(sort + " " + order);
        MarketSearchHistoryExample.Criteria criteria = marketSearchHistoryExample.createCriteria();
        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if (!StringUtils.isEmpty(keyword)) {
            criteria.andKeywordLike("%" + keyword + "%");
        }
        PageHelper.startPage(page, limit);
        return marketSearchHistoryMapper.selectByExampleSelective(marketSearchHistoryExample,
                MarketSearchHistory.Column.id, MarketSearchHistory.Column.userId, MarketSearchHistory.Column.keyword,
                MarketSearchHistory.Column.addTime, MarketSearchHistory.Column.updateTime, MarketSearchHistory.Column.deleted);
    }
}
