package com.cskaoyan.market.service.admin.impl;

import com.cskaoyan.market.db.domain.MarketRoleLabelOptions;
import com.cskaoyan.market.service.admin.AdminRoleOptionsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminRoleOptionsServiceImpl1 implements AdminRoleOptionsService {
    @Override
    public List<MarketRoleLabelOptions> options() {List<MarketRoleLabelOptions> list = new ArrayList<>();
       list.add(new MarketRoleLabelOptions(1,"超级管理员"));
       list.add(new MarketRoleLabelOptions(2,"商场管理员"));
       list.add(new MarketRoleLabelOptions(3,"推广管理员"));
       list.add(new MarketRoleLabelOptions(10,"啦啦啦"));
       return list;
    }
}
