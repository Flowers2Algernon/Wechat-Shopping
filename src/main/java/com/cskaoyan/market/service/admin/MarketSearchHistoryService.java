package com.cskaoyan.market.service.admin;

import com.cskaoyan.market.db.domain.MarketSearchHistory;

import java.util.List;

public interface MarketSearchHistoryService {
    List<MarketSearchHistory> list(Integer limit, Integer page, String userId, String sort, String order, String keyword);
}
