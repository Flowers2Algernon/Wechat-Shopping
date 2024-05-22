package com.cskaoyan.market.service.impl;


import com.cskaoyan.market.bo.AddGoodsBo;
import com.cskaoyan.market.db.domain.*;
import com.cskaoyan.market.db.mapper.*;
import com.cskaoyan.market.service.MarketGoodsService;
import com.cskaoyan.market.util.MybatisUtils;
import com.cskaoyan.market.vo.CatAndBrandVo;
import com.cskaoyan.market.vo.MarketGoodsVo;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GoodsServiceImpl
 * @Description: TODO
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/3/16 9:51
 * @Version V1.0
 **/
@Service
public class MarketGoodsServiceImpl implements MarketGoodsService {
    @Autowired
    MarketGoodsMapper goodsMapper;
    @Autowired
    MarketCategoryMapper categoryMapper;
    @Autowired
    MarketBrandMapper brandMapper;
    @Autowired
    MarketGoodsSpecificationMapper specificationMapper;
    @Autowired
    MarketGoodsAttributeMapper attributeMapper;
    @Autowired
    MarketGoodsProductMapper productMapper;


    @Override
    public List<MarketGoods> list(Integer page, Integer limit, String goodsSn, String goodsId, String name, String sort, String order) {

        MarketGoodsExample goodsExample = new MarketGoodsExample();
        MarketGoodsExample.Criteria criteria = goodsExample.createCriteria();

        goodsExample.setOrderByClause(sort + " " + order);
        if(!StringUtils.isEmpty(goodsId)){
            criteria.andIdEqualTo(Integer.parseInt(goodsId));
        }
        if(!StringUtils.isEmpty(goodsSn)){
            criteria.andGoodsSnLike("%" + goodsSn + "%");
        }
        if(!StringUtils.isEmpty(name)){
            criteria.andNameLike("%" + name + "%");
        }
        //分页查询
        PageHelper.startPage(page, limit);
        List<MarketGoods> goodsList = goodsMapper.selectByExample(goodsExample);
        return goodsList;
    }

    @Override
    public void delete(Integer id) {

        MarketGoodsExample goodsExample = new MarketGoodsExample();
        MarketGoodsExample.Criteria criteria = goodsExample.createCriteria();
        criteria.andIdEqualTo(id);

        if (id != null) {
            //goodsMapper.deleteByPrimaryKey(id);
            goodsMapper.deleteByExample(goodsExample);
        }
    }

    @Override
    public Map<String, List<CatAndBrandVo>> catAndBrand() {

        List<MarketCategory> marketCategories = categoryMapper.selectByExample(new MarketCategoryExample());
        List<MarketBrand> marketBrands = brandMapper.selectByExample(new MarketBrandExample());

        //分类中包含一级分类和二级分类，所以需要按照pid来进行分组
        Map<Integer, List<MarketCategory>> categoriesData = groupByPid(marketCategories);
        //获取一级类目
        List<CatAndBrandVo> categoryList = new ArrayList<>();
        List<CatAndBrandVo> brandList = new ArrayList<>();


        List<MarketCategory> l1Categories = categoriesData.get(0);
        for (MarketCategory l1 : l1Categories) {
            //把实体对象MarketCategory转换成CatAndBrandVo对象
            CatAndBrandVo l1CateVo = new CatAndBrandVo(l1.getId(), l1.getName(), null);
            List<CatAndBrandVo> childrenOfl1Vo = new ArrayList<>();
            l1CateVo.setChildren(childrenOfl1Vo);
            //去查找当前类目的二级类目
            List<MarketCategory> l2CategoriesOfl1 = categoriesData.get(l1.getId());
            if(l2CategoriesOfl1!=null){
                for (MarketCategory l2 : l2CategoriesOfl1) {
                    childrenOfl1Vo.add(new CatAndBrandVo(l2.getId(), l2.getName(), null));
                }
            }
            categoryList.add(l1CateVo);
        }

        //处理brand品牌
        for (MarketBrand brand : marketBrands) {
            brandList.add(new CatAndBrandVo(brand.getId(), brand.getName(), null));
        }

        //构造一个map，将二者合并在一起，组装返回给controller
        Map<String, List<CatAndBrandVo>> catAndBrands = new HashMap<>();
        catAndBrands.put("categoryList", categoryList);
        catAndBrands.put("brandList", brandList);
        return catAndBrands;
    }

