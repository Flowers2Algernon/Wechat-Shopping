package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.db.domain.*;
import com.cskaoyan.market.db.mapper.*;
import com.cskaoyan.market.service.wx.WxCartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Author: jyc
 * @Date: 2024/5/21 23:29
 */
@Service
public class WxCartServiceImpl implements WxCartService {
    @Autowired
    MarketCartMapper marketCartMapper;
    @Autowired
    MarketGoodsMapper marketGoodsMapper;
    @Autowired
    MarketGoodsSpecificationMapper marketGoodsSpecificationMapper;
    @Autowired
    MarketGoodsProductMapper marketGoodsProductMapper;
    @Autowired
    MarketAddressMapper marketAddressMapper;
    @Autowired
    MarketCouponUserMapper marketCouponUserMapper;
    @Autowired
    MarketCouponMapper marketCouponMapper;
    @Autowired
    MarketSystemMapper marketSystemMapper;

    @Override
    public Integer goodsCount(Integer userId) {
        int count=0;
        MarketCartExample marketCartExample = new MarketCartExample();
        MarketCartExample.Criteria criteria = marketCartExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andDeletedEqualTo(false);

        List<MarketCart> marketCarts = marketCartMapper.selectByExampleSelective(marketCartExample, MarketCart.Column.number);
        for (MarketCart marketCart : marketCarts) {
            count+=marketCart.getNumber();

        }
        return count;
    }

    @Override
    public Integer add(Integer goodsId, Short number, Integer productId, Integer userId) {
        MarketCartExample marketCartExample = new MarketCartExample();
        MarketCartExample.Criteria criteria = marketCartExample.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        criteria.andUserIdEqualTo(userId);
        criteria.andDeletedEqualTo(false);

        MarketGoodsExample marketGoodsExample = new MarketGoodsExample();
        MarketGoodsExample.Criteria criteria1 = marketGoodsExample.createCriteria();
        criteria1.andDeletedEqualTo(false);
        List<MarketGoods> goodsList = marketGoodsMapper.selectByExample(marketGoodsExample);
        Short goodsNum = 0;
        if(marketCartMapper.selectOneByExample(marketCartExample) != null){
            goodsNum = (short) (marketCartMapper.selectOneByExample(marketCartExample).getNumber()+number);}


        if(marketCartMapper.selectOneByExample(marketCartExample) != null){

            MarketCart updateValues = new MarketCart();
            updateValues.setNumber((Short) goodsNum);
            marketCartMapper.updateByExampleSelective(updateValues, marketCartExample);

        }else {
            MarketGoods marketGoods = new MarketGoods();
            for (MarketGoods goods : goodsList) {
                if (Objects.equals(goods.getId(), goodsId)) {
                    marketGoods = goods;
                    break;
                }
            }

            MarketCart insertCart = new MarketCart();
            insertCart.setUserId(userId);
            insertCart.setGoodsId(goodsId);
            insertCart.setGoodsSn(String.valueOf(goodsId));
            insertCart.setGoodsName(marketGoods.getName());
            insertCart.setProductId(productId);
            insertCart.setPrice(marketGoods.getRetailPrice());
            insertCart.setNumber(number);
            String[] unit = new String[]{marketGoods.getUnit()};
            insertCart.setSpecifications(unit);
            insertCart.setPicUrl(marketGoods.getPicUrl());
            insertCart.setAddTime(LocalDateTime.now());
            insertCart.setChecked(true);
            insertCart.setDeleted(false);


            marketCartMapper.insert(insertCart);

        }


        return goodsCount(userId);
    }

    @Override
    public boolean update(Integer goodsId, Integer id, Integer number, Integer productId, Integer userId) {
        if(!judgeNumber(productId,number)){
            return false;
        }else {
            MarketCart marketCart = new MarketCart();
            marketCart.setId(id);
            marketCart.setNumber(number.shortValue());
            marketCart.setProductId(productId);
            marketCart.setGoodsId(goodsId);
            marketCart.setUserId(userId);
            marketCartMapper.updateByPrimaryKeySelective(marketCart);
            return true;
        }
    }

    @Override
    public void delete(List<Integer> productIds, Integer userId) {
        MarketCartExample marketCartExample= new MarketCartExample();
        MarketCartExample.Criteria criteria = marketCartExample.createCriteria();
        criteria.andProductIdIn(productIds);
        criteria.andUserIdEqualTo(userId);

        MarketCart marketCart = new MarketCart();
        marketCart.setDeleted(true);
        marketCartMapper.updateByExampleSelective(marketCart,marketCartExample);
    }

