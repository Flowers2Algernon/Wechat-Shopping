package com.cskaoyan.market.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 薛松 xuesong_work@163.com
 * @Date: 2024/05/21/21:29
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderGoodsVo {
    private LocalDateTime addTime;
    private Integer comment;
    private Boolean deleted;
    private Integer goodsId;
    private String goodsName;
    private String goodsSn;
    private Integer id;
    private short number;
    private Integer orderId;
    private String picUrl;
    private BigDecimal price;
    private Integer productId;
    private String[] specifications;
    private LocalDateTime updateTime;
}
