package com.cskaoyan.market.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketCouponUserVo {
    private boolean available;
    private Integer cid;
    private String desc;
    private BigDecimal discount;
    private LocalDateTime endTime;
    private Integer id;
    private BigDecimal min;
    private String name;
    private LocalDateTime startTime;
    private String tag;
}