    @Override
    public Map<String,Object> index(Integer userId) {
        HashMap<String, Object> map = new HashMap<>();
        //先处理cartList--注意此处仅显示没有删除的
        MarketCartExample marketCartExample = new MarketCartExample();
        marketCartExample.createCriteria().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        List<MarketCart> marketCarts = marketCartMapper.selectByExample(marketCartExample);
        map.put("cartList", marketCarts);

        //接下来处理cartTotal
        BigDecimal checkedGoodsAmount = BigDecimal.ZERO;
        Integer checkedGoodsCount = 0;
        BigDecimal goodsAmount = BigDecimal.ZERO;
        Integer goodsCount = 0;
        for (MarketCart marketCart : marketCarts) {
            if (marketCart.getChecked()) {
                BigDecimal price = marketCart.getPrice();
                BigDecimal number = new BigDecimal(marketCart.getNumber());
                checkedGoodsAmount = checkedGoodsAmount.add(price.multiply(number));
                checkedGoodsCount += marketCart.getNumber();
            }
            BigDecimal allPrice = marketCart.getPrice();
            BigDecimal number = new BigDecimal(marketCart.getNumber());
            goodsAmount = goodsAmount.add(allPrice.multiply(number));
            goodsCount += marketCart.getNumber();
        }
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("checkedGoodsAmount", checkedGoodsAmount);
        map1.put("checkedGoodsCount", checkedGoodsCount);
        map1.put("goodsAmount", goodsAmount);
        map1.put("goodsCount", goodsCount);
        map.put("cartTotal", map1);
        return map;
    }

    @Override
    public void checked(Integer isChecked, List<Integer> productIds, Integer userId) {
        MarketCartExample marketCartExample= new MarketCartExample();
        MarketCartExample.Criteria criteria = marketCartExample.createCriteria();
        criteria.andProductIdIn(productIds);
        criteria.andUserIdEqualTo(userId);
        // List<MarketCart> cartList = marketCartMapper.selectByExample(marketCartExample);

        MarketCart marketCart = new MarketCart();
        marketCart.setChecked(isChecked == 1);
        marketCartMapper.updateByExampleSelective(marketCart,marketCartExample);
    }

    @Override
    public Integer addFast(Integer goodsId, short number, Integer productId, Integer userId) {
        MarketCartExample marketCartExample = new MarketCartExample();
        MarketCartExample.Criteria criteria = marketCartExample.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        criteria.andProductIdEqualTo(productId);
        criteria.andDeletedEqualTo(false);
        MarketCart marketCarts = marketCartMapper.selectOneByExample(marketCartExample);
        if(marketCarts==null){
            // 商品不存在，添加购物车
            if(!judgeNumber(productId, (int) number)){
                return 0;
            }
            MarketCart marketCart = new MarketCart();
            marketCart.setUpdateTime(LocalDateTime.now());
            marketCart.setAddTime(LocalDateTime.now());
            marketCart.setGoodsId(goodsId);
            marketCart.setNumber(number);
            marketCart.setProductId(productId);
            marketCart.setUserId(userId);
            MarketGoods marketGoods = marketGoodsMapper.selectByPrimaryKey(goodsId);
            marketCart.setPicUrl(marketGoods.getPicUrl());
            marketCart.setGoodsSn(marketGoods.getGoodsSn());
            marketCart.setGoodsName(marketGoods.getName());
            marketCart.setPrice(marketGoods.getRetailPrice());
            marketCart.setChecked(false);
            // 通过productId找出规格
            MarketGoodsProduct marketGoodsProduct = marketGoodsProductMapper.selectByPrimaryKey(productId);
            marketCart.setSpecifications(marketGoodsProduct.getSpecifications());
            marketCartMapper.insertSelective(marketCart);
            return marketCart.getId();
        }else {
            // 购物车中有此商品
            if(!judgeNumber(productId,marketCarts.getNumber()+number)){
                return 0;
            }
//            MarketCart marketCart = new MarketCart();
            marketCarts.setNumber(number);
            marketCarts.setUpdateTime(LocalDateTime.now());
            marketCartMapper.updateByExampleSelective(marketCarts,marketCartExample);
            return marketCarts.getId();
        }
    }

