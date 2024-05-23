package com.cskaoyan.market.service.wx;

import com.cskaoyan.market.db.domain.MarketCollect;
import com.cskaoyan.market.db.domain.MarketGoods;
import com.cskaoyan.market.vo.CollectVo;

import java.util.List;

public interface WxCollectService {
    List<CollectVo> list(Integer type, Integer page, Integer limit);

    int addordelete(Integer valueId, Integer type);

}
