package com.cskaoyan.market.service.impl;

import com.cskaoyan.market.db.domain.MarketUser;
import com.cskaoyan.market.db.domain.MarketUserExample;
import com.cskaoyan.market.db.mapper.MarketUserMapper;
import com.cskaoyan.market.service.MarketUserService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketUserServiceImpl implements MarketUserService {
    private final MarketUserMapper marketUserMapper;

    @Autowired
    public MarketUserServiceImpl(MarketUserMapper marketUserMapper) {
        this.marketUserMapper = marketUserMapper;
    }

    @Override
    public List<MarketUser> list(Integer limit, Integer page, String username, String mobile, String sort, String order) {
        MarketUserExample marketUserExample = new MarketUserExample();
        marketUserExample.setOrderByClause(sort + " " + order);
        MarketUserExample.Criteria criteria = marketUserExample.createCriteria();
        if (!StringUtils.isEmpty(username)) {
            criteria.andUsernameLike("%" + username + "%");
        }
        if (!StringUtils.isEmpty(mobile)) {
            criteria.andMobileLike("%" + mobile + "%");
        }
        PageHelper.startPage(page, limit);
        return marketUserMapper.selectByExampleSelective(marketUserExample, MarketUser.Column.addTime, MarketUser.Column.avatar, MarketUser.Column.deleted, MarketUser.Column.gender, MarketUser.Column.id, MarketUser.Column.lastLoginIp, MarketUser.Column.lastLoginTime,
                MarketUser.Column.mobile, MarketUser.Column.nickname, MarketUser.Column.password, MarketUser.Column.sessionKey, MarketUser.Column.status, MarketUser.Column.updateTime,
                MarketUser.Column.userLevel, MarketUser.Column.username, MarketUser.Column.weixinOpenid);
    }

    @Override
    public List<MarketUser> getDataFromId(CharSequence id) {
        MarketUserExample marketUserExample = new MarketUserExample();
        MarketUserExample.Criteria criteria = marketUserExample.createCriteria();
        if (!StringUtils.isEmpty(id)) {
            criteria.andIdEqualTo(Integer.parseInt((String) id));
        }
        return marketUserMapper.selectByExampleSelective(marketUserExample, MarketUser.Column.addTime, MarketUser.Column.avatar, MarketUser.Column.deleted, MarketUser.Column.gender, MarketUser.Column.id, MarketUser.Column.lastLoginIp, MarketUser.Column.lastLoginTime,
                MarketUser.Column.mobile, MarketUser.Column.nickname, MarketUser.Column.password, MarketUser.Column.sessionKey, MarketUser.Column.status, MarketUser.Column.updateTime,
                MarketUser.Column.userLevel, MarketUser.Column.username, MarketUser.Column.weixinOpenid);
    }
}