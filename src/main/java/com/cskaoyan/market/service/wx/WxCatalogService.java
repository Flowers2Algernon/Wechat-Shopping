package com.cskaoyan.market.service.wx;

import com.cskaoyan.market.db.domain.MarketCategory;

import java.util.List;

public interface WxCatalogService {
    List<MarketCategory> getList();

    MarketCategory getCurrent(int i);

    List<MarketCategory> getCurrentSub(int i);
}
