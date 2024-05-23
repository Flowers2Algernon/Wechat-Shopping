package com.cskaoyan.market.shiro;

import com.cskaoyan.market.db.domain.MarketUser;
import com.cskaoyan.market.service.wx.WxAuthService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/18 15:24
 * @Version 1.0
 */
@Component
public class WxRealm extends AuthorizingRealm {

    @Autowired
    WxAuthService wxAuthService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //小程序用户认证
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        MarketUser user = wxAuthService.getByUsername(username);
        String password = "";
        if(user != null){
            password = user.getPassword();
        }
        //注意：此时，把整个用户信息放入到主体中，不是把用户名
        return new SimpleAuthenticationInfo(user, password, getName());
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //目前系统小程序用户是没有鉴权，只需要认证即可
        //如果今后当前系统像京东一样，会有不同的会员体系，不同的会员等级有不同的专属页面，那么到时候也需要进行鉴权
        // VIP1~VIP7
        return null;
    }
}

