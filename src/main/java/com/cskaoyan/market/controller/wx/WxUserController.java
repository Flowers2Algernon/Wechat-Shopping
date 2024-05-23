package com.cskaoyan.market.controller.wx;

import com.cskaoyan.market.db.domain.MarketUser;
import com.cskaoyan.market.service.wx.WxUserService;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("wx/user")
public class WxUserController {

    @Autowired
    SecurityManager securityManager;
    @Autowired
    WxUserService wxUserService;

    @GetMapping("index")
    // //当前接口需要认证才可以访问
    // @RequiresAuthentication
    public Object index(){
        //获取自定义的请求头的值
        //直接从session域中获取数据
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        MarketUser user = (MarketUser) session.getAttribute("user");
        //此时可以访问到user具体的内容
        //下一步就是获取相应user的订单状况
        if(user==null){
            return ResponseUtil.fail(666,"请登录哟");
        }
        //业务要求是返回订单的数量，前提是要求能够知道当前的会话是谁，所以需要从session域里面获取数据
        // 订单状态对应的状态码
        // 101: '未付款', 102: '用户取消', 103: '系统取消', 201: '已付款', 202: '申请退款',
        // 203: '已退款', 301: '已发货', 401: '用户收货', 402: '系统收货'
        Map<String, Object> data = new HashMap<>();
        Map<String, String> order = new HashMap<>();
        Integer userId = user.getId();
        int uncomment = wxUserService.getUncomment(userId);
        int unpaid = wxUserService.getUnpaid(userId);
        int unrecv = wxUserService.getUnrecv(userId);
        int unship = wxUserService.getUnship(userId);

        order.put("uncomment", Integer.toString(uncomment));
        order.put("unpaid", Integer.toString(unpaid));
        order.put("unrecv", Integer.toString(unrecv));
        order.put("unship", Integer.toString(unship));

        data.put("order",order);

        Object ok = ResponseUtil.ok(data);
        return ok;
    }
}
