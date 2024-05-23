package com.cskaoyan.market.service.admin.impl;

import com.cskaoyan.market.db.domain.MarketIssue;
import com.cskaoyan.market.db.domain.MarketIssueExample;
import com.cskaoyan.market.db.mapper.MarketIssueMapper;
import com.cskaoyan.market.service.admin.MarketIssueService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MarketIssueServiceImpl implements MarketIssueService {

    @Autowired
    MarketIssueMapper marketIssueMapper;

    @Override
    public List<MarketIssue> list(Integer limit, Integer page, String question, String sort, String order) {
        /*SqlSession session = MybatisUtils.getSession();
        MarketIssueMapper marketIssueMapper = session.getMapper(MarketIssueMapper.class);*/
        MarketIssueExample marketIssueExample = new MarketIssueExample();
        marketIssueExample.setOrderByClause(sort + " " + order);
        MarketIssueExample.Criteria criteria = marketIssueExample.createCriteria();

        if(!StringUtils.isEmpty(question)){
            criteria.andQuestionLike("%" + question + "%");
        }

        PageHelper.startPage(page, limit);

        List<MarketIssue> marketIssues = marketIssueMapper.selectByExampleSelective(marketIssueExample, MarketIssue.Column.id,MarketIssue.Column.question,MarketIssue.Column.answer,MarketIssue.Column.addTime,MarketIssue.Column.updateTime,MarketIssue.Column.deleted);

       /* session.commit();
        session.close();*/
        return marketIssues;
    }

    @Override
    public MarketIssue createIssue(String question, String answer) {
       /* SqlSession session = MybatisUtils.getSession();
        MarketIssueMapper marketIssueMapper = session.getMapper(MarketIssueMapper.class);*/
        MarketIssue marketIssue = new MarketIssue();
        marketIssue.setQuestion(question);
        marketIssue.setAnswer(answer);
        marketIssue.setAddTime(LocalDateTime.now());
        marketIssue.setUpdateTime(LocalDateTime.now());
        marketIssue.setDeleted(false);
        marketIssueMapper.insertSelective(marketIssue);
      /*  session.commit();
        session.close();*/
        return marketIssue;
    }

    @Override
    public MarketIssue updateIssue(Integer id, String question, String answer, String addTime, String updateTime,String delete) {
      /*  SqlSession session = MybatisUtils.getSession();
        MarketIssueMapper marketIssueMapper = session.getMapper(MarketIssueMapper.class);*/
        MarketIssue marketIssue = new MarketIssue();
        marketIssue.setId(id);
        marketIssue.setAddTime(LocalDateTime.now());
        marketIssue.setUpdateTime(LocalDateTime.now());
        marketIssue.setQuestion(question);
        marketIssue.setAnswer(answer);
        marketIssue.setDeleted(false);

        marketIssueMapper.updateByPrimaryKey(marketIssue);
       /* session.commit();
        session.close();*/
        return marketIssue;
    }

    @Override
    public Integer deleteIssue(Integer id) {
       /* SqlSession session = MybatisUtils.getSession();
        MarketIssueMapper marketIssueMapper = session.getMapper(MarketIssueMapper.class);*/
        int deleteCount = marketIssueMapper.deleteByPrimaryKey(id);
        /*session.commit();
        session.close();*/
        return deleteCount;
    }
}
