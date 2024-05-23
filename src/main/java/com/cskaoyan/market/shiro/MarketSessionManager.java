package com.cskaoyan.market.shiro;

import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/18 15:25
 * @Version 1.0
 */
@Component
public class MarketSessionManager extends DefaultWebSessionManager {

    private static final String WX_TOKEN = "X-CskaoyanMarket-Token";

    //重写shiro里面的session获取编号的方式，之前没有重写之前是利用Cookie:JSESSIONID=xx来获取
    //重写过之后可以允许获取任意请求头里面的值来作为session的编号
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String header = httpRequest.getHeader(WX_TOKEN);
        if(header != null){
            return header;
        }
        return super.getSessionId(request, response);
    }
}
