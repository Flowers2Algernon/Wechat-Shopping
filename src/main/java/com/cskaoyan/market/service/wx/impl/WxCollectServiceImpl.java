package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.db.domain.*;
import com.cskaoyan.market.db.mapper.MarketCollectMapper;
import com.cskaoyan.market.db.mapper.MarketGoodsMapper;
import com.cskaoyan.market.service.wx.WxCollectService;
import com.cskaoyan.market.vo.CollectVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WxCollectServiceImpl implements WxCollectService {

    @Autowired
    MarketCollectMapper marketCollectMapper;

    @Autowired
    MarketGoodsMapper marketGoodsMapper;

    @Autowired
    SecurityManager securityManager;

    @Override
    public List<CollectVo> list(Integer type, Integer page, Integer limit) {

        MarketCollectExample marketCollectExample = new MarketCollectExample();
        MarketCollectExample.Criteria criteria1 = marketCollectExample.createCriteria();
        criteria1.andDeletedEqualTo(false);
        List<MarketCollect> marketCollects = marketCollectMapper.selectByExample(marketCollectExample);
        List<CollectVo> marketGoodsCollects = new ArrayList<>();
        for (int i = 0; i < marketCollects.size(); i++) {
            MarketGoodsExample marketGoodsExample = new MarketGoodsExample();
            MarketGoodsExample.Criteria criteria = marketGoodsExample.createCriteria();
            criteria.andIdEqualTo(marketCollects.get(i).getValueId());
            MarketGoods marketGoods = marketGoodsMapper.selectOneByExample(marketGoodsExample);
            CollectVo collectVo = new CollectVo();
            collectVo.setBrief(marketGoods.getBrief());
            collectVo.setId(marketCollects.get(i).getId());
            collectVo.setName(marketGoods.getName());
            collectVo.setPicUrl(marketGoods.getPicUrl());
            collectVo.setRetailPrice(marketGoods.getRetailPrice());
            collectVo.setType(Byte.valueOf("0"));
            collectVo.setValueId(marketGoods.getGoodsSn());
            marketGoodsCollects.add(collectVo);
        }

        return marketGoodsCollects;
    }


    @Override
    public int addordelete(Integer valueId, Integer type) {
        MarketCollectExample collectExample = new MarketCollectExample();
        MarketCollectExample.Criteria criteria = collectExample.createCriteria();
        if (valueId != null) {
            criteria.andValueIdEqualTo(valueId);
        }
        MarketCollect marketCollect = marketCollectMapper.selectOneByExample(collectExample);
        if (marketCollect != null) {
            if (marketCollect.getDeleted()) {
                //说明被删除了
                marketCollect.setDeleted(false);
                marketCollect.setUpdateTime(LocalDateTime.now());
                return marketCollectMapper.updateByPrimaryKey(marketCollect);
            } else {
                //说明未删除
                marketCollect.setDeleted(true);
                marketCollect.setUpdateTime(LocalDateTime.now());
                return marketCollectMapper.updateByPrimaryKey(marketCollect);
            }
        } else {
            MarketGoodsExample marketGoodsExample = new MarketGoodsExample();
            MarketGoodsExample.Criteria criteria1 = marketGoodsExample.createCriteria();
            criteria1.andIdEqualTo(valueId);
            MarketGoods marketGoods = marketGoodsMapper.selectOneByExample(marketGoodsExample);
            if (marketGoods != null) {
                MarketCollect marketCollect1 = new MarketCollect();
                SecurityUtils.setSecurityManager(securityManager);
                Subject subject = SecurityUtils.getSubject();
                Session session = subject.getSession();
                MarketUser user = (MarketUser) session.getAttribute("user");
                marketCollect1.setUserId(user.getId());
                marketCollect1.setAddTime(LocalDateTime.now());
                marketCollect1.setValueId(marketGoods.getId());
                marketCollect1.setType(Byte.valueOf("0"));
                marketCollect1.setDeleted(false);
                marketCollect1.setUpdateTime(LocalDateTime.now());
                return marketCollectMapper.insertSelective(marketCollect1);

            }
            return -1;
        }
    }


/*    @Override
    public void addordelete(int type, Integer valueId) {
        *//*MarketCollectExample marketCollectExample = new MarketCollectExample();
        MarketCollectExample.Criteria criteria = marketCollectExample.createCriteria();
        //添加
        if (valueId == null){
            MarketCollect collect = new MarketCollect();
            collect.setValueId(valueId);
            collect.setType(type);
            collect.setDeleted(true);
            return marketCollectMapper.updateByPrimaryKey(collect);
        }else {
            //删除
            MarketCollect collect = new MarketCollect();
            collect.setValueId(valueId);
            collect.setType(type);
            collect.setDeleted(false);
            return marketCollectMapper.updateByPrimaryKey(collect);
        }*//*
        MarketCollectExample collectExample = new MarketCollectExample();
        MarketCollectExample.Criteria criteria = collectExample.createCriteria();
        if (valueId != null) {
            criteria.andValueIdEqualTo(valueId);
        }
        MarketCollect marketCollect = marketCollectMapper.selectOneByExample(collectExample);
        if (marketCollect != null && marketCollect.getDeleted() != true) {
            //说明未删除,要删除
            marketCollect.setDeleted(true);
            marketCollect.setUpdateTime(LocalDateTime.now());
        } else {
            MarketGoodsExample marketGoodsExample = new MarketGoodsExample();
            MarketGoodsExample.Criteria criteria1 = marketGoodsExample.createCriteria();
            criteria1.andIdEqualTo(valueId);
            MarketGoods marketGoods = marketGoodsMapper.selectOneByExample(marketGoodsExample);
            if (marketGoods != null) {
                MarketCollect marketCollect1 = new MarketCollect();
                SecurityUtils.setSecurityManager(securityManager);
                Subject subject = SecurityUtils.getSubject();
                Session session = subject.getSession();
                MarketUser user = (MarketUser) session.getAttribute("user");
                marketCollect1.setUserId(user.getId());
                marketCollect1.setAddTime(LocalDateTime.now());
                marketCollect1.setValueId(marketGoods.getId());
                marketCollect1.setType(Byte.valueOf("0"));
                marketCollect1.setDeleted(false);
                marketCollect1.setUpdateTime(LocalDateTime.now());
                marketCollectMapper.insert(marketCollect1);
            }

            return ;
        }
    }*/
}
