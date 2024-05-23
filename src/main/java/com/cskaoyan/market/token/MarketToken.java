package com.cskaoyan.market.token;

import lombok.Data;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @ClassName MarketToken
 * @Description: TODO
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/4/1 11:05
 * @Version V1.0
 **/
@Data
public class MarketToken extends UsernamePasswordToken {

    //type的取值是admin或者wx
    private String type;

    //管理员登录的时候，type=admin；小程序用户登录的时候，type=wx
    public MarketToken(String username, String password, String type) {
        super(username, password);
        this.type = type;
    }
}
