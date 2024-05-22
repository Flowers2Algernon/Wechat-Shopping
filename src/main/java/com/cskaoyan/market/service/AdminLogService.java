package com.cskaoyan.market.service;

import com.cskaoyan.market.db.domain.MarketLog;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AdminLogService {

    List<MarketLog> list(Integer page, Integer limit, String username, String sort, String order);
}
