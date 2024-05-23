package com.cskaoyan.market.service.admin;

import com.cskaoyan.market.bo.AddGoodsBo;
import com.cskaoyan.market.db.domain.MarketGoods;
import com.cskaoyan.market.db.domain.MarketGoodsAttribute;
import com.cskaoyan.market.db.domain.MarketGoodsProduct;
import com.cskaoyan.market.db.domain.MarketGoodsSpecification;
import com.cskaoyan.market.vo.CatAndBrandVo;
import com.cskaoyan.market.vo.MarketGoodsVo;

import java.util.List;
import java.util.Map;

/**
 * @ClassName GoodsService
 * @Description: TODO
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/3/16 9:51
 * @Version V1.0
 **/
public interface MarketGoodsService {
    List<MarketGoods> list(Integer page, Integer limit, String goodsSn, String goodsId, String name, String sort, String order);

    Map<String, List<CatAndBrandVo>> catAndBrand();
    MarketGoodsVo detail(Integer goodsId);//根据goodsid从数据库中得到详情

    void insertOne(MarketGoods goods, List<MarketGoodsSpecification> specifications, List<MarketGoodsProduct> products, List<MarketGoodsAttribute> attributes);
    void delete(Integer id);

    boolean updated(AddGoodsBo goodsBo);
}
