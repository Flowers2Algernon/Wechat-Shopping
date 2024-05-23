package com.cskaoyan.market.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxHomeCouponVo {
    private short days;
    private String desc;
    private BigDecimal discount;
    private Integer id;
    private BigDecimal min;
    private String name;
    private String tag;
}
