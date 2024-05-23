package com.cskaoyan.market.service.wx;

/**
 * @Author: jyc
 * @Date: 2024/5/21 20:53
 */

public interface WxUserService {
    int getUncomment(Integer integer);

    int getUnpaid(Integer integer);

    int getUnrecv(Integer integer);

    int getUnship(Integer integer);
}
