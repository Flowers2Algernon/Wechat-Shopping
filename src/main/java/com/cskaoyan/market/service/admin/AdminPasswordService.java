package com.cskaoyan.market.service.admin;

import com.cskaoyan.market.db.domain.MarketAdmin;

/**
 * @Author: jyc
 * @Date: 2024/5/7 15:15
 */

public interface AdminPasswordService {
    void changePassword(MarketAdmin marketAdmin);
}
