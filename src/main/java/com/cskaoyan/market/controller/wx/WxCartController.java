package com.cskaoyan.market.controller.wx;

import com.cskaoyan.market.db.domain.MarketUser;
import com.cskaoyan.market.service.wx.WxCartService;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: jyc
 * @Date: 2024/5/21 23:27
 */
@RestController
@RequestMapping("wx/cart")
public class WxCartController {
    @Autowired
    WxCartService wxCartService;
    @Autowired
    SecurityManager securityManager;

    @GetMapping("goodscount")
    @RequiresAuthentication
    public Object goodsCount(){
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        MarketUser user = (MarketUser) session.getAttribute("user");
        if(user==null){
            return ResponseUtil.fail(666,"请登录哟");
        }
        Integer userId = user.getId();

        Integer count = 0;
        count = wxCartService.goodsCount(userId);
        return ResponseUtil.ok(count);
    }

    @PostMapping("add")
    @RequiresAuthentication
    public Object add(@RequestBody Map map){
        Integer goodsId = (Integer) map.get("goodsId");
        Integer number =  (Integer) map.get("number");
        Integer productId = (Integer) map.get("productId");

        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        MarketUser user = (MarketUser) session.getAttribute("user");
        if(user==null){
            return ResponseUtil.fail(666,"请登录哟");
        }
        Integer userId = user.getId();

        Integer success = wxCartService.add(goodsId,number.shortValue(),productId,userId);
        Integer integer = wxCartService.goodsCount(userId);
        if(success==0){
            return ResponseUtil.fail(888,"库存不够");
        }else {
            return ResponseUtil.ok(integer);
        }
    }

    @PostMapping("update")
    @RequiresAuthentication
    public Object update(@RequestBody Map<String,Integer> map){
        Integer goodsId = map.get("goodsId");
        Integer id = map.get("id");
        Integer number = map.get("number");
        Integer productId = map.get("productId");
        SecurityUtils.setSecurityManager(securityManager);

        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        MarketUser user = (MarketUser) session.getAttribute("user");
        if(user==null){
            return ResponseUtil.fail(666,"请登录哟");
        }
        Integer userId = user.getId();

        boolean success = wxCartService.update(goodsId,id,number,productId,userId);
        if(success){
            return ResponseUtil.ok();
        }else {
            return ResponseUtil.fail(888,"库存不够");
        }
    }

    @PostMapping("delete")
    @RequiresAuthentication
    public Object delete(@RequestBody Map<String, List<Integer>> map){
        List<Integer> productIds = map.get("productIds");

        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        MarketUser user = (MarketUser) session.getAttribute("user");
        if(user==null){
            return ResponseUtil.fail(666,"请登录哟");
        }
        Integer userId = user.getId();

        wxCartService.delete(productIds,userId);
        return ResponseUtil.ok(wxCartService.index(userId));
    }

    @GetMapping("index")

    public Object index(){
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        MarketUser user = (MarketUser) session.getAttribute("user");
        if(user==null){
            return ResponseUtil.fail(666,"请登录哟");
        }
        Integer userId = user.getId();
        Map<String,Object> data = wxCartService.index(userId);
        return ResponseUtil.ok(data);
    }

    @PostMapping("checked")
    @RequiresAuthentication
    public Object checked(@RequestBody Map map){
        Integer isChecked = (Integer)map.get("isChecked");
        List<Integer> productIds = (List<Integer>) map.get("productIds");

        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        MarketUser user = (MarketUser) session.getAttribute("user");
        if(user==null){
            return ResponseUtil.fail(666,"请登录哟");
        }
        Integer userId = user.getId();

        wxCartService.checked(isChecked,productIds,userId);
        return ResponseUtil.ok(wxCartService.index(userId));
    }

    @PostMapping("fastadd")
    @RequiresAuthentication
    public Object fastadd(@RequestBody Map map){
        Integer goodsId = (Integer) map.get("goodsId");
        Integer number =  (Integer) map.get("number");
        Integer productId = (Integer) map.get("productId");

        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        MarketUser user = (MarketUser) session.getAttribute("user");
        if(user==null){
            return ResponseUtil.fail(666,"请登录哟");
        }
        Integer userId = user.getId();

        Integer cartId = wxCartService.addFast(goodsId,number.shortValue(),productId,userId);
        if(cartId==0){//todo 此处存在fastAdd后cartId为null的情况
            return ResponseUtil.fail(888,"库存不够");
        }else {
            return ResponseUtil.ok(cartId);
        }
    }

    @GetMapping("checkout")
    @RequiresAuthentication
    public Object checkout(@RequestParam("cartId") Integer cartId, @RequestParam("addressId") Integer addressId, @RequestParam("couponId") Integer couponId, @RequestParam("userCouponId") Integer userCouponId, @RequestParam("grouponRulesId") Integer grouponRulesId) {
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        MarketUser user = (MarketUser) session.getAttribute("user");
        if (user == null) {
            return ResponseUtil.fail(666, "请登录哟");
        } else {
            Integer userId = user.getId();
            if (cartId == 0) {
                //通过购物车下单的,此时需要根据用户选中的信息来下单
                Map<String, Object> map = wxCartService.cartCheckout(addressId, couponId, userCouponId, grouponRulesId,userId);
                session.setAttribute("checkoutMoney",map.get("orderTotalPrice"));
                return ResponseUtil.ok(map);
            } else {
//                通过fastadd下单的，直接按照fastadd的请求goodsId和Number、productid来下单
                Map<String, Object> map = wxCartService.fastCheckout(cartId,addressId, couponId, userCouponId, grouponRulesId,userId);
                session.setAttribute("checkoutMoney",map.get("orderTotalPrice"));
                return ResponseUtil.ok(map);
            }
        }
    }
}
