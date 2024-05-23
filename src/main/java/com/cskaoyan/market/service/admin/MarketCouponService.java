package com.cskaoyan.market.service.admin;

import com.cskaoyan.market.db.domain.MarketCoupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 薛松 xuesong_work@163.com
 * @Date: 2024/05/06/22:09
 * @Description:
 */
public interface MarketCouponService {
    List<MarketCoupon> list(Integer page, Integer limit, String name, String status,String type, String sort, String order);
    MarketCoupon read(Integer id);


    MarketCoupon update(Integer id, String name, String desc, String tag, Integer total, BigDecimal discount, BigDecimal min, Short limit, Short type, Short status, Short goodsType, Integer[] goodsValue, String code, Short timeType, Short days, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime addTime, LocalDateTime updateTime, Boolean deleted);

    boolean delete(Integer id);


    MarketCoupon create(String name, String desc, String tag, Integer total, BigDecimal discount, BigDecimal min, Short limit, Short type, Short status, Short goodsType, Integer[] goodsValue, Short timeType, Short days, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime addTime, LocalDateTime updateTime);
}
