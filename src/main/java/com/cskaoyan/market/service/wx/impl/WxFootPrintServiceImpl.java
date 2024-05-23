package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.db.domain.*;
import com.cskaoyan.market.db.mapper.MarketFootprintMapper;
import com.cskaoyan.market.db.mapper.MarketGoodsMapper;
import com.cskaoyan.market.service.wx.WxFootPrintService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WxFootPrintServiceImpl implements WxFootPrintService {
    @Autowired
    MarketFootprintMapper marketFootprintMapper;
    @Autowired
    MarketGoodsMapper marketGoodsMapper;
    @Override
    public List list(Integer limit, Integer page ) {
        MarketFootprintExample footprintExample = new MarketFootprintExample();
        MarketFootprintExample.Criteria criteria = footprintExample.createCriteria();
        criteria.andDeletedEqualTo(false);

        MarketGoodsExample goodsExample = new MarketGoodsExample();
        MarketGoodsExample.Criteria goodsCriteria = goodsExample.createCriteria();
        // 分页查询
        //PageHelper.startPage(limit,page);
        List<MarketFootprint> marketFootprints = marketFootprintMapper.selectByExample(footprintExample);
        List<Map<String, Object>> resultList = new ArrayList<>();
        for(MarketFootprint marketFootprint : marketFootprints){

            Map<String, Object> map = new HashMap<>();
            map.put("addTime", marketFootprint.getAddTime());
            map.put("id", marketFootprint.getId());
            map.put("goodsId", marketFootprint.getGoodsId());

            MarketGoods marketGoods = marketGoodsMapper.selectByPrimaryKey(marketFootprint.getGoodsId());
                if(marketGoods !=null) {
                    map.put("retailPrice", marketGoods.getRetailPrice());
                    map.put("name", marketGoods.getName());
                    map.put("brief", marketGoods.getBrief());
                    map.put("picUrl", marketGoods.getPicUrl());
                }
            resultList.add(map);
        }
        return resultList;
    }

    @Override
    public void delete(Integer id) {
        MarketFootprintExample footprintExample = new MarketFootprintExample();
        MarketFootprintExample.Criteria criteria = footprintExample.createCriteria();
        criteria.andIdEqualTo(id);

        marketFootprintMapper.deleteByExample(footprintExample);
    }
    /*
    @Override
    public int getTotalCount(String userId, String valueId) {
        MarketFootprintExample footprintExample = new MarketFootprintExample();
        MarketFootprintExample.Criteria criteria = footprintExample.createCriteria();

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.parseInt(userId));
        }

        return (int) marketFootprintMapper.countByExample(footprintExample);
    }*/
}