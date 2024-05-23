package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.annotation.RequiresPermissionsDesc;
import com.cskaoyan.market.db.domain.MarketUser;
import com.cskaoyan.market.db.mapper.MarketUserMapper;
import com.cskaoyan.market.service.admin.MarketUserService;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    private final MarketUserService userService;
    @Autowired
    private MarketUserMapper marketUserMapper;

    @Autowired
    public AdminUserController(MarketUserService userService) {
        this.userService = userService;
    }

    @RequiresPermissions("admin:user:list")
    @RequiresPermissionsDesc(menu ={"用户管理","会员管理"},button ="查询")
    @GetMapping("/list")
    public Object list(@RequestParam("page") Integer page,
                       @RequestParam("limit") Integer limit,
                       @RequestParam(value = "username", required = false) String username,
                       @RequestParam(value = "mobile", required = false) String mobile,
                       @RequestParam(value = "sort", required = false) String sort,
                       @RequestParam(value = "order", required = false) String order) {
        List<MarketUser> marketUserList = userService.list(limit, page, username, mobile, sort, order);
        return ResponseUtil.okList(marketUserList);
    }
    @RequiresPermissions("admin:user:detail")
    @RequiresPermissionsDesc(menu ={"用户管理","会员管理"},button ="详情")
    @GetMapping("/detail")
    public Object detail(@RequestParam("id") String id) {
        List<MarketUser> marketUserList = userService.getDataFromId(id);
        if (!marketUserList.isEmpty()) {
            return ResponseUtil.ok(marketUserList.get(0));
        } else {
            return ResponseUtil.fail(401,"User not found");
        }
    }
    @RequiresPermissions("admin:user:update")
    @RequiresPermissionsDesc(menu ={"用户管理","会员管理"},button ="编辑")
    @PostMapping("/update")
    public Object update(@RequestBody Map<String, Object> data) {
        Integer id = (Integer) data.get("id");
        String username = (String) data.get("username");
        String password = (String) data.get("password");
        String mobile = (String) data.get("mobile");
        String nickname = (String) data.get("nickname");
        String avatar = (String) data.get("avatar");
        Integer gender = (Integer) data.get("gender");
        Byte userLevel = data.get("userLevel") != null ? ((Integer) data.get("userLevel")).byteValue() : null;
        Byte status = data.get("status") != null ? ((Integer) data.get("status")).byteValue() : null;

        MarketUser marketUser = new MarketUser();
        marketUser.setId(id);
        marketUser.setUsername(username);
        marketUser.setPassword(password);
        marketUser.setMobile(mobile);
        marketUser.setNickname(nickname);
        marketUser.setAvatar(avatar);
        marketUser.setGender(gender.byteValue());
        marketUser.setUserLevel(userLevel);
        marketUser.setStatus(status);

        try {
            marketUserMapper.updateByPrimaryKeySelective(marketUser);
            return ResponseUtil.ok();
        } catch (Exception e) {
            System.out.println("Update failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user: " + e.getMessage());
        }
    }
}

