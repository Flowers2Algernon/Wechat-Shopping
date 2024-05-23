package com.cskaoyan.market.controller;

import com.cskaoyan.market.annotation.RequiresPermissionsDesc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/category")
@Validated
public class AdminCategoryController {
    private final Log logger = LogFactory.getLog(AdminCategoryController.class);

    @RequiresPermissions("admin:category:list")
    @RequiresPermissionsDesc(menu = {"商场管理", "类目管理"}, button = "查询")
    @GetMapping("/list")
    public Object list() {

        return null;
    }

    @RequiresPermissions("admin:category:create")
    @RequiresPermissionsDesc(menu = {"商场管理", "类目管理"}, button = "添加")
    @PostMapping("/create")
    public Object create() {
        return null;
    }

    @RequiresPermissions("admin:category:read")
    @RequiresPermissionsDesc(menu = {"商场管理", "类目管理"}, button = "详情")
    @GetMapping("/read")
    public Object read() {
        return null;
    }

    @RequiresPermissions("admin:category:update")
    @RequiresPermissionsDesc(menu = {"商场管理", "类目管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update() {
        return null;
    }

    @RequiresPermissions("admin:category:delete")
    @RequiresPermissionsDesc(menu = {"商场管理", "类目管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete() {
       return null;
    }

    @RequiresPermissions("admin:category:list")
    @GetMapping("/l1")
    public Object catL1() {
       return null;
    }
}
