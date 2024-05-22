package com.cskaoyan.market.bo;

import com.cskaoyan.market.db.domain.MarketGoods;
import com.cskaoyan.market.db.domain.MarketGoodsAttribute;
import com.cskaoyan.market.db.domain.MarketGoodsProduct;
import com.cskaoyan.market.db.domain.MarketGoodsSpecification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName AddGoodsBo
 * @Description: TODO
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/3/16 11:41
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddGoodsBo {

    private MarketGoods goods;

    private List<MarketGoodsSpecification> specifications;

    private List<MarketGoodsProduct> products;

    private List<MarketGoodsAttribute> attributes;
}
