package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.db.domain.MarketFeedback;
import com.cskaoyan.market.db.mapper.MarketFeedbackMapper;
import com.cskaoyan.market.service.wx.WxFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: jyc
 * @Date: 2024/5/21 17:10
 */
@Service
public class WxFeedBackServiceImpl implements WxFeedbackService {
    @Autowired
    MarketFeedbackMapper mapper;

    @Override
    public void submit(String mobile, String content, String feedType, Boolean hasPicture, List<String> picUrls,
                       Integer userId, String userName) {
        MarketFeedback marketFeedback = new MarketFeedback();
        marketFeedback.setAddTime(LocalDateTime.now());
        marketFeedback.setUpdateTime(LocalDateTime.now());
        marketFeedback.setFeedType(feedType);
        marketFeedback.setHasPicture(hasPicture);
        marketFeedback.setMobile(mobile);
        marketFeedback.setUserId(userId);
        marketFeedback.setContent(content);
        marketFeedback.setUsername(userName);
        marketFeedback.setPicUrls(picUrls.toArray(new String[0]));
        mapper.insertSelective(marketFeedback);
    }
}
