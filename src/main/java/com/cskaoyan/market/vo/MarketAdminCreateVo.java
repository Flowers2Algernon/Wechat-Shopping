package com.cskaoyan.market.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketAdminCreateVo {
    private Integer id;
    private String username;
    private String avatar;
    private Integer[] roleIds;
    private String password;
    private LocalDateTime addTime;
    private LocalDateTime updateTime;
}