    @Transactional
    @Override
    public void insertOne(MarketGoods goods, List<MarketGoodsSpecification> specifications, List<MarketGoodsProduct> products, List<MarketGoodsAttribute> attributes) {

        //将上述数据存储到4张表，要求保障事务，其他三张表需要依赖于goods表插入之后得到的id编号
        goods.setAddTime(LocalDateTime.now());
        goods.setUpdateTime(LocalDateTime.now());
        //你可以理解为当前此处设置的价格是搜索页面中看到的最低价格，而内部的实际价格，是product里面的价格
        BigDecimal retailPrice = products.get(0).getPrice();
        for (int i = 1; i < products.size(); i++) {
            BigDecimal price = products.get(i).getPrice();
            if(retailPrice.compareTo(price) == 1){
                //retailPrice大于price，那么把小的赋值过来
                retailPrice = price;
            }
        }
        goods.setRetailPrice(retailPrice);
        goods.setDeleted(false);

        //执行完插入会后，会有编号
        goodsMapper.insertSelective(goods);
        Integer id = goods.getId();
        for (MarketGoodsSpecification sp : specifications) {
            sp.setGoodsId(id);
            sp.setUpdateTime(LocalDateTime.now());
            sp.setAddTime(LocalDateTime.now());
            sp.setDeleted(false);
            specificationMapper.insertSelective(sp);
        }
        for (MarketGoodsProduct product : products) {
            product.setGoodsId(id);
            product.setAddTime(LocalDateTime.now());
            product.setUpdateTime(LocalDateTime.now());
            product.setDeleted(false);
            productMapper.insertSelective(product);
        }
        for (MarketGoodsAttribute attribute : attributes) {
            attribute.setGoodsId(id);
            attribute.setAddTime(LocalDateTime.now());
            attribute.setUpdateTime(LocalDateTime.now());
            attribute.setDeleted(false);
            attributeMapper.insertSelective(attribute);
        }
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
    public MarketGoodsVo detail(Integer Id) {

        //获取商品信息
        MarketGoodsExample goodsExample = new MarketGoodsExample();
        MarketGoodsExample.Criteria criteria = goodsExample.createCriteria();
        criteria.andIdEqualTo(Id);
        MarketGoods marketGoods = goodsMapper.selectByPrimaryKey(Id);

        //获取商品属性信息
        MarketGoodsAttributeExample attributeExample = new MarketGoodsAttributeExample();
        MarketGoodsAttributeExample.Criteria attributeCriteria = attributeExample.createCriteria();
        attributeCriteria.andGoodsIdEqualTo(Id);
        List<MarketGoodsAttribute> attributes =attributeMapper.selectByExample(attributeExample);

        //通过session获取mapper接口的实例，用于使用数据库相关操作
        MarketGoodsSpecificationExample goodsSpecificationExample = new MarketGoodsSpecificationExample();
        //创建一个MarketGoodsSpecificationExample对象，用于定义查询的条件
        MarketGoodsSpecificationExample.Criteria specificationCriteria = goodsSpecificationExample.createCriteria();
        //创建一个Criteria对象，用于设置查询条件。
        specificationCriteria.andGoodsIdEqualTo(Id);
        //设置查询条件，通过andIdEqualTo方法将goodsId参数与id字段进行比较，
        // 以确定查询结果中的MarketGoodsSpecification对象的id值与goodsId相等
        List<MarketGoodsSpecification> specifications = specificationMapper.selectByExample(goodsSpecificationExample);


        MarketGoodsProductExample goodsProductExample = new MarketGoodsProductExample();
        MarketGoodsProductExample.Criteria goodsProductCriteria = goodsProductExample.createCriteria();
        goodsProductCriteria.andGoodsIdEqualTo(Id);
        List<MarketGoodsProduct> products= productMapper.selectByExample(goodsProductExample);


        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(marketGoods.getCategoryId());

        List<MarketGoods> marketGoodsList =goodsMapper.selectByExample(goodsExample);
        MarketGoods goods = marketGoodsList.get(0);

        return new MarketGoodsVo(products,specifications,attributes,goods,categoryIds);
    }

    /*private static List<MarketGoodsProduct> getProductDate(SqlSession session, Integer Id) {
        //MarketGoodsProductMapper productMapper = session.getMapper(MarketGoodsProductMapper.class);
        //        MarketGoodsProductExample productExample = new MarketGoodsProductExample();
        //        MarketGoodsProductExample.Criteria criteria4 = productExample.createCriteria();
        //        if (id != null) {
        //            criteria4.andGoodsIdEqualTo(id);
        //        }
        MarketGoodsProductMapper goodsProductMapper = session.getMapper(MarketGoodsProductMapper.class);
        MarketGoodsProductExample goodsProductExample = new MarketGoodsProductExample();
        MarketGoodsProductExample.Criteria goodsProductCriteria = goodsProductExample.createCriteria();
        goodsProductCriteria.andIdEqualTo(Id);
        List<MarketGoodsProduct> marketGoodsProduct= goodsProductMapper.selectByExample(goodsProductExample);

        return marketGoodsProduct;
    }*/

    @Override
    public boolean updated(AddGoodsBo goodsBo) {

        MarketGoodsExample goodsExample = new MarketGoodsExample();
        MarketGoodsProductExample goodsProductExample = new MarketGoodsProductExample();
        MarketGoodsSpecificationExample goodsSpecificationExample = new MarketGoodsSpecificationExample();

        MarketGoodsExample.Criteria criteria = goodsExample.createCriteria();
        MarketGoodsProductExample.Criteria ProductCriteria = goodsProductExample.createCriteria();
        MarketGoodsSpecificationExample.Criteria SpecificationCriteria = goodsSpecificationExample.createCriteria();


        if (goodsBo.getGoods() != null) {
            criteria.andIdEqualTo(goodsBo.getGoods().getId());
            goodsMapper.updateByExampleWithBLOBs(goodsBo.getGoods(), goodsExample);
            return true;
        }
        if (goodsBo.getProducts() != null) {
            for (MarketGoodsProduct product : goodsBo.getProducts()) {
                ProductCriteria.andGoodsIdEqualTo(product.getGoodsId());
                productMapper.updateByExample(product, goodsProductExample);
                return true;
            }
        }
        if (goodsBo.getSpecifications()!= null) {
            for (MarketGoodsSpecification specification : goodsBo.getSpecifications()) {
                SpecificationCriteria.andGoodsIdEqualTo(specification.getGoodsId());
                specificationMapper.updateByExample(specification, goodsSpecificationExample);
                return true;
            }
        }
        return false;
    }
}
