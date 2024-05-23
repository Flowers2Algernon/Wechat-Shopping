package com.cskaoyan.market.controller;

import com.cskaoyan.market.service.WxHomeService;
import com.cskaoyan.market.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("wx/home")
public class WxHomeController {
    @Autowired
    WxHomeService wxHomeService;
    @GetMapping("index")
    public Object index(){
        Map<String, Object> index = wxHomeService.index();
        return ResponseUtil.ok(index);
    }
}
