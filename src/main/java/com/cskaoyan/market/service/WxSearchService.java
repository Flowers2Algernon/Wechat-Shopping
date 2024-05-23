package com.cskaoyan.market.service;

public interface WxSearchService {

    public Object searchIndex();

    Object searchHelper(String keyword);

    void cleanHistory();
}
