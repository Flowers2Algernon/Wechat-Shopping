package com.cskaoyan.market.service.impl;

import com.cskaoyan.market.db.domain.*;
import com.cskaoyan.market.db.mapper.*;
import com.cskaoyan.market.service.WxHomeService;
import com.cskaoyan.market.vo.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WxHomeServiceImpl implements WxHomeService {
    @Autowired
    MarketAdMapper marketAdMapper;
    @Autowired
    MarketBrandMapper marketBrandMapper;
    @Autowired
    MarketCategoryMapper marketCategoryMapper;
    @Autowired
    MarketCouponMapper marketCouponMapper;
    @Autowired
    SecurityManager securityManager;
    @Autowired
    MarketGoodsMapper marketGoodsMapper;
    @Autowired
    MarketTopicMapper marketTopicMapper;
    @Override
    public Map<String,Object> index() {
        HashMap<String, Object> map = new HashMap<>();
        //首先查询banner--从market-ad中查询
        MarketAdExample marketAdExample = new MarketAdExample();
        List<MarketAd> marketAds = marketAdMapper.selectByExample(marketAdExample);
        map.put("banner",marketAds);
        //接下来处理brandList--从market-brand中获取
        List<WxHomeBrandVo> wxHomeBrandVos = new ArrayList<>();
        MarketBrandExample marketBrandExample = new MarketBrandExample();
        List<MarketBrand> marketBrands = marketBrandMapper.selectByExample(marketBrandExample);
        int count = 0;
        for (MarketBrand marketBrand : marketBrands) {
            if (count>=9){
                break;
            }
            WxHomeBrandVo wxHomeBrandVo = new WxHomeBrandVo();
            wxHomeBrandVo.setDesc(marketBrand.getDesc());
            wxHomeBrandVo.setId(marketBrand.getId());
            wxHomeBrandVo.setName(marketBrand.getName());
            wxHomeBrandVo.setPicUrl(marketBrand.getPicUrl());
            wxHomeBrandVo.setFloorPrice(marketBrand.getFloorPrice());
            wxHomeBrandVos.add(wxHomeBrandVo);
            count++;
        }
        map.put("brandList",wxHomeBrandVos);
        //接下来处理channel数据--从market-category中获取数据
        ArrayList<WxHomeCategoryChannelVo> channelVos = new ArrayList<>();
        MarketCategoryExample marketCategoryExample = new MarketCategoryExample();
        List<MarketCategory> marketCategories = marketCategoryMapper.selectByExample(marketCategoryExample);
        int channelCount = 0;
        for (MarketCategory marketCategory : marketCategories) {
            if (channelCount>=9){
                break;
            }
            WxHomeCategoryChannelVo channelVo = new WxHomeCategoryChannelVo();
            channelVo.setId(marketCategory.getId());
            channelVo.setName(marketCategory.getName());
            channelVo.setIconUrl(marketCategory.getIconUrl());
            channelVos.add(channelVo);
            channelCount++;
        }
        map.put("channel",channelVos);
        //接下来处理couponList--数据从marketCoupon中获取
        ArrayList<WxHomeCouponVo> wxHomeCouponVos = new ArrayList<>();
        MarketCouponExample marketCouponExample = new MarketCouponExample();
        //此处需要实现一个逻辑，如果是未登录，就输出所有优惠券中的前三条
        //如果已登录，就查询用户没有领取的三条优惠券
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        MarketUser user = (MarketUser) session.getAttribute("user");
        if (user==null){
            //输出所有优惠券中的前三条
            List<MarketCoupon> list = marketCouponMapper.selectByExample(marketCouponExample);
            for (int i=0;i<3;i++){
                MarketCoupon marketCoupon = list.get(i);
                WxHomeCouponVo wxHomeCouponVo = new WxHomeCouponVo();
                wxHomeCouponVo.setDays(marketCoupon.getDays());
                wxHomeCouponVo.setMin(marketCoupon.getMin());
                wxHomeCouponVo.setDesc(marketCoupon.getDesc());
                wxHomeCouponVo.setId(marketCoupon.getId());
                wxHomeCouponVo.setDiscount(marketCoupon.getDiscount());
                wxHomeCouponVo.setTag(marketCoupon.getTag());
                wxHomeCouponVos.add(wxHomeCouponVo);
            }
            map.put("couponList",wxHomeCouponVos);
        }else {
            //输出当前用户未领取的前三条
            Integer userId = user.getId();
            MarketCouponUserExample marketCouponUserExample = new MarketCouponUserExample();
            MarketCouponUserExample.Criteria criteria = marketCouponUserExample.createCriteria();
            criteria.andUserIdEqualTo(userId);
            List<MarketCoupon> list = marketCouponMapper.selectByExample(marketCouponExample);
            for (int i=0;i<3;i++){
                MarketCoupon marketCoupon = list.get(i);
                WxHomeCouponVo wxHomeCouponVo = new WxHomeCouponVo();
                wxHomeCouponVo.setDays(marketCoupon.getDays());
                wxHomeCouponVo.setMin(marketCoupon.getMin());
                wxHomeCouponVo.setDesc(marketCoupon.getDesc());
                wxHomeCouponVo.setId(marketCoupon.getId());
                wxHomeCouponVo.setDiscount(marketCoupon.getDiscount());
                wxHomeCouponVo.setTag(marketCoupon.getTag());
                wxHomeCouponVos.add(wxHomeCouponVo);
            }
            map.put("couponList",wxHomeCouponVos);
        }
        //此处获取floorGoodsList数据--数据来源是category表和goods表
        //limit输出，6/7
        List<MarketCategoryFloorGoodsVo> list = new ArrayList<>();
        MarketCategoryExample marketCategoryExample1 = new MarketCategoryExample();
        List<MarketCategory> marketCategories1 = marketCategoryMapper.selectByExample(marketCategoryExample1);
        MarketGoodsExample marketGoodsExample = new MarketGoodsExample();
        List<MarketGoods> marketGoodsList = marketGoodsMapper.selectByExample(marketGoodsExample);
        for (int i = 0;i<6;i++){
            MarketCategory marketCategory = marketCategories1.get(i);
            Integer categoryId = marketCategory.getId();
            ArrayList<MarketFloorGoodsVo> marketFloorGoodsVos = new ArrayList<>();//种类中的商品
            for (MarketGoods marketGoods : marketGoodsList) {
                if (marketGoods.getCategoryId().equals(categoryId)){
                    //逻辑是什么？从goods中选择7个跟当前种类id相同的商品出来
                    MarketFloorGoodsVo marketFloorGoodsVo = new MarketFloorGoodsVo();
                    marketFloorGoodsVo.setId(marketGoods.getId());
                    marketFloorGoodsVo.setName(marketGoods.getName());
                    marketFloorGoodsVo.setPicUrl(marketGoods.getPicUrl());
                    marketFloorGoodsVo.setHot(marketGoods.getIsHot());
                    marketFloorGoodsVo.setNew(marketGoods.getIsNew());
                    marketFloorGoodsVo.setCounterPrice(marketGoods.getCounterPrice());
                    marketFloorGoodsVo.setRetailPrice(marketGoods.getRetailPrice());
                    marketFloorGoodsVo.setBrief(marketGoods.getBrief());
                    marketFloorGoodsVos.add(marketFloorGoodsVo);
                }
            }
            MarketCategoryFloorGoodsVo marketCategoryFloorGoodsVo = new MarketCategoryFloorGoodsVo();
            marketCategoryFloorGoodsVo.setId(categoryId);
            marketCategoryFloorGoodsVo.setName(marketCategory.getName());
            marketCategoryFloorGoodsVo.setGoodsList(marketFloorGoodsVos);
            list.add(marketCategoryFloorGoodsVo);
        }
        map.put("floorGoodsList",list);

        //接下来处理hotGoodsList--数据从goods表中获取--调7个出来即可
        List<MarketFloorGoodsVo> marketFloorGoodsVos = new ArrayList<>();
        List<MarketGoods> marketGoodsList1 = marketGoodsMapper.selectByExample(marketGoodsExample);
        int hotGoodsCount = 1;
            for (MarketGoods marketGoods : marketGoodsList1) {
                if (marketGoods.getIsHot()){
                    MarketFloorGoodsVo marketFloorGoodsVo = new MarketFloorGoodsVo();
                    marketFloorGoodsVo.setId(marketGoods.getId());
                    marketFloorGoodsVo.setName(marketGoods.getName());
                    marketFloorGoodsVo.setPicUrl(marketGoods.getPicUrl());
                    marketFloorGoodsVo.setHot(marketGoods.getIsHot());
                    marketFloorGoodsVo.setNew(marketGoods.getIsNew());
                    marketFloorGoodsVo.setCounterPrice(marketGoods.getCounterPrice());
                    marketFloorGoodsVo.setRetailPrice(marketGoods.getRetailPrice());
                    marketFloorGoodsVo.setBrief(marketGoods.getBrief());
                    marketFloorGoodsVos.add(marketFloorGoodsVo);
                }
                hotGoodsCount++;
                if (hotGoodsCount>8){
                    break;
                }
            }
            map.put("hotGoodsList",marketFloorGoodsVos);

            //接下来处理newsGoodsList
        List<MarketFloorGoodsVo> marketNewFloorGoodsVos = new ArrayList<>();
        List<MarketGoods> marketGoodsList2 = marketGoodsMapper.selectByExample(marketGoodsExample);
        int newGoodsCount = 1;
        for (MarketGoods marketGoods : marketGoodsList2) {
            if (marketGoods.getIsHot()){
                MarketFloorGoodsVo marketFloorGoodsVo = new MarketFloorGoodsVo();
                marketFloorGoodsVo.setId(marketGoods.getId());
                marketFloorGoodsVo.setName(marketGoods.getName());
                marketFloorGoodsVo.setPicUrl(marketGoods.getPicUrl());
                marketFloorGoodsVo.setHot(marketGoods.getIsHot());
                marketFloorGoodsVo.setNew(marketGoods.getIsNew());
                marketFloorGoodsVo.setCounterPrice(marketGoods.getCounterPrice());
                marketFloorGoodsVo.setRetailPrice(marketGoods.getRetailPrice());
                marketFloorGoodsVo.setBrief(marketGoods.getBrief());
                marketNewFloorGoodsVos.add(marketFloorGoodsVo);
            }
            newGoodsCount++;
            if (newGoodsCount>8){
                break;
            }
        }
        map.put("newGoodsList",marketNewFloorGoodsVos);

        //接下来处理topicList
        ArrayList<MarketTopicVo> marketTopicVos = new ArrayList<>();
        MarketTopicExample marketTopicExample = new MarketTopicExample();
        List<MarketTopic> marketTopics = marketTopicMapper.selectByExample(marketTopicExample);
        int topicCount = 0;
        for (MarketTopic marketTopic : marketTopics) {
            MarketTopicVo marketTopicVo = new MarketTopicVo();
            marketTopicVo.setId(marketTopic.getId());
            marketTopicVo.setPicUrl(marketTopic.getPicUrl());
            marketTopicVo.setTitle(marketTopic.getTitle());
            marketTopicVo.setReadCount(marketTopic.getReadCount());
            marketTopicVo.setPrice(marketTopic.getPrice());
            marketTopicVos.add(marketTopicVo);
            topicCount++;
            if (topicCount>5){
                break;
            }
        }
        map.put("topicList",marketTopicVos);
        return map;
    }
}