    @Override
    public Map<String, Object> cartCheckout(Integer addressId, Integer couponId, Integer userCouponId, Integer grouponRulesId, Integer userId) {
        //直接通过购物车下单
        HashMap<String, Object> resultMap = new HashMap<>();
        //首先获得选中的商品信息
        MarketCartExample marketCartExample = new MarketCartExample();
        marketCartExample.createCriteria().andUserIdEqualTo(userId).andCheckedEqualTo(true).andDeletedEqualTo(false);
        List<MarketCart> marketCarts = marketCartMapper.selectByExample(marketCartExample);
        //获取checkedGoodsList
        resultMap.put("checkedGoodsList",marketCarts);

        //获取基本价格--商品数量*商品单价
        BigDecimal netPrice = BigDecimal.ZERO;
        for (MarketCart marketCart : marketCarts) {
            BigDecimal number = BigDecimal.valueOf(marketCart.getNumber());
            netPrice = netPrice.add(marketCart.getPrice().multiply(number));
        }
        resultMap.put("goodsTotalPrice", netPrice);//商品实际价格goodsTotalPrice

        MarketAddressExample marketAddressExample = new MarketAddressExample();
        marketAddressExample.createCriteria().andUserIdEqualTo(userId);
        List<MarketAddress> marketAddresses = marketAddressMapper.selectByExample(marketAddressExample);
        if (addressId == 0) {
            //采用默认地址
            for (MarketAddress marketAddress : marketAddresses) {
                if (marketAddress.getIsDefault()) {
                    resultMap.put("addressId", marketAddress.getId());//设置addressId
                    resultMap.put(("checkedAddress"), marketAddress);//设置checkedAddress项
                }
            }
        } else {
            //采用当前地址
            for (MarketAddress marketAddress : marketAddresses) {
                if (marketAddress.getId().equals(addressId)) {
                    resultMap.put("addressId", marketAddress.getId());//设置addressId
                    resultMap.put(("checkedAddress"), marketAddress);//设置checkedAddress项
                }
            }
        }
        //判断优惠券使用,没有可用的优惠券则返回-1
        MarketCouponUserExample marketCouponUserExample = new MarketCouponUserExample();
        marketCouponUserExample.createCriteria().andUserIdEqualTo(userId).andDeletedEqualTo(false).andStatusEqualTo((short) 0);
        List<MarketCouponUser> marketCouponUserList = marketCouponUserMapper.selectByExample(marketCouponUserExample);
        BigDecimal dontIncludeFreightFeeNetPrice = BigDecimal.ZERO;
        if (couponId == -1||userCouponId==-1) {
            //查询有哪些可用的优惠券
//            if (marketCouponUserList == null) {
                resultMap.put("couponId", -1);
                resultMap.put("couponPrice", 0);
                resultMap.put("userCouponId",-1);
                dontIncludeFreightFeeNetPrice = netPrice;
//            } else {
                //若存在可用优惠券，在满足min-最低付款金额的基础上，默认选择能减除最多的优惠券
//                BigDecimal maxCouponMoney = BigDecimal.ZERO;
//                Integer actualCouponId = -1;
//                Integer marketCouponUserId = -1;
//                for (MarketCouponUser marketCouponUser : marketCouponUserList) {
//                    //根据couponId得到实际的coupon。
//                    //在实际coupon中进行判断是否满足这些优惠券使用的最小金额
//                    MarketCouponExample marketCouponExample = new MarketCouponExample();
//                    marketCouponExample.createCriteria().andIdEqualTo(marketCouponUser.getCouponId());
//                    MarketCoupon marketCoupon = marketCouponMapper.selectOneByExample(marketCouponExample);
//                    if (marketCoupon.getMin().compareTo(netPrice) <= 0) {
//                        //此时可使用该优惠券
//                        if (maxCouponMoney.compareTo(marketCoupon.getDiscount()) <= 0) {
//                            maxCouponMoney = marketCoupon.getDiscount();
//                            actualCouponId = marketCoupon.getId();
//                        }
//                        marketCouponUserId = marketCouponUser.getUserId();
//                    }
//                }
//                resultMap.put("couponId", actualCouponId);
//                resultMap.put("couponPrice", maxCouponMoney);
//                resultMap.put("userCouponId",userCouponId);
//                dontIncludeFreightFeeNetPrice = netPrice.subtract(maxCouponMoney);
//            }
        } else {
            //使用指定的优惠券--利用couponId来指定
            //selectList中已经做了筛选
            MarketCouponUserExample marketCouponUserExample1 = new MarketCouponUserExample();
            marketCouponUserExample1.createCriteria().andIdEqualTo(couponId);
            MarketCouponUser marketCouponUser = marketCouponUserMapper.selectOneByExample(marketCouponUserExample1);
            MarketCouponExample marketCouponExample = new MarketCouponExample();
            MarketCoupon marketCoupon = null;
            if (marketCouponUser!=null){
                marketCouponExample.createCriteria().andIdEqualTo(marketCouponUser.getCouponId());
                marketCoupon = marketCouponMapper.selectOneByExample(marketCouponExample);
            }
            if (marketCoupon==null){
                resultMap.put("couponId", -1);
                resultMap.put("couponPrice", 0);
                resultMap.put("userCouponId",-1);
                dontIncludeFreightFeeNetPrice = netPrice;
            }else {
                if (netPrice.compareTo(marketCoupon.getMin()) >= 0) {
                    //可用使用该优惠券
                    resultMap.put("couponId", couponId);
                    resultMap.put("couponPrice", marketCoupon.getDiscount());
                    resultMap.put("userCouponId",userCouponId);
                    dontIncludeFreightFeeNetPrice = netPrice.subtract(marketCoupon.getDiscount());
                } else {
                    resultMap.put("couponId", -1);
                    resultMap.put("couponPrice", 0);
                    resultMap.put("userCouponId",-1);
                    dontIncludeFreightFeeNetPrice = netPrice;
                }
            }
        }

        //运费处理，从market_system中获取
        MarketSystemExample marketSystemExample = new MarketSystemExample();
        marketSystemExample.createCriteria().andKeyNameEqualTo("market_express_freight_min");
        MarketSystem marketSystem = marketSystemMapper.selectOneByExample(marketSystemExample);
        MarketSystemExample marketSystemExample1 = new MarketSystemExample();
        marketSystemExample1.createCriteria().andKeyNameEqualTo("market_express_freight_value");
        MarketSystem marketSystem1 = marketSystemMapper.selectOneByExample(marketSystemExample1);
        BigDecimal actualPrice = BigDecimal.ZERO;
        if (marketSystem != null && marketSystem1 != null) {
            //系统设置不为Null时，使用系统设置值
            BigDecimal minFreeFreightAmount = BigDecimal.valueOf(Long.parseLong(marketSystem.getKeyValue()));
            BigDecimal freightPrice = BigDecimal.valueOf(Long.parseLong(marketSystem1.getKeyValue()));
            if (netPrice.compareTo(minFreeFreightAmount)>=0){
                //此时满足满减规则
                resultMap.put("freightPrice",0);
                //无需运费
                resultMap.put("actualPrice",dontIncludeFreightFeeNetPrice);
                resultMap.put("orderTotalPrice",dontIncludeFreightFeeNetPrice);
            }else{
                //此时需要运费
                resultMap.put("freightPrice",freightPrice);
                actualPrice = dontIncludeFreightFeeNetPrice.add(freightPrice);
                resultMap.put("actualPrice",actualPrice);
                resultMap.put("orderTotalPrice",actualPrice);
            }
        } else {
            //系统设置缺失时，使用默认满88-8
            if (netPrice.compareTo(BigDecimal.valueOf(88))>=0){
                //此时满足满减规则
                resultMap.put("freightPrice",0);
                //无需运费
                resultMap.put("actualPrice",dontIncludeFreightFeeNetPrice);
                resultMap.put("orderTotalPrice",dontIncludeFreightFeeNetPrice);
            }else{
                //此时需要运费
                resultMap.put("freightPrice",8);
                actualPrice = dontIncludeFreightFeeNetPrice.add(BigDecimal.valueOf(8));
                resultMap.put("actualPrice",actualPrice);
                resultMap.put("orderTotalPrice",actualPrice);
            }
        }

        //团购及其他功能，使用默认值
        resultMap.put("grouponPrice",0);
        resultMap.put("grouponRulesId",0);
        resultMap.put("availableCouponLength",0);
        resultMap.put("cartId",0);
        return resultMap;
    }

