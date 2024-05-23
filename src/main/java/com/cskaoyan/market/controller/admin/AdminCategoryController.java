package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.annotation.RequiresPermissionsDesc;
import com.cskaoyan.market.bo.DeleteCategoryBo;
import com.cskaoyan.market.bo.UpdateCategoryBo;
import com.cskaoyan.market.db.domain.MarketCategory;
import com.cskaoyan.market.service.admin.CategoryService;
import com.cskaoyan.market.util.JacksonUtil;
import com.cskaoyan.market.util.ResponseUtil;
import com.cskaoyan.market.vo.L1Vo;
import com.cskaoyan.market.vo.ListVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/category")
public class AdminCategoryController {
    @Autowired
    CategoryService categoryService;

    @RequiresPermissions("admin:category:list")
    @RequiresPermissionsDesc(menu = {"商场管理","类目管理"},button = "查询")
    @GetMapping("list")
    public Object list(){
        List<ListVo> list = categoryService.list();
        return ResponseUtil.okList(list);
    }

    @GetMapping("l1")
    public Object l1(){
        List<L1Vo> l1VoList = categoryService.l1();
        return ResponseUtil.okList(l1VoList);
    }

    @RequiresPermissions("admin:category:delete")
    @RequiresPermissionsDesc(menu = {"商场管理","类目管理"},button = "删除")
    @PostMapping("delete")
    public Object delete(@RequestBody DeleteCategoryBo deleteCategoryBo) throws JsonProcessingException {
        // String requestBody = map.toString();
        // DeleteCategoryBo deleteCategoryBo = JacksonUtil.getObjectMapper().readValue(requestBody, DeleteCategoryBo.class);
        categoryService.delete(deleteCategoryBo);
        return JacksonUtil.writeValueAsString(ResponseUtil.ok());
    }
    @RequiresPermissions("admin:category:update")
    @RequiresPermissionsDesc(menu = {"商场管理","类目管理"},button = "编辑")
    @PostMapping("update")
    public Object update(@RequestBody UpdateCategoryBo updateCategoryBo) throws JsonProcessingException {
        // String requestBody = map.toString();
        // UpdateCategoryBo updateCategoryBo = JacksonUtil.getObjectMapper().readValue(requestBody, UpdateCategoryBo.class);

        categoryService.update(updateCategoryBo);
        return JacksonUtil.writeValueAsString(ResponseUtil.ok());
    }
    @RequiresPermissions("admin:category:create")
    @RequiresPermissionsDesc(menu = {"商场管理","类目管理"},button = "创建")
    @PostMapping("create")
    public Object create(@RequestBody MarketCategory marketCategory) throws JsonProcessingException {

        MarketCategory insertOne = categoryService.insertOne(marketCategory);
        return ResponseUtil.ok(insertOne);
    }

}
