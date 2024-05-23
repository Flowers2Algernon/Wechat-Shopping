package com.cskaoyan.market.service.admin;

import com.cskaoyan.market.db.domain.MarketLog;

import java.util.List;

public interface AdminLogService {

    List<MarketLog> list(Integer page, Integer limit, String username, String sort, String order);
}
