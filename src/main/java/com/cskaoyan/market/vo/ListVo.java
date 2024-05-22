package com.cskaoyan.market.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListVo {
    private String desc;
    private String iconUrl;
    private Integer id;
    private String keywords;
    private String level;
    private String name;
    private String picUrl;
    private List<ListVo> children;
}
