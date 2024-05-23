package com.cskaoyan.market.controller.wx;

import com.cskaoyan.market.db.domain.MarketUser;
import com.cskaoyan.market.service.wx.WxCartService;
import com.cskaoyan.market.service.wx.WxOrderService;
import com.cskaoyan.market.util.ResponseUtil;
import com.cskaoyan.market.vo.OrderDetailVo;
import com.cskaoyan.market.vo.OrderListVo;
import com.cskaoyan.market.vo.OrderSubmitVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 薛松 xuesong_work@163.com
 * @Date: 2024/05/20/17:31
 * @Description:
 */
@RestController
@RequestMapping("wx/order")
public class WxOrderController {
    @Autowired
    WxOrderService wxOrderService;
    @Autowired
    WxCartService wxCartService;
    @Autowired
    SecurityManager securityManager;

    @PostMapping("submit")
    @RequiresAuthentication
    public Object submit(@RequestBody Map map) {
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        MarketUser user = (MarketUser) session.getAttribute("user");
        if (user == null) {
            return ResponseUtil.fail(501, "请登录");
        } else {
            Integer userId = user.getId();
            Integer addressId = (Integer) map.get("addressId");
            Integer cartId = (Integer) map.get("cartId");
            Integer couponId = (Integer) map.get("couponId");
            Integer grouponLinkId = (Integer) map.get("grouponLinkId");
            Integer grouponRulesId = (Integer) map.get("grouponRulesId");
            String message = (String) map.get("message");
            Integer userCouponId = (Integer) map.get("userCouponId");
            BigDecimal money = (BigDecimal) session.getAttribute("checkoutMoney");
            Integer orderId = wxOrderService.submit(addressId, cartId, couponId, grouponRulesId, grouponLinkId, message, userCouponId, userId,money);
            if (orderId==-1){
                //已经提交过了订单
                return ResponseUtil.fail(702,"此订单已提交");
            }
            HashMap<String, Integer> resultMap = new HashMap<>();
            resultMap.put("grouponLinkId",grouponLinkId);
            resultMap.put("orderId",orderId);
            return ResponseUtil.ok(resultMap);
        }
    }

    // 订单列表
    @GetMapping("list")
    public Object list(@RequestParam(value = "showType", required = false) Integer showType,
                       @RequestParam(value = "page", required = false) Integer page,
                       @RequestParam(value = "limit", required = false) Integer limit,
                       @SessionAttribute(value = "user", required = false) MarketUser user) {

        try {
            List<OrderListVo> orderList = wxOrderService.list(user, showType, page, limit);
            if (orderList != null && !orderList.isEmpty()) {
                return ResponseUtil.okList(orderList); // 假设ResponseUtil是一个工具类，用于封装响应数据
            } else {
                return ResponseUtil.ok(); // 如果订单列表为空，也可以返回一个成功但无数据的响应
            }
        } catch (Exception e) {
            // 异常处理，例如登录检查失败等
            return ResponseUtil.fail(); // 返回错误信息
        }
    }

    //订单详情
    @GetMapping("detail")
    public Object detail(@RequestParam(value = "orderId", required = false) Integer orderId) {
        try {

            OrderDetailVo detail = wxOrderService.detail(orderId);
            if (detail != null) {
                return ResponseUtil.ok(detail);

            } else {
                return ResponseUtil.ok();
            }
        } catch (Exception e) {
           e.printStackTrace();
            return ResponseUtil.ok();
        }
    }

    //取消订单
    @PostMapping("cancel")
    public Object cancel(@RequestBody Map map) {
        Integer orderId = (Integer) map.get("orderId");
        boolean cancel = wxOrderService.cancel(orderId);
        return cancel ? ResponseUtil.ok() : ResponseUtil.fail(-1, "取消失败");
    }

    //付款
    @PostMapping("prepay")
    public Object prepay(@RequestBody Map map) {
        Integer orderId = (Integer) map.get("orderId");
        boolean prepay = wxOrderService.prepay(orderId);
        return prepay ? ResponseUtil.ok() : ResponseUtil.fail(-1, "支付失败");
    }
    //提交购物车
//    @PostMapping("submit")
//    public Object submit(@RequestBody OrderSubmitVo orderSubmitVo) {
//        try {//todo 验证用户是否登录
//        /*if(){
//            return ResponseUtil.fail(-1, "用户未登录");
//        }*/
//            //todo 调用  返回{"errno":0,"data":{"orderId":390,"grouponLinkId":0},"errmsg":"成功"}
//            //wxOrderService.submit(orderSubmitVo, loginUser);
//
//            return ResponseUtil.ok();
//        }catch (Exception e) {
//            return ResponseUtil.fail(-1, "提交失败");
//        }
//
//    }



    //删除订单
    @PostMapping("delete")
    public Object delete(@RequestBody Map map) {
        Integer orderId = (Integer) map.get("orderId");
        boolean delete = wxOrderService.delete(orderId);
        return delete ? ResponseUtil.ok() : ResponseUtil.fail(-1, "删除失败");

    }

    //申请退款
    @PostMapping("refund")
    public Object refund(@RequestBody Map map) {
        Integer orderId = (Integer) map.get("orderId");
        boolean refund = wxOrderService.refund(orderId);
        return refund ? ResponseUtil.ok() : ResponseUtil.fail(-1, "退款失败");
    }

    //订单收货 确认收货
    @PostMapping("confirm")
    public Object confirm(@RequestBody Map map) {
        Integer orderId = (Integer) map.get("orderId");
        boolean confirm = wxOrderService.confirm(orderId);
        return confirm ? ResponseUtil.ok() : ResponseUtil.fail(-1, "确认收款失败");
    }

    //订单评价 商品详情
    @GetMapping("goods")
    public Object goods(@RequestParam(value = "orderId", required = false) Integer orderId,
                        @RequestParam(value = "goodsId", required = false) Integer goodsId) {
        return ResponseUtil.ok(wxOrderService.goods(orderId, goodsId));

    }

    //订单评价
    @PostMapping("comment")
    public Object comment(@RequestBody Map map) {
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        if (session != null) {
            MarketUser user = (MarketUser) session.getAttribute("user");
            if (user != null) {
                String content = (String) map.get("content");
                Boolean hasPicture = (Boolean) map.get("hasPicture");
                Integer orderGoodsId = (Integer) map.get("orderGoodsId");
                Integer star = (Integer) map.get("star");
                String[] picUrls = new String[]{""};
                if (hasPicture) {
                    picUrls = (String[]) map.get("picUrls");
                }
                Integer comment = wxOrderService.comment(user.getId(), content, hasPicture, orderGoodsId, star, picUrls);
                if (comment != null) {
                    return ResponseUtil.ok();
                } else {
                    return ResponseUtil.fail();
                }
            }

        }
        return null;
    }
}