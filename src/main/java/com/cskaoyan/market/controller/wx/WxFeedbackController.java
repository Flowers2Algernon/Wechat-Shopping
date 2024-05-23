package com.cskaoyan.market.controller.wx;

import com.cskaoyan.market.db.domain.MarketUser;
import com.cskaoyan.market.service.wx.WxFeedbackService;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: jyc
 * @Date: 2024/5/21 12:21
 */
@RestController
@RequestMapping("wx/feedback")
public class WxFeedbackController {
    @Autowired
    WxFeedbackService wxFeedbackService;
    @Autowired
    SecurityManager securityManager;

    @PostMapping("submit")
    @RequiresAuthentication
    public Object submit(@RequestBody Map<String,Object> map){

        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        MarketUser user = (MarketUser) session.getAttribute("user");

        String mobile = (String)map.get("mobile");
        String content = (String)map.get("content");
        String feedType = (String)map.get("feedType");
        Boolean hasPicture = (Boolean) map.get("hasPicture");
        List<String> picUrls = (List<String>) map.get("picUrls");
        Integer userId = user.getId();
        String username = user.getUsername();
        if(mobile.length()==11) {
            wxFeedbackService.submit(mobile,content,feedType,hasPicture,picUrls,userId,username);
            return ResponseUtil.ok();
        }else {
            return ResponseUtil.badArgument();
        }
    }

}
