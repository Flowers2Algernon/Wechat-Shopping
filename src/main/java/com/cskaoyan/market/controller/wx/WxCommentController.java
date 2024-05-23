package com.cskaoyan.market.controller.wx;

import com.cskaoyan.market.annotation.RequiresPermissionsDesc;
import com.cskaoyan.market.db.domain.MarketComment;
import com.cskaoyan.market.db.domain.MarketUser;
import com.cskaoyan.market.service.wx.WxCommentService;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("wx/comment")
public class WxCommentController {
    @Autowired
    WxCommentService commentService;
    private final Log logger = LogFactory.getLog(WxCommentController.class);

    @GetMapping("list")
    public Object list(String valueId ,String limit,String page,Byte type,Byte showType)
    {
        Integer pageNumber = null;
        Integer limitNumber = null;
        try{
            pageNumber = Integer.parseInt(page);
            limitNumber = Integer.parseInt(limit);
        } catch (Exception e) {
            return ResponseUtil.badArgument();
        }
        /*int total = commentService.getTotalCount(valueId);
        int pages = (int) Math.ceil((double) total / limitNumber);
        Object comments = commentService.list(valueId,limitNumber,pageNumber,type,showType);
        Map<String ,Object> commentsList = new HashMap<>();
        commentsList.put("list",comments);
        commentsList.put("total",total);
        commentsList.put("pages",pages);
        commentsList.put("page",pageNumber);
        commentsList.put("limit",limitNumber);*/

        return ResponseUtil.okList(commentService.list(valueId, limitNumber, pageNumber, type,showType));
    }

    @GetMapping("count")
    public Object count(String valueId, Byte type) {
        Map<String, Object> count = commentService.count(valueId, type);
        return ResponseUtil.ok(count);
    }
    @PostMapping("post")
    public Object post(@RequestBody MarketComment marketComment, HttpSession session) {
        MarketUser user = (MarketUser) session.getAttribute("user");
        if (user == null) {
            return ResponseUtil.unlogin();
        }
        marketComment.setAddTime(LocalDateTime.now());
        marketComment.setUpdateTime(LocalDateTime.now());
        marketComment.setAdminContent("");
        marketComment.setDeleted(false);
        marketComment.setUserId(user.getId());
        if (commentService.post(marketComment)) {
            return ResponseUtil.ok(marketComment);
        }
        return ResponseUtil.fail(106, "添加评论失败");
    }
}
