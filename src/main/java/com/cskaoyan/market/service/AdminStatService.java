package com.cskaoyan.market.service;

import java.util.List;
import java.util.Map;

/**
 * @Author: jyc
 * @Date: 2024/5/8 9:12
 */

public interface AdminStatService {
    List<Map<String,String>> getUserRows();

    List<Map<String, String>> getOrderRows();

    List<Map<String, String>> getGoodsRows();
}
