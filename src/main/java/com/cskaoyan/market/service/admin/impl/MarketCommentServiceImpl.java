package com.cskaoyan.market.service.admin.impl;

import com.cskaoyan.market.db.domain.MarketComment;
import com.cskaoyan.market.db.domain.MarketCommentExample;
import com.cskaoyan.market.db.mapper.MarketCommentMapper;
import com.cskaoyan.market.service.admin.MarketCommentService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketCommentServiceImpl implements MarketCommentService {
    @Autowired
    MarketCommentMapper marketCommentMapper;
    @Override
    public List<MarketComment> list(Integer page, Integer limit, String userId,String valueId,String sort, String order) {
        MarketCommentExample commentExample = new MarketCommentExample();
        MarketCommentExample.Criteria criteria = commentExample.createCriteria();

        // 设置排序规则
        commentExample.setOrderByClause(sort + " " + order);

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.parseInt(userId));
        }
        if (!StringUtils.isEmpty(valueId)) {
            criteria.andValueIdEqualTo(Integer.parseInt(valueId));
        }
        criteria.andDeletedEqualTo(false);

        // 分页查询
        PageHelper.startPage(page, limit);
        List<MarketComment> marketComment = marketCommentMapper.selectByExampleSelective(commentExample, MarketComment.Column.id,
                MarketComment.Column.addTime,MarketComment.Column.deleted,MarketComment.Column.type,MarketComment.Column.adminContent,
                MarketComment.Column.hasPicture, MarketComment.Column.content,MarketComment.Column.picUrls,MarketComment.Column.star,
                MarketComment.Column.updateTime,MarketComment.Column.userId,MarketComment.Column.valueId);
        return marketComment;
    }/*
    @Override
    public List<MarketComment> list(Integer page, Integer limit, String userId,String valueId, String sort, String order) {
        SqlSession session = MybatisUtils.getSession();
        //通过调用 MybatisUtils.getSession() 方法获取一个数据库会话对象。
        MarketCommentMapper marketCommentMapper = session.getMapper(MarketCommentMapper.class);
        //通过会话对象获取一个商品评论的数据访问对象
        MarketCommentExample marketCommentExample = new MarketCommentExample();
        //创建一个 MarketcommentExample 对象，用于构建查询条件。
        //设置排序规则

        MarketCommentExample.Criteria criteria = marketCommentExample.createCriteria();
        //创建一个查询条件的对象，用于构建筛选条件
        marketCommentExample.setOrderByClause(sort + " " + order);
        //设置查询结果的排序规则，根据传递的 sort 和 order 字符串进行排序。
        if(!StringUtils.isEmpty(userId)){
            criteria.andIdEqualTo(Integer.parseInt(userId));
        }
        if(!StringUtils.isEmpty(valueId)){
            criteria.andValueIdEqualTo(Integer.parseInt(valueId));
        }

        //分页查询 代码一定要紧跟着查询语句，不要中间间隔很多行代码，如果中间代码出现问题，很容易会出现分页异常
        PageHelper.startPage(page, limit);
        //使用分页插件 PageHelper 开始分页，设置页面数和每页显示的数据量
        List<MarketComment> marketComment = marketCommentMapper.selectByExampleSelective(marketCommentExample, MarketComment.Column.id,
                MarketComment.Column.addTime,MarketComment.Column.deleted,MarketComment.Column.type,MarketComment.Column.adminContent,
                MarketComment.Column.hasPicture, MarketComment.Column.content,MarketComment.Column.picUrls,MarketComment.Column.star,
                MarketComment.Column.updateTime,MarketComment.Column.userId,MarketComment.Column.valueId);
        session.commit();
        session.close();
        return marketComment;
    }*/
    @Override
    public void delete(Integer id) {

        MarketComment marketComment = new MarketComment();
        marketComment.setId(id);
        marketComment.setDeleted(true);
        marketCommentMapper.updateByPrimaryKeySelective(marketComment);
    }/*
    @Override
    public void delete(Integer id) {

        SqlSession session = MybatisUtils.getSession();
        MarketCommentMapper commentMapper = session.getMapper(MarketCommentMapper.class);
        MarketCommentExample commentExample = new MarketCommentExample();
        MarketCommentExample.Criteria criteria = commentExample.createCriteria();
        criteria.andIdEqualTo(id);

        if (id != null) {

            //commentMapper.deleteByPrimaryKey(id);
            commentMapper.deleteByExample(commentExample);
        }

        session.commit();
        session.close();

    }*/
    @Override
    public int getTotalCount(String userId, String valueId) {
        MarketCommentExample commentExample = new MarketCommentExample();
        MarketCommentExample.Criteria criteria = commentExample.createCriteria();

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.parseInt(userId));
        }
        if (!StringUtils.isEmpty(valueId)) {
            criteria.andValueIdEqualTo(Integer.parseInt(valueId));
        }

        return (int) marketCommentMapper.countByExample(commentExample);
    }
}
