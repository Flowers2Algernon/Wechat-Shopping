package com.cskaoyan.market.service;

import com.cskaoyan.market.db.domain.MarketFootprint;

import java.util.List;

public interface MarketFootprintService {
    List<MarketFootprint> list(Integer limit, Integer page, String userId, String sort, String order, String goodsId);
}
