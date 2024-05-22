package com.cskaoyan.market.controller;

import com.cskaoyan.market.db.domain.MarketFootprint;
import com.cskaoyan.market.service.MarketFootprintService;
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
@RequestMapping("/admin/footprint")
public class AdminFootprintController {
    private final MarketFootprintService footprintService;

    @Autowired
    public AdminFootprintController(MarketFootprintService footprintService) {
        this.footprintService = footprintService;
    }

    @GetMapping("/list")
    public Object list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @RequestParam(required = false) String userId,
                       @RequestParam(required = false) String goodsId,
                       @RequestParam(required = false) String sort,
                       @RequestParam(required = false) String order) {
        List<MarketFootprint> marketFootprintList = footprintService.list(limit, page, userId, sort, order, goodsId);
        return ResponseUtil.okList(marketFootprintList);
    }
}
