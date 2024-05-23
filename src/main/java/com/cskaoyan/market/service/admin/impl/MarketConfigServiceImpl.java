package com.cskaoyan.market.service.admin.impl;

import com.cskaoyan.market.db.domain.MarketSystem;
import com.cskaoyan.market.db.domain.MarketSystemExample;
import com.cskaoyan.market.db.mapper.MarketSystemMapper;
import com.cskaoyan.market.service.admin.MarketConfigService;
import com.cskaoyan.market.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Author: jyc
 * @Date: 2024/5/6 20:46
 */
@Service
public class MarketConfigServiceImpl implements MarketConfigService {
    @Autowired
    MarketSystemMapper marketSystemMapper;

    @Override
    public MarketSystem queryMarketConfigByName(String name) {
        //构造条件MarketSystemExample
        MarketSystemExample marketSystemExample = new MarketSystemExample();
        MarketSystemExample.Criteria criteria = marketSystemExample.createCriteria();
        criteria.andKeyNameEqualTo(name);

        MarketSystem marketSystem =marketSystemMapper.selectOneByExample(marketSystemExample);

        return marketSystem;
    }

    @Override
    public void updateById(MarketSystem marketSystem) {
        marketSystem.setUpdateTime(LocalDateTime.now());
        SqlSession session = MybatisUtils.getSession();
        MarketSystemMapper mapper = session.getMapper(MarketSystemMapper.class);
        mapper.updateByPrimaryKeySelective(marketSystem);
        session.commit();
        session.close();
    }
}
