package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.annotation.RequiresPermissionsDesc;
import com.cskaoyan.market.db.domain.MarketAd;
import com.cskaoyan.market.service.admin.MarketAdService;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 薛松 xuesong_work@163.com
 * @Date: 2024/05/06/17:14
 * @Description:广告模块
 */
@RestController
@RequestMapping("/admin/ad")
public class AdminAdController{

    @Autowired
    private MarketAdService adService;
    /*@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String op = requestURI.replace(req.getContextPath() + "/admin/ad/", "");
        if ("create".equals(op)){
            create(req,resp);
        } else if ("update".equals(op)) {
            update(req,resp);
        } else if ("delete".equals(op)) {
            delete(req,resp);
        }
    }*/

    @RequiresPermissions("admin:ad:delete")
    @RequiresPermissionsDesc(menu = {"推广管理", "广告管理"}, button = "删除")
    @PostMapping ("delete")
    public Object delete(@RequestBody MarketAd marketAd) throws IOException {
        //String requestBody = req.getReader().readLine();

        //MarketAd marketAd = JacksonUtil.getObjectMapper().readValue(requestBody, MarketAd.class);

        boolean delete= adService.delete(marketAd.getId());

        // 根据删除操作的结果构建响应
        return delete ? ResponseUtil.ok() : ResponseUtil.fail(-1, "删除失败");
        //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));
    }

    @RequiresPermissions("admin:ad:update")
    @RequiresPermissionsDesc(menu = {"推广管理", "广告管理"}, button = "编辑")
    @PostMapping("update")
    public Object update(@RequestParam MarketAd marketAd) throws IOException {
        //获取请求体里的json字符串
        //String requestBody = req.getReader().readLine();

        //将json转为MarketAd对象
        //MarketAd marketAd = JacksonUtil.getObjectMapper().readValue(requestBody, MarketAd.class);

        MarketAd updateAd = adService.update(marketAd.getId(),marketAd.getName(),marketAd.getLink(),marketAd.getUrl(),marketAd.getPosition(),marketAd.getContent(),marketAd.getEnabled(),marketAd.getAddTime());
        if(updateAd != null){
            return ResponseUtil.ok(updateAd);
            //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));
        }else {
            return ResponseUtil.fail();
            //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));
        }
    }
    @RequiresPermissions("admin:ad:create")
    @RequiresPermissionsDesc(menu = {"推广管理", "广告管理"}, button = "添加")
    @PostMapping("create")
    public Object create(@RequestBody MarketAd marketAd) {

        try {
            // 处理具体的业务逻辑，将数据存储到表中
            MarketAd createdAd = adService.create(
                    marketAd.getName(),
                    marketAd.getContent(),
                    marketAd.getUrl(),
                    marketAd.getLink(),
                    marketAd.getPosition(),
                    marketAd.getEnabled()
            );

            if (createdAd != null) {
                // 使用ResponseEntity封装成功的响应
                return ResponseUtil.ok(createdAd);
            } else {
                // 如果创建失败，可以自定义错误消息或使用现有的ResponseUtil.fail()，假设它返回一个错误响应对象
                return ResponseUtil.fail();
            }
        } catch (Exception e) {
            // 捕获并处理可能的异常，例如数据库操作异常等
            System.out.println(e);
            return null;
        }
    }
    /*@PostMapping("create")
    public Object create( @RequestParam String name,
                          @RequestParam String content,
                          @RequestParam String url,
                          @RequestParam String link,
                          @RequestParam String position,
                          @RequestParam Boolean enabled)  throws IOException{

        // 获取请求体里的json字符串
        //String requestBody = req.getReader().readLine();
        //System.out.println(requestBody);

        // 将 JSON 字符串转换为 MarketAd 对象
        //MarketAd marketAd = JacksonUtil.getObjectMapper().readValue(requestBody, MarketAd.class);

        // 处理具体的业务逻辑 将数据存储到表中
        //MarketAd createdAd = adService.create(marketAd.getName(),marketAd.getContent(),marketAd.getUrl(),marketAd.getLink(),marketAd.getPosition(),marketAd.getEnabled());
        MarketAd createdAd = adService.create(name, content, url, link, Byte.parseByte(position), enabled);
        // 返回响应
        if (createdAd != null) {
            return ResponseUtil.ok(createdAd);
            //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));
        } else {
            return ResponseUtil.fail();
            //.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));
        }

    }*/

    /*   @Override
       protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           String requestURI = req.getRequestURI();
           String op = requestURI.replace(req.getContextPath() + "/admin/ad/", "");
           if ("list".equals(op)){
               list(req,resp);
           }
       }*/
    //展示所有的广告信息，分页查询
    //逻辑：根据用户输入的查询条件，查询到符合条件的与用户信息列表，但需要注意使用分页操作，还需要设定一个排序规则
    @RequiresPermissions("admin:ad:list")
    @RequiresPermissionsDesc(menu = {"推广管理", "广告管理"}, button = "查询")
    @GetMapping("list")
    public Object list(HttpServletRequest req) throws IOException {
        //路径测试
        //System.out.println("list");

        //1.接收用户提交过来的请求参数信息
        String pageParam = req.getParameter("page");//分页当前页
        String limitParam = req.getParameter("limit");//分页每页数量
        String name = req.getParameter("name");//广告标题
        String content = req.getParameter("content");//广告内容
        String sort = req.getParameter("sort");//排序规则
        String order = req.getParameter("order");//排序规则


        Integer page = null;
        Integer limit = null;
        try{
            page = Integer.parseInt(pageParam);
            limit = Integer.parseInt(limitParam);

        }catch (NumberFormatException e){
            return ResponseUtil.badArgument();//401参数有问题
            /*String s = JacksonUtil.getObjectMapper().writeValueAsString(object);
            resp.getWriter().println(s);
            return;*/
        }

        //2.处理具体的业务逻辑
        List<MarketAd> marketAdList =  adService.list(page,limit,name,content,sort,order);

        //3.返回响应
        return ResponseUtil.okList(marketAdList);
        //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));

    }

}
