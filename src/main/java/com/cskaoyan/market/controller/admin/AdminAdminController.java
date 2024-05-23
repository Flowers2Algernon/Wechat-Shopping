package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.annotation.RequiresPermissionsDesc;
import com.cskaoyan.market.db.domain.MarketAdmin;
import com.cskaoyan.market.db.mapper.MarketStorageMapper;
import com.cskaoyan.market.service.admin.AdminAdminService;
import com.cskaoyan.market.util.ResponseUtil;
import com.cskaoyan.market.vo.MarketAdminCreateVo;
import com.cskaoyan.market.vo.MarketAdminListVo;
import com.cskaoyan.market.vo.MarketAdminUpdateVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("admin/admin")
public class AdminAdminController {
    @Autowired
    AdminAdminService adminAdminService;
    //    private AdminAdminService adminAdminService = new AdminAdminServiceImpl();
    @Autowired
    MarketStorageMapper marketStorageMapper;
    @Autowired
    SecurityManager securityManager;

    @RequiresPermissions(("admin:admin:delete"))
    @RequiresPermissionsDesc(menu = {"系统管理","管理员管理"},button = "删除")
    @PostMapping("delete")
    public Object delete(@RequestBody MarketAdmin marketAdmin, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        adminAdminService.delete(marketAdmin);
        Object ok = ResponseUtil.ok();
        return ok;
    }
    @RequiresPermissions("admin:admin:update")
    @RequiresPermissionsDesc(menu = {"系统管理","管理员管理"},button = "更新")
    @PostMapping("update")
    public Object update(@RequestBody MarketAdminUpdateVo marketAdminUpdateVo) {
        if (StringUtils.isEmpty(marketAdminUpdateVo.getUsername()) || StringUtils.isEmpty(marketAdminUpdateVo.getPassword())) {
            Object fail = ResponseUtil.fail(404, "用户名或密码不能为空");
            return fail;
        }
        if (!marketAdminUpdateVo.getUsername().matches("^[a-zA-Z].*")) {
            Object fail = ResponseUtil.fail(408, "用户名不符合规范");
            return fail;
        }
        adminAdminService.update(marketAdminUpdateVo);
        //需要将marketAdmin1对象转换为实体VO对象
        MarketAdminUpdateVo marketAdminUpdateVo1 = new MarketAdminUpdateVo(marketAdminUpdateVo.getId(), marketAdminUpdateVo.getUsername(), marketAdminUpdateVo.getAvatar(), marketAdminUpdateVo.getRoleIds(), marketAdminUpdateVo.getPassword());
        Object ok = ResponseUtil.ok(marketAdminUpdateVo1);
        //todo 此处update 还有问题，会返回400错误
        return ok;
    }
    @RequiresPermissions("admin:admin:create")
    @RequiresPermissionsDesc(menu = {"系统管理","管理员管理"},button = "添加")
    @PostMapping("create")
    public Object create(@RequestBody MarketAdmin marketAdmin) {
        //做一些校验工作
        if (StringUtils.isEmpty(marketAdmin.getUsername()) || StringUtils.isEmpty(marketAdmin.getPassword())) {
            Object fail = ResponseUtil.fail(404, "用户名或密码不能为空");
            return fail;
        }
        if (!marketAdmin.getUsername().matches("^[a-zA-Z].*")) {
            Object fail = ResponseUtil.fail(408, "用户名不符合规范");
            return fail;
        }
        LocalDateTime now = LocalDateTime.now();
        MarketAdmin marketAdmin1 = adminAdminService.create(marketAdmin.getUsername(), marketAdmin.getAvatar(), marketAdmin.getPassword(), null, marketAdmin.getRoleIds(), now, now);
        MarketAdminCreateVo marketAdminCreateVo = new MarketAdminCreateVo();
        marketAdminCreateVo.setUsername(marketAdmin.getUsername());
        marketAdminCreateVo.setId(marketAdmin1.getId());
        marketAdminCreateVo.setAvatar(marketAdmin.getAvatar());
        marketAdminCreateVo.setPassword(marketAdmin.getPassword());
        marketAdminCreateVo.setRoleIds(marketAdmin.getRoleIds());
        marketAdminCreateVo.setUpdateTime(LocalDateTime.now());
        marketAdminCreateVo.setAddTime(LocalDateTime.now());
        Object ok = ResponseUtil.ok(marketAdminCreateVo);
        return ok;
    }
    @RequiresPermissions("admin:admin:list")
    @RequiresPermissionsDesc(menu = {"系统管理","管理员管理"},button = "查询")
    @GetMapping("list")
    public Object list(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //此list方法中需要将请求参数获取到之后将其传递给相应的service层实现来获取对象
        String pageParam = req.getParameter("page");
        String limitParam = req.getParameter("limit");
        String sort = req.getParameter("sort");
        String order = req.getParameter("order");
        String username = req.getParameter("username");
        Integer page = null;
        Integer limit = null;
        try {
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
            if (limitParam != null) {
                limit = Integer.parseInt(limitParam);
            }
        } catch (Exception e) {
            //此时有非法输入
            return ResponseUtil.badArgument();
        }
        List<MarketAdminListVo> list = adminAdminService.list(limit, page, username, sort, order);
        Object o = ResponseUtil.okList(list);
        return o;
    }
}
