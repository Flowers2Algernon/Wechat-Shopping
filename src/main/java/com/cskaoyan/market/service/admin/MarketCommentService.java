package com.cskaoyan.market.service.admin;

import com.cskaoyan.market.db.domain.MarketComment;

import java.util.List;

public interface MarketCommentService {
    //http://localhost:8083/admin/comment/list?
    // page=1&limit=20&userId=1&sort=add_time&order=desc
    List<MarketComment> list(Integer page,Integer limit, String userId,String valueId,String sort, String order);
    void delete(Integer id);
    int getTotalCount(String userId, String valueId);
}
