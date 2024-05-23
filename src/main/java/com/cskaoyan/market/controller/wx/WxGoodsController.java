package com.cskaoyan.market.controller.wx;

import com.cskaoyan.market.service.wx.WxGoodsService;
import com.cskaoyan.market.util.ResponseUtil;
import com.cskaoyan.market.vo.MarketGoodsCategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("wx/goods")
public class WxGoodsController {
    @Autowired
    WxGoodsService wxGoodsService;



    @GetMapping("category")
    @ResponseBody
    public Object category(Integer id){
        return wxGoodsService.category(id);

    }
    @GetMapping("list")
    public Object list( String keyword,Integer categoryId, Integer brandId,Integer page, Integer limit,
                        String sort,String order,Boolean isNew,Boolean isHot){
        return wxGoodsService.list(keyword,categoryId,brandId,page,limit,sort,order,isNew,isHot);
    }
    @GetMapping("count")
    public Object index(){
        return wxGoodsService.count();
    }
    @GetMapping("detail")
    public Object detail(Integer id){
        return wxGoodsService.goodsDetail(id);
    }

    @GetMapping("related")
    public Object related(@RequestParam int id){
        return ResponseUtil.okList(wxGoodsService.goodsRelated(id));
    }

}
