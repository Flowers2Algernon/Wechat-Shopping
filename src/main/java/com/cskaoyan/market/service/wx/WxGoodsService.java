package com.cskaoyan.market.service.wx;

import java.util.List;

public interface WxGoodsService {
    Object category (Integer id);

    Object list(String keyword,Integer categoryId, Integer brandId,Integer page, Integer limit,
                String sort,String order,Boolean isNew,Boolean isHot);

    Object goodsDetail(int id);

    List goodsRelated(int id);

    Object count();
}
