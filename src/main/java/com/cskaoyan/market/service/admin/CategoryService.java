package com.cskaoyan.market.service.admin;

import com.cskaoyan.market.bo.DeleteCategoryBo;
import com.cskaoyan.market.bo.UpdateCategoryBo;
import com.cskaoyan.market.db.domain.MarketCategory;
import com.cskaoyan.market.vo.L1Vo;
import com.cskaoyan.market.vo.ListVo;

import java.util.List;

public interface CategoryService {
    List<ListVo> list();
    List<L1Vo> l1();

    MarketCategory insertOne(MarketCategory marketCategory);

    void update(UpdateCategoryBo updateCategoryBo);

    void delete(DeleteCategoryBo deleteCategoryBo);
}
