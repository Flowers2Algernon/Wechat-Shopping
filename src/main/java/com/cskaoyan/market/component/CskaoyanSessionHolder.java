package com.cskaoyan.market.component;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/18 10:21
 * @Version 1.0
 */
@Component
public class CskaoyanSessionHolder {

    //自己实现会话技术
    private static Map<String, HttpSession> sessionMap = new HashMap<>();

    public void putSession(String sessionId, HttpSession session){
        sessionMap.put(sessionId, session);
    }

    public HttpSession getSession(String sessionId){
        return sessionMap.get(sessionId);
    }
}
