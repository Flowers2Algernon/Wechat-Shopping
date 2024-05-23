package com.cskaoyan.market.service.admin;

import com.cskaoyan.market.db.domain.MarketKeyword;

import java.util.List;

public interface MarketKeywordService {
    List<MarketKeyword> list(Integer page, Integer limit, String keyword, String url, String sort, String order);

    MarketKeyword createKeyword(String keyword, String url, Boolean isDefault, Boolean isHot);

    MarketKeyword updateKeyword(Integer id,String updateTime,String keyword, String url, Boolean isDefault, Boolean isHot, String addTime,Boolean delete,Integer sortOrder);

    Integer deleteKeyword(Integer id);
}
