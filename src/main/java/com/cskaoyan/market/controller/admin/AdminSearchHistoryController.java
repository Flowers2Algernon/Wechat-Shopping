package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.annotation.RequiresPermissionsDesc;
import com.cskaoyan.market.db.domain.MarketSearchHistory;
import com.cskaoyan.market.service.admin.MarketSearchHistoryService;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

    @RequiresPermissions("admin:history:list")
    @RequiresPermissionsDesc(menu ={"用户管理","搜索历史"},button ="查询")
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


