package com.cskaoyan.market.controller;

import com.cskaoyan.market.service.WxSearchService;
import com.cskaoyan.market.service.WxTopicService;
import com.cskaoyan.market.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("wx/topic")
public class WxTopicController {

    @Autowired
    WxTopicService wxTopicService;


    @GetMapping("list")
    public Object list(){

        return ResponseUtil.okList(wxTopicService.topicList());
    }
    @GetMapping("related")
    public Object related(@RequestParam int id){
        return ResponseUtil.okList(wxTopicService.topicRelated(id));

    }
    @GetMapping("detail")
    public Object detail(@RequestParam int id){
        return ResponseUtil.ok(wxTopicService.topicDetail(id));
    }



}
