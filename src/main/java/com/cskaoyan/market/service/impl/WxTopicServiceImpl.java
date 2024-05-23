package com.cskaoyan.market.service.impl;

import com.cskaoyan.market.db.domain.MarketGoods;
import com.cskaoyan.market.db.domain.MarketGoodsExample;
import com.cskaoyan.market.db.domain.MarketTopic;
import com.cskaoyan.market.db.domain.MarketTopicExample;
import com.cskaoyan.market.db.mapper.MarketGoodsMapper;
import com.cskaoyan.market.db.mapper.MarketTopicMapper;
import com.cskaoyan.market.service.WxTopicService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WxTopicServiceImpl implements WxTopicService {

    @Autowired
    MarketTopicMapper marketTopicMapper;
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
        criteria.andDeletedEqualTo(false);
        criteria.andIdEqualTo(id);
        List<MarketTopic> list = marketTopicMapper.selectByExample(marketTopicExample);

        MarketTopic marketTopic = list.get(0);
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
