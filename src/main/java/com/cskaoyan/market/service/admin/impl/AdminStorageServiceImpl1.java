package com.cskaoyan.market.service.admin.impl;

import com.cskaoyan.market.db.domain.MarketStorage;
import com.cskaoyan.market.db.domain.MarketStorageExample;
import com.cskaoyan.market.db.mapper.MarketStorageMapper;
import com.cskaoyan.market.service.admin.AdminStorageService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminStorageServiceImpl1 implements AdminStorageService {
    @Autowired
    MarketStorageMapper marketStorageMapper;

    @Override
    public void create(MarketStorage marketStorage) {
        //此处实现数据库中具体的插入逻辑
        marketStorageMapper.insert(marketStorage);
    }

    @Override
    public List<MarketStorage> list(Integer limit, Integer page, String key, String name, String sort, String order) {
        MarketStorageExample marketStorageExample = new MarketStorageExample();
        marketStorageExample.setOrderByClause(sort + " " + order);
        MarketStorageExample.Criteria criteria = marketStorageExample.createCriteria();
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (!StringUtils.isEmpty(key)) {
            criteria.andKeyLike("%" + key + "%");
        }
        //分页
        PageHelper.startPage(page, limit);
        //执行查询操作
        List<MarketStorage> marketStorages = marketStorageMapper.selectByExample(marketStorageExample);
        //不要忘记提交数据库
        return marketStorages;
    }

    @Override
    public void update(MarketStorage marketStorage) {
        marketStorageMapper.updateByPrimaryKeySelective(marketStorage);
    }

    @Override
    public void delete(MarketStorage marketStorage) {
        marketStorageMapper.deleteByPrimaryKey(marketStorage.getId());
    }
}
