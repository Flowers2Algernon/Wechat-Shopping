package com.cskaoyan.market.cloud;

import java.io.InputStream;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/21 10:18
 * @Version 1.0 云服务的功能
 */
public interface CloudService {

    //一个是oss功能 文件上传
    String oss(InputStream inputStream);

    //一个是短信验证码功能
    void sms(String phoneNumber, String code);
}
