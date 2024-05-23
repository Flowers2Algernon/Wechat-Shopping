package com.cskaoyan.market.service.admin;

import com.cskaoyan.market.db.domain.MarketIssue;

import java.util.List;

public interface MarketIssueService {
    List<MarketIssue> list(Integer limit, Integer page, String question, String sort, String order);

    MarketIssue createIssue(String question, String answer);

    MarketIssue updateIssue(Integer id, String question, String answer, String addTime, String updateTime,String delete);

    Integer deleteIssue(Integer id);




}
