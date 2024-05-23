package com.cskaoyan.market.controller.wx;

import com.cskaoyan.market.cloud.CloudService;
import com.cskaoyan.market.db.domain.MarketUser;
import com.cskaoyan.market.service.wx.WxAuthService;

import com.cskaoyan.market.shiro.MarketToken;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    @Autowired
    CloudService cloudService;

    @PostMapping("login")
    public Object login(@RequestBody Map map){
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return ResponseUtil.badArgument();
        }
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        MarketToken token = new MarketToken(username, password, "wx");
//        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
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

    private Map<String, String> mobileCodeMap = new HashMap<>();
    @PostMapping("regCaptcha")
    public Object regCaptcha(@RequestBody Map<String, String> mobiles) {

        String mobile = mobiles.get("mobile");
        String code = generateCode();
        mobileCodeMap.remove(mobile);
        mobileCodeMap.put(mobile, code);
        cloudService.sms(mobile, code);
        return null;
    }

    @PostMapping("verifyCode")
    public boolean verifyCode(@RequestBody Map<String, String> codeInfo) {
        String mobile = codeInfo.get("mobile");
        String inputCode = codeInfo.get("code");
        String storedCode = mobileCodeMap.get(mobile);
        if (storedCode != null && storedCode.equals(inputCode)) {
            mobileCodeMap.remove(mobile);
            return true;
        }
        return false;
    }

    //这个部分需要大家去实现，一般返回的是6位的数字
    private String generateCode() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    @PostMapping("register")
    public Object register(@RequestBody Map<String, String> codeInfo) {
        String mobile = codeInfo.get("mobile");
        String code = codeInfo.get("code");
        String password = codeInfo.get("password");
        String username = codeInfo.get("username");

        String storedCode = mobileCodeMap.get(mobile);

        Integer num = 3;

        if (storedCode != null && storedCode.equals(code)) {

           num = wxAuthService.registerAuth(mobile,code,password,username);
            if (num == 0) {
                mobileCodeMap.remove(mobile);

                return login(codeInfo);
            }


        }
        switch (num){
            case 0:
                return ResponseUtil.ok();
            case 1:
                return ResponseUtil.fail(704,"用户名重复");
            case 2:
                return ResponseUtil.fail(705,"手机号码已被注册");
            default:
                return ResponseUtil.badArgument();

        }

    }

    @PostMapping("reset")
    public Object reset(@RequestBody Map<String, String> codeInfo) {
        String mobile = codeInfo.get("mobile");
        String code = codeInfo.get("code");
        String password = codeInfo.get("password");


        String storedCode = mobileCodeMap.get(mobile);

        Integer num = 3;

        if (storedCode != null && storedCode.equals(code)) {

            num = wxAuthService.resetAuth(mobile,code,password);

            mobileCodeMap.remove(mobile);
        }
        switch (num){
            case 0:
                return ResponseUtil.ok();
            case 1:
                return ResponseUtil.fail(1,"未找到用户");
            default:
                return ResponseUtil.badArgument();
        }

    }
}

