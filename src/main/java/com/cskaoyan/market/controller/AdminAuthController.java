package com.cskaoyan.market.controller;

import com.cskaoyan.market.token.MarketToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName AdminAuthController
 * @Description: TODO
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/4/1 11:18
 * @Version V1.0
 **/
@RestController
@RequestMapping("admin/auth")
public class AdminAuthController {

    @Autowired
    SecurityManager securityManager;

    @PostMapping("login")
    public Object login(){
        //下面两行代码的作用是将securityManager和subject产生关联
        //因为下面subject.login()会进一步调用securityManager，所以二者需要关联在一起
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new MarketToken("admin123", "admin123", "admin"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
