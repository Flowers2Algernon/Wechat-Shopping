package com.cskaoyan.market.aliyun;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/21 10:13
 * @Version 1.0
 */
@Component
@ConfigurationProperties(prefix = "aliyun")
@Data
public class AliyunProperties {

    private String accessKeyId;

    private String accessKeySecret;

    private AliyunOSS oss;

    private AliyunSms sms;
}
