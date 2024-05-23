package com.cskaoyan.market.shiro;

import com.cskaoyan.market.db.domain.MarketAdmin;
import com.cskaoyan.market.service.wx.MarketAdminService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/22 10:06
 * @Version 1.0
 */
@Component
public class AdminRealm extends AuthorizingRealm {

    @Autowired
    MarketAdminService adminService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //管理员认证----应该从market_admin表获取数据
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        MarketAdmin admin = adminService.getByUsername(username);
        String password = "";
        if(admin != null){
            password = admin.getPassword();
        }
        return new SimpleAuthenticationInfo(admin, password, getName());
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //这里面应该去返回当前用户具有的权限和角色信息
        MarketAdmin admin = (MarketAdmin) principals.getPrimaryPrincipal();
        Integer[] roleId = admin.getRoleIds();
        //应该到数据库中去查询当前用户的角色和权限信息
        List<String> permissions = adminService.getPermissionsByRoleId(roleId);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(permissions);
        return simpleAuthorizationInfo;
    }

}
