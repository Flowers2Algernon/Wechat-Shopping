package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.annotation.RequiresPermissionsDesc;
import com.cskaoyan.market.db.domain.MarketFeedback;
import com.cskaoyan.market.service.admin.MarketFeedbackService;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/feedback")
public class AdminFeedbackController {
    private final MarketFeedbackService feedbackService;

    @Autowired
    public AdminFeedbackController(MarketFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @RequiresPermissions("admin:feedback:list")
    @RequiresPermissionsDesc(menu ={"用户管理","意见反馈"},button ="查询")
    @GetMapping("/list")
    public Object list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @RequestParam(required = false) String username,
                       @RequestParam(required = false) String id,
                       @RequestParam(required = false) String sort,
                       @RequestParam(required = false) String order) {
        List<MarketFeedback> marketFeedbackList = feedbackService.list(limit, page, username, sort, order, id);
        return ResponseUtil.okList(marketFeedbackList);
    }
}



