package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.db.domain.MarketAdmin;
import com.cskaoyan.market.db.domain.MarketPermission;
import com.cskaoyan.market.db.domain.MarketPermissionExample;
import com.cskaoyan.market.db.mapper.MarketPermissionMapper;
import com.cskaoyan.market.service.admin.MarketAdminService;
import com.cskaoyan.market.util.Permission;
import com.cskaoyan.market.util.PermissionUtil;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/3 16:26
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/auth")
public class AdminAuthController {

    @Autowired
    MarketAdminService adminService ;
    @Autowired
    private ApplicationContext context;
    @Autowired
    MarketPermissionMapper marketPermissionMapper;

    @PostMapping("logout")
    public Object logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        if(session!=null){
            session.invalidate();
        }
        Object ok = ResponseUtil.ok();
        return ok;
    }

    @PostMapping("login")
    //登录的流程：获取接收用户的请求参数信息；校验，系统中进行比对(调用service、mapper)，根据比对的结果返回一个回执信息
    public Object login(@RequestBody Map<String,String>map,HttpSession session,HttpServletRequest req) {
        //不能用，因为不是key=value&key=value格式
//        String username = req.getParameter("username");
        //下面两个方法都是可以获取请求体的，只不过第二个又进一步包装了一下，可以使用readLine方法，更加简化一些
//        req.getInputStream();
//         String requestBody = req.getReader().readLine();
        //System.out.println(requestBody);
        //可以使用之前介绍的方法，objectMapper.readValue使用一个对象来接收，便可以完成数据的封装工作
        //但是如果使用上述方案，那么需要去构建一个类Admin，此时我不想去额外再去构建一个类
        //可以使用objectMapper提供的另外的功能，将json字符串解析成为一棵树，去读取指定的节点
        //一般情况下，如果json字符串里面额度属性非常少，比如就一两个，那么此时额外构造一个类，就比较不划算，此时可以使用下面的方法来进行
        // {"username":"admin","password":"admin13"}或者{"id":5}建议直接使用下面的方法来去获取json字符串里面的数据，不需要准备额外构建一个类对象
        String username = map.get("username");
        String password = map.get("password");

        //做一些校验工作
        //isEmpty:==null或者length==0
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return ResponseUtil.fail(404, "用户名或者密码不能为空");
        }
        //调用模型的代码，也就是进一步去调用service，service进一步去调用mapper
        //返回一个对象还是返回一个数量，查询到的行数？？？？？？应该返回哪一个，取决于后面的业务有没有获取除了用户名以外的其他信息，比如头像等信息
        //返回一个MarketAdmin对象比较合适
        MarketAdmin marketAdmin = adminService.login(username, password);
        if(marketAdmin == null){
            return ResponseUtil.fail(605, "用户名或者密码不正确");
        }
        marketAdmin.setLastLoginIp(req.getRemoteAddr());
        marketAdmin.setLastLoginTime(LocalDateTime.now());
        adminService.updateById(marketAdmin);
        System.out.println("登录");
        //用户名、密码匹配------做到和公网具有相同的返回结果
        //设置响应体
        Map<String, Object> data = new HashMap<>();
        Map<String, String> adminInfo = new HashMap<>();

        adminInfo.put("nickName", marketAdmin.getUsername());
        adminInfo.put("avatar", marketAdmin.getAvatar());
        data.put("adminInfo", adminInfo);
        //token其实就是session域的编号
        session.setAttribute("admin", marketAdmin);
        data.put("token", session.getId());

        Object ok = ResponseUtil.ok(data);
        return ok;
//        resp.getWriter().println();
    }

    @GetMapping("info")
    public Object info(HttpSession session){
        //当前结果需要返回上述的json字符串
        //从session域里面获取
        MarketAdmin admin = (MarketAdmin) session.getAttribute("admin");
        if (admin != null) {
            Integer[] roleIds = admin.getRoleIds();
            Integer roleId = roleIds[0];
            System.out.println(roleId);
            Map<String, Object> data = new HashMap<>();
            if(roleId==1){
                data.put("roles", Arrays.asList("超级管理员"));
                data.put("name", admin.getUsername());
                data.put("perms", Arrays.asList("*"));
                data.put("avatar", admin.getAvatar());
                return ResponseUtil.ok(data);
            }else if(roleId==2){
                data.put("roles", Arrays.asList("商场管理员"));
                data.put("name", admin.getUsername());
                String[] mallPerms = {"GET /admin/order/list","POST /admin/issue/update","POST /admin/brand/delete"
                        ,"POST /admin/order/reply","POST /admin/order/delete","POST /admin/brand/create"
                        ,"POST /admin/order/refund","POST /admin/issue/delete","POST /admin/category/delete"
                        ,"GET /admin/category/read","POST /admin/keyword/delete","POST /admin/keyword/update"
                        ,"GET /admin/keyword/list","GET /admin/category/list","POST /admin/category/update"
                        ,"GET /admin/order/detail","GET /admin/issue/list","POST /admin/issue/create"
                        ,"GET /admin/brand/list","POST /admin/keyword/create","GET /admin/keyword/read"
                        ,"POST /admin/category/create","POST /admin/order/ship","GET /admin/brand/read"
                        ,"POST /admin/brand/update"};
                data.put("perms", Arrays.asList(mallPerms));
                data.put("avatar", admin.getAvatar());
                System.out.println("商场管理员");
                return ResponseUtil.ok(data);
            }else if(roleId==3){
                data.put("roles", Arrays.asList("推广管理员"));
                data.put("name", admin.getUsername());
                List<String> promotionPerms = Arrays.asList(
                        "GET /admin/ad/list",
                        "POST /admin/ad/delete",
                        "POST /admin/ad/create",
                        "POST /admin/ad/update",
                        "GET /admin/groupon/list",
                        "POST /admin/groupon/update",
                        "POST /admin/groupon/create",
                        "GET /admin/groupon/read",
                        "POST /admin/groupon/delete",
                        "POST /admin/topic/create",
                        "GET /admin/topic/read",
                        "GET /admin/topic/list",
                        "POST /admin/topic/delete",
                        "POST /admin/topic/update",
                        "GET /admin/coupon/list",
                        "POST /admin/coupon/delete",
                        "GET /admin/coupon/read",
                        "POST /admin/coupon/create",
                        "POST /admin/coupon/update"
                );
                data.put("perms", promotionPerms);
                data.put("avatar", admin.getAvatar());
                return ResponseUtil.ok(data);
            }
        }
        return ResponseUtil.fail(605, "用户名或者密码不正确");
    }

    private Set<String> getAssignedPermissions(Integer roleId) {
        String basicPackage = "com.cskaoyan.market.controller.admin";
        if(roleId==1){
            List<Permission> permissions = PermissionUtil.listPermission(context, basicPackage);
            Set<String> assignedPermission = PermissionUtil.listPermissionString(permissions);
            return assignedPermission;
        }else {
            MarketPermissionExample marketPermissionExample = new MarketPermissionExample();
            MarketPermissionExample.Criteria criteria = marketPermissionExample.createCriteria();
            criteria.andRoleIdEqualTo(roleId);
            List<MarketPermission> marketPermissions = marketPermissionMapper.selectByExample(marketPermissionExample);
            Set<String> assignedPermissionSet = new HashSet<>();
            for (MarketPermission marketPermission : marketPermissions) {
                assignedPermissionSet.add(marketPermission.getPermission());
            }
            return assignedPermissionSet;
        }
    }
}
