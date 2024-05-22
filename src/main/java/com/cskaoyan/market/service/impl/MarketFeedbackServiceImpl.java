package com.cskaoyan.market.service.impl;

import com.cskaoyan.market.db.domain.MarketFeedback;
import com.cskaoyan.market.db.domain.MarketFeedbackExample;
import com.cskaoyan.market.db.mapper.MarketFeedbackMapper;
import com.cskaoyan.market.service.MarketFeedbackService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketFeedbackServiceImpl implements MarketFeedbackService {
    private final MarketFeedbackMapper marketFeedbackMapper;

    @Autowired
    public MarketFeedbackServiceImpl(MarketFeedbackMapper marketFeedbackMapper) {
        this.marketFeedbackMapper = marketFeedbackMapper;
    }

    @Override
    public List<MarketFeedback> list(Integer limit, Integer page, String username, String sort, String order, String id) {
        MarketFeedbackExample marketFeedbackExample = new MarketFeedbackExample();
        marketFeedbackExample.setOrderByClause(sort + " " + order);
        MarketFeedbackExample.Criteria criteria = marketFeedbackExample.createCriteria();
        if (!StringUtils.isEmpty(username)) {
            criteria.andUsernameLike("%" + username + "%");
        }
        if (!StringUtils.isEmpty(id)) {
            criteria.andIdEqualTo(Integer.valueOf(id));
        }
        PageHelper.startPage(page, limit);
        return marketFeedbackMapper.selectByExampleSelective(marketFeedbackExample,
                MarketFeedback.Column.id, MarketFeedback.Column.userId, MarketFeedback.Column.username,
                MarketFeedback.Column.mobile, MarketFeedback.Column.feedType, MarketFeedback.Column.content,
                MarketFeedback.Column.status, MarketFeedback.Column.hasPicture, MarketFeedback.Column.picUrls,
                MarketFeedback.Column.addTime, MarketFeedback.Column.updateTime, MarketFeedback.Column.deleted);
    }
}
