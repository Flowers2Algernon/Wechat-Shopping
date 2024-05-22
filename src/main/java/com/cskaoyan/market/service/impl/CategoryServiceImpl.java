package com.cskaoyan.market.service.impl;

import com.cskaoyan.market.bo.DeleteCategoryBo;
import com.cskaoyan.market.bo.UpdateCategoryBo;
import com.cskaoyan.market.db.domain.MarketCategory;
import com.cskaoyan.market.db.domain.MarketCategoryExample;
import com.cskaoyan.market.db.mapper.MarketCategoryMapper;
import com.cskaoyan.market.service.CategoryService;
import com.cskaoyan.market.vo.L1Vo;
import com.cskaoyan.market.vo.ListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    MarketCategoryMapper marketCategoryMapper;
    @Override
    public List<ListVo> list() {
        // SqlSession session = MybatisUtils.getSession();
        // MarketCategoryMapper categoryMapper = session.getMapper(MarketCategoryMapper.class);
        List<MarketCategory> marketCategories = marketCategoryMapper.selectByExample(new MarketCategoryExample());

        // session.close();

        Map<Integer, List<MarketCategory>> categoriesData = groupByPid(marketCategories);
        List<ListVo> categoryList = new ArrayList<>();
        List<MarketCategory> l1Categories = categoriesData.get(0);

        try {
            for (MarketCategory l1 : l1Categories) {
                ListVo l1CateVo = new ListVo(l1.getDesc(),l1.getIconUrl(),l1.getId(),l1.getKeywords(),l1.getLevel(),l1.getName(),l1.getPicUrl(),null);
                List<ListVo> childrenOfl1Vo = new ArrayList<>();
                l1CateVo.setChildren(childrenOfl1Vo);

                List<MarketCategory> l2CategoriesOfl1 = categoriesData.get(l1.getId());
                if(l2CategoriesOfl1!=null&&l2CategoriesOfl1.size()>0){
                    for (MarketCategory l2 : l2CategoriesOfl1) {
                    childrenOfl1Vo.add(new ListVo(l2.getDesc(),l2.getIconUrl(),l2.getId(),l2.getKeywords(),l2.getLevel(),l2.getName(), l2.getPicUrl(), null));
                }
                }
                categoryList.add(l1CateVo);
                System.out.println(l2CategoriesOfl1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String,Object> listMap = new HashMap<>();
        listMap.put("limit",16);
        listMap.put("list",categoryList);
        listMap.put("page",1);
        listMap.put("pages",1);
        listMap.put("total",16);

        return categoryList;
    }

    private Map<Integer, List<MarketCategory>> groupByPid(List<MarketCategory> marketCategories) {
        //新建一个map
        Map<Integer, List<MarketCategory>> categoriesData = new HashMap<>();
        //整体的思路是：遍历类目集合，迭代出来每一个类目，尝试从map中查找数据，如果找不到，则创建一个list，用这个list来存储当前的类目，放入map中，下一次，相同的类目pid编号便可以获取到list了
        for (MarketCategory category : marketCategories) {
            List<MarketCategory> list = categoriesData.get(category.getPid());
            if(list == null){
                //当前map中没有对应的数据
                list = new ArrayList<>();
            }
            list.add(category);
            categoriesData.put(category.getPid(), list);
        }
        return categoriesData;
    }

    @Override
    public List<L1Vo> l1() {
        // SqlSession session = MybatisUtils.getSession();
        // MarketCategoryMapper categoryMapper = session.getMapper(MarketCategoryMapper.class);
        List<MarketCategory> marketCategories = marketCategoryMapper.selectByExample(new MarketCategoryExample());
        // session.close();

        List<L1Vo> l1VoList = new ArrayList<>();

        Map<Integer, List<MarketCategory>> categoriesData = groupByPid(marketCategories);
        List<MarketCategory> l1Categories = categoriesData.get(0);
        for (MarketCategory l1 : l1Categories) {
            L1Vo l1Vo = new L1Vo(l1.getName(), l1.getId());
            l1VoList.add(l1Vo);
        }
        Map<String,Object> l1Map = new HashMap<>();
        l1Map.put("limit",16);
        l1Map.put("list",l1VoList);
        l1Map.put("page",1);
        l1Map.put("pages",1);
        l1Map.put("total",16);
        return l1VoList;
    }

    @Override
    public MarketCategory insertOne(MarketCategory marketCategory) {
        // SqlSession session = MybatisUtils.getSession();
        // MarketCategoryMapper categoryMapper = session.getMapper(MarketCategoryMapper.class);
        marketCategoryMapper.insert(marketCategory);
        // session.commit();
        // session.close();
        return null;
    }

    @Override
    public void update(UpdateCategoryBo updateCategoryBo) {

        // SqlSession session = MybatisUtils.getSession();
        // MarketCategoryMapper categoryMapper = session.getMapper(MarketCategoryMapper.class);
        MarketCategoryExample marketCategoryExample = new MarketCategoryExample();
        MarketCategoryExample.Criteria criteria = marketCategoryExample.createCriteria();
        Integer id = updateCategoryBo.getId();
        if(!StringUtils.isEmpty(Integer.toString(id))){
            criteria.andIdEqualTo(id);
        }
        List<MarketCategory> marketCategories = marketCategoryMapper.selectByExampleSelective(marketCategoryExample);
        MarketCategory marketCategory = marketCategories.get(0);
        marketCategory.setDesc(updateCategoryBo.getDesc());
        marketCategory.setIconUrl(updateCategoryBo.getIconUrl());
        marketCategory.setKeywords(updateCategoryBo.getKeywords());
        marketCategory.setLevel(updateCategoryBo.getLevel());
        marketCategory.setName(updateCategoryBo.getName());
        marketCategory.setPicUrl(updateCategoryBo.getPicUrl());

        marketCategoryMapper.updateByExample(marketCategory,marketCategoryExample);
    //     session.commit();
    //     session.close();
    }

    @Override
    public void delete(DeleteCategoryBo deleteCategoryBo) {
        // SqlSession session = MybatisUtils.getSession();
        // MarketCategoryMapper categoryMapper = session.getMapper(MarketCategoryMapper.class);
        MarketCategoryExample marketCategoryExample = new MarketCategoryExample();
        MarketCategoryExample.Criteria criteria = marketCategoryExample.createCriteria();
        String id = deleteCategoryBo.getId().toString();
        if(!StringUtils.isEmpty(id)){
            criteria.andIdEqualTo(Integer.parseInt(id));
        }
        marketCategoryMapper.deleteByExample(marketCategoryExample);
        // session.commit();
        // session.close();
    }
}
