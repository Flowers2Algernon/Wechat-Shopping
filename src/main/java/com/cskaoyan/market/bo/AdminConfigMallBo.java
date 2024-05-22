package com.cskaoyan.market.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminConfigMallBo {
    private String market_mall_address;
    private String market_mall_latitude;
    private  String market_mall_longitude;
    private  String market_mall_name;
    private String market_mall_phone;
    private String market_mall_qq;

}
