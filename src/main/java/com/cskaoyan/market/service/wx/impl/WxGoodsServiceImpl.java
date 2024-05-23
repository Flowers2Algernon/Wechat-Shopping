package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.db.domain.*;
import com.cskaoyan.market.db.mapper.*;
import com.cskaoyan.market.service.wx.WxGoodsService;
import com.cskaoyan.market.util.ResponseUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WxGoodsServiceImpl implements WxGoodsService {
    @Autowired
    MarketCategoryMapper marketCategoryMapper;
    @Autowired
    MarketGoodsMapper marketGoodsMapper;
    @Autowired
    MarketGoodsAttributeMapper marketGoodsAttributeMapper;
    @Autowired
    MarketBrandMapper marketBrandMapper;
    @Autowired
    MarketCommentMapper marketCommentMapper;
    @Autowired
    MarketOrderGoodsMapper marketOrderGoodsMapper;
    @Autowired
    MarketIssueMapper marketIssueMapper;
    @Autowired
    MarketGoodsProductMapper marketGoodsProductMapper;
    @Autowired
    MarketGoodsSpecificationMapper marketGoodsSpecificationMapper;
    @Autowired
    MarketCollectMapper marketCollectMapper;
    @Autowired
    MarketSearchHistoryMapper marketSearchHistoryMapper;
    @Autowired
    SecurityManager securityManager;
    @Autowired
    MarketFootprintMapper marketFootprintMapper;
    @Override
    public Object category(Integer id) {
        Map<String,Object> map = new HashMap<>();
        //根据前端传入的id信息查找到对应的marketCategory对象
        MarketCategoryExample marketCategoryExample = new MarketCategoryExample();
        MarketCategoryExample.Criteria criteria1 = marketCategoryExample.createCriteria();
        criteria1.andIdEqualTo(id);
        List<MarketCategory> marketCategories = marketCategoryMapper.selectByExample(marketCategoryExample);
        MarketCategory Category = marketCategories.get(0);
        Integer pid = Category.getPid();

        if(pid==0){
            //说明传入的是一级目录的id
            MarketCategoryExample marketCategoryExample2 = new MarketCategoryExample();
            MarketCategory parent = marketCategoryMapper.selectByExample(marketCategoryExample2).get(0);

            MarketCategoryExample.Criteria criteriaL1 = marketCategoryExample2.createCriteria();
            criteriaL1.andPidEqualTo(id);
            List<MarketCategory> brotherCategoriesL1 = marketCategoryMapper.selectByExample(marketCategoryExample2);

            MarketCategory currentCategoryL1 = brotherCategoriesL1.get(0);
            map.put("brotherCategory",brotherCategoriesL1);
            map.put("currentCategory",currentCategoryL1);
            map.put("parentCategory",parent);
        }
        if(pid!=0){
            MarketCategoryExample marketCategoryExample3 = new MarketCategoryExample();
            MarketCategoryExample.Criteria criteria3 = marketCategoryExample3.createCriteria();
            criteria3.andPidEqualTo(pid);
            List<MarketCategory> brotherCategory = marketCategoryMapper.selectByExampleSelective(marketCategoryExample3);

            MarketCategoryExample marketCategoryExample4 = new MarketCategoryExample();
            MarketCategoryExample.Criteria criteria2 = marketCategoryExample4.createCriteria();
            criteria2.andIdEqualTo(pid);
            List<MarketCategory> parents = marketCategoryMapper.selectByExample(marketCategoryExample4);
            MarketCategory parentCategory = parents.get(0);

            map.put("brotherCategory",brotherCategory);
            map.put("currentCategory",Category);
            map.put("parentCategory",parentCategory);
        }
        return ResponseUtil.ok(map);
    }

    @Override
    public Object list(String keyword,Integer categoryId, Integer brandId,Integer page, Integer limit,
                       String sort,String order,Boolean isNew,Boolean isHot) {
        Map<String, Object> data = new HashMap<String, Object>();
        if(categoryId!=null&&categoryId!=0) {
            List<MarketCategory> filterCategoryList = new ArrayList<>();

            MarketCategoryExample marketCategoryExample = new MarketCategoryExample();
            List<MarketCategory> marketCategories = marketCategoryMapper.selectByExample(marketCategoryExample);

            MarketGoodsExample marketGoodsExample = new MarketGoodsExample();
            PageHelper.startPage(page, limit);
            List<MarketGoods> marketGoods = marketGoodsMapper.selectByExample(marketGoodsExample);
                //使用分页插件
                Page page1 = (Page) marketGoods;
                data.put("total", page1.getTotal());
                data.put("page", page1.getPageNum());
                data.put("limit", page1.getPageSize());
                data.put("pages", page1.getPages());

            MarketGoodsExample marketGoodsExample1 = new MarketGoodsExample();
            MarketGoodsExample.Criteria criteria = marketGoodsExample1.createCriteria();
            criteria.andCategoryIdEqualTo(categoryId);
            List<MarketGoods> marketGoods1 = marketGoodsMapper.selectByExample(marketGoodsExample1);

            data.put("list", marketGoods1);

            MarketGoodsExample marketGoodsExample2 = new MarketGoodsExample();
            List<MarketGoods> marketGoods2 = marketGoodsMapper.selectByExample(marketGoodsExample2);
            //此处filterCategoryList为废案，只在keyword中有用，此处懒得改了
            for (MarketCategory marketCategory : marketCategories) {
                Integer id = marketCategory.getId();
                if (id != 0) {
                    List<MarketGoods> goodsList = new ArrayList<>();
                    for (MarketGoods goods : marketGoods2) {
                        if (goods.getCategoryId() == id) {
                            goodsList.add(goods);
                        }
                    }
                    if (goodsList != null) {
                        filterCategoryList.add(marketCategory);
                    }
                }
            }
            data.put("filterCategoryList", filterCategoryList);
        }
        if(brandId!=null){
            List<MarketCategory> filterCategoryList = new ArrayList<>();
            MarketCategoryExample marketCategoryExample1 = new MarketCategoryExample();
            MarketCategoryExample.Criteria criteriaCategory = marketCategoryExample1.createCriteria();
            criteriaCategory.andPidEqualTo(1005000);
            filterCategoryList = marketCategoryMapper.selectByExample(marketCategoryExample1);
            data.put("filterCategoryList",filterCategoryList);
            MarketGoodsExample marketGoodsExampleBrand = new MarketGoodsExample();
            MarketGoodsExample.Criteria criteriaBrand = marketGoodsExampleBrand.createCriteria();
            criteriaBrand.andBrandIdEqualTo(brandId);
            PageHelper.startPage(page, limit);
            List<MarketGoods> marketGoods1 = marketGoodsMapper.selectByExample(marketGoodsExampleBrand);
            data.put("list",marketGoods1);
            Page page2 = (Page) marketGoods1;
            data.put("total", page2.getTotal());
            data.put("page", page2.getPageNum());
            data.put("limit", page2.getPageSize());
            data.put("pages", page2.getPages());
        }
        if(keyword!=null){
            List<MarketCategory> filterCategoryListOld = new ArrayList<>();

            // String decodeKeyword = null;
            // try {
            //     decodeKeyword = URLDecoder.decode(keyword, "UTF-8");
            // } catch (UnsupportedEncodingException e) {
            //     throw new RuntimeException(e);
            // }

            MarketGoodsExample marketGoodsExample = new MarketGoodsExample();
            marketGoodsExample.setOrderByClause(sort + " " +order);
            MarketGoodsExample.Criteria criteria = marketGoodsExample.createCriteria();
            criteria.andNameLike("%" + keyword + "%");
            PageHelper.startPage(page,limit);
            List<MarketGoods> list = marketGoodsMapper.selectByExample(marketGoodsExample);

            // SecurityUtils.setSecurityManager(securityManager);
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession();
            MarketUser user = (MarketUser) session.getAttribute("user");
            if(user==null){
                return ResponseUtil.fail(666,"请登录哟");
            }
            Integer userId = user.getId();
            MarketSearchHistoryExample marketSearchHistoryExample = new MarketSearchHistoryExample();
            List<MarketSearchHistory> searchHistories = marketSearchHistoryMapper.selectByExample(marketSearchHistoryExample);
            int count = 0;
            for (MarketSearchHistory searchHistory : searchHistories) {
                if(searchHistory.getKeyword().equals(keyword)&&searchHistory.getDeleted().equals(false)){
                    count++;
                }

            }

            if(count==0){
                MarketSearchHistory marketSearchHistory = new MarketSearchHistory();

                //将搜索记录keyword写入数据库
                marketSearchHistory.setKeyword(keyword);
                marketSearchHistory.setAddTime(LocalDateTime.now());
                marketSearchHistory.setUserId(userId);
                marketSearchHistory.setUpdateTime(LocalDateTime.now());
                marketSearchHistory.setDeleted(false);
                marketSearchHistoryMapper.insertSelective(marketSearchHistory);
            }


            MarketCategoryExample marketCategoryExample = new MarketCategoryExample();
            MarketCategoryExample.Criteria criteria1 = marketCategoryExample.createCriteria();
            criteria1.andPidNotEqualTo(0);
            List<MarketCategory> categoryList = marketCategoryMapper.selectByExample(marketCategoryExample);
            List<Integer> categoryIdList =new ArrayList<>();
            for (MarketGoods marketGoods : list) {
                categoryIdList.add(marketGoods.getCategoryId());
            }
            for (Integer i : categoryIdList) {
                for (MarketCategory marketCategory : categoryList) {
                    if(Objects.equals(marketCategory.getId(), i)){
                        filterCategoryListOld.add(marketCategory);
                    }
                }
            }
            //去重
            List<MarketCategory> filterCategoryList = filterCategoryListOld.stream().distinct().collect(Collectors.toList());
            data.put("list",list);

            List<MarketGoods> list1 = new ArrayList<>();
            if(categoryId!=0){
                for (MarketGoods marketGoods : list) {
                    if(marketGoods.getCategoryId().equals(categoryId)){
                        list1.add(marketGoods);
                    }
                }
                data.put("list",list1);
            }

            Page page2 = (Page) list;
            data.put("total", page2.getTotal());
            data.put("page", page2.getPageNum());
            data.put("limit", page2.getPageSize());
            data.put("pages", page2.getPages());
            data.put("filterCategoryList",filterCategoryList);

        }
        if(isHot!=null&&isHot.equals(true)){
            List<MarketCategory> filterCategoryListOld = new ArrayList<>();

            MarketGoodsExample marketGoodsExample = new MarketGoodsExample();
            marketGoodsExample.setOrderByClause(sort + " " +order);
            MarketGoodsExample.Criteria criteria = marketGoodsExample.createCriteria();
            criteria.andIsHotEqualTo(true);
            List<MarketGoods> marketGoods1 = marketGoodsMapper.selectByExample(marketGoodsExample);

            PageHelper.startPage(page,limit);
            List<MarketGoods> list = marketGoodsMapper.selectByExample(marketGoodsExample);

            MarketCategoryExample marketCategoryExample = new MarketCategoryExample();
            MarketCategoryExample.Criteria criteria1 = marketCategoryExample.createCriteria();
            criteria1.andPidNotEqualTo(0);
            List<MarketCategory> categoryList = marketCategoryMapper.selectByExample(marketCategoryExample);
            List<Integer> categoryIdList =new ArrayList<>();
            for (MarketGoods marketGoods : marketGoods1) {
                categoryIdList.add(marketGoods.getCategoryId());
            }
            for (Integer i : categoryIdList) {
                for (MarketCategory marketCategory : categoryList) {
                    if(Objects.equals(marketCategory.getId(), i)){
                        filterCategoryListOld.add(marketCategory);
                    }
                }
            }
            //去重
            List<MarketCategory> filterCategoryList = filterCategoryListOld.stream().distinct().collect(Collectors.toList());

            data.put("list",list);
            List<MarketGoods> list1 = new ArrayList<>();
            if(categoryId!=0){
                for (MarketGoods marketGoods : marketGoods1) {
                    if(marketGoods.getCategoryId().equals(categoryId)){
                        list1.add(marketGoods);
                    }
                }
                data.put("list",list1);
            }

            Page page2 = (Page) list;
            data.put("total", page2.getTotal());
            data.put("page", page2.getPageNum());
            data.put("limit", page2.getPageSize());
            data.put("pages", page2.getPages());
            data.put("filterCategoryList",filterCategoryList);
        }

        if(isNew!=null&&isNew.equals(true)){
            List<MarketCategory> filterCategoryListOld = new ArrayList<>();

            MarketGoodsExample marketGoodsExample = new MarketGoodsExample();
            marketGoodsExample.setOrderByClause(sort + " " +order);
            MarketGoodsExample.Criteria criteria = marketGoodsExample.createCriteria();
            criteria.andIsNewEqualTo(true);
            List<MarketGoods> marketGoods1 = marketGoodsMapper.selectByExample(marketGoodsExample);

            PageHelper.startPage(page,limit);
            List<MarketGoods> list = marketGoodsMapper.selectByExample(marketGoodsExample);

            MarketCategoryExample marketCategoryExample = new MarketCategoryExample();
            MarketCategoryExample.Criteria criteria1 = marketCategoryExample.createCriteria();
            criteria1.andPidNotEqualTo(0);
            List<MarketCategory> categoryList = marketCategoryMapper.selectByExample(marketCategoryExample);
            List<Integer> categoryIdList =new ArrayList<>();
            for (MarketGoods marketGoods : marketGoods1) {
                categoryIdList.add(marketGoods.getCategoryId());
            }

            for (Integer i : categoryIdList) {
                for (MarketCategory marketCategory : categoryList) {
                    if(Objects.equals(marketCategory.getId(), i)){
                        filterCategoryListOld.add(marketCategory);
                    }
                }
            }
            //去重
            List<MarketCategory> filterCategoryList = filterCategoryListOld.stream().distinct().collect(Collectors.toList());

            data.put("list",list);
            List<MarketGoods> list1 = new ArrayList<>();
            if(categoryId!=0){
                for (MarketGoods marketGoods : marketGoods1) {
                    if(marketGoods.getCategoryId().equals(categoryId)){
                        list1.add(marketGoods);
                    }
                }
                data.put("list",list1);
            }


            Page page2 = (Page) list;
            data.put("total", page2.getTotal());
            data.put("page", page2.getPageNum());
            data.put("limit", page2.getPageSize());
            data.put("pages", page2.getPages());
            data.put("filterCategoryList",filterCategoryList);
        }
       return ResponseUtil.ok(data);
    }
    @Override
    public Object goodsDetail(int id) {
        MarketGoodsAttributeExample marketGoodsAttributeExample = new MarketGoodsAttributeExample();
        MarketGoodsAttributeExample.Criteria criteria = marketGoodsAttributeExample.createCriteria();
        criteria.andGoodsIdEqualTo(id);
        List<MarketGoodsAttribute> attribute = marketGoodsAttributeMapper.selectByExample(marketGoodsAttributeExample);

        MarketGoodsExample marketGoodsExample = new MarketGoodsExample();
        MarketGoodsExample.Criteria criteria2 = marketGoodsExample.createCriteria();
        criteria2.andIdEqualTo(id);
        String shareUrl = marketGoodsMapper.selectOneByExample(marketGoodsExample).getShareUrl();
        Integer brandId = marketGoodsMapper.selectOneByExample(marketGoodsExample).getBrandId();
        MarketGoods info = marketGoodsMapper.selectOneByExample(marketGoodsExample);

        //加入足迹中footprint
        MarketFootprint footprint = new MarketFootprint();
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        MarketUser user = (MarketUser) session.getAttribute("user");
        if(user==null){
            return ResponseUtil.fail(666,"请登录哟");
        }
        Integer userId = user.getId();
        footprint.setUserId(userId);
        footprint.setGoodsId(info.getId());
        footprint.setAddTime(LocalDateTime.now());
        footprint.setUpdateTime(LocalDateTime.now());
        footprint.setDeleted(false);
        marketFootprintMapper.insertSelective(footprint);

        MarketBrandExample marketBrandExample = new MarketBrandExample();
        MarketBrandExample.Criteria criteria1 = marketBrandExample.createCriteria();
        criteria1.andIdEqualTo(brandId);

        MarketBrand brand = marketBrandMapper.selectOneByExample(marketBrandExample);

        MarketCommentExample marketCommentExample = new MarketCommentExample();
        MarketCommentExample.Criteria criteria3 = marketCommentExample.createCriteria();
        criteria3.andValueIdEqualTo(id);
        List<MarketComment> commentLi = marketCommentMapper.selectByExample(marketCommentExample);
        Integer count = commentLi.size();
        if (count>4){
            commentLi = commentLi.subList(0,4);
        }

        Map<String,Object> comment = new HashMap<>();
        comment.put("count", count);
        comment.put("data", commentLi);

        MarketIssueExample marketIssueExample = new MarketIssueExample();
        MarketIssueExample.Criteria criteria4 = marketIssueExample.createCriteria();
        criteria4.andDeletedEqualTo(false);

        List<MarketIssue> issue = marketIssueMapper.selectByExample(marketIssueExample);

        MarketGoodsProductExample marketGoodsProductExample = new MarketGoodsProductExample();
        MarketGoodsProductExample.Criteria criteria5 = marketGoodsProductExample.createCriteria();
        criteria5.andGoodsIdEqualTo(id);

        List<MarketGoodsProduct> productList = marketGoodsProductMapper.selectByExample(marketGoodsProductExample);


        MarketGoodsSpecificationExample marketGoodsSpecificationExample = new MarketGoodsSpecificationExample();
        MarketGoodsSpecificationExample.Criteria criteria6 = marketGoodsSpecificationExample.createCriteria();
        criteria6.andGoodsIdEqualTo(id);

        List<MarketGoodsSpecification> goodsSpecifications = marketGoodsSpecificationMapper.selectByExample(marketGoodsSpecificationExample);

        List<Map<String, Object>> specificationList = new ArrayList<>();

        for (MarketGoodsSpecification specification : goodsSpecifications) {
            String name = specification.getSpecification();

            // 查找是否已存在该规格名称的 Map
            Map<String, Object> existingMap = null;
            for (Map<String, Object> map : specificationList) {
                String existingName = (String) map.get("name");
                if (existingName.equals(name)) {
                    existingMap = map;
                    break;
                }
            }

            if (existingMap == null) {
                // 如果不存在该规格名称的 Map，则创建一个新的 Map，并将其添加到 specificationList 中
                Map<String, Object> newMap = new HashMap<>();
                newMap.put("name", name);
                List<MarketGoodsSpecification> valueList = new ArrayList<>();
                valueList.add(specification);
                newMap.put("valueList", valueList);
                specificationList.add(newMap);
            } else {
                // 如果已存在该规格名称的 Map，则将当前规格对象添加到对应的 valueList 中
                List<MarketGoodsSpecification> valueList = (List<MarketGoodsSpecification>) existingMap.get("valueList");
                valueList.add(specification);
            }
        }

        MarketCollectExample marketCollectExample = new MarketCollectExample();
        MarketCollectExample.Criteria criteria7 = marketCollectExample.createCriteria();
        criteria7.andDeletedEqualTo(false);
        criteria7.andValueIdEqualTo(id);


        Map<String,Object> data = new HashMap<>();
        data.put("attribute", attribute);
        data.put("brand", brand);
        data.put("comment", comment);
        data.put("info", info);
        data.put("issue", issue);
        data.put("productList", productList);
        data.put("share", true);
        data.put("shareImage", shareUrl);
        data.put("specificationList", specificationList);

        if(marketCollectMapper.selectByExample(marketCollectExample).isEmpty()){
        data.put("userHasCollect", 0);}else{data.put("userHasCollect", 1);}

        return ResponseUtil.ok(data);
    }

    @Override
    public List goodsRelated(int id) {

        MarketGoodsExample marketGoodsExample = new MarketGoodsExample();
        MarketGoodsExample.Criteria criteria2 = marketGoodsExample.createCriteria();
        criteria2.andIdEqualTo(id);
        Integer categoryId = marketGoodsMapper.selectOneByExample(marketGoodsExample).getCategoryId();

        MarketGoodsExample marketGoodsExample2 = new MarketGoodsExample();
        MarketGoodsExample.Criteria criteria = marketGoodsExample2.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<MarketGoods> list = marketGoodsMapper.selectByExample(marketGoodsExample2);

        return list;
    }

    @Override
    public Object count() {
        int data = 0;
        MarketGoodsExample marketGoodsExample = new MarketGoodsExample();
        List<MarketGoods> marketGoods = marketGoodsMapper.selectByExample(marketGoodsExample);
        for (MarketGoods marketGood : marketGoods) {
            data++;
        }
        return ResponseUtil.ok(data);
    }


}
