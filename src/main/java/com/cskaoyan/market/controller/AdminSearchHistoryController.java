package com.cskaoyan.market.controller;

import com.cskaoyan.market.db.domain.MarketSearchHistory;
import com.cskaoyan.market.service.MarketSearchHistoryService;
import com.cskaoyan.market.util.JacksonUtil;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/history")
public class AdminSearchHistoryController {
    private final MarketSearchHistoryService historyService;

    @Autowired
    public AdminSearchHistoryController(MarketSearchHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/list")
    public Object list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @RequestParam(required = false) String userId,
                       @RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String sort,
                       @RequestParam(required = false) String order) {
        List<MarketSearchHistory> marketHistoryList = historyService.list(limit, page, userId, sort, order, keyword);
        return ResponseUtil.okList(marketHistoryList);
    }
}


