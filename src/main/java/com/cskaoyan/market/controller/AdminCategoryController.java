package com.cskaoyan.market.controller;

import com.cskaoyan.market.bo.DeleteCategoryBo;
import com.cskaoyan.market.bo.UpdateCategoryBo;
import com.cskaoyan.market.db.domain.MarketCategory;
import com.cskaoyan.market.service.CategoryService;
import com.cskaoyan.market.util.JacksonUtil;
import com.cskaoyan.market.util.ResponseUtil;
import com.cskaoyan.market.vo.L1Vo;
import com.cskaoyan.market.vo.ListVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("admin/category")
public class AdminCategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("list")
    @ResponseBody
    public Object list(){
        List<ListVo> list = categoryService.list();
        return ResponseUtil.okList(list);
    }

    @GetMapping("l1")
    @ResponseBody
    public Object l1(){
        List<L1Vo> l1VoList = categoryService.l1();
        return ResponseUtil.okList(l1VoList);
    }
    // @Override
    // protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //     String requestURI = req.getRequestURI();
    //     String op = requestURI.replace(req.getContextPath() + "/admin/category/", "");
    //     if ("list".equals(op)) {
    //         list(req,resp);
    //     }
    //     if ("l1".equals(op)){
    //         l1(req,resp);
    //     }
    // }

    // @Override
    // protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //     String requestURI = req.getRequestURI();
    //     String op = requestURI.replace(req.getContextPath() + "/admin/category/", "");
    //     if("create".equals(op)){
    //         create(req,resp);
    //     }
    //     if("update".equals(op)){
    //         update(req,resp);
    //     }
    //     if ("delete".equals(op)){
    //         delete(req,resp);
    //     }
    // }

    @PostMapping("delete")
    @ResponseBody
    public Object delete(@RequestBody DeleteCategoryBo deleteCategoryBo) throws JsonProcessingException {
        // String requestBody = map.toString();
        // DeleteCategoryBo deleteCategoryBo = JacksonUtil.getObjectMapper().readValue(requestBody, DeleteCategoryBo.class);
        categoryService.delete(deleteCategoryBo);
        return JacksonUtil.writeValueAsString(ResponseUtil.ok());
    }
    // private void delete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    //     req.setCharacterEncoding("UTF-8");
    //     StringBuilder requestBodyBuilder = new StringBuilder();
    //     BufferedReader reader = req.getReader();
    //     String line;
    //     while ((line = reader.readLine()) != null) {
    //         requestBodyBuilder.append(line);
    //     }
    //     String requestBody = requestBodyBuilder.toString();
    //     DeleteCategoryBo deleteCategoryBo = JacksonUtil.getObjectMapper().readValue(requestBody, DeleteCategoryBo.class);
    //     categoryService.delete(deleteCategoryBo);
    //     resp.getWriter().println(JacksonUtil.writeValueAsString(ResponseUtil.ok()));
    //
    // }
    @PostMapping("update")
    @ResponseBody
    public Object update(@RequestBody UpdateCategoryBo updateCategoryBo) throws JsonProcessingException {
        // String requestBody = map.toString();
        // UpdateCategoryBo updateCategoryBo = JacksonUtil.getObjectMapper().readValue(requestBody, UpdateCategoryBo.class);

        categoryService.update(updateCategoryBo);
        return JacksonUtil.writeValueAsString(ResponseUtil.ok());
    }
    // private void update(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    //     req.setCharacterEncoding("UTF-8");
    //     StringBuilder requestBodyBuilder = new StringBuilder();
    //     BufferedReader reader = req.getReader();
    //     String line;
    //     while ((line = reader.readLine()) != null) {
    //         requestBodyBuilder.append(line);
    //     }
    //     String requestBody = requestBodyBuilder.toString();
    //     UpdateCategoryBo updateCategoryBo = JacksonUtil.getObjectMapper().readValue(requestBody, UpdateCategoryBo.class);
    //     categoryService.update(updateCategoryBo);
    //     resp.getWriter().println(JacksonUtil.writeValueAsString(ResponseUtil.ok()));
    // }
    @PostMapping("create")
    @ResponseBody
    public Object create(@RequestBody MarketCategory marketCategory) throws JsonProcessingException {
        // String requestBody = map.toString();
        // ObjectMapper objectMapper = JacksonUtil.getObjectMapper();
        // MarketCategory marketCategory = objectMapper.readValue(requestBody, MarketCategory.class);
        MarketCategory insertOne = categoryService.insertOne(marketCategory);
        return ResponseUtil.ok(insertOne);
    }
    // private void create(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    //     req.setCharacterEncoding("UTF-8");
    //     StringBuilder requestBodyBuilder = new StringBuilder();
    //     BufferedReader reader = req.getReader();
    //     String line;
    //     while ((line = reader.readLine()) != null) {
    //         requestBodyBuilder.append(line);
    //     }
    //     String requestBody = requestBodyBuilder.toString();
    //     ObjectMapper objectMapper = JacksonUtil.getObjectMapper();
    //     MarketCategory marketCategory = objectMapper.readValue(requestBody, MarketCategory.class);
    //     MarketCategory insertOne = categoryService.insertOne(marketCategory);
    //     resp.getWriter().println(JacksonUtil.writeValueAsString(ResponseUtil.ok(insertOne)));
    // }

    // private void l1(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    //     List<L1Vo> l1VoList = categoryService.l1();
    //     resp.getWriter().println(JacksonUtil.writeValueAsString(ResponseUtil.okList(l1VoList)));
    // }

    // private void list(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    //     List<ListVo> list = categoryService.list();
    //     resp.getWriter().println(JacksonUtil.writeValueAsString(ResponseUtil.okList(list)));
    // }
}
