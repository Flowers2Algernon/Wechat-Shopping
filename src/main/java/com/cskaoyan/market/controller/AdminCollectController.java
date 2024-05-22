package com.cskaoyan.market.controller;

import com.cskaoyan.market.db.domain.MarketCollect;
import com.cskaoyan.market.service.MarketCollectService;
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
@RequestMapping("/admin/collect")
public class AdminCollectController {
    private final MarketCollectService collectService;

    @Autowired
    public AdminCollectController(MarketCollectService collectService) {
        this.collectService = collectService;
    }

    @GetMapping("/list")
    public Object list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @RequestParam(required = false) String userId,
                       @RequestParam(required = false) String valueId,
                       @RequestParam(required = false) String sort,
                       @RequestParam(required = false) String order) {
        List<MarketCollect> marketCollectList = collectService.list(limit, page, userId, sort, order, valueId);
        return ResponseUtil.okList(marketCollectList);
    }
}