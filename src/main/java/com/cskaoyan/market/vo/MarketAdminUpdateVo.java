package com.cskaoyan.market.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketAdminUpdateVo {
    private Integer id;
    private String username;
    private String avatar;
    private Integer[] roleIds;
    private String password;
}
