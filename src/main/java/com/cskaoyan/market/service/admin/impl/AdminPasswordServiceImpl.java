package com.cskaoyan.market.service.admin.impl;

import com.cskaoyan.market.db.domain.MarketAdmin;
import com.cskaoyan.market.db.mapper.MarketAdminMapper;
import com.cskaoyan.market.service.admin.AdminPasswordService;
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
