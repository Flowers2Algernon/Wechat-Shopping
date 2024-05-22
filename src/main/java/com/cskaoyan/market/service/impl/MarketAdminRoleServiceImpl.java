package com.cskaoyan.market.service.impl;

import com.cskaoyan.market.db.domain.MarketPermission;
import com.cskaoyan.market.db.domain.MarketPermissionExample;
import com.cskaoyan.market.db.domain.MarketRole;
import com.cskaoyan.market.db.domain.MarketRoleExample;
import com.cskaoyan.market.db.mapper.MarketPermissionMapper;
import com.cskaoyan.market.db.mapper.MarketRoleMapper;
import com.cskaoyan.market.service.MarketAdminRoleService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public void postPermissions(Integer roleId, Map<Integer, String> permissions) {
        Set<Map.Entry<Integer, String>> entries = permissions.entrySet();
        for (Map.Entry<Integer, String> entry : entries) {
            MarketPermission marketPermission = new MarketPermission();
            marketPermission.setPermission(entry.getValue());
            marketPermission.setRoleId(roleId);
            marketPermissionMapper.insert(marketPermission);
        }
    }

    @Override
    public LinkedHashMap<Integer,String> getPermissions(Integer roleId) {
        MarketPermissionExample marketPermissionExample = new MarketPermissionExample();
        MarketPermissionExample.Criteria criteria = marketPermissionExample.createCriteria();
        criteria.andRoleIdEqualTo(roleId);
        List<MarketPermission> marketPermissions = marketPermissionMapper.selectByExample(marketPermissionExample);
        LinkedHashMap<Integer, String> integerStringLinkedHashMap = new LinkedHashMap<>();
        int count = 0;
        for (MarketPermission marketPermission : marketPermissions) {
            integerStringLinkedHashMap.put(count,marketPermission.getPermission());
            count++;
        }
        return integerStringLinkedHashMap;
    }
}