    @Override
    public Map<String, Object> fastCheckout(Integer cartId, Integer addressId, Integer couponId, Integer userCouponId, Integer grouponRulesId, Integer userId) {
        //直接通过购物车下单
        HashMap<String, Object> resultMap = new HashMap<>();
        //首先获得选中的商品信息
        MarketCartExample marketCartExample = new MarketCartExample();
        marketCartExample.createCriteria().andIdEqualTo(cartId);
        MarketCart marketCart = marketCartMapper.selectOneByExample(marketCartExample);
        ArrayList<MarketCart> marketCarts = new ArrayList<>();
        marketCarts.add(marketCart);
        //获取checkedGoodsList
        resultMap.put("checkedGoodsList",marketCarts);

        //获取基本价格--商品数量*商品单价
        BigDecimal netPrice = BigDecimal.ZERO;
        BigDecimal number = BigDecimal.valueOf(marketCart.getNumber());
        netPrice = netPrice.add(marketCart.getPrice().multiply(number));
        resultMap.put("goodsTotalPrice", netPrice);//商品实际价格goodsTotalPrice

        MarketAddressExample marketAddressExample = new MarketAddressExample();
        marketAddressExample.createCriteria().andUserIdEqualTo(userId);
        List<MarketAddress> marketAddresses = marketAddressMapper.selectByExample(marketAddressExample);
        if (addressId == 0) {
            //采用默认地址
            for (MarketAddress marketAddress : marketAddresses) {
                if (marketAddress.getIsDefault()) {
                    resultMap.put("addressId", marketAddress.getId());//设置addressId
                    resultMap.put(("checkedAddress"), marketAddress);//设置checkedAddress项
                }
            }
        } else {
            //采用当前地址
            for (MarketAddress marketAddress : marketAddresses) {
                if (marketAddress.getId().equals(addressId)) {
                    resultMap.put("addressId", marketAddress.getId());//设置addressId
                    resultMap.put(("checkedAddress"), marketAddress);//设置checkedAddress项
                    break;
                }
            }
        }
        //判断优惠券使用,没有可用的优惠券则返回-1
        MarketCouponUserExample marketCouponUserExample = new MarketCouponUserExample();
        marketCouponUserExample.createCriteria().andUserIdEqualTo(userId).andDeletedEqualTo(false).andStatusEqualTo((short) 0);
        List<MarketCouponUser> marketCouponUserList = marketCouponUserMapper.selectByExample(marketCouponUserExample);
        BigDecimal dontIncludeFreightFeeNetPrice = BigDecimal.ZERO;
        if (couponId == -1||userCouponId==-1) {
            //查询有哪些可用的优惠券
//            if (marketCouponUserList == null) {
            resultMap.put("couponId", -1);
            resultMap.put("couponPrice", 0);
            resultMap.put("userCouponId",-1);
            dontIncludeFreightFeeNetPrice = netPrice;
//            } else {
            //若存在可用优惠券，在满足min-最低付款金额的基础上，默认选择能减除最多的优惠券
//                BigDecimal maxCouponMoney = BigDecimal.ZERO;
//                Integer actualCouponId = -1;
//                Integer marketCouponUserId = -1;
//                for (MarketCouponUser marketCouponUser : marketCouponUserList) {
//                    //根据couponId得到实际的coupon。
//                    //在实际coupon中进行判断是否满足这些优惠券使用的最小金额
//                    MarketCouponExample marketCouponExample = new MarketCouponExample();
//                    marketCouponExample.createCriteria().andIdEqualTo(marketCouponUser.getCouponId());
//                    MarketCoupon marketCoupon = marketCouponMapper.selectOneByExample(marketCouponExample);
//                    if (marketCoupon.getMin().compareTo(netPrice) <= 0) {
//                        //此时可使用该优惠券
//                        if (maxCouponMoney.compareTo(marketCoupon.getDiscount()) <= 0) {
//                            maxCouponMoney = marketCoupon.getDiscount();
//                            actualCouponId = marketCoupon.getId();
//                        }
//                        marketCouponUserId = marketCouponUser.getUserId();
//                    }
//                }
//                resultMap.put("couponId", actualCouponId);
//                resultMap.put("couponPrice", maxCouponMoney);
//                resultMap.put("userCouponId",userCouponId);
//                dontIncludeFreightFeeNetPrice = netPrice.subtract(maxCouponMoney);
//            }
        } else {
            //使用指定的优惠券--利用couponId来指定
            //selectList中已经做了筛选
            MarketCouponUserExample marketCouponUserExample1 = new MarketCouponUserExample();
            marketCouponUserExample1.createCriteria().andIdEqualTo(couponId);
            MarketCouponUser marketCouponUser = marketCouponUserMapper.selectOneByExample(marketCouponUserExample1);
            MarketCouponExample marketCouponExample = new MarketCouponExample();
            if (marketCouponUser!=null){
                marketCouponExample.createCriteria().andIdEqualTo(marketCouponUser.getCouponId());
            }
            MarketCoupon marketCoupon = marketCouponMapper.selectOneByExample(marketCouponExample);
            if (marketCoupon==null||marketCouponUser==null){
                resultMap.put("couponId", -1);
                resultMap.put("couponPrice", 0);
                resultMap.put("userCouponId",-1);
                dontIncludeFreightFeeNetPrice = netPrice;
            }else {
                if (netPrice.compareTo(marketCoupon.getMin()) >= 0) {
                    //可用使用该优惠券
                    resultMap.put("couponId", couponId);
                    resultMap.put("couponPrice", marketCoupon.getDiscount());
                    resultMap.put("userCouponId",userCouponId);
                    dontIncludeFreightFeeNetPrice = netPrice.subtract(marketCoupon.getDiscount());
                } else {
                    resultMap.put("couponId", -1);
                    resultMap.put("couponPrice", 0);
                    resultMap.put("userCouponId",-1);
                    dontIncludeFreightFeeNetPrice = netPrice;
                }
            }
        }

        //运费处理，从market_system中获取
        MarketSystemExample marketSystemExample = new MarketSystemExample();
        marketSystemExample.createCriteria().andKeyNameEqualTo("market_express_freight_min");
        MarketSystem marketSystem = marketSystemMapper.selectOneByExample(marketSystemExample);
        MarketSystemExample marketSystemExample1 = new MarketSystemExample();
        marketSystemExample1.createCriteria().andKeyNameEqualTo("market_express_freight_value");
        MarketSystem marketSystem1 = marketSystemMapper.selectOneByExample(marketSystemExample1);
        BigDecimal actualPrice = BigDecimal.ZERO;
        if (marketSystem != null && marketSystem1 != null) {
            //系统设置不为Null时，使用系统设置值
            BigDecimal minFreeFreightAmount = BigDecimal.valueOf(Long.parseLong(marketSystem.getKeyValue()));
            BigDecimal freightPrice = BigDecimal.valueOf(Long.parseLong(marketSystem1.getKeyValue()));
            if (netPrice.compareTo(minFreeFreightAmount)>=0){
                //此时满足满减规则
                resultMap.put("freightPrice",0);
                //无需运费
                resultMap.put("actualPrice",dontIncludeFreightFeeNetPrice);
                resultMap.put("orderTotalPrice",dontIncludeFreightFeeNetPrice);
            }else{
                //此时需要运费
                resultMap.put("freightPrice",freightPrice);
                actualPrice = dontIncludeFreightFeeNetPrice.add(freightPrice);
                resultMap.put("actualPrice",actualPrice);
                resultMap.put("orderTotalPrice",actualPrice);
            }
        } else {
            //系统设置缺失时，使用默认满88-8
            if (netPrice.compareTo(BigDecimal.valueOf(88))>=0){
                //此时满足满减规则
                resultMap.put("freightPrice",0);
                //无需运费
                resultMap.put("actualPrice",dontIncludeFreightFeeNetPrice);
                resultMap.put("orderTotalPrice",dontIncludeFreightFeeNetPrice);
            }else{
                //此时需要运费
                resultMap.put("freightPrice",8);
                actualPrice = dontIncludeFreightFeeNetPrice.add(BigDecimal.valueOf(8));
                resultMap.put("actualPrice",actualPrice);
                resultMap.put("orderTotalPrice",actualPrice);
            }
        }

        //团购及其他功能，使用默认值
        resultMap.put("grouponPrice",0);
        resultMap.put("grouponRulesId",0);
        resultMap.put("availableCouponLength",0);
        resultMap.put("cartId",0);
        return resultMap;
    }

    public boolean judgeNumber(Integer productId,Integer number){
        // 判断库存够不够
        MarketGoodsProduct marketGoodsProduct = marketGoodsProductMapper.selectByPrimaryKey(productId);
        if(number <= marketGoodsProduct.getNumber()){
            return true;
        }else {
            return false;
        }
    }
}
