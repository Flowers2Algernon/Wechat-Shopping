package com.cskaoyan.market.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketCouponVo {
    private Integer id;
    private String name;
    private String desc;
    private String tag;
    private BigDecimal discount;
    private BigDecimal min;
    private short days;
}
