package com.cskaoyan.market.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/6 9:47
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketRegionVo {

    private Integer id;

    private String name;

    private Byte type;

    private Integer code;

    private List<MarketRegionVo> children;
}
