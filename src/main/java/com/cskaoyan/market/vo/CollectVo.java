package com.cskaoyan.market.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectVo {
    private String brief;
    private Integer id;
    private  String name;
    private String picUrl;
    private BigDecimal retailPrice;
    private Byte type;
    private String valueId;
}
