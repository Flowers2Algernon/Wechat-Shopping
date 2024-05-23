package com.cskaoyan.market.service.wx;

public interface WxBrandService {
    Object detail(Integer id);

    Object list( Integer page, Integer limit);
}
