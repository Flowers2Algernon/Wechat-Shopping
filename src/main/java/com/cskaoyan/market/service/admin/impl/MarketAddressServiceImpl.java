package com.cskaoyan.market.service.admin.impl;

import com.cskaoyan.market.db.domain.MarketAddress;
import com.cskaoyan.market.db.domain.MarketAddressExample;
import com.cskaoyan.market.db.mapper.MarketAddressMapper;
import com.cskaoyan.market.service.admin.MarketAddressService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketAddressServiceImpl implements MarketAddressService {
    private final MarketAddressMapper marketAddressMapper;

    @Autowired
    public MarketAddressServiceImpl(MarketAddressMapper marketAddressMapper) {
        this.marketAddressMapper = marketAddressMapper;
    }

    @Override
    public List<MarketAddress> list(Integer limit, Integer page, String userId, String sort, String order, String name) {
        MarketAddressExample marketAddressExample = new MarketAddressExample();
        marketAddressExample.setOrderByClause(sort + " " + order);
        MarketAddressExample.Criteria criteria = marketAddressExample.createCriteria();
        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        PageHelper.startPage(page, limit);
        return marketAddressMapper.selectByExampleSelective(marketAddressExample,
                MarketAddress.Column.id, MarketAddress.Column.name, MarketAddress.Column.userId,
                MarketAddress.Column.province, MarketAddress.Column.city, MarketAddress.Column.county, MarketAddress.Column.addressDetail,
                MarketAddress.Column.areaCode, MarketAddress.Column.postalCode, MarketAddress.Column.tel, MarketAddress.Column.isDefault,
                MarketAddress.Column.addTime, MarketAddress.Column.updateTime, MarketAddress.Column.deleted);
    }
}