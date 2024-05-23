package com.cskaoyan.market.service.wx;

import com.cskaoyan.market.db.domain.MarketComment;

import java.util.List;
import java.util.Map;

public interface WxCommentService {
   List list(String valueId, Integer limit, Integer page, Byte type, Byte showType);

    Map<String, Object> count(String valueId, Byte type);
    int getTotalCount(String valueId);
    boolean post(MarketComment marketComment);
}
