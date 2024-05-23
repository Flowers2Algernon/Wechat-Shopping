package com.cskaoyan.market.controller.wx;

import com.cskaoyan.market.service.wx.WxBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("wx/brand")
public class WxBrandController {
    @Autowired
    WxBrandService wxBrandService;

    @GetMapping("detail")
    public Object brand(Integer id){
        return wxBrandService.detail(id);
    }
    @GetMapping("list")
    public Object list(Integer page,Integer limit){
        return wxBrandService.list(page,limit);

    }
}
