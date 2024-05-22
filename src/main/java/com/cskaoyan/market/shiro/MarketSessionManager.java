package com.cskaoyan.market.shiro;

import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
@Component
public class MarketSessionManager extends DefaultWebSessionManager {
    private static final String WX_TOKEN = "X-Cskaoyanmarket-Admin-Token";

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
