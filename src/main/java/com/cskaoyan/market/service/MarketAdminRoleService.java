package com.cskaoyan.market.service;

import com.cskaoyan.market.db.domain.MarketRole;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface MarketAdminRoleService {
   List<MarketRole> list(Integer page,Integer limit,String name,String sort,String order);

   MarketRole create(String name, String desc);

   void delete(Integer id);

   void update(Integer id, String name, String desc);

   void postPermissions(Integer roleId, Map<Integer, String> permissions);

   LinkedHashMap<Integer,String> getPermissions(Integer roleId);
}
