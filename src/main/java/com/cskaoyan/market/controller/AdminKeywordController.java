package com.cskaoyan.market.controller;

import com.cskaoyan.market.db.domain.MarketKeyword;
import com.cskaoyan.market.service.MarketKeywordService;
import com.cskaoyan.market.service.impl.MarketKeywordServiceImpl;
import com.cskaoyan.market.util.JacksonUtil;
import com.cskaoyan.market.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/keyword")
public class AdminKeywordController {

    //private MarketKeywordService keywordService = new MarketKeywordServiceImpl();
    @Autowired
    MarketKeywordService keywordService;

/*    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String op = requestURI.replace(req.getContextPath()+"/admin/keyword/","");
        if ("list".equals(op)){
            list(req,resp);
        }
    }*/

    @GetMapping("list")
    public ResponseEntity<Object> list(@RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) Integer limit,
                                       @RequestParam(required = false) String keyword,
                                       @RequestParam(required = false) String url,
                                       @RequestParam(required = false) String sort,
                                       @RequestParam(required = false) String order) {
    /*    String pageParam = req.getParameter("page");
        String limitParam = req.getParameter("limit");
        String keyword = req.getParameter("keyword");
        String url = req.getParameter("url");
        String sort = req.getParameter("sort");
        String order = req.getParameter("order");

        Integer limit = null;
        Integer page = null;*/
        try {
 /*           limit = Integer.parseInt(limitParam);
            page = Integer.parseInt(pageParam);*/
            List<MarketKeyword> keywordList = keywordService.list(page,limit,keyword,url,sort,order);
            return ResponseEntity.ok(ResponseUtil.okList(keywordList));

        }catch (Exception e){
            //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(ResponseUtil.badArgument()));
            return ResponseEntity.badRequest().body(ResponseUtil.badArgument());
        }
        //List<MarketKeyword> keywordList = keywordService.list(page,limit,keyword,url,sort,order);
        //Object o = ResponseUtil.okList(keywordList);
        //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));

    }

   /* @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String op = requestURI.replace(req.getContextPath() + "/admin/keyword/", "");
        if ("create".equals(op)){
            create(req,resp);
        }else if ("update".equals(op)){
            update(req,resp);
        }else if ("delete".equals(op)){
            delete(req,resp);
        }
    }*/

    @PostMapping("delete")
    public Object delete(@RequestBody Map map) {
        //String requestBody = req.getReader().readLine();
        //Integer id = JacksonUtil.parseInteger(requestBody, "id");
        Integer id = (Integer) map.get("id");
        keywordService.deleteKeyword(id);
        //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(ResponseUtil.ok()));
        return ResponseUtil.ok();
    }

    @PostMapping("update")
    public Object update(@RequestBody Map map) {
        Integer id = (Integer) map.get("id");
        String updateTime = (String) map.get("updateTime");
        String keyword = (String) map.get("keyword");
        String url = (String) map.get("url");
        Boolean isHot = (Boolean) map.get("isHot");
        Boolean isDefault = (Boolean) map.get("isDefault");
        String addTime = (String) map.get("addTime");
        Boolean delete = (Boolean) map.get("delete");
        Integer sortOrder = (Integer) map.get("sortOrder");
        keywordService.updateKeyword(id,updateTime,keyword,url,isDefault,isHot,addTime,delete,sortOrder);
        return ResponseUtil.ok(keywordService.updateKeyword(id,updateTime,keyword,url,isDefault,isHot,addTime,delete,sortOrder));

        /*String requestBody = req.getReader().readLine();

        Integer id = JacksonUtil.parseInteger(requestBody, "id");
        String updateTime = JacksonUtil.parseString(requestBody, "updateTime");
        String keyword = JacksonUtil.parseString(requestBody, "keyword");
        String url = JacksonUtil.parseString(requestBody, "url");
        Boolean isHot = JacksonUtil.parseBoolean(requestBody, "isHot");
        Boolean isDefault = JacksonUtil.parseBoolean(requestBody, "isDefault");
        String addTime = JacksonUtil.parseString(requestBody, "addTime");
        Boolean delete = JacksonUtil.parseBoolean(requestBody,"delete");
        Integer sortOrder = JacksonUtil.parseInteger(requestBody,"sortOrder");

        MarketKeyword updateMarketKeyword = keywordService.updateKeyword(id,updateTime,keyword,url,isDefault,isHot,addTime,delete,sortOrder);

        resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(ResponseUtil.ok(updateMarketKeyword)));
*/
    }

    @PostMapping("create")
    public Object create(@RequestBody Map map) {
        String keyword = (String) map.get("keyword");
        String url = (String) map.get("url");
        Boolean isHot = (Boolean) map.get("isHot");
        Boolean isDefault = (Boolean) map.get("isDefault");
        keywordService.createKeyword(keyword,url,isDefault,isHot);
        return ResponseUtil.ok(keywordService.createKeyword(keyword,url,isDefault,isHot));

        /*String requestBody = req.getReader().readLine();

        String keyword = JacksonUtil.parseString(requestBody, "keyword");
        String url = JacksonUtil.parseString(requestBody, "url");
        Boolean isHot = JacksonUtil.parseBoolean(requestBody, "isHot");
        Boolean isDefault = JacksonUtil.parseBoolean(requestBody, "isDefault");

        MarketKeyword createMarketKeyword = keywordService.createKeyword(keyword,url,isDefault,isHot);

        resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(ResponseUtil.ok(createMarketKeyword)));
 */   }
}
