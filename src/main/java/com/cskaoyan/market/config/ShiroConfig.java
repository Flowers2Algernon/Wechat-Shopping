package com.cskaoyan.market.config;

import com.cskaoyan.market.shiro.AdminRealm;
import com.cskaoyan.market.shiro.MarketAuthenticator;
import com.cskaoyan.market.shiro.MarketSessionManager;
import com.cskaoyan.market.shiro.WxRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/18 15:22
 * @Version 1.0
 */
@Configuration
public class ShiroConfig {

    //需要关联authenticator和realm之间的关系
    //虽然使用@Component注解也可以向容器中去注册authenticator，但是我们需要将realm和认证器进行关联
    //此时使用@Bean注解更方便一些
    @Bean
    public MarketAuthenticator marketAuthenticator(AdminRealm adminRealm, WxRealm wxRealm){
        List<Realm> realms = new ArrayList<>();
        realms.add(wxRealm);
        realms.add(adminRealm);
        MarketAuthenticator marketAuthenticator = new MarketAuthenticator();
        marketAuthenticator.setRealms(realms);
        return marketAuthenticator;
    }


    //将认证器和securitymanager进行关联
    @Bean
    public SecurityManager securityManager(AdminRealm adminRealm, MarketSessionManager sessionManager,MarketAuthenticator marketAuthenticator){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(sessionManager);
        //还需要设置一个默认的realm，用来进行鉴权的时候使用
        securityManager.setRealm(adminRealm);
        //关联一个认证器
        securityManager.setAuthenticator(marketAuthenticator);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        //此时shiroFilter就不需要再设置map了
        return factoryBean;
    }

    //注解式鉴权需要使用到当前的组件对象
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
