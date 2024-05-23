package com.cskaoyan.market.controller.admin;


import com.cskaoyan.market.annotation.RequiresPermissionsDesc;
import com.cskaoyan.market.db.domain.MarketTopic;
import com.cskaoyan.market.service.admin.MarketTopicService;
import com.cskaoyan.market.util.ResponseUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 薛松 xuesong_work@163.com
 * @Date: 2024/05/06/17:19
 * @Description:专题模块
 */
@RestController
@RequestMapping("/admin/topic")
public class AdminTopicController {
    @Autowired
    private MarketTopicService topicService ;
    @Autowired
    ObjectMapper objectMapper;

    /*@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String op = requestURI.replace(req.getContextPath() + "/admin/topic/", "");
        if ("create".equals(op)) {
            create(req, resp);
        } else if ("update".equals(op)) {
            update(req, resp);
        } else if ("delete".equals(op)) {
            delete(req, resp);
        } else if ("batch-delete".equals(op)) {
            batchDelete(req, resp);
        }
    }*/

    @RequiresPermissions("admin:topic:bath-delete")
    @RequiresPermissionsDesc(menu = {"推广管理", "专题管理"}, button = "批量删除")
    @PostMapping("batch-delete")
    public Object batchDelete(@RequestBody Map<String, Object> map) throws IOException {

        if (!map.containsKey("ids") || !(map.get("ids") instanceof Collection)) {
            return ResponseUtil.fail(-1, "请求体格式错误，未找到有效的'ids'列表");
        }

        try {
            // 将"ids"对应的值转换为List<Integer>
            List<Integer> ids = objectMapper.convertValue(map.get("ids"), new TypeReference<List<Integer>>() {});

            // 调用服务层方法进行批量删除
            boolean batchDeleteResult = topicService.batchDelete(ids);

            return batchDeleteResult ? ResponseUtil.ok() : ResponseUtil.fail(-1, "批量删除失败");
        } catch (Exception e) {
            // 转换或删除过程中出现异常
            return ResponseUtil.fail(-1, "处理请求时发生错误：" + e.getMessage());
        }
    }

    /*@PostMapping("batch-delete")
    public Object batchDelete(@RequestBody Map map )throws IOException {
        //req.getReader().lines()` 创建了一个由请求体中的每一行构成的流。
        // `collect(Collectors.joining(System.lineSeparator()))` 使用`collect`方法汇聚这个流，并通过`Collectors.joining`收集器将这些行连接成一个单一的字符串。
        // 这里的分隔符是`System.lineSeparator()`，它会根据操作系统自动选择合适的行结束符（例如，在Windows上是`\r\n`，在Unix/Linux上是`\n`），从而确保跨平台的兼容性。
        //String requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        //将JSON字符串或输入流解析成一个可遍历的树结构——JsonNode对象
        //JsonNode rootNode = JacksonUtil.getObjectMapper().readTree(requestBody);
        //从rootNode中尝试获取名为"ids"的子节点
        //JsonNode idsNode = rootNode.get("ids");
        //检查"ids"节点是否存在并且是一个数组类型
        *//*if (idsNode != null && idsNode.isArray()) {
            // 如果是数组，则使用Jackson的convertValue方法将"ids"节点的内容转换为List<Integer>
            *//**//*List<Integer> ids = JacksonUtil.getObjectMapper().convertValue(idsNode, new TypeReference<List<Integer>>() {
            });*//**//*

            boolean batchDeleteResult = topicService.batchDelete(ids);

            Object o = batchDeleteResult ? ResponseUtil.ok() : ResponseUtil.fail(-1, "删除失败");
            resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));
        } else {
            //如果"ids"字段不存在或不是数组，设置响应状态码为400（Bad Request），表示客户端请求有误
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("无效的请求体，缺少或格式错误的'ids'字段");
        }*//*

        boolean batchDeleteResult = topicService.batchDelete(ids);
        return batchDeleteResult ? ResponseUtil.ok() : ResponseUtil.fail(-1, "批量删除失败");

    }*/

