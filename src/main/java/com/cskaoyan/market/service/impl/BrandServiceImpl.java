package com.cskaoyan.market.service.impl;

import com.cskaoyan.market.db.domain.MarketBrand;
import com.cskaoyan.market.db.domain.MarketBrandExample;
import com.cskaoyan.market.db.domain.MarketGoods;
import com.cskaoyan.market.db.mapper.MarketBrandMapper;
import com.cskaoyan.market.service.BrandService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    MarketBrandMapper marketBrandMapper;
    @Override
    public List<MarketBrand> list(Integer page, Integer limit, String id, String name, String sort, String order) {
        // SqlSession session = MybatisUtils.getSession();
        // MarketBrandMapper brandMapper = session.getMapper(MarketBrandMapper.class);
        MarketBrandExample brandExample = new MarketBrandExample();
        brandExample.setOrderByClause(sort + " " +order);
        MarketBrandExample.Criteria criteria = brandExample.createCriteria();
        if(!StringUtils.isEmpty(id)){
            criteria.andIdEqualTo(Integer.parseInt(id));
        }
        if (!StringUtils.isEmpty(name)){
            criteria.andNameLike("%"+name+"%");
        }
        PageHelper.startPage(page,limit);
        List<MarketBrand> marketBrands = marketBrandMapper.selectByExample(brandExample);
        // session.commit();
        // session.close();

        return marketBrands;
    }
    @Override
    public MarketBrand insertOne(MarketBrand marketBrand) {
        // SqlSession session = MybatisUtils.getSession();
        // MarketBrandMapper marketBrandMapper = session.getMapper(MarketBrandMapper.class);

        marketBrand.setAddTime(LocalDateTime.now());
        marketBrand.setUpdateTime(LocalDateTime.now());

        marketBrandMapper.insertSelective(marketBrand);
        // session.commit();
        // session.close();
        // MarketBrandExample brandExample = new MarketBrandExample();
        // List<MarketBrand> marketBrands = marketBrandMapper.selectByExample(brandExample);
        return marketBrand;
    }

    @Override
    public MarketBrand update(MarketBrand marketBrand) {
        // SqlSession session = MybatisUtils.getSession();
        // MarketBrandMapper marketBrandMapper = session.getMapper(MarketBrandMapper.class);
        marketBrand.setUpdateTime(LocalDateTime.now());
        marketBrand.setUpdateTime(LocalDateTime.now());
        marketBrandMapper.insertSelective(marketBrand);
        // session.commit();
        // session.close();
        return marketBrand;
    }

    @Override
    public void delete(MarketBrand marketBrand) {
        // SqlSession session = MybatisUtils.getSession();
        // MarketBrandMapper marketBrandMapper = session.getMapper(MarketBrandMapper.class);
        MarketBrandExample brandExample = new MarketBrandExample();
        MarketBrandExample.Criteria criteria = brandExample.createCriteria();
        String id = marketBrand.getId().toString();
        if(!StringUtils.isEmpty(id)){
            criteria.andIdEqualTo(Integer.parseInt(id));
        }
        marketBrandMapper.deleteByExample(brandExample);
        // session.commit();
        // session.close();
    }

}
