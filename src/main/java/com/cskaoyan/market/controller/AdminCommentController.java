package com.cskaoyan.market.controller;
import com.cskaoyan.market.db.domain.MarketComment;
import com.cskaoyan.market.service.MarketCommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import com.cskaoyan.market.db.domain.MarketComment;
import com.cskaoyan.market.service.MarketCommentService;
import com.cskaoyan.market.service.impl.MarketCommentServiceImpl;
import com.cskaoyan.market.util.JacksonUtil;
import com.cskaoyan.market.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/comment")
public class AdminCommentController {
    //http://localhost:8083/admin/comment/
    // list?page=1&limit=20&userId=1&valueId=2&sort=add_time&order=desc
    //请求方法:GET
    @Autowired
    private MarketCommentService commentService;
    /*@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqestURI = req.getRequestURI();
        String op = reqestURI.replace(req.getContextPath()+ "/admin/comment/", "");
        if("list".equals(op)){
            list(req, resp);
        }
    }*/

    /*@RequestParam(value = "userId", required = false) String userId,
@RequestParam(value = "valueId", required = false) String valueId,*/
    @GetMapping("/list")

    public Object list(String page, String limit,String userId,String valueId, String sort, String order) {
        Integer pageNumber = null;
        Integer limitNumber = null;
        try {
            pageNumber = Integer.parseInt(page);
            limitNumber = Integer.parseInt(limit);
        } catch (Exception e) {
            return ResponseUtil.badArgument();
        }
        List<MarketComment> comments = commentService.list(pageNumber, limitNumber, userId, valueId,sort, order);
        // 获取总记录数和总页数
        int total = commentService.getTotalCount(userId, valueId);
        int pages = (int) Math.ceil((double) total / limitNumber);

        // 构建响应对象
        Map<String, Object> result = new HashMap<>();
        result.put("list", comments);
        result.put("pages", pages);
        result.put("total", total);
        result.put("limit", limitNumber);
        result.put("page", pageNumber);

        return ResponseUtil.ok(result);
        //return result;
    }/*
    private void list(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pageParam = req.getParameter("page");
        String limitParam = req.getParameter("limit");
        String userId = req.getParameter("userId");
        String valueId = req.getParameter("valueId");
        String sort = req.getParameter("sort");
        String order = req.getParameter("order");

        Integer limit = null;
        Integer page = null;
        try {
            limit = Integer.parseInt(limitParam);
            page = Integer.parseInt(pageParam);
        } catch (NumberFormatException e) {
            resp.getWriter().println(JacksonUtil.writeValueAsString(ResponseUtil.badArgument()));
            return;
        }
        List<MarketComment> commentList = commentService.list(page, limit,userId, valueId,sort, order);
        Object o = ResponseUtil.okList(commentList);
        resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));

    }*/
@PostMapping("/delete")
public void delete(@RequestBody MarketComment comment) {

    commentService.delete(comment.getId());
}
   /* @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqestURI = req.getRequestURI();
        String op = reqestURI.replace(req.getContextPath()+ "/admin/comment/", "");
        if("delete".equals(op)){
            delete(req, resp);
        }
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ObjectMapper objectMapper = JacksonUtil.getObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        MarketComment comment = objectMapper.readValue(req.getReader(), MarketComment.class);
        String id = String.valueOf(comment.getId());
        //boolean deleted = true;

        commentService.delete(Integer.valueOf(id));
        //Object o = ResponseUtil.ok(trueDeleted);
        resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(ResponseUtil.ok()));
    }*/

}
