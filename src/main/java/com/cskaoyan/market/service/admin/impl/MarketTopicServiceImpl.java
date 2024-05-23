package com.cskaoyan.market.service.admin.impl;

import com.cskaoyan.market.db.domain.MarketGoods;
import com.cskaoyan.market.db.domain.MarketTopic;
import com.cskaoyan.market.db.domain.MarketTopicExample;
import com.cskaoyan.market.db.mapper.MarketGoodsMapper;
import com.cskaoyan.market.db.mapper.MarketTopicMapper;
import com.cskaoyan.market.service.admin.MarketTopicService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 薛松 xuesong_work@163.com
 * @Date: 2024/05/07/0:58
 * @Description:
 */
@Service
public class MarketTopicServiceImpl implements MarketTopicService {
    @Autowired
    MarketTopicMapper marketTopicMapper;
    @Autowired
    MarketGoodsMapper marketGoodsMapper;
    @Override
    public List<MarketTopic> list(Integer page, Integer limit, String title, String subtitle, String sort, String order) {
        //SqlSession session= MybatisUtils.getSession();
        //MarketTopicMapper marketTopicMapper = session.getMapper(MarketTopicMapper.class);
        MarketTopicExample example=new MarketTopicExample();
        MarketTopicExample.Criteria criteria=example.createCriteria();
        //有sql注入风险
        example.setOrderByClause(sort+" "+order);
        if(StringUtils.isNotBlank(title))
        {
            criteria.andTitleLike("%"+title+"%");
        }
        if(StringUtils.isNotBlank(subtitle))
        {
            criteria.andSubtitleLike("%"+subtitle+"%");
        }
        criteria.andDeletedEqualTo(false);
        //在执行之前添加分页
        PageHelper.startPage(page,limit);
        List<MarketTopic> marketTopics = marketTopicMapper.selectByExampleSelective(example,MarketTopic.Column.addTime,MarketTopic.Column.content,MarketTopic.Column.deleted,MarketTopic.Column.goods,MarketTopic.Column.id,MarketTopic.Column.picUrl,MarketTopic.Column.price,MarketTopic.Column.readCount,MarketTopic.Column.sortOrder,MarketTopic.Column.subtitle,MarketTopic.Column.title,MarketTopic.Column.updateTime);
        //session.commit();
        //session.close();
        return marketTopics;
    }

    @Override
    public MarketTopic create(Integer[] goods, String title, String subtitle, String picUrl, String content, BigDecimal price, String readCount) {
        //SqlSession session = MybatisUtils.getSession();
        //MarketTopicMapper marketTopicMapper = session.getMapper(MarketTopicMapper.class);

        MarketTopic marketTopic = new MarketTopic();
        marketTopic.setTitle(title);
        marketTopic.setSubtitle(subtitle);
        marketTopic.setPrice(price);
        marketTopic.setReadCount(readCount);
        marketTopic.setPicUrl(picUrl);
        marketTopic.setGoods(goods);
        marketTopic.setAddTime(LocalDateTime.now());
        marketTopic.setUpdateTime(LocalDateTime.now());
        marketTopic.setContent(content);

        marketTopicMapper.insertSelective(marketTopic);
        //session.commit();

        MarketTopic createTopic = marketTopicMapper.selectByPrimaryKey(marketTopic.getId());
        //session.close();

        return createTopic;
    }

    @Override
    public MarketTopic update(Integer id, String title, String subtitle, BigDecimal price, String readCount, String picUrl, Integer sortOrder, Integer[] goods, LocalDateTime addTime, LocalDateTime updateTime, Boolean deleted, String content) {
        //SqlSession session = MybatisUtils.getSession();
        //MarketTopicMapper marketTopicMapper = session.getMapper(MarketTopicMapper.class);

        MarketTopic marketTopic = new MarketTopic();

        marketTopic.setId(id);
        marketTopic.setTitle(title);
        marketTopic.setSubtitle(subtitle);
        marketTopic.setPrice(price);
        marketTopic.setReadCount(readCount);
        marketTopic.setPicUrl(picUrl);
        marketTopic.setGoods(goods);
        marketTopic.setAddTime(LocalDateTime.now());
        marketTopic.setUpdateTime(LocalDateTime.now());
        marketTopic.setDeleted(deleted);
        marketTopic.setContent(content);

        int rowsAffected = marketTopicMapper.updateByPrimaryKeySelective(marketTopic);
        //session.commit();

        MarketTopic updateTopic = marketTopicMapper.selectByPrimaryKey(id);
        //session.close();

        if (rowsAffected > 0) {
            return updateTopic;
        } else {
            return null;
        }
    }

    @Override
    public Map<String, Object> read(Integer id) {
        Map<String, Object> map = new HashMap<>();
        List<MarketGoods> goodsList = new ArrayList<>();
        //SqlSession session = MybatisUtils.getSession();
        try {
            //MarketTopicMapper topicMapper = session.getMapper(MarketTopicMapper.class);
            //MarketGoodsMapper goodsMapper = session.getMapper(MarketGoodsMapper.class);
            MarketTopic topic = marketTopicMapper.selectByPrimaryKey(id);
            map.put("topic", topic);
            //根据商品TD找到商品
            Integer[] goodsIds = topic.getGoods();
            for (Integer goodsId : goodsIds) {
                MarketGoods marketGoods = marketGoodsMapper.selectByPrimaryKey(goodsId);
                goodsList.add(marketGoods);
            }
            map.put("goodsList", goodsList);
            //session.commit();
            return map;
        } catch (Exception e) {
            return null;
        } /*finally {
            session.close();
        }*/
    }


    @Override
    public boolean delete (Integer id){
        //SqlSession session = MybatisUtils.getSession();
        //MarketTopicMapper marketTopicMapper = session.getMapper(MarketTopicMapper.class);
        try {

            int rowsAffected = marketTopicMapper.logicalDeleteByPrimaryKey(id);
            //彻底删除
            //int rowsAffected = marketTopicMapper.deleteByPrimaryKey(id);
            //session.commit();
            return rowsAffected > 0;
        } catch (Exception e) {
            //session.rollback();
            throw new RuntimeException("删除失败", e);
        } finally {
            //session.close();
        }
    }

    @Override
    public boolean batchDelete (List < Integer > idsToDelete) {
        try {
            int totalIds = idsToDelete.size();
            int rowsAffected = 0;
            for (Integer id : idsToDelete) {
                rowsAffected += marketTopicMapper.logicalDeleteByPrimaryKey(id);
            }

            return rowsAffected == totalIds; // 确保所有ID对应的记录都成功删除
        } catch (Exception e) {
            throw new RuntimeException("批量逻辑删除失败", e);
        }
    }


}
