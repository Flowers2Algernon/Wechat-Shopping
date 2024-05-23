package com.cskaoyan.market.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxHomeBrandVo {
    private String desc;
    private BigDecimal floorPrice;
    private Integer id;
    private String name;
    private String picUrl;
}
