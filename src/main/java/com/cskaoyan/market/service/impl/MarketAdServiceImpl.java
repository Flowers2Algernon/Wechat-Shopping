package com.cskaoyan.market.service.impl;

import com.cskaoyan.market.db.domain.MarketAd;
import com.cskaoyan.market.db.domain.MarketAdExample;
import com.cskaoyan.market.db.mapper.MarketAdMapper;
import com.cskaoyan.market.service.MarketAdService;
import com.cskaoyan.market.util.MybatisUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 薛松 xuesong_work@163.com
 * @Date: 2024/05/06/17:59
 * @Description:
 */
@Service
public class MarketAdServiceImpl implements MarketAdService {
    @Autowired
    MarketAdMapper marketAdMapper;
    @Override
    public List<MarketAd> list(Integer page, Integer limit, String name, String content, String sort, String order) {
        //SqlSession session = MybatisUtils.getSession();
        //MarketAdMapper marketAdMapper = session.getMapper(MarketAdMapper.class);

        MarketAdExample marketAdExample = new MarketAdExample();

        // 添加逻辑删除过滤条件，只查询deleted为false（假设未删除状态为false）的记录
        MarketAdExample.Criteria baseCriteria = marketAdExample.createCriteria();
        baseCriteria.andDeletedEqualTo(false);

        //add_time desc 此处有sql注入风险 接受了用户输入数据 应增加一个对sort的判断
        marketAdExample.setOrderByClause(sort + " " + order);

        // 根据name和content动态添加查询条件
        MarketAdExample.Criteria searchCriteria = marketAdExample.or(); // 创建一个新的or条件组用于搜索条件

        if (StringUtils.isNotBlank(name)) {
            baseCriteria.andNameLike("%" + name + "%");
        }
        if (StringUtils.isNotBlank(content)) {
            baseCriteria.andContentLike("%" + content + "%");
        }


        //select xxx,xxx from market_ad where xxx = xx like xx order by xx desc limit xx offset xxx;
        //在执行查询之前增加一行代码进行分页 不添加则不分页
        PageHelper.startPage(page, limit);
        List<MarketAd> marketAds = marketAdMapper.selectByExample(marketAdExample);
        //session.commit();
        //session.close();


        return marketAds;
    }

    @Override
    public MarketAd create(String name, String content, String url, String link, Byte position, Boolean enabled) {
        //SqlSession session = MybatisUtils.getSession();
        //MarketAdMapper marketAdMapper = session.getMapper(MarketAdMapper.class);

        MarketAd marketAd = new MarketAd();
        marketAd.setName(name);
        marketAd.setContent(content);
        marketAd.setUrl(url);
        marketAd.setLink(link);
        marketAd.setPosition(position);
        marketAd.setEnabled(enabled);
        marketAd.setAddTime(LocalDateTime.now());
        marketAd.setUpdateTime(LocalDateTime.now());

        //插入数据库
        marketAdMapper.insertSelective(marketAd);
        //session.commit();

        //返回创建成功的广告对象
        MarketAdExample marketAdExample = new MarketAdExample();
        marketAdExample.createCriteria().andUrlEqualTo(marketAd.getContent());
        MarketAd marketAd1 = marketAdMapper.selectOneByExample(marketAdExample);
        MarketAd createdAd = marketAdMapper.selectByPrimaryKey(marketAd.getId());
        //session.close();

        return marketAd1;
    }

    @Override
    public MarketAd update(Integer id, String name, String link, String url, Byte position, String content, Boolean enabled, LocalDateTime addTime) {
        //SqlSession session = MybatisUtils.getSession();
        //MarketAdMapper marketAdMapper = session.getMapper(MarketAdMapper.class);

        MarketAd marketAd = new MarketAd();
        marketAd.setId(id);
        marketAd.setName(name);
        marketAd.setLink(link);
        marketAd.setUrl(url);
        marketAd.setPosition(position);
        marketAd.setContent(content);
        marketAd.setEnabled(enabled);
        marketAd.setAddTime(addTime);
        marketAd.setUpdateTime(LocalDateTime.now());

        //更新数据库
        int rowsAffected = marketAdMapper.updateByPrimaryKeySelective(marketAd);
        //session.commit();

        //返回更新后的广告对象
        MarketAd updatedAd = marketAdMapper.selectByPrimaryKey(id);
        //session.close();

        //检查是否更新成功
        if (rowsAffected > 0) {
            return updatedAd;
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(Integer id) {
        //SqlSession session = MybatisUtils.getSession();
        try {
            //MarketAdMapper marketAdMapper = session.getMapper(MarketAdMapper.class);
            int rowsAffected = marketAdMapper.logicalDeleteByPrimaryKey(id);
            //彻底删除
            //int rowsAffected = marketAdMapper.deleteByPrimaryKey(id);
            //session.commit();
            return rowsAffected > 0;
        } catch (Exception e) {
            //session.rollback();
            throw new RuntimeException("删除失败", e);
        } /*finally {
            //session.close();
        }*/
    }
}


