package com.cskaoyan.market.controller;

import com.cskaoyan.market.service.WxAuthService;
import com.cskaoyan.market.service.WxSearchService;
import com.cskaoyan.market.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("wx/search")
public class WxSearchController {

    @Autowired
    WxSearchService wxSearchService;

    @GetMapping("index")
    public Object index(){
        return ResponseUtil.ok(wxSearchService.searchIndex());
    }

    @GetMapping("helper")
    public Object helper(@RequestParam(required = false) String keyword){
        return ResponseUtil.ok(wxSearchService.searchHelper(keyword));
    }

    @PostMapping("clearhistory")
    public Object clearhistory(){
        try{
            wxSearchService.cleanHistory();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseUtil.badArgument();
        }
        return ResponseUtil.ok();
    }

}
