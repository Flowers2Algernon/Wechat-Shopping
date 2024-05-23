package com.cskaoyan.market.service.wx;

import com.cskaoyan.market.db.domain.MarketCouponUser;
import com.cskaoyan.market.vo.MarketCouponUserVo;

import java.util.List;

public interface MarketCouponUserService {

    List<MarketCouponUserVo> mylist(Integer userId,Short status, Integer page, Integer limit);

    boolean receive(Integer userId, Integer couponId);

    Integer exchange(Integer userId, String code);

    List<MarketCouponUserVo> selectList(Integer userId);
}
