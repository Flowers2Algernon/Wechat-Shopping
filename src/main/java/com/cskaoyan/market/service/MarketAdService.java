package com.cskaoyan.market.service;

import com.cskaoyan.market.db.domain.MarketAd;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 薛松 xuesong_work@163.com
 * @Date: 2024/05/06/17:58
 * @Description:
 */
public interface MarketAdService {
    List<MarketAd> list(Integer page, Integer limit, String name, String content, String sort, String order);

    MarketAd create(String name, String content, String url, String link, Byte position, Boolean enabled);

    MarketAd update(Integer id, String name, String link, String url, Byte position, String content, Boolean enabled, LocalDateTime addTime);

    boolean delete(Integer id);
}
