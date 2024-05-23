package com.cskaoyan.market.controller.wx;

import com.cskaoyan.market.db.domain.MarketAddress;
import com.cskaoyan.market.service.wx.WxAddressService;
import com.cskaoyan.market.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wx/address")
public class WxAddressController {

    @Autowired
    WxAddressService addressService;

    @GetMapping("list")
    public Object list(){

        List<MarketAddress> wxAddressList = addressService.list();

        return ResponseUtil.okList(wxAddressList);

    }

    //没验证还
    @GetMapping("detail")
    public ResponseEntity<Object> detail(Integer id){

        Object addressDetail = addressService.detail(id);

        return ResponseEntity.ok(ResponseUtil.ok(addressDetail));


    }

    @PostMapping("save")
    public Object save(@RequestBody Map map){
        Integer id = (Integer) map.get("id");
        String name = (String) map.get("name");
        String tel = (String) map.get("tel");
        String province = (String) map.get("province");
        String city = (String) map.get("city");
        String county = (String) map.get("county");
        String areaCode = (String) map.get("areaCode");
        String addressDetail = (String) map.get("addressDetail");
        Object isDefault = map.get("isDefault");

        int res = addressService.save(id,name,tel,province,city,county,areaCode,addressDetail,isDefault);

        return ResponseEntity.ok(ResponseUtil.ok(res));
    }

    @PostMapping("delete")
    public Object delete(@RequestBody Map map){

        Integer id = (Integer) map.get("id");

        addressService.deleteAddress(id);

        return ResponseUtil.ok();

    }

}
