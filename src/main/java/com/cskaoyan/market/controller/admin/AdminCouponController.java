package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.annotation.RequiresPermissionsDesc;
import com.cskaoyan.market.db.domain.MarketCoupon;
import com.cskaoyan.market.service.admin.MarketCouponService;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 薛松 xuesong_work@163.com
 * @Date: 2024/05/06/17:17
 * @Description:优惠券模块
 */
@RestController
@RequestMapping("/admin/coupon")
public class AdminCouponController {
    @Autowired
    private MarketCouponService couponService;
   /* @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String op = requestURI.replace(req.getContextPath() + "/admin/coupon/", "");
        if("update".equals(op)){
            update(req,resp);
        } else if ("create".equals(op)) {
            create(req,resp);
        }else if ("delete".equals(op)) {
            delete(req,resp);
        }
    }*/

    @RequiresPermissions("admin:coupon:create")
    @RequiresPermissionsDesc(menu = {"推广管理", "优惠券管理"}, button = "添加")
    @PostMapping("create")
    public Object create(@RequestBody MarketCoupon marketCoupon) throws IOException {
        //String requestBody = req.getReader().readLine();
        //MarketCoupon marketCoupon = JacksonUtil.getObjectMapper().readValue(requestBody, MarketCoupon.class);

        MarketCoupon createCoupon = couponService.create(marketCoupon.getName(),marketCoupon.getDesc(),marketCoupon.getTag()
                ,marketCoupon.getTotal(),marketCoupon.getDiscount(),marketCoupon.getMin(),marketCoupon.getLimit(),marketCoupon.getType()
                ,marketCoupon.getStatus(),marketCoupon.getGoodsType(),marketCoupon.getGoodsValue(),marketCoupon.getTimeType(),marketCoupon.getDays(),
                marketCoupon.getStartTime(),marketCoupon.getEndTime(),marketCoupon.getAddTime(),marketCoupon.getUpdateTime());
        if(createCoupon!=null){
            return ResponseUtil.ok(createCoupon);
            //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));
        }else {
            return ResponseUtil.fail();
            //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));
        }


    }

    @RequiresPermissions("admin:coupon:delete")
    @RequiresPermissionsDesc(menu = {"推广管理", "优惠券管理"}, button = "删除")
    @PostMapping("delete")
    public Object delete(@RequestBody MarketCoupon marketCoupon) throws IOException {
        //String requestBody = req.getReader().readLine();

        //MarketCoupon marketCoupon = JacksonUtil.getObjectMapper().readValue(requestBody, MarketCoupon.class);

        boolean delete = couponService.delete(marketCoupon.getId());

        return  delete ? ResponseUtil.ok() : ResponseUtil.fail(-1, "删除失败");
        //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));

    }

    @RequiresPermissions("admin:coupon:update")
    @RequiresPermissionsDesc(menu = {"推广管理", "优惠券管理"}, button = "编辑")
    @PostMapping("update")
    public Object update(MarketCoupon marketCoupon) throws IOException {
        //String requestBody = req.getReader().readLine();
        //MarketCoupon marketCoupon = JacksonUtil.getObjectMapper().readValue(requestBody, MarketCoupon.class);

        //daytype?code?
        MarketCoupon updateCoupon = couponService.update(marketCoupon.getId(),marketCoupon.getName(),marketCoupon.getDesc(),marketCoupon.getTag(),marketCoupon.getTotal(),
                marketCoupon.getDiscount(),marketCoupon.getMin(),marketCoupon.getLimit(),marketCoupon.getType(),marketCoupon.getStatus(),marketCoupon.getGoodsType(),
                marketCoupon.getGoodsValue(),marketCoupon.getCode(),marketCoupon.getTimeType(),marketCoupon.getDays(),marketCoupon.getStartTime(),marketCoupon.getEndTime(),
                marketCoupon.getAddTime(),marketCoupon.getUpdateTime(),marketCoupon.getDeleted());
        if (updateCoupon != null) {
            return ResponseUtil.ok(updateCoupon);
            //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));
        } else {
            return ResponseUtil.fail();
            //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));
        }


    }

   /* @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String op = requestURI.replace(req.getContextPath() + "/admin/coupon/", "");
        if("list".equals(op)){
            list(req,resp);
        }
        else if("read".equals(op)){
            read(req,resp);
        }
    }*/


    //展示所有的优惠券信息，分页查询
    //逻辑：根据用户输入的查询条件，查询到符合条件的与用户信息列表，但需要注意使用分页操作，还需要设定一个排序规则

    @RequiresPermissions("admin:coupon:list")
    @RequiresPermissionsDesc(menu = {"推广管理", "优惠券管理"}, button = "查询")
    @GetMapping("list")
    public Object list(HttpServletRequest req) throws IOException {
        //路径测试
        //System.out.println("list");

        //1.接收用户提交的请求参数信息
        String pageParm = req.getParameter("page");
        String limitParm = req.getParameter("limit");
        String name = req.getParameter("name");//优惠券名称
        String status = req.getParameter("status");//优惠券范围
        String type = req.getParameter("type");//优惠券类型
        String sort = req.getParameter("sort");
        String order = req.getParameter("order");

        Integer page = null;
        Integer limit = null;
        try {
            page = Integer.parseInt(pageParm);
            limit = Integer.parseInt(limitParm);

        }catch (NumberFormatException e){
            return  ResponseUtil.badArgument();//401参数有问题
            /*String s = JacksonUtil.getObjectMapper().writeValueAsString(object);
            resp.getWriter().println(s);
            return;*/
        }
        //2.处理业务逻辑
        List<MarketCoupon>marketCouponList = couponService.list(page,limit,name,status,type,sort,order);

        //3.返回响应
        return ResponseUtil.okList(marketCouponList);
        //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));
    }
    @RequiresPermissions("admin:coupon:read")
    @RequiresPermissionsDesc(menu = {"推广管理", "优惠券管理"}, button = "详情")
    @GetMapping("read")
    public Object read(String idParm) throws IOException {
        //String idParm = req.getParameter("id");
        Integer id = null;
        try {
            id = Integer.parseInt(idParm);
        } catch (NumberFormatException e) {
            return ResponseUtil.badArgument();//401参数有问题
           /* String s = JacksonUtil.getObjectMapper().writeValueAsString(object);
            resp.getWriter().println(s);
            return;*/
        }

        MarketCoupon marketCoupon = couponService.read(id);

        if(marketCoupon == null){
            return ResponseUtil.fail(404, "优惠券不存在");
            /*String s = JacksonUtil.getObjectMapper().writeValueAsString(fail);
            resp.getWriter().println(s);*/
        }

        return ResponseUtil.ok(marketCoupon);
        //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));

    }
}
