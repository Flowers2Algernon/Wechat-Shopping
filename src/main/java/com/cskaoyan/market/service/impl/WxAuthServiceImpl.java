package com.cskaoyan.market.service.impl;

import com.cskaoyan.market.db.domain.MarketUser;
import com.cskaoyan.market.db.domain.MarketUserExample;
import com.cskaoyan.market.db.mapper.MarketUserMapper;
import com.cskaoyan.market.service.WxAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/18 9:52
 * @Version 1.0
 */
@Service
public class WxAuthServiceImpl implements WxAuthService {

    @Autowired
    MarketUserMapper userMapper;

    @Override
    public MarketUser login(String username, String password) {
        MarketUserExample marketUserExample = new MarketUserExample();
        marketUserExample.createCriteria().andUsernameEqualTo(username).andPasswordEqualTo(password);
        return userMapper.selectOneByExample(marketUserExample);
    }

    @Override
    public MarketUser getByUsername(String username) {
        //利用username查询当前的用户信息
        MarketUserExample marketUserExample = new MarketUserExample();
        marketUserExample.createCriteria().andUsernameEqualTo(username);
        return userMapper.selectOneByExample(marketUserExample);
    }
}
