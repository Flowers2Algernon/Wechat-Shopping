package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.db.domain.*;
import com.cskaoyan.market.db.mapper.MarketCommentMapper;
import com.cskaoyan.market.db.mapper.MarketGoodsMapper;
import com.cskaoyan.market.db.mapper.MarketTopicMapper;
import com.cskaoyan.market.service.wx.WxTopicService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WxTopicServiceImpl implements WxTopicService {

    @Autowired
    MarketTopicMapper marketTopicMapper;
    @Autowired
    MarketCommentMapper marketCommentMapper;
    @Autowired
    MarketGoodsMapper marketGoodsMapper;
    @Override
    public List topicList() {

        List<MarketTopic> marketTopicList;
        MarketTopicExample marketTopicExample = new MarketTopicExample();
        MarketTopicExample.Criteria criteria = marketTopicExample.createCriteria();

        criteria.andDeletedEqualTo(false);
        PageHelper.startPage(1, 10);

        marketTopicList = marketTopicMapper.selectByExample(marketTopicExample);


        return marketTopicList;

    }


    @Override
    public List topicRelated(int id) {

        MarketTopicExample marketTopicExample = new MarketTopicExample();
        MarketTopicExample.Criteria criteria = marketTopicExample.createCriteria();
        criteria.andDeletedEqualTo(false);
        List<MarketTopic> list = marketTopicMapper.selectByExample(marketTopicExample);
        if(list.size()>4){
            list = list.subList(0,4);
        }


        return list;
    }

    @Override
    public Object topicDetail(int id) {
        MarketTopicExample marketTopicExample = new MarketTopicExample();
        MarketTopicExample.Criteria criteria = marketTopicExample.createCriteria();
        criteria.andIdEqualTo(id);
        MarketTopic marketTopic = marketTopicMapper.selectOneByExampleSelective(marketTopicExample, MarketTopic.Column.addTime,
                MarketTopic.Column.content, MarketTopic.Column.deleted, MarketTopic.Column.goods, MarketTopic.Column.id,
                MarketTopic.Column.picUrl, MarketTopic.Column.price, MarketTopic.Column.readCount, MarketTopic.Column.sortOrder,
                MarketTopic.Column.subtitle, MarketTopic.Column.title, MarketTopic.Column.updateTime);

        Integer[] goodsId = marketTopic.getGoods();
        List<MarketGoods> goods = new ArrayList<>();

        MarketGoodsExample marketGoodsExample = new MarketGoodsExample();
        MarketGoodsExample.Criteria criteria1 = marketGoodsExample.createCriteria();
        criteria1.andDeletedEqualTo(false);

        List<MarketGoods> goodsadd = marketGoodsMapper.selectByExample(marketGoodsExample);

        for (int i = 0; i < goodsId.length; i++) {

            for (MarketGoods good : goodsadd) {
                if (Objects.equals(good.getId(), goodsId[i])) {
                    goods.add(good);
                    break;
                }
            }
        }

        Map<String,Object> data = new HashMap<>();
        data.put("goods", goods);
        data.put("topic", marketTopic);



        return data;
    }
}
