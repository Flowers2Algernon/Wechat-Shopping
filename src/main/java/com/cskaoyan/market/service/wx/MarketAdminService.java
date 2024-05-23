package com.cskaoyan.market.service.wx;

import com.cskaoyan.market.db.domain.MarketAdmin;

import java.util.List;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/22 10:25
 * @Version 1.0
 */
public interface MarketAdminService {
    MarketAdmin getByUsername(String username);

    List<String> getPermissionsByRoleId(Integer[] roleId);
}
