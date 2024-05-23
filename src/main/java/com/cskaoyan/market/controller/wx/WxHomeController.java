package com.cskaoyan.market.controller.wx;

import com.cskaoyan.market.service.wx.WxHomeService;
import com.cskaoyan.market.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("wx/home")
public class WxHomeController {

    @Autowired
    WxHomeService wxHomeService;


    @GetMapping("index")
    public Object index(){

        return ResponseUtil.ok(wxHomeService.mainHome());
    }
}
