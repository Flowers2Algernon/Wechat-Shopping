package com.cskaoyan.market.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/22 10:13
 * @Version 1.0
 */
public class MarketToken extends UsernamePasswordToken {

    //领用这个type来标识究竟是小程序还是后台管理系统；如果是wx表示的是小程序；如果是admin表示的是后台管理系统
    private String type;

    public MarketToken(String username, String password, String type) {
        super(username, password);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
