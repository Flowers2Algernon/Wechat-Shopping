package com.cskaoyan.market.bo;

import com.cskaoyan.market.db.domain.MarketCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCategoryBo {
    private List<MarketCategory> children;
    private String desc;
    private String iconUrl;
    private Integer id;
    private String keywords;
    private String level;
    private String name;
    private String picUrl;
}
