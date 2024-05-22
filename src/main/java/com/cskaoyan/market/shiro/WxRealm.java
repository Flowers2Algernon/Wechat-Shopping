package com.cskaoyan.market.shiro;

import com.cskaoyan.market.db.domain.MarketUser;
import com.cskaoyan.market.service.WxAuthService;
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
        return null;
    }


}
