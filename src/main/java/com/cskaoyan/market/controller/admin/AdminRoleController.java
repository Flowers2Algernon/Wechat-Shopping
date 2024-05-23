package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.db.domain.MarketPermission;
import com.cskaoyan.market.db.domain.MarketPermissionExample;
import com.cskaoyan.market.db.domain.MarketRole;
import com.cskaoyan.market.db.domain.MarketRoleLabelOptions;
import com.cskaoyan.market.db.mapper.MarketPermissionMapper;
import com.cskaoyan.market.service.admin.AdminRoleOptionsService;
import com.cskaoyan.market.service.admin.MarketAdminRoleService;
import com.cskaoyan.market.util.Permission;
import com.cskaoyan.market.util.PermissionUtil;
import com.cskaoyan.market.util.ResponseUtil;
import com.cskaoyan.market.vo.PermVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("admin/role")
public class AdminRoleController {

    //扫描后台管理系统中的所有的controller组件
    private static final String YOUR_PACKAGE_NAME = "com.cskaoyan.market.controller.admin";

    private List<PermVo> systemPermissions;

    private Set<String> systemPermissionsString = null;

    //对于大家来说，完成当前接口需要做以下事情：
    //1.将后台管理系统中所有的接口加上@RequiresPermission @RequiresPermissionDesc 注解 getMAPPING PostMapping
    //2.给大家准备好的工具类整合到你的项目中，实现assignedPermissions的功能（如果是非超级管理员，非常简单，直接查询数据库即可；但是如果是超级管理员，那么需要将*转换成全部的权限信息，全部的权限信息可以从systemPermissions里面获取，可以通过debug调试）
    //3.这个接口写完之后，还需要去写一个/admin/auth/info接口，项目一是固定写死的，项目二需要根据对应的情况返回正确的接口
    @Autowired
    AdminRoleOptionsService adminRoleOptionsService;
    @Autowired
    MarketAdminRoleService marketAdminRoleService;
    @Autowired
    private ApplicationContext context;
    @Autowired
    MarketPermissionMapper marketPermissionMapper;

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
        List<String> permissions = (List<String>) map.get("permissions");
        marketAdminRoleService.postPermissions(roleId, permissions);
        return ResponseUtil.ok();
    }

    @GetMapping("permissions")
    public Object permissions(@RequestParam Integer roleId) {
        //系统的权限已经给大家写好了
        //获取到系统的所有的权限配置，也就是controller的handle方法上面标注的@RequiresPermissions注解
        List<PermVo> systemPermissions = getSystemPermissions();
        //当前用户所拥有的权限信息，数据库中当前用户存储的权限；其中需要注意的是如果是*，那么需要将其转换成systemPermission
        //因为前端无法识别出*
        //这部分需要大家去完成 完成当前角色所拥有的权限信息
        Set<String> assignedPermissions = getAssignedPermissions(roleId);

        Map<String,Object> data = new HashMap<>();
        data.put("assignedPermissions", assignedPermissions);
        data.put("systemPermissions",systemPermissions);
        return ResponseUtil.ok(data);
    }

    /**
     * 获取当前系统所有的权限
     */
    private List<PermVo> getSystemPermissions() {
        String basicPackage = YOUR_PACKAGE_NAME;
        if (systemPermissions == null) {
            //扫描当前系统中的所有的权限信息，主要是扫描@GetMapping @PostMapping @RequiresPermissions @RequiresPermissionDesc
            List<Permission> permissions = PermissionUtil.listPermission(context, basicPackage);
            systemPermissions = PermissionUtil.listPermVo(permissions);
            systemPermissionsString = PermissionUtil.listPermissionString(permissions);
        }
        return systemPermissions;
    }

    /**
     * 获取当前roleId所赋予的权限
     * 如果查询到的权限是*，则需要将其转换成系统权限，因为前端无法识别出*代表所有权限
     */
    private Set<String> getAssignedPermissions(Integer roleId) {
        String basicPackage = YOUR_PACKAGE_NAME;
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
