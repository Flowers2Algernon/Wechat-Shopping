package com.cskaoyan.market.service.impl;

import com.cskaoyan.market.db.domain.*;
import com.cskaoyan.market.db.mapper.MarketCommentMapper;
import com.cskaoyan.market.db.mapper.MarketOrderGoodsMapper;
import com.cskaoyan.market.db.mapper.MarketOrderMapper;
import com.cskaoyan.market.db.mapper.MarketUserMapper;
import com.cskaoyan.market.service.AdminStatService;
import com.cskaoyan.market.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: jyc
 * @Date: 2024/5/8 9:13
 */
@Service
public class AdminStatServiceImpl implements AdminStatService {
    @Autowired
    MarketOrderMapper marketOrderMapper;
    @Autowired
    MarketUserMapper marketUserMapper;
    @Autowired
    MarketOrderGoodsMapper marketOrderGoodsMapper;

    @Override
    public List<Map<String, String>> getUserRows() {
        List<MarketUser> marketUsers = marketUserMapper.selectByExample(new MarketUserExample());
        List<Map<String,String>> userRows = new ArrayList<>();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (MarketUser marketUser : marketUsers) {
            int isExist = 0;
            LocalDateTime addTime = marketUser.getAddTime();
            String addDate = addTime.format(dateFormat);
            for (Map<String, String> userMap : userRows) {
                if(addDate.equals(userMap.get("day"))){
                    int newAmount = Integer.parseInt(userMap.get("users"))+1;
                    userMap.put("users",Integer.toString(newAmount));
                    isExist=1;
                }
            }
            if(isExist==0){
                Map<String,String> userMap = new HashMap<>();
                userMap.put("day",addDate);
                userMap.put("users","1");
                userRows.add(userMap);
            }
        }
        return userRows;
    }

    @Override
    public List<Map<String, String>> getOrderRows() {

        MarketOrderExample marketOrderExample = new MarketOrderExample();
        MarketOrderExample.Criteria criteria = marketOrderExample.createCriteria();
        criteria.andDeletedEqualTo(MarketOrder.NOT_DELETED);

        List<MarketOrder> marketOrders = marketOrderMapper.selectByExample(marketOrderExample);
        List<Map<String,String>> orderRows = new ArrayList<>();
        // 要显示每个日期的顾客数量，实现方法是用map存储每个日期对应的顾客编号
        Map<String,List<Integer>> dateCustomerMap = new HashMap<>();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (MarketOrder marketOrder : marketOrders) {
            int isExist = 0;
            LocalDateTime addTime = marketOrder.getAddTime();
            String addDate = addTime.format(dateFormat);
            for (Map<String, String> orderMap : orderRows) {
                if(addDate.equals(orderMap.get("day"))){
                    //找出该日期对应的顾客列表
                    List<Integer> list = dateCustomerMap.get(addDate);
                    if(!list.contains(marketOrder.getUserId())){
                        int newCustomers = Integer.parseInt(orderMap.get("customers"))+1;
                        orderMap.put("customers",Integer.toString(newCustomers));
                        list.add(marketOrder.getUserId());
                        dateCustomerMap.put(addDate,list);
                    }
                    BigDecimal oldAmount = new BigDecimal(orderMap.get("amount"));
                    BigDecimal newAmount = oldAmount.add(marketOrder.getActualPrice());
                    orderMap.put("amount",newAmount.toString());

                    int oldOrder = Integer.parseInt(orderMap.get("orders"));
                    int newOrder = oldOrder+1;
                    orderMap.put("orders",Integer.toString(newOrder));

                    int oldCustomers = Integer.parseInt(orderMap.get("customers"));

                    BigDecimal newPcr = newAmount.divide(new BigDecimal(oldCustomers),2, RoundingMode.HALF_UP);
                    orderMap.put("pcr",newPcr.toString());

                    isExist=1;
                }
            }
            if(isExist==0){
                List<Integer> customerList = new ArrayList<>();
                customerList.add(marketOrder.getUserId());
                dateCustomerMap.put(addDate,customerList);

                Map<String,String> orderMap = new HashMap<>();
                orderMap.put("amount",marketOrder.getActualPrice().toString());
                orderMap.put("orders","1");
                orderMap.put("customers","1");
                orderMap.put("day",addDate);
                orderMap.put("pcr",marketOrder.getActualPrice().toString());
                orderRows.add(orderMap);
            }
        }

        return orderRows;
    }

    @Override
    public List<Map<String, String>> getGoodsRows() {

        MarketOrderGoodsExample marketOrderGoodsExample = new MarketOrderGoodsExample();
        MarketOrderGoodsExample.Criteria criteria = marketOrderGoodsExample.createCriteria();
        criteria.andDeletedEqualTo(MarketOrderGoods.NOT_DELETED);

        List<MarketOrderGoods> marketOrderGoods = marketOrderGoodsMapper.selectByExample(marketOrderGoodsExample);
        List<Map<String,String>> goodsRows = new ArrayList<>();
        // 要显示每个日期的订单数量，实现方法是用map存储每个日期对应的订单编号
        Map<String,List<Integer>> dateOrderMap = new HashMap<>();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //遍历每个查找出来的goods
        for (MarketOrderGoods marketOrderGood : marketOrderGoods) {
            //对应日期是否已经存储
            int isExist = 0;
            LocalDateTime addTime = marketOrderGood.getAddTime();
            String addDate = addTime.format(dateFormat);

            for (Map<String, String> goodsMap : goodsRows) {
                if(addDate.equals(goodsMap.get("day"))){
                    //找出该日期对应的订单列表
                    List<Integer> list = dateOrderMap.get(addDate);
                    if(!list.contains(marketOrderGood.getOrderId())){
                        int newOrders = Integer.parseInt(goodsMap.get("orders"))+1;
                        goodsMap.put("orders",Integer.toString(newOrders));
                        list.add(marketOrderGood.getOrderId());
                        dateOrderMap.put(addDate,list);
                    }
                    BigDecimal oldAmount = new BigDecimal(goodsMap.get("amount"));
                    BigDecimal newAmount = oldAmount.add(marketOrderGood.getPrice());
                    goodsMap.put("amount",newAmount.toString());

                    int oldProducts = Integer.parseInt(goodsMap.get("products"));
                    int newProducts = oldProducts+1;
                    goodsMap.put("products",Integer.toString(newProducts));

                    isExist=1;
                }
            }
            if(isExist==0){
                List<Integer> orderList = new ArrayList<>();
                orderList.add(marketOrderGood.getOrderId());
                dateOrderMap.put(addDate,orderList);

                Map<String,String> goodsMap = new HashMap<>();
                goodsMap.put("amount",marketOrderGood.getPrice().toString());
                goodsMap.put("orders","1");
                goodsMap.put("day",addDate);
                goodsMap.put("products","1");
                goodsRows.add(goodsMap);
            }
        }

        return goodsRows;
    }
}
