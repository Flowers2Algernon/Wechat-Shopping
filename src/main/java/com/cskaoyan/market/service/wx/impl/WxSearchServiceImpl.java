package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.db.domain.*;
import com.cskaoyan.market.db.mapper.MarketKeywordMapper;
import com.cskaoyan.market.db.mapper.MarketSearchHistoryMapper;
import com.cskaoyan.market.service.wx.WxSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WxSearchServiceImpl implements WxSearchService {

    @Autowired
    private MarketKeywordMapper marketKeywordMapper;
    @Autowired
    private MarketSearchHistoryMapper marketSearchHistoryMapper;



    @Override
    public Object searchIndex() {
        List<MarketKeyword> defaultKeyword;
        List<MarketSearchHistory> historyKeywordList;
        List<MarketKeyword> hotKeywordList;


        MarketKeywordExample marketKeywordExample = new MarketKeywordExample();
        MarketKeywordExample.Criteria criteria = marketKeywordExample.createCriteria();

        criteria.andIsDefaultEqualTo(true);

        defaultKeyword =  marketKeywordMapper.selectByExample(marketKeywordExample);



        MarketKeywordExample marketKeywordExample2 = new MarketKeywordExample();
        MarketKeywordExample.Criteria criteria2 = marketKeywordExample2.createCriteria();

        criteria2.andIsHotEqualTo(true);

        hotKeywordList = marketKeywordMapper.selectByExample(marketKeywordExample2);



        MarketSearchHistoryExample marketSearchHistoryExample = new MarketSearchHistoryExample();
        MarketSearchHistoryExample.Criteria criteria3 = marketSearchHistoryExample.createCriteria();

        criteria3.andDeletedEqualTo(false);

        historyKeywordList = marketSearchHistoryMapper.selectByExample(marketSearchHistoryExample);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("defaultKeyword", defaultKeyword);
        data.put("historyKeywordList", historyKeywordList);
        data.put("hotKeywordList", hotKeywordList);
        return data;
    }

    @Override
    public Object searchHelper(String keyword) {


        List<MarketSearchHistory> historyKeywordList;
        List<MarketKeyword> marketKeywordsList;

        MarketSearchHistoryExample marketSearchHistoryExample = new MarketSearchHistoryExample();
        MarketSearchHistoryExample.Criteria criteria3 = marketSearchHistoryExample.createCriteria();

        criteria3.andKeywordLike("%" + keyword + "%");
        criteria3.andDeletedEqualTo(false);

        historyKeywordList = marketSearchHistoryMapper.selectByExample(marketSearchHistoryExample);


        MarketKeywordExample marketKeywordExample2 = new MarketKeywordExample();
        MarketKeywordExample.Criteria criteria2 = marketKeywordExample2.createCriteria();

        criteria2.andIsHotEqualTo(true);
        criteria2.andKeywordLike("%" + keyword + "%");
        criteria2.andDeletedEqualTo(false);

        marketKeywordsList = marketKeywordMapper.selectByExample(marketKeywordExample2);



        List<String> data = new ArrayList<>();


        for (MarketSearchHistory history : historyKeywordList) {
            String keywordValue = history.getKeyword();
            data.add(keywordValue);
        }

        for(MarketKeyword keyword1 : marketKeywordsList){

            String keywordValue = keyword1.getKeyword();
            data.add(keywordValue);

        }

        return data;
    }

    @Override
    public void cleanHistory() {
        MarketSearchHistory marketSearchHistory = new MarketSearchHistory();
        marketSearchHistory.setDeleted(true);
        MarketSearchHistoryExample marketSearchHistoryExample = new MarketSearchHistoryExample();


        marketSearchHistoryMapper.updateByExampleSelective(marketSearchHistory,marketSearchHistoryExample);

    }
}
