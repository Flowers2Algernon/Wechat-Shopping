package com.cskaoyan.market.service;

import com.cskaoyan.market.db.domain.MarketTopic;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 薛松 xuesong_work@163.com
 * @Date: 2024/05/07/0:58
 * @Description:
 */
public interface MarketTopicService {

    List<MarketTopic> list(Integer page, Integer limit, String title, String subtitle, String sort, String order);

    MarketTopic create(Integer[] goods, String title, String subtitle, String picUrl, String content, BigDecimal price, String readCount);


    MarketTopic update(Integer id, String title, String subtitle, BigDecimal price, String readCount, String picUrl, Integer sortOrder, Integer[] goods, LocalDateTime addTime, LocalDateTime updateTime, Boolean deleted, String content);

    Map<String, Object> read(Integer id);

    boolean delete(Integer id);

    boolean batchDelete(List<Integer> idsToDelete);
}
