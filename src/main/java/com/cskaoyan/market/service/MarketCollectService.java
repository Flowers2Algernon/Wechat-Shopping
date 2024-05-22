package com.cskaoyan.market.service;

import com.cskaoyan.market.db.domain.MarketCollect;

import java.util.List;

public interface MarketCollectService {
    List<MarketCollect> list(Integer limit, Integer page, String userId, String sort, String order, String valueId);
}
