package com.cskaoyan.market.service.admin.impl;

import com.cskaoyan.market.db.domain.MarketRegion;
import com.cskaoyan.market.db.domain.MarketRegionExample;
import com.cskaoyan.market.db.mapper.MarketRegionMapper;
import com.cskaoyan.market.service.admin.MarketRegionService;
import com.cskaoyan.market.vo.MarketRegionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/6 9:39
 * @Version 1.0
 */
@Service
public class MarketRegionServiceImpl implements MarketRegionService {
    @Autowired
    MarketRegionMapper mapper;

    @Override
    public List<MarketRegionVo> list() {
        //是把整张表里面的数据全部查询出来了 里面包含了省、市、区
        List<MarketRegion> marketRegions = mapper.selectByExample(new MarketRegionExample());

        //需要获取省份数据，如何获取省份数据？？？？？最简单的方式是分组，按照pid来进行分组
        Map<Integer, List<MarketRegion>> regionMap = groupByPidRegion(marketRegions);


        //下面需要做的事情就是把实体对象转换成vo
        List<MarketRegionVo> marketRegionVoList = new ArrayList<>();

        List<MarketRegion> provinceList = regionMap.get(0);
        for (MarketRegion province : provinceList) {
            //省
            Integer provinceId = province.getId();
            List<MarketRegionVo> cityVOList = new ArrayList<>();

            MarketRegionVo provinceVo = new MarketRegionVo(provinceId, province.getName(), province.getType(), province.getCode(), cityVOList);
            //这个cities是当前省份下面城市
            List<MarketRegion> cities = regionMap.get(provinceId);
            for (MarketRegion city : cities) {
                //市
                Integer cityId = city.getId();
                //市下面有所有的区
                List<MarketRegionVo> areaVOList = new ArrayList<>();
                MarketRegionVo cityVO = new MarketRegionVo(cityId, city.getName(), city.getType(), city.getCode(), areaVOList);
                List<MarketRegion> areas = regionMap.get(cityId);
                for (MarketRegion area : areas) {
                    //区
                    areaVOList.add(new MarketRegionVo(area.getId(), area.getName(), area.getType(), area.getCode(), null));
                }
                //接收当前省下面的所有的城市信息
                cityVOList.add(cityVO);
            }

            //省vo放入list中
            marketRegionVoList.add(provinceVo);
        }

        return marketRegionVoList;
    }

    private Map<Integer, List<MarketRegion>> groupByPidRegion(List<MarketRegion> marketRegions) {
        Map<Integer, List<MarketRegion>> regionMap = new HashMap<>();
        for (MarketRegion region : marketRegions) {
            Integer pid = region.getPid();
            //session里面的购物车思想是一致的
            //先尝试从map中获取，对于第一次查询，肯定是null，创建一个list，放入map中
            List<MarketRegion> list = regionMap.get(pid);
            if(list == null){
                list = new ArrayList<>();
            }
            list.add(region);
            regionMap.put(pid, list);
        }
        return regionMap;
    }
}
