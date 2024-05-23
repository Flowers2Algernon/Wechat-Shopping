package com.cskaoyan.market.service.admin;

import com.cskaoyan.market.db.domain.MarketAdmin;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/4 9:26
 * @Version 1.0
 */
public interface MarketAdminService {
    MarketAdmin login(String username, String password);
    void updateById(MarketAdmin marketAdmin);
}
