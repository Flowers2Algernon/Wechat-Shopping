package com.cskaoyan.market.service.admin;

import com.cskaoyan.market.db.domain.MarketBrand;

import java.util.List;

public interface BrandService {

    List<MarketBrand> list(Integer page, Integer limit, String id, String name, String sort, String order);
    MarketBrand insertOne(MarketBrand marketBrand) ;

    MarketBrand update(MarketBrand marketBrand);

    void delete(MarketBrand marketBrand);
}
