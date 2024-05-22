package com.cskaoyan.market.vo;

import com.cskaoyan.market.db.domain.MarketGoods;
import com.cskaoyan.market.db.domain.MarketGoodsAttribute;
import com.cskaoyan.market.db.domain.MarketGoodsProduct;
import com.cskaoyan.market.db.domain.MarketGoodsSpecification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketGoodsVo {
    private List<MarketGoodsProduct> products;
    private List<MarketGoodsSpecification> specifications;
    private List<MarketGoodsAttribute> attributes;
    private MarketGoods goods;
    private List<Integer> categoryIds;

}
