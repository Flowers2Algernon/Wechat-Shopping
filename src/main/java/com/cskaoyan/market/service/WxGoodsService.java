package com.cskaoyan.market.service;

import com.cskaoyan.market.vo.MarketFloorGoodsVo;

import java.util.List;
import java.util.Map;

public interface WxGoodsService {
    Map<String, Object> detail(Integer id);

    List<MarketFloorGoodsVo> related(Integer id);

}
