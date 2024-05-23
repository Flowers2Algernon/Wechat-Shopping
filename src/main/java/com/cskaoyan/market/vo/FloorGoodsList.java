package com.cskaoyan.market.Vo;

import com.cskaoyan.market.db.domain.MarketCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FloorGoodsList {

    int id;
    String name;
    List<MarketCategory> goodsList;
}
