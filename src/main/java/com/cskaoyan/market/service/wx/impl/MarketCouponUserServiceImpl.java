package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.db.domain.MarketCoupon;
import com.cskaoyan.market.db.domain.MarketCouponExample;
import com.cskaoyan.market.db.domain.MarketCouponUser;
import com.cskaoyan.market.db.domain.MarketCouponUserExample;
import com.cskaoyan.market.db.mapper.MarketCouponMapper;
import com.cskaoyan.market.db.mapper.MarketCouponUserMapper;
import com.cskaoyan.market.service.wx.MarketCouponUserService;
import com.cskaoyan.market.vo.MarketCouponUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MarketCouponUserServiceImpl implements MarketCouponUserService {
    @Autowired
    MarketCouponUserMapper marketCouponUserMapper;
    @Autowired
    MarketCouponMapper marketCouponMapper;
    @Override
    public List<MarketCouponUserVo> mylist(Integer userId,Short status, Integer page, Integer limit) {
        MarketCouponUserExample marketCouponUserExample = new MarketCouponUserExample();
        MarketCouponUserExample.Criteria criteria = marketCouponUserExample.createCriteria();
        criteria.andStatusEqualTo(status);
        criteria.andUserIdEqualTo(userId);
        List<MarketCouponUser> list = marketCouponUserMapper.selectByExample(marketCouponUserExample);
        List<MarketCouponUserVo> marketCouponUserVos = new ArrayList<>();
        for (MarketCouponUser marketCouponUser : list) {
            MarketCoupon marketCoupon = marketCouponMapper.selectByPrimaryKey(marketCouponUser.getCouponId());
            MarketCouponUserVo marketCouponUserVo = new MarketCouponUserVo();
            marketCouponUserVo.setId(marketCoupon.getId());
            marketCouponUserVo.setDesc(marketCoupon.getDesc());
            marketCouponUserVo.setAvailable(marketCouponUser.getDeleted());
            marketCouponUserVo.setName(marketCoupon.getName());
            marketCouponUserVo.setMin(marketCoupon.getMin());
            marketCouponUserVo.setEndTime(marketCoupon.getEndTime());
            marketCouponUserVo.setStartTime(marketCoupon.getStartTime());
            marketCouponUserVo.setTag(marketCoupon.getTag());
            marketCouponUserVo.setDiscount(marketCoupon.getDiscount());
            marketCouponUserVo.setCid(marketCouponUser.getCouponId());
            marketCouponUserVos.add(marketCouponUserVo);
        }
        return marketCouponUserVos;
    }

    @Override
    public boolean receive(Integer userId, Integer couponId) {
        MarketCouponUserExample marketCouponUserExample = new MarketCouponUserExample();
        MarketCouponUserExample.Criteria criteria = marketCouponUserExample.createCriteria();
        //此处根据userId从数据库中选出所有该user拥有的优惠券
        //然后循环遍历判断是否拥有当前优惠券，有则直接返回true，无则将其添加到数据库中并返回false
        criteria.andUserIdEqualTo(userId);
        List<MarketCouponUser> list = marketCouponUserMapper.selectByExample(marketCouponUserExample);
        for (MarketCouponUser marketCouponUser : list) {
            if (marketCouponUser.getCouponId().equals(couponId)){
                return true;
            }
        }
        //此时该用户没有该优惠券，将其加入到该用户的账户中
        MarketCoupon marketCoupon = marketCouponMapper.selectByPrimaryKey(couponId);
        MarketCouponUser marketCouponUser = new MarketCouponUser();
        marketCouponUser.setCouponId(couponId);
        marketCouponUser.setUserId(userId);
        marketCouponUser.setAddTime(LocalDateTime.now());
        marketCouponUser.setEndTime(marketCoupon.getEndTime());
        marketCouponUser.setDeleted(false);
        marketCouponUser.setStartTime(marketCoupon.getStartTime());
        marketCouponUser.setEndTime(marketCoupon.getEndTime());
        marketCouponUser.setStatus((short) 0);
        marketCouponUser.setUpdateTime(LocalDateTime.now());
        marketCouponUserMapper.insertSelective(marketCouponUser);
        return false;
    }

    @Override
    public Integer exchange(Integer userId, String code) {
        //首先校验code是否正确，不正确直接返回状态码，正确则得出相应的couponId
        //其次校验当前用户是否已经领取该优惠券，已领取直接返回状态码，未领取则与数据库交互插入
        MarketCouponExample marketCouponExample = new MarketCouponExample();
        List<MarketCoupon> list = marketCouponMapper.selectByExample(marketCouponExample);
        MarketCouponUserExample marketCouponUserExample = new MarketCouponUserExample();
        List<MarketCouponUser> couponUserList = marketCouponUserMapper.selectByExample(marketCouponUserExample);
        for (MarketCoupon marketCoupon : list) {
            if (code.equals(marketCoupon.getCode())){
                for (MarketCouponUser marketCouponUser : couponUserList) {
                    if (marketCouponUser.getCouponId().equals(marketCoupon.getId())){
                        return 1;
                    }
                }
                //此处意味着该优惠券存在，且目前用户没有领取
                MarketCouponUser marketCouponUser = new MarketCouponUser();
                marketCouponUser.setUpdateTime(LocalDateTime.now());
                marketCouponUser.setStatus((short) 0);
                marketCouponUser.setDeleted(false);
                marketCouponUser.setUserId(userId);
                marketCouponUser.setAddTime(LocalDateTime.now());
                marketCouponUser.setCouponId(marketCoupon.getId());
                marketCouponUser.setStartTime(marketCoupon.getStartTime());
                marketCouponUser.setEndTime(marketCoupon.getEndTime());
                marketCouponUserMapper.insertSelective(marketCouponUser);
                return 2;
            }
        }
        return 0;//直接返回优惠券不正确
    }

    @Override
    public List<MarketCouponUserVo> selectList(Integer userId) {
        //根据userId来得到相应的用户所拥有的优惠券
        MarketCouponUserExample marketCouponUserExample = new MarketCouponUserExample();
        //此处选择coupon需要保障出现的优惠券没有超过有效期
        marketCouponUserExample.createCriteria().andUserIdEqualTo(userId).andDeletedEqualTo(false).andStatusEqualTo((short) 0);
        List<MarketCouponUser> list = marketCouponUserMapper.selectByExample(marketCouponUserExample);
        if (list==null){
            return null;
        }else {
//            PageHelper.startPage(1,2);
            List<MarketCouponUserVo> marketCouponUserVos = new ArrayList<>();
            for (MarketCouponUser marketCouponUser : list) {
                if (marketCouponUser.getEndTime()!=null){
                    if (LocalDateTime.now().isAfter(marketCouponUser.getEndTime())){
                        continue;
                    }
                    MarketCouponExample marketCouponExample = new MarketCouponExample();
                    marketCouponExample.createCriteria().andIdEqualTo(marketCouponUser.getCouponId());
                    MarketCoupon marketCoupon = marketCouponMapper.selectOneByExample(marketCouponExample);
                    MarketCouponUserVo marketCouponUserVo = new MarketCouponUserVo();
                    marketCouponUserVo.setDesc(marketCoupon.getDesc());
                    marketCouponUserVo.setName(marketCoupon.getName());
                    marketCouponUserVo.setAvailable(true);
                    marketCouponUserVo.setId(marketCoupon.getId());
                    marketCouponUserVo.setTag(marketCoupon.getTag());
                    marketCouponUserVo.setStartTime(marketCoupon.getStartTime());
                    marketCouponUserVo.setEndTime(marketCoupon.getEndTime());
                    marketCouponUserVo.setDiscount(marketCoupon.getDiscount());
                    marketCouponUserVo.setCid(marketCouponUser.getId());
                    marketCouponUserVo.setMin(marketCoupon.getMin());
                    marketCouponUserVos.add(marketCouponUserVo);
                }else {
                    continue;
                }
            }
            return marketCouponUserVos;
        }
    }

}
