package com.cskaoyan.market.service;

import com.cskaoyan.market.db.domain.MarketFeedback;

import java.util.List;

public interface MarketFeedbackService {
    List<MarketFeedback> list(Integer limit, Integer page, String username, String sort, String order, String id);
}
