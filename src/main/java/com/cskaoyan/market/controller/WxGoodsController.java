package com.cskaoyan.market.controller;

import com.cskaoyan.market.service.WxGoodsService;
import com.cskaoyan.market.util.ResponseUtil;
import com.cskaoyan.market.vo.MarketFloorGoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("wx/goods")
public class WxGoodsController {
    @Autowired
    WxGoodsService wxGoodsService;

    @GetMapping("detail")
    public Object detail(Integer id) {
        Map<String, Object> map = wxGoodsService.detail(id);
        return ResponseUtil.ok(map);
    }

    @GetMapping("related")
    public Object related(Integer id) {
        if (id == null) {
            return ResponseUtil.badArgument();
        }
        List<MarketFloorGoodsVo> list = wxGoodsService.related(id);
        return ResponseUtil.okList(list);
    }
}
