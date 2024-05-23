package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.db.domain.MarketOrderExample;
import com.cskaoyan.market.db.mapper.MarketOrderMapper;
import com.cskaoyan.market.service.wx.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: jyc
 * @Date: 2024/5/21 20:53
 */
@Service
public class WxUserServiceImpl implements WxUserService {
    @Autowired
    MarketOrderMapper mapper;

    // 订单状态对应的状态码
    // 101: '未付款', 102: '用户取消', 103: '系统取消', 201: '已付款', 202: '申请退款',
    // 203: '已退款', 301: '已发货', 401: '用户收货', 402: '系统收货'

    @Override
    public int getUncomment(Integer userId) {
        MarketOrderExample marketOrderExample = new MarketOrderExample();
        MarketOrderExample.Criteria criteria = marketOrderExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andDeletedEqualTo(false);
        criteria.andOrderStatusEqualTo((short) 401);
        long l = mapper.countByExample(marketOrderExample);
        return (int) l;
    }

    @Override
    public int getUnpaid(Integer userId) {
        MarketOrderExample marketOrderExample = new MarketOrderExample();
        MarketOrderExample.Criteria criteria = marketOrderExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andDeletedEqualTo(false);
        criteria.andOrderStatusEqualTo((short) 101);
        long l = mapper.countByExample(marketOrderExample);
        return (int) l;
    }

    @Override
    public int getUnrecv(Integer userId) {
        MarketOrderExample marketOrderExample = new MarketOrderExample();
        MarketOrderExample.Criteria criteria = marketOrderExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andDeletedEqualTo(false);
        criteria.andOrderStatusEqualTo((short) 301);
        long l = mapper.countByExample(marketOrderExample);
        return (int) l;
    }

    @Override
    public int getUnship(Integer userId) {
        MarketOrderExample marketOrderExample = new MarketOrderExample();
        MarketOrderExample.Criteria criteria = marketOrderExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andDeletedEqualTo(false);
        criteria.andOrderStatusEqualTo((short) 201);
        long l = mapper.countByExample(marketOrderExample);
        return (int) l;
    }
}
