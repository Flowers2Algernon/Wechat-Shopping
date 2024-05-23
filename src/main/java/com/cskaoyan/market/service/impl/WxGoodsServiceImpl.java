package com.cskaoyan.market.service.impl;

import com.cskaoyan.market.db.domain.*;
import com.cskaoyan.market.db.mapper.*;
import com.cskaoyan.market.service.WxGoodsService;
import com.cskaoyan.market.vo.MarketCommentUserVo;
import com.cskaoyan.market.vo.MarketFloorGoodsVo;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WxGoodsServiceImpl implements WxGoodsService {
    @Autowired
    MarketGoodsAttributeMapper marketGoodsAttributeMapper;
    @Autowired
    MarketGoodsMapper marketGoodsMapper;
    @Autowired
    MarketBrandMapper marketBrandMapper;
    @Autowired
    MarketCommentMapper marketCommentMapper;
    @Autowired
    MarketUserMapper marketUserMapper;
    @Autowired
    MarketIssueMapper marketIssueMapper;
    @Autowired
    MarketGoodsProductMapper marketGoodsProductMapper;
    @Autowired
    MarketGoodsSpecificationMapper marketGoodsSpecificationMapper;
    @Autowired
    MarketCollectMapper marketCollectMapper;

    @Override
    public Map<String, Object> detail(Integer goodsId) {
        MarketGoodsExample marketGoodsExample = new MarketGoodsExample();
        MarketGoodsExample.Criteria criteria1 = marketGoodsExample.createCriteria();
        criteria1.andIdEqualTo(goodsId);
        MarketGoods marketGoods = marketGoodsMapper.selectOneByExample(marketGoodsExample);
        Map<String, Object> map = new HashMap<>();
        //首先处理attribute
        MarketGoodsAttributeExample marketGoodsAttributeExample = new MarketGoodsAttributeExample();
        MarketGoodsAttributeExample.Criteria criteria = marketGoodsAttributeExample.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        List<MarketGoodsAttribute> marketGoodsAttributes = marketGoodsAttributeMapper.selectByExample(marketGoodsAttributeExample);
        map.put("attribute", marketGoodsAttributes);
        //接下来处理brand
        //通过brandId来获取
        Integer brandId = marketGoods.getBrandId();
        if (brandId != null) {
            MarketBrandExample marketBrandExample = new MarketBrandExample();
            marketBrandExample.createCriteria().andIdEqualTo(brandId);
            MarketBrand marketBrands = marketBrandMapper.selectOneByExample(marketBrandExample);
            map.put("brand", marketBrands);
        } else {
            map.put("brand", null);
        }
        //接下来处理comment
        ArrayList<MarketCommentUserVo> marketCommentUserVos = new ArrayList<>();
        MarketCommentExample marketCommentExample = new MarketCommentExample();
        marketCommentExample.createCriteria().andValueIdEqualTo(goodsId);
        List<MarketComment> marketComments = marketCommentMapper.selectByExample(marketCommentExample);
        int commentCount = 0;
        for (MarketComment marketComment : marketComments) {
            MarketCommentUserVo marketCommentUserVo = new MarketCommentUserVo();
            marketCommentUserVo.setAddTime(marketComment.getAddTime());
            marketCommentUserVo.setPicUrls(marketComment.getPicUrls());
            marketCommentUserVo.setContent(marketComment.getContent());
            marketCommentUserVo.setAdminContent(marketComment.getAdminContent());
            marketCommentUserVo.setId(marketComment.getId());
            //获取到对应的user,来得到其nickname和avatar
            MarketUserExample marketUserExample = new MarketUserExample();
            marketCommentExample.createCriteria().andIdEqualTo(marketComment.getUserId());
            MarketUser marketUser = marketUserMapper.selectOneByExample(marketUserExample);
            marketCommentUserVo.setAvatar(marketUser.getAvatar());
            marketCommentUserVo.setNickname(marketUser.getNickname());
            marketCommentUserVos.add(marketCommentUserVo);
            commentCount++;
        }
        Map<String, Object> map1 = new HashMap<>();
        map1.put("count", commentCount);
        map1.put("data", marketCommentUserVos);
        map.put("comment", map1);

        //接下来设置info
        map.put("info", marketGoods);

        //接下来处理issue
        MarketIssueExample marketIssueExample = new MarketIssueExample();
        List<MarketIssue> marketIssues = marketIssueMapper.selectByExample(marketIssueExample);
        map.put("issue", marketIssues);

        //接下来处理productList
        MarketGoodsProductExample marketGoodsProductExample = new MarketGoodsProductExample();
        marketGoodsProductExample.createCriteria().andGoodsIdEqualTo(goodsId);
        List<MarketGoodsProduct> marketGoodsProducts = marketGoodsProductMapper.selectByExample(marketGoodsProductExample);
        map.put("productList", marketGoodsProducts);

        //接下来处理share
        if (marketGoods.getShareUrl()!=null){
            map.put("share", true);
            map.put("shareImage", marketGoods.getShareUrl());
        }else {
            map.put("share", false);
            map.put("shareImage",null);
        }

        //处理shareImage

        //处理specification
        MarketGoodsSpecificationExample marketGoodsSpecificationExample = new MarketGoodsSpecificationExample();
        marketGoodsSpecificationExample.createCriteria().andGoodsIdEqualTo(goodsId);
        List<MarketGoodsSpecification> marketGoodsSpecifications = marketGoodsSpecificationMapper.selectByExample(marketGoodsSpecificationExample);
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("name", marketGoodsSpecifications.get(0).getSpecification());
        map2.put("valueList", marketGoodsSpecifications);
        map.put("specificationList", map2);
        //处理userHasCollect
        MarketCollectExample marketCollectExample = new MarketCollectExample();
//        Integer userID = marketComments.get(0).getUserId();这种方法获得userId不可靠

        marketCollectExample.createCriteria().andValueIdEqualTo(goodsId);
        List<MarketCollect> marketCollects = marketCollectMapper.selectByExample(marketCollectExample);
        map.put("userHasCollect", marketCollects.size());
        return map;
    }

    @Override
    public List<MarketFloorGoodsVo> related(Integer goodsId) {
        ArrayList<MarketFloorGoodsVo> marketFloorGoodsVos = new ArrayList<>();
        MarketGoodsExample marketGoodsExample = new MarketGoodsExample();
        marketGoodsExample.createCriteria().andIdEqualTo(goodsId);
        MarketGoods marketGoods = marketGoodsMapper.selectOneByExample(marketGoodsExample);
        Integer categoryId = marketGoods.getCategoryId();
        MarketGoodsExample marketGoodsExample1 = new MarketGoodsExample();
        marketGoodsExample1.createCriteria().andCategoryIdEqualTo(categoryId);
        List<MarketGoods> list = marketGoodsMapper.selectByExample(marketGoodsExample1);
        for (MarketGoods goods : list) {
            MarketFloorGoodsVo marketFloorGoodsVo = new MarketFloorGoodsVo();
            marketFloorGoodsVo.setBrief(goods.getBrief());
            marketFloorGoodsVo.setHot(goods.getIsHot());
            marketFloorGoodsVo.setId(goods.getId());
            marketFloorGoodsVo.setName(goods.getName());
            marketFloorGoodsVo.setRetailPrice(goods.getRetailPrice());
            marketFloorGoodsVo.setCounterPrice(goods.getCounterPrice());
            marketFloorGoodsVo.setPicUrl(goods.getPicUrl());
            marketFloorGoodsVo.setNew(goods.getIsNew());
            marketFloorGoodsVos.add(marketFloorGoodsVo);
            if (marketFloorGoodsVos.size()>=6){
                break;
            }
        }
        return marketFloorGoodsVos;
    }
}
