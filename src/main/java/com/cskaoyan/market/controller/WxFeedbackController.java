package com.cskaoyan.market.controller;

import com.cskaoyan.market.util.ResponseUtil;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: jyc
 * @Date: 2024/5/21 12:21
 */
@RestController
@RequestMapping("wx/feedback")
public class WxFeedbackController {

    @PostMapping("submit")
    @RequiresAuthentication
    public Object submit(){
        return ResponseUtil.ok();
    }

}
