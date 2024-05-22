package com.cskaoyan.market.controller;

import com.cskaoyan.market.db.domain.MarketBrand;
import com.cskaoyan.market.service.BrandService;
import com.cskaoyan.market.util.ResponseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/brand")
public class AdminBrandController {
    @Autowired
    BrandService brandService;

    @GetMapping("list")
    @ResponseBody
    public Object list(String page, String limit, String id, String name, String sort, String order){
        // String pageParam = (String) map.get("page");
        // String limitParam = (String) map.get("limit");
        // String id = (String) map.get("id");
        // String name = (String) map.get("name");
        // String sort = (String) map.get("sort");
        // String order = (String) map.get("order");
        Integer page1 = null;
        Integer limit1 = null;
        try {
            page1 = Integer.parseInt(page);
            limit1 = Integer.parseInt(limit);
        }catch (Exception e){
            return ResponseUtil.badArgument();
        }
        List<MarketBrand> brandList = brandService.list(page1, limit1, id, name, sort, order);
        return ResponseUtil.okList(brandList);

    }

    // @Override
    // protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //     String requestURI = req.getRequestURI();
    //     String op = requestURI.replace(req.getContextPath() + "/admin/brand/", "");
    //     if("create".equals(op)){
    //         create(req, resp);
    //     }
    //     if("update".equals(op)){
    //         update(req,resp);
    //     }
    //     if("delete".equals(op)){
    //         delete(req,resp);
    //     }
    // }
    @PostMapping("delete")
    @ResponseBody
    public Object delete(@RequestBody MarketBrand marketBrand){
        // String requestBody = map.toString();
        // try {
        //     MarketBrand marketBrand = JacksonUtil.getObjectMapper().readValue(requestBody, MarketBrand.class);
        //     brandService.delete(marketBrand);
        // } catch (JsonProcessingException e) {
        //     throw new RuntimeException(e);
        // }
        brandService.delete(marketBrand);
        return ResponseUtil.ok();
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
    //     MarketBrand marketBrand = JacksonUtil.getObjectMapper().readValue(requestBody, MarketBrand.class);
    //     brandService.delete(marketBrand);
    //     resp.getWriter().println(JacksonUtil.writeValueAsString(ResponseUtil.ok()));
    //
    // }
    @PostMapping("update")
    @ResponseBody
    public Object update(@RequestBody MarketBrand marketBrand) throws JsonProcessingException {
        // String requestBody = map.toString();
        // MarketBrand marketBrand = JacksonUtil.getObjectMapper().readValue(requestBody, MarketBrand.class);
        MarketBrand updateMarketBrand = brandService.update(marketBrand);
        return ResponseUtil.ok(updateMarketBrand);
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
    //     MarketBrand marketBrand = JacksonUtil.getObjectMapper().readValue(requestBody, MarketBrand.class);
    //     MarketBrand updateMarketBrand = brandService.update(marketBrand);
    //     resp.getWriter().println(JacksonUtil.writeValueAsString(ResponseUtil.ok(updateMarketBrand)));
    //
    //
    // }
    @PostMapping("create")
    @ResponseBody
    public Object create(@RequestBody MarketBrand marketBrand) throws JsonProcessingException {
        // List<CreateBrandBo> list = new ArrayList<>();
        // list.add(createBrandBo);
        // System.out.println(list.get(0));
        // MarketBrand marketBrand = JacksonUtil.getObjectMapper().readValue(createBrandBo, MarketBrand.class);
        MarketBrand insertOneMarketBrand = brandService.insertOne(marketBrand);
        return ResponseUtil.ok(insertOneMarketBrand);

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
    //     MarketBrand marketBrand = JacksonUtil.getObjectMapper().readValue(requestBody, MarketBrand.class);
    //     MarketBrand insertOneMarketBrand = brandService.insertOne(marketBrand);
    //
    //     resp.getWriter().println(JacksonUtil.writeValueAsString(ResponseUtil.ok(insertOneMarketBrand)));
    // }
}
