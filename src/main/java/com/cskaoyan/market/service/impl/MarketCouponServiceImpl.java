package com.cskaoyan.market.service.impl;

import com.cskaoyan.market.db.domain.MarketCoupon;
import com.cskaoyan.market.db.domain.MarketCouponExample;
import com.cskaoyan.market.db.mapper.MarketCouponMapper;
import com.cskaoyan.market.service.MarketCouponService;
import com.cskaoyan.market.util.MybatisUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 薛松 xuesong_work@163.com
 * @Date: 2024/05/06/22:12
 * @Description:
 */
@Service
public class MarketCouponServiceImpl implements MarketCouponService {
    @Autowired
    MarketCouponMapper marketCouponMapper;
    @Override
    public List<MarketCoupon> list(Integer page, Integer limit, String name, String status, String type, String sort, String order) {
        //SqlSession session=MybatisUtils.getSession();
        //MarketCouponMapper mapper = session.getMapper(MarketCouponMapper.class);
        MarketCouponExample example = new MarketCouponExample();
        MarketCouponExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedEqualTo(false);
        example.setOrderByClause(sort+" "+order);
        if(StringUtils.isNotBlank(name))
        {
            criteria.andNameLike("%"+name+"%");
        }
        if(!StringUtils.isEmpty(type))
        {
            criteria.andTypeEqualTo(Short.valueOf(type));
        }
        if(!StringUtils.isEmpty(status))
        {
            criteria.andStatusEqualTo(Short.valueOf(status));
        }
        PageHelper.startPage(page,limit);
        List<MarketCoupon> couponList = marketCouponMapper.selectByExample(example);
        //session.commit();
        //session.close();
        return couponList;
    }

    @Override
    public MarketCoupon read(Integer id) {
        //SqlSession session = MybatisUtils.getSession();
        //MarketCouponMapper marketCouponMapper = session.getMapper(MarketCouponMapper.class);

        MarketCoupon marketCoupon = marketCouponMapper.selectByPrimaryKey(id);
        //session.commit();
        //session.close();
        return marketCoupon;

    }

    @Override
    public MarketCoupon update(Integer id, String name, String desc, String tag, Integer total, BigDecimal discount, BigDecimal min, Short limit, Short type, Short status, Short goodsType, Integer[] goodsValue, String code, Short timeType, Short days, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime addTime, LocalDateTime updateTime, Boolean deleted) {
        //SqlSession session = MybatisUtils.getSession();
        //MarketCouponMapper marketCouponMapper = session.getMapper(MarketCouponMapper.class);

        MarketCoupon marketCoupon = new MarketCoupon();

        marketCoupon.setId(id);
        marketCoupon.setName(name);
        marketCoupon.setDesc(desc);
        marketCoupon.setTag(tag);
        marketCoupon.setTotal(total);
        marketCoupon.setDiscount(discount);
        marketCoupon.setMin(min);
        marketCoupon.setLimit(limit);
        marketCoupon.setType(type);
        marketCoupon.setStatus(status);
        marketCoupon.setGoodsType(goodsType);
        marketCoupon.setGoodsValue(goodsValue);
        marketCoupon.setTimeType(timeType);
        marketCoupon.setDays(days);
        marketCoupon.setStartTime(startTime);
        marketCoupon.setEndTime(endTime);
        marketCoupon.setAddTime(addTime);
        marketCoupon.setUpdateTime(updateTime);
        marketCoupon.setDeleted(deleted);

        int rowsAffected = marketCouponMapper.updateByPrimaryKeySelective(marketCoupon);
        //session.commit();

        MarketCoupon updateCoupon = marketCouponMapper.selectByPrimaryKey(id);
        if(rowsAffected>0){
            System.out.println("rowsAffected="+ rowsAffected );
            return updateCoupon;
        }
        else {
            return null;
        }
    }

    @Override
    public boolean delete(Integer id) {
        //SqlSession session = MybatisUtils.getSession();

        try{
            //MarketCouponMapper marketCouponMapper = session.getMapper(MarketCouponMapper.class);
            int rowsAffected = marketCouponMapper.logicalDeleteByPrimaryKey(id);
            //彻底删
            //int rowsAffected = marketCouponMapper.deleteByPrimaryKey(id);
            //session.commit();
            return rowsAffected>0;
        }catch (Exception e){
            //session.rollback();
            throw new RuntimeException("删除失败",e);
        }/*finally {
            session.close();
        }*/
    }

    @Override
    public MarketCoupon create(String name, String desc, String tag, Integer total, BigDecimal discount, BigDecimal min, Short limit, Short type, Short status, Short goodsType, Integer[] goodsValue, Short timeType, Short days, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime addTime, LocalDateTime updateTime) {
        //SqlSession session = MybatisUtils.getSession();
        //MarketCouponMapper marketCouponMapper = session.getMapper(MarketCouponMapper.class);

        MarketCoupon marketCoupon = new MarketCoupon();

        marketCoupon.setName(name);
        marketCoupon.setDesc(desc);
        marketCoupon.setTag(tag);
        marketCoupon.setTotal(total);
        marketCoupon.setDiscount(discount);
        marketCoupon.setMin(min);
        marketCoupon.setLimit(limit);
        marketCoupon.setType(type);
        marketCoupon.setStatus(status);
        marketCoupon.setGoodsType(goodsType);
        marketCoupon.setGoodsValue(goodsValue);
        marketCoupon.setTimeType(timeType);
        marketCoupon.setDays(days);
        marketCoupon.setStartTime(startTime);
        marketCoupon.setEndTime(endTime);
        marketCoupon.setAddTime(addTime);
        marketCoupon.setUpdateTime(updateTime);


        marketCouponMapper.insertSelective(marketCoupon);
        //session.commit();

        MarketCoupon createdCoupon = marketCouponMapper.selectByPrimaryKey(marketCoupon.getId());
        //session.close();

        return createdCoupon;
    }


}
