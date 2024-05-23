package com.cskaoyan.market.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketFloorGoodsVo {
    private String brief;
    private BigDecimal counterPrice;
    private Integer id;
    private boolean isHot;
    private boolean isNew;
    private String name;
    private String picUrl;
    private BigDecimal retailPrice;
}
