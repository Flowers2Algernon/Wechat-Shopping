package com.cskaoyan.market.service.impl;

import com.cskaoyan.market.db.domain.MarketAdmin;
import com.cskaoyan.market.db.mapper.MarketAdminMapper;
import com.cskaoyan.market.service.AdminPasswordService;
import com.cskaoyan.market.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Author: jyc
 * @Date: 2024/5/7 15:16
 */
@Service
public class AdminPasswordServiceImpl implements AdminPasswordService {

    @Autowired
    MarketAdminMapper marketAdminMapper;

    @Override
    public void changePassword(MarketAdmin marketAdmin) {

        marketAdmin.setUpdateTime(LocalDateTime.now());
        marketAdminMapper.updateByPrimaryKeySelective(marketAdmin);

    }
}
