package com.cskaoyan.market.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketCategoryFloorGoodsVo {
    private String name;
    private Integer id;
    private List<MarketFloorGoodsVo> goodsList;
}
