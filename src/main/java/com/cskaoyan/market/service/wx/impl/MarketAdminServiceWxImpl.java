package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.db.domain.MarketAdmin;
import com.cskaoyan.market.db.domain.MarketAdminExample;
import com.cskaoyan.market.db.domain.MarketPermission;
import com.cskaoyan.market.db.domain.MarketPermissionExample;
import com.cskaoyan.market.db.mapper.MarketAdminMapper;
import com.cskaoyan.market.db.mapper.MarketPermissionMapper;
import com.cskaoyan.market.service.wx.MarketAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/22 10:26
 * @Version 1.0
 */
@Service
public class MarketAdminServiceWxImpl implements MarketAdminService {

    @Autowired
    MarketAdminMapper adminMapper;

    @Autowired
    MarketPermissionMapper marketPermissionMapper;

    @Override
    public MarketAdmin getByUsername(String username) {
        MarketAdminExample marketAdminExample = new MarketAdminExample();
        marketAdminExample.createCriteria().andUsernameEqualTo(username);
        return adminMapper.selectOneByExample(marketAdminExample);
    }

    @Override
    public List<String> getPermissionsByRoleId(Integer[] roleId) {

        MarketPermissionExample marketPermissionExample = new MarketPermissionExample();
        MarketPermissionExample.Criteria criteria = marketPermissionExample.createCriteria();
        criteria.andDeletedEqualTo(false);

        List<MarketPermission> marketPermissions = marketPermissionMapper.selectByExample(marketPermissionExample);

        List<String> data = new ArrayList<>();
        for (int i = 0; i < marketPermissions.size(); i++) {
            for (int i1 = 0; i1 < roleId.length; i1++) {

                if(Objects.equals(marketPermissions.get(i).getRoleId(), roleId[i1])){
                    data.add(marketPermissions.get(i).getPermission());
                }

            }

        }

        return data;
    }
}
