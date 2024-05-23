package com.cskaoyan.market.controller;

import com.cskaoyan.market.util.Permission;
import com.cskaoyan.market.util.PermissionUtil;
import com.cskaoyan.market.vo.PermVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @ClassName AdminRoleController
 * @Description: TODO
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2023/4/6 9:38
 * @Version V1.0
 **/
@RestController
@RequestMapping("admin/role")
public class AdminRoleController {

    //TODO 将此处替换成你的controller包名
//    private static final String YOUR_PACKAGE_NAME = "com.cskaoyan.market.controller";

    private static final String YOUR_PACKAGE_NAME = "com.cskaoyan.market.controller.admin";

    private List<PermVo> systemPermissions;

    private Set<String> systemPermissionsString = null;


    @Autowired
    private ApplicationContext context;

    @RequestMapping("permission")
    public Object permission(Integer roleId){
        //系统的权限已经给大家写好了
        //获取到系统的所有的权限配置，也就是controller的handle方法上面标注的@RequiresPermissions注解
        List<PermVo> systemPermissions = getSystemPermissions();
        //当前用户所拥有的权限信息，数据库中当前用户存储的权限；其中需要注意的是如果是*，那么需要将其转换成systemPermission
        //因为前端无法识别出*
        Set<String> assignedPermissions = getAssignedPermissions(roleId);

        return null;
    }

    /**
     * 获取当前系统所有的权限
     * @return
     */
    private List<PermVo> getSystemPermissions() {
        String basicPackage = YOUR_PACKAGE_NAME;
        if (systemPermissions == null) {
            List<Permission> permissions = PermissionUtil.listPermission(context, basicPackage);
            systemPermissions = PermissionUtil.listPermVo(permissions);
            systemPermissionsString = PermissionUtil.listPermissionString(permissions);
        }
        return systemPermissions;
    }

    /**
     * 获取当前roleId所赋予的权限
     * 如果查询到的权限是*，则需要将其转换成系统权限，因为前端无法识别出*代表所有权限
     * @param roleId
     * @return
     */
    private Set<String> getAssignedPermissions(Integer roleId) {
        return null;
    }


}
