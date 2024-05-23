package com.cskaoyan.market.service.admin.impl;

import com.cskaoyan.market.db.domain.MarketLog;
import com.cskaoyan.market.db.domain.MarketLogExample;
import com.cskaoyan.market.db.mapper.MarketLogMapper;
import com.cskaoyan.market.service.admin.AdminLogService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminLogServiceImpl implements AdminLogService {
    @Autowired
    MarketLogMapper marketLogMapper;

    @Override
    public List<MarketLog> list(Integer page, Integer limit, String username, String sort, String order) {
        MarketLogExample marketLogExample = new MarketLogExample();
        MarketLogExample.Criteria criteria = marketLogExample.createCriteria();
        marketLogExample.setOrderByClause(sort+" "+order);
        if (!StringUtils.isEmpty(username)){
            criteria.andAdminLike("%"+username+"%");
        }
        PageHelper.startPage(page,limit);
        List<MarketLog> list = marketLogMapper.selectByExample(marketLogExample);
        return list;

    }
}
