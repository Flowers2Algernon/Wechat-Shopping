package com.cskaoyan.market.service.admin;

import com.cskaoyan.market.db.domain.MarketAddress;

import java.util.List;

public interface MarketAddressService {
    List<MarketAddress> list(Integer limit, Integer page, String username, String sort, String order,String name);
}
