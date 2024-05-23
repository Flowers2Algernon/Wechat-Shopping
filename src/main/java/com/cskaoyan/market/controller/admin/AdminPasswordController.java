package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.db.domain.MarketAdmin;
import com.cskaoyan.market.service.admin.AdminPasswordService;
import com.cskaoyan.market.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * @Author: jyc
 * @Date: 2024/5/7 14:50
 * 修改密码
 */
@RestController
@RequestMapping("/admin/profile/password")
public class AdminPasswordController {

    @Autowired
    AdminPasswordService adminPasswordService;

    @PostMapping
    public Object password(@RequestBody Map<String,String> map,HttpServletRequest request) throws ServletException, IOException {
        String oldPassword = map.get("oldPassword");
        String newPassword = map.get("newPassword");

        HttpSession session = request.getSession();
        MarketAdmin admin = (MarketAdmin) session.getAttribute("admin");
        if(!admin.getPassword().equals(oldPassword)){
            Object fail = ResponseUtil.fail(602, "原密码不正确");
            return fail;
        }
        admin.setPassword(newPassword);
        adminPasswordService.changePassword(admin);
        Object ok = ResponseUtil.ok();
        return ok;
    }
}
