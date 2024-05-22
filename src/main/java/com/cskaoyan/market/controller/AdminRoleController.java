package com.cskaoyan.market.controller;

import com.cskaoyan.market.db.domain.MarketAdmin;
import com.cskaoyan.market.db.domain.MarketRole;
import com.cskaoyan.market.db.domain.MarketRoleLabelOptions;
import com.cskaoyan.market.service.AdminRoleOptionsService;
import com.cskaoyan.market.service.MarketAdminRoleService;
import com.cskaoyan.market.service.impl.AdminRoleOptionsServiceImpl1;
import com.cskaoyan.market.util.JacksonUtil;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("admin/role")
public class AdminRoleController {
    @Autowired
    AdminRoleOptionsService adminRoleOptionsService;
    @Autowired
    MarketAdminRoleService marketAdminRoleService;

    @GetMapping("options")
    private Object options(HttpServletRequest req, HttpServletResponse resp) {
        //请求体中什么也没有，生成一个value和label的list当作data返回即可
        List<MarketRoleLabelOptions> list = adminRoleOptionsService.options();
        Object o = ResponseUtil.okList(list);
        return o;
    }

    @GetMapping("list")
    public Object list(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "limit", defaultValue = "20") Integer limit, @RequestParam(value = "name", defaultValue = "") String name, @RequestParam(value = "sort", defaultValue = "add_time") String sort, @RequestParam(value = "order", defaultValue = "desc") String order) {
        List<MarketRole> list = marketAdminRoleService.list(page, limit, name, sort, order);
        return ResponseUtil.okList(list);
    }

    @PostMapping("create")
    public Object create(@RequestBody Map map) {
        String name = (String) map.get("name");
        String desc = (String) map.get("desc");
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }
        MarketRole newRole = marketAdminRoleService.create(name, desc);
        return ResponseUtil.ok(newRole);
    }

    @PostMapping("delete")
    public Object delete(@RequestBody Map map) {
        Integer id = (Integer) map.get("id");
        marketAdminRoleService.delete(id);
        return ResponseUtil.ok();
    }

    @PostMapping("update")
    public Object update(@RequestBody Map map) {
        String name = (String) map.get("name");
        String desc = (String) map.get("desc");
        Integer id = (Integer) map.get("id");
        marketAdminRoleService.update(id, name, desc);
        return ResponseUtil.ok();
    }

    @PostMapping("permissions")
    public Object permissions(@RequestBody Map map) {
        Integer roleId = (Integer) map.get("roleId");
        Map<Integer, String> permissions = (Map<Integer, String>) map.get("permissions");
        marketAdminRoleService.postPermissions(roleId, permissions);
        return ResponseUtil.ok();
    }

    @GetMapping("permissions")
    public Object permissions(@RequestParam Integer roleId) {
        LinkedHashMap<Integer, String> assignedPermissions = marketAdminRoleService.getPermissions(roleId);
        //how to get full systemPermissions

        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("assignedPermissions", assignedPermissions);
        return null;
    }
}
