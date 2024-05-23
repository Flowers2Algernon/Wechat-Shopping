package com.cskaoyan.market.service.wx;

import com.cskaoyan.market.db.domain.MarketAddress;

import java.util.List;

public interface WxAddressService {
    List<MarketAddress> list();

    Object detail(Integer id);

    int save(Integer id, String name, String tel, String province, String city, String county, String areaCode, String addressDetail, Object isDefault);

    Integer deleteAddress(Integer id);
}
