package com.cskaoyan.market.service.wx;

public interface WxSearchService {

    public Object searchIndex();

    Object searchHelper(String keyword);

    void cleanHistory();
}
