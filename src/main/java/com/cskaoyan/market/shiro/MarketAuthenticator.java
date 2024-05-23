package com.cskaoyan.market.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/22 10:11
 * @Version 1.0
 */
//此处我不打算使用@Component注解的方式向容器中去注册组件，而是使用@Bean注解，有什么好处吗？
//需要对于authenticator进行设置，和realms进行关联
//@Component
public class MarketAuthenticator extends ModularRealmAuthenticator {

    //重写认证逻辑
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        //在认证器中需要做的事情便是识别出当前的认证请求是小程序的还是后台管理系统的
        //需求：如果是管理员登录，则进入到adminRealm中；如果是小程序用户登录，则进入到wxRealm中
        MarketToken token = (MarketToken) authenticationToken;
        String type = token.getType();
        //type的取值是wx或者admin；如果是wx，说明是小程序用户，应该分发到wxRealm；如果是admin，说明是后台管理系统，需要分发到adminRealm中
        //现在需要做的事情便是获取realms集合
        List<Realm> candidates = new ArrayList<>();
        Collection<Realm> realms = getRealms();
        for (Realm realm : realms) {
            if(realm.getName().toLowerCase().contains(type.toLowerCase())){
                //如果realm的名称中包含了type的值，那么便是我们需要去处理的realm
                candidates.add(realm);
            }
        }
        if (candidates.size() == 1) {
            return doSingleRealmAuthentication(candidates.iterator().next(), token);
        } else {
            return doMultiRealmAuthentication(candidates, token);
        }
    }
}
