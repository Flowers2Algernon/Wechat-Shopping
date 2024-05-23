package com.cskaoyan.market.vo;
import com.cskaoyan.market.db.domain.MarketOrderGoods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 薛松 xuesong_work@163.com
 * @Date: 2024/05/20/20:09
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderListVo {
    private BigDecimal actualPrice;

    private short aftersaleStatus;

    private List<MarketOrderGoods> goodsList;

    private Map<String,Boolean>handleOperation;

    private Integer id;

    private Boolean isGroupin;

    private Boolean deleted;

    private String orderSn;

    private String orderStatusText;
}
