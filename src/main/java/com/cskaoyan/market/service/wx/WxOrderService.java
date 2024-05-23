package com.cskaoyan.market.service.wx;

import com.cskaoyan.market.db.domain.MarketUser;
import com.cskaoyan.market.vo.OrderDetailVo;
import com.cskaoyan.market.vo.OrderGoodsVo;
import com.cskaoyan.market.vo.OrderListVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 薛松 xuesong_work@163.com
 * @Date: 2024/05/20/18:00
 * @Description:
 */
public interface WxOrderService {
   List<OrderListVo> list(MarketUser user, Integer showType, Integer page, Integer limit);

   OrderDetailVo detail(Integer orderId);

   boolean cancel(Integer orderId);

   boolean prepay(Integer orderId);

   boolean delete(Integer orderId);

   boolean refund(Integer orderId);
   boolean confirm(Integer orderId);

   OrderGoodsVo goods(Integer orderId, Integer goodsId);

   Integer comment(Integer userId, String content, Boolean hasPicture, Integer orderGoodsId, Integer star, String[] picUrls);

   Integer submit(Integer addressId, Integer cartId, Integer couponId, Integer grouponRulesId, Integer grouponLinkId, String message, Integer userCouponId, Integer userId, BigDecimal money);
}
