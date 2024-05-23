package com.cskaoyan.market.service.admin.impl;

import com.cskaoyan.market.db.domain.MarketKeyword;
import com.cskaoyan.market.db.domain.MarketKeywordExample;
import com.cskaoyan.market.db.mapper.MarketKeywordMapper;
import com.cskaoyan.market.service.admin.MarketKeywordService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MarketKeywordServiceImpl implements MarketKeywordService {

    @Autowired
    MarketKeywordMapper marketKeywordMapper;

    @Override
    public List<MarketKeyword> list(Integer page, Integer limit, String keyword, String url, String sort, String order) {
        //SqlSession session = MybatisUtils.getSession();
        //MarketKeywordMapper marketKeywordMapper = session.getMapper(MarketKeywordMapper.class);
        MarketKeywordExample keywordExample = new MarketKeywordExample();
        keywordExample.setOrderByClause(sort + " " + order);
        MarketKeywordExample.Criteria criteria = keywordExample.createCriteria();
        if (!StringUtils.isEmpty(keyword)){
            criteria.andKeywordLike("%" + keyword + "%");
        }
        if (!StringUtils.isEmpty(url)){
            criteria.andUrlEqualTo(url);
        }

        PageHelper.startPage(page,limit);
        List<MarketKeyword> marketKeywords = marketKeywordMapper.selectByExample(keywordExample);
        //session.commit();
        //session.close();
        return marketKeywords;
    }

    @Override
    public MarketKeyword createKeyword(String keyword, String url, Boolean isDefault, Boolean isHot) {
      /*  SqlSession session1 = MybatisUtils.getSession();
        MarketKeywordMapper marketKeywordMapper1 = session1.getMapper(MarketKeywordMapper.class);*/
        MarketKeyword createMarketKeyword = new MarketKeyword();

        createMarketKeyword.setAddTime(LocalDateTime.now());
        createMarketKeyword.setIsDefault(isDefault);
        createMarketKeyword.setIsHot(isHot);
        createMarketKeyword.setKeyword(keyword);
        createMarketKeyword.setUpdateTime(LocalDateTime.now());
        createMarketKeyword.setUrl(url);

        marketKeywordMapper.insertSelective(createMarketKeyword);
   /*     session1.commit();
        session1.close();*/
        return createMarketKeyword;
    }

    @Override
    public MarketKeyword updateKeyword(Integer id,String updateTime,String keyword, String url, Boolean isDefault, Boolean isHot, String addTime,Boolean delete,Integer sortOrder) {
   /*     SqlSession session2 = MybatisUtils.getSession();
        MarketKeywordMapper marketKeywordMapper2 = session2.getMapper(MarketKeywordMapper.class);*/
        MarketKeyword updateMarketKeyword = new MarketKeyword();

        updateMarketKeyword.setUrl(url);
        updateMarketKeyword.setIsHot(isHot);
        updateMarketKeyword.setIsDefault(isDefault);
        updateMarketKeyword.setKeyword(keyword);
        updateMarketKeyword.setUpdateTime(LocalDateTime.now());
        updateMarketKeyword.setAddTime(LocalDateTime.now());
        updateMarketKeyword.setDeleted(delete);
        updateMarketKeyword.setSortOrder(sortOrder);
        updateMarketKeyword.setId(id);

        marketKeywordMapper.updateByPrimaryKey(updateMarketKeyword);
/*        session2.commit();
        session2.close();*/

        return updateMarketKeyword;
    }

    @Override
    public Integer deleteKeyword(Integer id) {
    /*    SqlSession session3 = MybatisUtils.getSession();
        MarketKeywordMapper marketKeywordMapper3 = session3.getMapper(MarketKeywordMapper.class);*/

        int deleteCount = marketKeywordMapper.deleteByPrimaryKey(id);

/*        session3.commit();
        session3.close();*/

        return deleteCount;
    }
}
