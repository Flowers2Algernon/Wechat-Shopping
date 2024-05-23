package com.cskaoyan.market.service.admin;

import com.cskaoyan.market.db.domain.MarketStorage;

import java.util.List;

public interface AdminStorageService {
    void create(MarketStorage marketStorage);

    List<MarketStorage> list(Integer limit, Integer page, String key, String name, String sort, String order);

    void update(MarketStorage marketStorage1);

    void delete(MarketStorage marketStorage);
}
