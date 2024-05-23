package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.vo.FloorGoodsListVo;
import com.cskaoyan.market.db.domain.*;
import com.cskaoyan.market.db.mapper.*;
import com.cskaoyan.market.service.wx.WxHomeService;
import com.cskaoyan.market.vo.WxHomeCouponVo;
import com.github.pagehelper.PageHelper;
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
    MarketGoodsMapper marketGoodsMapper;
    @Autowired
    MarketTopicMapper marketTopicMapper;
    @Autowired
    SecurityManager securityManager;


    @Override
    public Object mainHome() {
        Map<String, Object> data = new HashMap<String, Object>();

        MarketAdExample marketAdExample = new MarketAdExample();
        MarketAdExample.Criteria criteria = marketAdExample.createCriteria();
        criteria.andDeletedEqualTo(false);

        List<MarketAd> banner = marketAdMapper.selectByExample(marketAdExample);


        MarketBrandExample marketBrandExample = new MarketBrandExample();
        MarketBrandExample.Criteria criteria1 = marketBrandExample.createCriteria();
        criteria1.andDeletedEqualTo(false);

        List<MarketBrand> brandList = marketBrandMapper.selectByExample(marketBrandExample);

        MarketCategoryExample marketCategoryExample = new MarketCategoryExample();
        MarketCategoryExample.Criteria criteria2 = marketCategoryExample.createCriteria();
        criteria2.andDeletedEqualTo(false);
        criteria2.andLevelEqualTo("L1");
        MarketCategoryExample marketCategoryExample1 = new MarketCategoryExample();
        MarketCategoryExample.Criteria criteria4 = marketCategoryExample1.createCriteria();
        criteria4.andDeletedEqualTo(false);
        criteria4.andLevelEqualTo("L2");

        List<MarketCategory> channel = marketCategoryMapper.selectByExampleSelective(marketCategoryExample, MarketCategory.Column.id, MarketCategory.Column.iconUrl, MarketCategory.Column.name);
        List<MarketCategory> floorGoodsListL1 = marketCategoryMapper.selectByExampleSelective(marketCategoryExample, MarketCategory.Column.id, MarketCategory.Column.iconUrl, MarketCategory.Column.name);
        List<MarketCategory> floorGoodsListL2 = marketCategoryMapper.selectByExample(marketCategoryExample1);

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
            data.put("couponList",wxHomeCouponVos);
        }else {
            //输出当前用户未领取的前三条
            Integer userId = user.getId();
            MarketCouponUserExample marketCouponUserExample = new MarketCouponUserExample();
            MarketCouponUserExample.Criteria criteria6 = marketCouponUserExample.createCriteria();
            criteria6.andUserIdEqualTo(userId);
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
            data.put("couponList",wxHomeCouponVos);
        }

        MarketGoodsExample marketGoodsExample = new MarketGoodsExample();
        MarketGoodsExample.Criteria criteria5 = marketGoodsExample.createCriteria();
        criteria5.andDeletedEqualTo(false);
        criteria5.andIsHotEqualTo(true);

        MarketGoodsExample marketGoodsExample2 = new MarketGoodsExample();
        MarketGoodsExample.Criteria criteria6 = marketGoodsExample2.createCriteria();
        criteria6.andDeletedEqualTo(false);
        criteria6.andIsNewEqualTo(true);

        List<MarketGoods> hotGoodsList = marketGoodsMapper.selectByExample(marketGoodsExample);
        List<MarketGoods> newGoodsList = marketGoodsMapper.selectByExample(marketGoodsExample2);

        MarketTopicExample marketTopicExample = new MarketTopicExample();
        MarketTopicExample.Criteria criteria7 = marketTopicExample.createCriteria();
        criteria7.andDeletedEqualTo(false);

        MarketGoodsExample marketGoodsExample1 = new MarketGoodsExample();
        MarketGoodsExample.Criteria criteria8 = marketGoodsExample1.createCriteria();
        criteria8.andDeletedEqualTo(false);
        List<MarketGoods> goodsListAll = marketGoodsMapper.selectByExample(marketGoodsExample1);

        List<MarketTopic> topicList = marketTopicMapper.selectByExample(marketTopicExample);


        List<FloorGoodsListVo> floorGoodsListVo = new ArrayList<>();


        for (MarketCategory item1 : floorGoodsListL1) {
            int id = item1.getId();
            String name = item1.getName();

            List<MarketGoods> goodsList = new ArrayList<>();

            for (MarketCategory item2 : floorGoodsListL2) {
                int pid = item2.getPid();
                int cId = item2.getId();
                if (pid == id) {
                    for(MarketGoods item3 : goodsListAll){
                        int goodsCId = item3.getCategoryId();
                        if (goodsCId == cId){
                        goodsList.add(item3);}

                    }
                }
            }

            if (goodsList.size() > 10){
                goodsList = goodsList.subList(0, 9);
            }

            FloorGoodsListVo floorGoods = new FloorGoodsListVo();
            floorGoods.setId(id);
            floorGoods.setName(name);
            floorGoods.setGoodsList(goodsList);

            floorGoodsListVo.add(floorGoods);
        }



        if(banner.size()>4){
            banner = banner.subList(0,5);
        }
        data.put("banner", banner);
        if (brandList.size() > 10){
            brandList = brandList.subList(0, 9);
        }
        data.put("brandList", brandList);
        if (channel.size() > 10){
            channel = channel.subList(0, 9);
        }
        data.put("channel", channel);
//        data.put("couponList", couponList);
        if (floorGoodsListVo.size() > 6){
            floorGoodsListVo = floorGoodsListVo.subList(0, 6);
        }
        data.put("floorGoodsList", floorGoodsListVo);
        if (hotGoodsList.size() > 5){
            hotGoodsList = hotGoodsList.subList(0, 5);
        }
        data.put("hotGoodsList", hotGoodsList);
        if (newGoodsList.size() > 5){
            newGoodsList = newGoodsList.subList(0, 5);
        }
        data.put("newGoodsList", newGoodsList);
        data.put("topicList", topicList);


        PageHelper.startPage(1, 10);


        return data;
    }

}