    @RequiresPermissions("admin:topic:delete")
    @RequiresPermissionsDesc(menu = {"推广管理", "专题管理"}, button = "删除")
    @PostMapping("delete")
    public Object delete(@RequestBody MarketTopic marketTopic) throws IOException {
        //String requestBody = req.getReader().readLine();

        //MarketTopic marketTopic = JacksonUtil.getObjectMapper().readValue(requestBody, MarketTopic.class);

        boolean delete = topicService.delete(marketTopic.getId());


        // 根据删除操作的结果构建响应
        return delete ? ResponseUtil.ok() : ResponseUtil.fail(-1, "删除失败");
        //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));
    }
    @RequiresPermissions("admin:topic:update")
    @RequiresPermissionsDesc(menu = {"推广管理", "专题管理"}, button = "编辑")
    @PostMapping("update")
    public Object update(@RequestBody MarketTopic marketTopic) throws IOException {
        //String resquestBody = req.getReader().readLine();
        //MarketTopic marketTopic = JacksonUtil.getObjectMapper().readValue(resquestBody, MarketTopic.class);

        MarketTopic updateTopic = topicService.update(marketTopic.getId(), marketTopic.getTitle(), marketTopic.getSubtitle(), marketTopic.getPrice(),
                marketTopic.getReadCount(), marketTopic.getPicUrl(), marketTopic.getSortOrder(), marketTopic.getGoods(),
                marketTopic.getAddTime(), marketTopic.getUpdateTime(), marketTopic.getDeleted(), marketTopic.getContent());
        if (updateTopic != null) {
            return ResponseUtil.ok(updateTopic);
            //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));
        } else {
            return ResponseUtil.fail();
            //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));
        }
    }
    @RequiresPermissions("admin:topic:create")
    @RequiresPermissionsDesc(menu = {"推广管理", "专题管理"}, button = "添加")
    @PostMapping("create")
    public Object create(@RequestBody MarketTopic marketTopic) throws IOException {
        //获取请求体中的json字符串
        //String requestBody = req.getReader().readLine();
        //将字符串转换为MarketTopic对象
        //MarketTopic marketTopic = JacksonUtil.getObjectMapper().readValue(requestBody, MarketTopic.class);
        //处理业务逻辑
        MarketTopic createTopic = topicService.create(marketTopic.getGoods(), marketTopic.getTitle(), marketTopic.getSubtitle(),
                marketTopic.getPicUrl(), marketTopic.getContent(), marketTopic.getPrice(), marketTopic.getReadCount());

        if (createTopic != null) {
            return ResponseUtil.ok(createTopic);
            //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));
        } else {
            return  ResponseUtil.fail();
            //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));
        }


    }

    /*@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String op = requestURI.replace(req.getContextPath() + "/admin/topic/", "");
        if ("list".equals(op)) {
            list(req, resp);
        } else if ("read".equals(op)) {
            read(req, resp);
        }

    }*/

    @RequiresPermissions("admin:topic:read")
    @RequiresPermissionsDesc(menu = {"推广管理", "专题管理"}, button = "详情")
    @GetMapping("read")
    public Object read(Integer id) throws IOException {

        //String idParm = req.getParameter("id");
        /*Integer id = null;
        try {
            id = Integer.parseInt(idParm);
        } catch (Exception e) {
            //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(ResponseUtil.fail()));
            System.out.println("!!!!!!!!!test2");
            return ResponseUtil.fail();
        }*/

        Map<String, Object> topic = topicService.read(id);

        if (topic == null) {

            return ResponseUtil.fail();
            /*String s = JacksonUtil.getObjectMapper().writeValueAsString(fail);
            resp.getWriter().println(s);*/
        }

        return ResponseUtil.ok(topic);
        //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));

    }

    @RequiresPermissions("admin:topic:list")
    @RequiresPermissionsDesc(menu = {"推广管理", "专题管理"}, button = "查询")
    @GetMapping("list")
    public Object list(HttpServletRequest req) throws IOException {
        String pageParam = req.getParameter("page");
        String limitParam = req.getParameter("limit");
        String title = req.getParameter("title");
        String subtitle = req.getParameter("subtitle");
        String sort = req.getParameter("sort");
        String order = req.getParameter("order");

        Integer page = null;
        Integer limit = null;

        try {
            page = Integer.parseInt(pageParam);
            limit = Integer.parseInt(limitParam);
        } catch (NumberFormatException e) {
            return ResponseUtil.badArgument();
            //String s = JacksonUtil.getObjectMapper().writeValueAsString(object);
            //resp.getWriter().println(s);
        }
        List<MarketTopic> marketTopicList = topicService.list(page, limit, title, subtitle, sort, order);

        return  ResponseUtil.okList(marketTopicList);
        //resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));

    }
}
