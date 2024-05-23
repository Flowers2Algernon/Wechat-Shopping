package com.cskaoyan.market.vo;

import com.cskaoyan.market.db.domain.MarketCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentVo {
    private MarketCategory currentCategory;
    private List<MarketCategory> currentSubCategory;
}
