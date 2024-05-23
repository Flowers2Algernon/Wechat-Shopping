package com.cskaoyan.market.service.wx;

import com.cskaoyan.market.db.domain.MarketFeedback;
import com.cskaoyan.market.db.domain.MarketFootprint;

import java.util.List;

public interface WxFootPrintService {
    List list(Integer limit,Integer page);
    //int getTotalCount(String userId,String goodsId);
    void delete(Integer id);
}
