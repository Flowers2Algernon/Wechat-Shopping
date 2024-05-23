package com.cskaoyan.market.controller;

import com.cskaoyan.market.component.CskaoyanSessionHolder;
import com.cskaoyan.market.db.domain.MarketUser;
import com.cskaoyan.market.service.WxAuthService;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/18 9:49
 * @Version 1.0
 */
@RestController
@RequestMapping("wx/auth")
public class WxAuthController {

    @Autowired
    WxAuthService wxAuthService;

    @Autowired
    SecurityManager securityManager;


    @PostMapping("login")
    public Object login(@RequestBody Map map){
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return ResponseUtil.badArgument();
        }
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
        }catch (Exception e){
            return ResponseUtil.fail(404, "用户名密码不匹配");
        }

        MarketUser user = (MarketUser) subject.getPrincipal();

        Session session = subject.getSession();
        session.setAttribute("user", user);
        Map<String, Object> data = new HashMap<>();
        data.put("token", session.getId());

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("nickName", user.getNickname());
        userInfo.put("avatarUrl", user.getAvatar());
        data.put("userInfo", userInfo);
        return ResponseUtil.ok(data);
    }
    @PostMapping("logout")
    public Object logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResponseUtil.ok();
    }
}
