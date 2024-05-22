package com.cskaoyan.market.service;

import com.cskaoyan.market.db.domain.MarketUser;

import java.util.List;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/4 11:27
 * @Version 1.0
 */
public interface MarketUserService {
    List<MarketUser> list(Integer limit, Integer page, String username, String mobile, String sort, String order);


    List<MarketUser> getDataFromId(CharSequence id);
}
