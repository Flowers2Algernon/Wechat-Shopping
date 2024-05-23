package com.cskaoyan.market.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 薛松 xuesong_work@163.com
 * @Date: 2024/05/22/19:59
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSubmitVo {
    private Integer cartId;
    private Integer addressId;
    private Integer couponId;
    private Integer userCouponId;
    private String message;
    private Integer grouponRulesId;
    private Integer grouponLinkId;
}
