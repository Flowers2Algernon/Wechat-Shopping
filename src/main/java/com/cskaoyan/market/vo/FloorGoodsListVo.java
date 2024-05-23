package com.cskaoyan.market.vo;

import com.cskaoyan.market.db.domain.MarketGoods;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FloorGoodsListVo {

    int id;
    String name;
    List<MarketGoods> goodsList;


}
