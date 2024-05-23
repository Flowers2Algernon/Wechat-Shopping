package com.cskaoyan.market.service.admin.impl;

import com.cskaoyan.market.db.domain.*;
import com.cskaoyan.market.db.mapper.MarketCommentMapper;
import com.cskaoyan.market.db.mapper.MarketOrderGoodsMapper;
import com.cskaoyan.market.db.mapper.MarketOrderMapper;
import com.cskaoyan.market.db.mapper.MarketUserMapper;
import com.cskaoyan.market.service.admin.AdminOrderService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author: jyc
 * @Date: 2024/5/8 20:03
 */
@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    @Autowired
    MarketOrderMapper marketOrderMapper;
    @Autowired
    MarketCommentMapper marketCommentMapper;
    @Autowired
    MarketUserMapper marketUserMapper;
    @Autowired
    MarketOrderGoodsMapper marketOrderGoodsMapper;

    @Override
    // 评论回复
    public boolean updateReply(String commentId, String content) {
        MarketComment marketComment = marketCommentMapper.selectByPrimaryKey(Integer.parseInt(commentId));
        if(marketComment.getAdminContent().length()>0){
            return false;
        }else {
            marketComment.setAdminContent(content);
            marketCommentMapper.updateByPrimaryKeySelective(marketComment);
            return true;
        }
    }

    @Override
    public List<MarketOrder> list(Integer page, Integer limit, String sort, String order, String orderId, String[] orderStatusArray, String start, String end, String userId, String orderSn) {

        MarketOrderExample marketOrderExample = new MarketOrderExample();
        MarketOrderExample.Criteria criteria = marketOrderExample.createCriteria();
        marketOrderExample.setOrderByClause(sort+" "+order);
        criteria.andDeletedEqualTo(false);
        if(!StringUtils.isEmpty(orderId)){
            criteria.andIdEqualTo(Integer.parseInt(orderId));
        }
        if(orderStatusArray!=null&&!StringUtils.isEmpty(Arrays.toString(orderStatusArray))){
            List<Short> orderStatusList = new ArrayList<>();
            for (String s : orderStatusArray) {
                orderStatusList.add(Short.parseShort(s));
            }
            criteria.andOrderStatusIn(orderStatusList);
        }
        if(!StringUtils.isEmpty(userId)){
            criteria.andUserIdEqualTo(Integer.parseInt(userId));
        }
        if(!StringUtils.isEmpty(orderSn)){
            criteria.andOrderSnLike("%"+orderSn+"%");
        }
        if(!StringUtils.isEmpty(start)&&!StringUtils.isEmpty(end)){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startTime = LocalDateTime.parse(start, formatter);
            LocalDateTime endTime = LocalDateTime.parse(end, formatter);
            // LocalDateTime startTime = LocalDateTime.parse(start);
            // LocalDateTime endTime = LocalDateTime.parse(end);
            criteria.andAddTimeBetween(startTime,endTime);
        }else if(!StringUtils.isEmpty(start)){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startTime = LocalDateTime.parse(start, formatter);
            // LocalDateTime startTime = LocalDateTime.parse(start);
            criteria.andAddTimeGreaterThan(startTime);
        }else if(!StringUtils.isEmpty(end)){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime endTime = LocalDateTime.parse(end, formatter);
            // LocalDateTime endTime = LocalDateTime.parse(end);
            criteria.andAddTimeLessThan(endTime);
        }
        //分页查询
        PageHelper.startPage(page, limit);
        List<MarketOrder> ordersList = marketOrderMapper.selectByExample(marketOrderExample);
        return ordersList;
    }

    @Override
    public void refund(String orderId, String refundMoney) {
        MarketOrder marketOrder = new MarketOrder();
        marketOrder.setId(Integer.parseInt(orderId));
        marketOrder.setUpdateTime(LocalDateTime.now());
        marketOrder.setOrderStatus((short) 203);
        marketOrder.setRefundAmount(new BigDecimal(refundMoney));
        marketOrder.setRefundTime(LocalDateTime.now());
        marketOrderMapper.updateByPrimaryKeySelective(marketOrder);
    }

    @Override
    public Map<String, Object> detail(String id) {
        Map<String,Object> data = new HashMap<>();

        MarketOrder marketOrder = marketOrderMapper.selectByPrimaryKey(Integer.parseInt(id));
        MarketOrderGoodsExample marketOrderGoodsExample = new MarketOrderGoodsExample();
        MarketOrderGoodsExample.Criteria criteria = marketOrderGoodsExample.createCriteria();
        criteria.andOrderIdEqualTo(Integer.parseInt(id));
        List<MarketOrderGoods> marketOrderGoods = marketOrderGoodsMapper.selectByExample(marketOrderGoodsExample);
        int userId = marketOrder.getUserId();
        MarketUser user = marketUserMapper.selectByPrimaryKey(userId);
        MarketUser marketUser = new MarketUser();
        marketUser.setNickname(user.getNickname());
        marketUser.setAvatar(user.getAvatar());

        data.put("order",marketOrder);
        data.put("orderGoods",marketOrderGoods);
        data.put("user",marketUser);
        return data;
    }

    @Override
    public void delete(String orderId) {
        marketOrderMapper.deleteByPrimaryKey(Integer.parseInt(orderId));
    }

    @Override
    public void ship(String orderId, String shipChannel, String shipSn) {
        MarketOrder marketOrder = new MarketOrder();
        marketOrder.setId(Integer.parseInt(orderId));
        marketOrder.setUpdateTime(LocalDateTime.now());
        marketOrder.setOrderStatus((short) 301);
        marketOrder.setShipChannel(shipChannel);
        marketOrder.setShipSn(shipSn);
        marketOrder.setShipTime(LocalDateTime.now());
        marketOrderMapper.updateByPrimaryKeySelective(marketOrder);
    }
}
