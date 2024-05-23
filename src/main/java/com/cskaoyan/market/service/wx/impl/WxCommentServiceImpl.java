package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.db.domain.MarketComment;
import com.cskaoyan.market.db.domain.MarketCommentExample;
import com.cskaoyan.market.db.domain.MarketUser;
import com.cskaoyan.market.db.mapper.MarketCommentMapper;
import com.cskaoyan.market.db.mapper.MarketUserMapper;
import com.cskaoyan.market.service.wx.WxCommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WxCommentServiceImpl implements WxCommentService {
    @Autowired
    MarketCommentMapper marketCommentMapper;

    @Autowired
    MarketUserMapper marketUserMapper;

    @Override
    public List list(String valueId, Integer pageNumber, Integer limitNumber, Byte type, Byte showType) {

        MarketCommentExample commentExample = new MarketCommentExample();
        MarketCommentExample.Criteria criteria = commentExample.createCriteria();
        criteria.andValueIdEqualTo(Integer.parseInt(valueId));
        //criteria.andTypeEqualTo(type);
        criteria.andDeletedEqualTo(false);
        if (showType == 1) {
            //criteria.andHasPictureEqualTo(true);
        }
        // 分页查询
        //PageHelper.startPage(pageNumber, limitNumber);
        List<MarketComment> marketCommentList = marketCommentMapper.selectByExample(commentExample);
        List<Map<String, Object>> resultList = new ArrayList<>();
        for(MarketComment marketComment : marketCommentList){

            Map<String, Object> map = new HashMap<>();
            map.put("addTime", marketComment.getAddTime());
            map.put("adminContent", marketComment.getAdminContent());
            map.put("content", marketComment.getContent());
            map.put("picList", marketComment.getPicUrls());
            MarketUser user = marketUserMapper.selectByPrimaryKey(marketComment.getUserId());

            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("avatarUrl", user.getAvatar());
            userInfo.put("nickName", user.getNickname());
            map.put("userInfo", userInfo);
            resultList.add(map);
        }
        return resultList;
    }

    @Override
    public Map<String, Object> count(String valueId, Byte type) {
        MarketCommentExample commentExample = new MarketCommentExample();
        MarketCommentExample.Criteria criteria = commentExample.createCriteria();
        if (valueId != null) {
            criteria.andValueIdEqualTo(Integer.parseInt(valueId));
        }
/*
        if (hasPicCount != null) {
            criteria.andHasPictureEqualTo(hasPicCount);
        }
        if (allCount != null) {
            criteria.andTotalEqualTo(allCount);
        }
*/
        criteria.andDeletedEqualTo(false);
        criteria.andTypeEqualTo(type);
        long allCount = marketCommentMapper.countByExample(commentExample);
        // 统计有图片的评论数量
        criteria.andHasPictureEqualTo(true);
        long hasPicCount = marketCommentMapper.countByExample(commentExample);

        Map<String, Object> count = new HashMap<>();
        count.put("allCount", allCount);
        count.put("hasPicCount", hasPicCount);

        return count;
    }

    @Override
    public int getTotalCount(String valueId) {
        MarketCommentExample commentExample = new MarketCommentExample();
        MarketCommentExample.Criteria criteria = commentExample.createCriteria();

        if (!StringUtils.isEmpty(valueId)) {
            criteria.andValueIdEqualTo(Integer.parseInt(valueId));
        }

        return (int) marketCommentMapper.countByExample(commentExample);
    }
    @Override
    public boolean post(MarketComment marketComment) {
        int row = marketCommentMapper.insert(marketComment);
        return row == 1;
    }
}