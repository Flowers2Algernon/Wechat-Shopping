package com.cskaoyan.market.service.admin.impl;

import com.cskaoyan.market.db.domain.MarketAdmin;
import com.cskaoyan.market.db.domain.MarketAdminExample;
import com.cskaoyan.market.db.mapper.MarketAdminMapper;
import com.cskaoyan.market.service.admin.MarketAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/4 9:26
 * @Version 1.0
 */
@Service
public class MarketAdminServiceAdminImpl implements MarketAdminService {
    @Autowired
    MarketAdminMapper marketAdminMapper;

    @Override
    public MarketAdmin login(String username, String password) {
        //紧接着应该在service中调用mapper，因为我们使用的是mybatis，所以不需要再去编写mapper实现类对象
        //mapper接口，mapper映射文件里面都是关于表的一系列的操作，其实可以通过模板去生成
        //逆向工程还可以帮助我们去生成实体类对象
        MarketAdminExample marketAdminExample = new MarketAdminExample();
        //criteria用来构造一系列的where条件
        MarketAdminExample.Criteria criteria = marketAdminExample.createCriteria();

        criteria.andUsernameEqualTo(username);
        criteria.andPasswordEqualTo(password);
        MarketAdmin marketAdmin = marketAdminMapper.selectOneByExample(marketAdminExample);
        return marketAdmin;
    }

    @Override
    public void updateById(MarketAdmin marketAdmin) {
        marketAdmin.setUpdateTime(LocalDateTime.now());
        marketAdminMapper.updateByPrimaryKeySelective(marketAdmin);
    }
}
