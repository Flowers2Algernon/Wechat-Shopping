package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.db.domain.MarketCoupon;
import com.cskaoyan.market.db.domain.MarketCouponExample;
import com.cskaoyan.market.db.mapper.MarketCouponMapper;
import com.cskaoyan.market.service.wx.MarketCouponService;
import com.cskaoyan.market.vo.MarketCouponVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class MarketCouponServiceWxImpl implements MarketCouponService {
    @Autowired
    MarketCouponMapper marketCouponMapper;

    @Override
    public List<MarketCouponVo> list() {
        MarketCouponExample marketCouponExample = new MarketCouponExample();
        List<MarketCoupon> list = marketCouponMapper.selectByExample(marketCouponExample);
        List<MarketCouponVo> marketCouponVos = new ArrayList<>();
        for (MarketCoupon marketCoupon : list) {
            MarketCouponVo marketCouponVo = new MarketCouponVo();
            marketCouponVo.setDays(marketCoupon.getDays());
            marketCouponVo.setId(marketCoupon.getId());
            marketCouponVo.setName(marketCoupon.getName());
            marketCouponVo.setMin(marketCoupon.getMin());
            marketCouponVo.setTag(marketCoupon.getTag());
            marketCouponVo.setDesc(marketCoupon.getDesc());
            marketCouponVo.setDiscount(marketCoupon.getDiscount());
            marketCouponVos.add(marketCouponVo);
        }
        return marketCouponVos;
    }
}
