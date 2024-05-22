package com.cskaoyan.market.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketAdminListVo {
    private Integer id;
    private String username;
    private String avatar;
    private Integer[] roleIds;
}
