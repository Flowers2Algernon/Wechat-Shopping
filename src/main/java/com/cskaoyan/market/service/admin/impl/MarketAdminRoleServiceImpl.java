package com.cskaoyan.market.service.admin.impl;

import com.cskaoyan.market.db.domain.MarketPermission;
import com.cskaoyan.market.db.domain.MarketPermissionExample;
import com.cskaoyan.market.db.domain.MarketRole;
import com.cskaoyan.market.db.domain.MarketRoleExample;
import com.cskaoyan.market.db.mapper.MarketPermissionMapper;
import com.cskaoyan.market.db.mapper.MarketRoleMapper;
import com.cskaoyan.market.service.admin.MarketAdminRoleService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MarketAdminRoleServiceImpl implements MarketAdminRoleService {
    @Autowired
    MarketRoleMapper marketRoleMapper;
    @Autowired
    MarketPermissionMapper marketPermissionMapper;
    @Override
    public List<MarketRole> list(Integer page, Integer limit, String name, String sort, String order) {
        MarketRoleExample marketRoleExample = new MarketRoleExample();
        MarketRoleExample.Criteria criteria = marketRoleExample.createCriteria();
        if (!StringUtils.isEmpty(name)){
            criteria.andNameEqualTo(name);
        }
        PageHelper.startPage(page,limit);
        List<MarketRole> marketRoles = marketRoleMapper.selectByExample(marketRoleExample);
        return marketRoles;
    }

    @Override
    public MarketRole create(String name, String desc) {
        MarketRole role = new MarketRole();
        role.setName(name);
        role.setAddTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        role.setDesc(desc);
        MarketRoleExample marketRoleExample = new MarketRoleExample();
        List<MarketRole> marketRoles = marketRoleMapper.selectByExample(marketRoleExample);
        role.setId(marketRoles.size()+1);
        marketRoleMapper.insertSelective(role);
        return role;
    }

    @Override
    public void delete(Integer id) {
        marketRoleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Integer id, String name, String desc) {
        MarketRole role = marketRoleMapper.selectByPrimaryKey(id);
        role.setName(name);
        role.setDesc(desc);
        marketRoleMapper.updateByPrimaryKey(role);
    }

    @Override
    public void postPermissions(Integer roleId, List<String> permissions) {
        MarketPermissionExample marketPermissionExample = new MarketPermissionExample();
        MarketPermissionExample.Criteria criteria = marketPermissionExample.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        marketPermissionMapper.deleteByExample(marketPermissionExample);
        for (String permission : permissions) {
            MarketPermission marketPermission = new MarketPermission();
            marketPermission.setPermission(permission);
            marketPermission.setAddTime(LocalDateTime.now());
            marketPermission.setUpdateTime(LocalDateTime.now());
            marketPermission.setDeleted(false);
            marketPermission.setRoleId(roleId);
            marketPermissionMapper.insert(marketPermission);
        }
    }

    @Override
    public List<String> getPermissions(Integer roleId) {
        MarketPermissionExample marketPermissionExample = new MarketPermissionExample();
        MarketPermissionExample.Criteria criteria = marketPermissionExample.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        List<MarketPermission> marketPermissions = marketPermissionMapper.selectByExample(marketPermissionExample);
        List<String> assignedPermissionList = new ArrayList<>();
        for (MarketPermission marketPermission : marketPermissions) {
            assignedPermissionList.add(marketPermission.getPermission());
        }
        return assignedPermissionList;
    }

}
