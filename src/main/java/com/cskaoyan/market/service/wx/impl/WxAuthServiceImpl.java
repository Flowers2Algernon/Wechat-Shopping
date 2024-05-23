package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.db.domain.MarketUser;
import com.cskaoyan.market.db.domain.MarketUserExample;
import com.cskaoyan.market.db.mapper.MarketUserMapper;
import com.cskaoyan.market.service.wx.WxAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    @Override
    public Integer registerAuth(String mobile, String code, String password, String username) {
        MarketUserExample marketUserExample = new MarketUserExample();
        marketUserExample.createCriteria().andUsernameEqualTo(username);
        if(userMapper.selectOneByExample(marketUserExample)!=null) {
            return 1;
        }
        MarketUserExample marketUserExample2 = new MarketUserExample();
        marketUserExample2.createCriteria().andMobileEqualTo(mobile);
        if(userMapper.selectOneByExample(marketUserExample2)!=null) {
            return 2;
        }
        MarketUser insertUser = new MarketUser();
        insertUser.setUsername(username);
        insertUser.setPassword(password);
        insertUser.setGender((byte) 1);
        insertUser.setMobile(mobile);
        insertUser.setAddTime(LocalDateTime.now());
        insertUser.setUpdateTime(LocalDateTime.now());
        insertUser.setLastLoginIp("0.0.0.1");
        insertUser.setNickname(username);
        insertUser.setAvatar("");



        userMapper.insertSelective(insertUser);

        return 0;
    }

    @Override
    public Integer resetAuth(String mobile, String code, String password) {
        MarketUserExample marketUserExample = new MarketUserExample();
        marketUserExample.createCriteria().andMobileEqualTo(mobile);
        if(userMapper.selectOneByExample(marketUserExample)==null) {
            return 1;
        }

        MarketUser updateUser = new MarketUser();
        updateUser.setPassword(password);

        userMapper.updateByExampleSelective(updateUser,marketUserExample);
        return 0;
    }
}
