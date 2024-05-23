package com.cskaoyan.market.service.wx;

import java.util.List;
import java.util.Map;

public interface WxCartService {
    Integer goodsCount(Integer userId);

    Integer add(Integer goodsId, Short number, Integer productId, Integer userId);

    boolean update(Integer goodsId, Integer id, Integer number, Integer productId, Integer userId);

    void delete(List<Integer> productIds, Integer userId);

    Map<String,Object> index(Integer userId);

    void checked(Integer isChecked, List<Integer> productIds, Integer userId);

    Integer addFast(Integer goodsId, short shortValue, Integer productId, Integer userId);

    Map<String, Object> cartCheckout(Integer addressId, Integer couponId, Integer userCouponId, Integer grouponRulesId,Integer userId);

    Map<String, Object> fastCheckout(Integer cartId, Integer addressId, Integer couponId, Integer userCouponId, Integer grouponRulesId, Integer userId);
}
