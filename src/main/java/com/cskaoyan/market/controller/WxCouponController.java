package com.cskaoyan.market.controller;

import com.cskaoyan.market.db.domain.MarketCoupon;
import com.cskaoyan.market.db.domain.MarketCouponUser;
import com.cskaoyan.market.db.domain.MarketUser;
import com.cskaoyan.market.db.mapper.MarketCouponUserMapper;
import com.cskaoyan.market.service.MarketCouponService;
import com.cskaoyan.market.service.MarketCouponUserService;
import com.cskaoyan.market.util.ResponseUtil;
import com.cskaoyan.market.vo.MarketCouponUserVo;
import com.cskaoyan.market.vo.MarketCouponVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("wx/coupon")
public class WxCouponController {
    @Autowired
    MarketCouponUserService marketCouponuserService;
    @Autowired
    MarketCouponService marketCouponService;
    @Autowired
    SecurityManager securityManager;

    @GetMapping("mylist")
    @RequiresAuthentication
    public Object mylist(@RequestParam("status") Short status, @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        MarketUser user = (MarketUser) session.getAttribute("user");
        Integer userId = user.getId();
        List<MarketCouponUserVo> list = marketCouponuserService.mylist(userId, status, page, limit);
        return ResponseUtil.okList(list);
    }

    @GetMapping("list")
    public Object list() {
        List<MarketCouponVo> list = marketCouponService.list();
        return ResponseUtil.okList(list);
    }

    @PostMapping("receive")
    @RequiresAuthentication
    public Object receive(@RequestBody Map map) {
        Integer couponId = (Integer) map.get("couponId");
        //核心逻辑是判断当前用户是否有该优惠券，没有则返回成功
        //有则返回已领取
        //现在遇到的问题是如何判断当前用户是谁
        //通过shiro来获取
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        MarketUser user = (MarketUser) session.getAttribute("user");
        Integer userId = user.getId();
        boolean receive = marketCouponuserService.receive(userId, couponId);
        if (receive) return ResponseUtil.fail(740, "优惠券已经领取过");
        return ResponseUtil.ok();
    }

    @PostMapping("exchange")
    @RequiresAuthentication
    public Object exchange(@RequestBody Map map) {
        String code = (String) map.get("code");
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        MarketUser user = (MarketUser) session.getAttribute("user");
        Integer userId = user.getId();
        Integer successCode = marketCouponuserService.exchange(userId, code);
        if (successCode==0){
            return  ResponseUtil.fail(742,"优惠券不正确");
        }else if (successCode==1){
            return ResponseUtil.fail(740,"优惠券已兑换");
        }else return ResponseUtil.ok();
    }
    @GetMapping("selectlist")
    @RequiresAuthentication
    public Object selectlist(){
        //todo 等做完购物车熟悉参数后再来做
        return null;
    }
}
