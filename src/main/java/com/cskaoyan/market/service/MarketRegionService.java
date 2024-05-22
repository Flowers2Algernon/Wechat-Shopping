package com.cskaoyan.market.service;

import com.cskaoyan.market.vo.MarketRegionVo;

import java.util.List;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/6 9:39
 * @Version 1.0
 */
public interface MarketRegionService {

    //关于当前接口的实现，捏能够想到的有几种实现方式？？？？
    //1.先查询省，省for迭代，去查询市，市数据for迭代，再查询区-------------------频繁查询数据库
    //2.进行一次查询，查询出全部的数据；分类：省、市、区数据，再组装
    //3.进行一次查询，查询出全部的数据，但是此时不分类，先把省数据填充好，全量循环处理
    //写多个service实现类，在不同的实现类中编写不同的方案
    List<MarketRegionVo> list();
}
