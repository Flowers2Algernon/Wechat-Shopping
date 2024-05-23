package com.cskaoyan.market.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketTopicVo {
    private Integer id;
    private String picUrl;
    private BigDecimal price;
    private String readCount;
    private String subtitle;
    private String title;
}
