package com.cskaoyan.market.service.admin;

import com.cskaoyan.market.db.domain.MarketOrder;

import java.util.List;
import java.util.Map;

public interface AdminOrderService {
    boolean updateReply(String commentId, String content);

    List<MarketOrder> list(Integer page, Integer limit, String sort, String order, String orderId, String[] orderStatusArray, String start, String end, String userId, String orderSn);

    void refund(String orderId, String refundMoney);

    void ship(String orderId, String shipChannel, String shipSn);

    void delete(String orderId);

    Map<String, Object> detail(String id);
}
