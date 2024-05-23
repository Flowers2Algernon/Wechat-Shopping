package com.cskaoyan.market.controller.wx;

import com.cskaoyan.market.service.wx.WxCollectService;
import com.cskaoyan.market.util.ResponseUtil;
import com.cskaoyan.market.vo.CollectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/collect")
public class WxCollectController {

    @Autowired
    WxCollectService collectService;

    @GetMapping("list")
    public Object list(Integer type, Integer page, Integer limit){

            List<CollectVo> wxCollectList = collectService.list(type,page,limit);

            return ResponseUtil.okList(wxCollectList);
    }

    @PostMapping("addordelete")
    public Object addordelete(@RequestBody Map map){
        Integer type = (Integer) map.get("type");
        Integer valueId = (Integer) map.get("valueId");

        int res = collectService.addordelete(valueId,type);
        if(res<=0){
            return ResponseUtil.fail();
        }

        return ResponseUtil.ok();
    }


}
