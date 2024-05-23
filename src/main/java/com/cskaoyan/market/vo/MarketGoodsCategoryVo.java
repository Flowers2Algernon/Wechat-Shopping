package com.cskaoyan.market.vo;

import com.cskaoyan.market.db.domain.MarketCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketGoodsCategoryVo {
    private List<MarketCategory> brotherCategory;
    private MarketCategory currentCategory;
    private MarketCategory parentCategory;
}
