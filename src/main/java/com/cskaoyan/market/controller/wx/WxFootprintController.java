package com.cskaoyan.market.controller.wx;

import com.cskaoyan.market.db.domain.MarketComment;
import com.cskaoyan.market.db.domain.MarketFootprint;
import com.cskaoyan.market.service.wx.WxFootPrintService;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("wx/footprint")
public class WxFootprintController {
    @Autowired
    WxFootPrintService footPrintService;

    @GetMapping("list")
    public Object footPrintList(String limit,String page){
        Integer pageNumber;
        Integer limitNumber;
        try{
            pageNumber = Integer.parseInt(page);
            limitNumber = Integer.parseInt(limit);
        }catch(Exception e){
            return ResponseUtil.badArgument();
            // 获取总记录数和总页数
        }// 获取总记录数和总页数

        /*int total = footPrintService.getTotalCount(userId,goodsId );
        int pages = (int) Math.ceil((double) total / limitNumber);

        Object footprintList = footPrintService.list(limitNumber,pageNumber,userId,goodsId);
        Map<String, Object> result = new HashMap<>();
        result.put("page",pageNumber);
        result.put("limit",limitNumber);
        result.put("pages",pages);
        result.put("total",total);
        result.put("list",footprintList);
        return result;*/
        List<MarketFootprint> footprintList = footPrintService.list(limitNumber,pageNumber);
        return ResponseUtil.okList(footprintList);
    }
    @PostMapping("delete")
    public Object delete(@RequestBody MarketFootprint marketFootprint) {

        footPrintService.delete(marketFootprint.getId());
        return ResponseUtil.ok("成功");
    }
}
