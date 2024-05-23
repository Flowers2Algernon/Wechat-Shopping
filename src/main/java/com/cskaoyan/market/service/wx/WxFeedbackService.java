package com.cskaoyan.market.service.wx;

import java.util.List;

public interface WxFeedbackService {
    void submit(String mobile, String content, String feedType, Boolean hasPicture, List<String> picUrl,
                Integer userId, String userName);
}
