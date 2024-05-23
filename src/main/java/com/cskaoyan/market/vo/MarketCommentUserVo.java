package com.cskaoyan.market.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketCommentUserVo {
    private LocalDateTime addTime;
    private String adminContent;
    private String avatar;
    private String content;
    private Integer id;
    private String nickname;
    private String[] picUrls;
}
