package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.annotation.RequiresPermissionsDesc;
import com.cskaoyan.market.db.domain.MarketKeyword;
import com.cskaoyan.market.service.admin.MarketKeywordService;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @RequiresPermissions("admin:keyword:list")
    @RequiresPermissionsDesc(menu = {"商场管理","关键词"},button = "查询")
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

    @RequiresPermissions("admin:keyword:delete")
    @RequiresPermissionsDesc(menu = {"商场管理","关键词"},button = "删除")
    @PostMapping("delete")
    public Object delete(@RequestBody Map map) {
        //String requestBody = req.getReader().readLine();
        //Integer id = JacksonUtil.parseInteger(requestBody, "id");
        Integer id = (Integer) map.get("id");
        keywordService.deleteKeyword(id);
        //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(ResponseUtil.ok()));
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:keyword:update")
    @RequiresPermissionsDesc(menu = {"商场管理","关键词"},button = "编辑")
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
        MarketKeyword updateMarketKeyword = keywordService.updateKeyword(id,updateTime,keyword,url,isDefault,isHot,addTime,delete,sortOrder);
        return ResponseUtil.ok(updateMarketKeyword);

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

    @RequiresPermissions("admin:keyword:create")
    @RequiresPermissionsDesc(menu = {"商场管理","关键词"},button = "添加")
    @PostMapping("create")
    public Object create(@RequestBody Map map) {
        String keyword = (String) map.get("keyword");
        String url = (String) map.get("url");
        Boolean isHot = (Boolean) map.get("isHot");
        Boolean isDefault = (Boolean) map.get("isDefault");
        MarketKeyword createMarketKeyword = keywordService.createKeyword(keyword,url,isDefault,isHot);
        return ResponseUtil.ok(createMarketKeyword);

        /*String requestBody = req.getReader().readLine();

        String keyword = JacksonUtil.parseString(requestBody, "keyword");
        String url = JacksonUtil.parseString(requestBody, "url");
        Boolean isHot = JacksonUtil.parseBoolean(requestBody, "isHot");
        Boolean isDefault = JacksonUtil.parseBoolean(requestBody, "isDefault");

        MarketKeyword createMarketKeyword = keywordService.createKeyword(keyword,url,isDefault,isHot);

        resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(ResponseUtil.ok(createMarketKeyword)));
 */   }
}
