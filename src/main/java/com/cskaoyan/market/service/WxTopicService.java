package com.cskaoyan.market.service;

import java.util.List;

public interface WxTopicService {
    List topicList();

    List topicRelated(int id);

    Object topicDetail(int id);
}
