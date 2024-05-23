package com.cskaoyan.market.vo;

import com.cskaoyan.market.db.domain.MarketOrderGoods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 薛松 xuesong_work@163.com
 * @Date: 2024/05/21/11:35
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVo {
    private List<Integer> expressInfo;

    private Map<String,Object> orderInfo;

    private List<MarketOrderGoods>orderGoods;

}
