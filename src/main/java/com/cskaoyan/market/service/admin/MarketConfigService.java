package com.cskaoyan.market.service.admin;

import com.cskaoyan.market.db.domain.MarketSystem;

public interface MarketConfigService {
    MarketSystem queryMarketConfigByName(String name);
    void updateById(MarketSystem marketSystem);
}
