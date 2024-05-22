package com.cskaoyan.market.controller;

import com.cskaoyan.market.db.domain.MarketIssue;
import com.cskaoyan.market.service.MarketIssueService;
import com.cskaoyan.market.service.impl.MarketIssueServiceImpl;
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
@RequestMapping("/admin/issue")
public class AdminIssueController {

    //private MarketIssueService issueService = new MarketIssueServiceImpl();
    @Autowired
    MarketIssueService issueService;

/*    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String op = requestURI.replace(req.getContextPath() + "/admin/issue/", "");
        if ("list".equals(op)) {
            list(req, resp);
        }
    }*/

    @GetMapping("list")
    public ResponseEntity<Object> list(@RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) Integer limit,
                                       @RequestParam(required = false) String question,
                                       @RequestParam(required = false) String sort,
                                       @RequestParam(required = false) String order) {
   /*     String pageParam = req.getParameter("page");
        String limitParam = req.getParameter("limit");
        String question = req.getParameter("question");
        String sort = req.getParameter("sort");
        String order = req.getParameter("order");
        Integer page = null;
        Integer limit = null;*/
        try {
/*            page = Integer.parseInt(pageParam);
            limit = Integer.parseInt(limitParam);*/
            List<MarketIssue> marketIssueList = issueService.list(limit, page, question, sort, order);
            return ResponseEntity.ok(ResponseUtil.okList(marketIssueList));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(ResponseUtil.badArgument());
        }
/*        List<MarketIssue> marketIssueList = issueService.list(limit, page, question, sort, order);
        Object o = ResponseUtil.okList(marketIssueList);
        resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(o));*/
    }

 /*   @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        String op = requestURI.replace(req.getContextPath() + "/admin/issue/", "");
        if ("create".equals(op)) {
            create(req, resp);
        } else if ("update".equals(op)) {
            update(req, resp);
        } else if ("delete".equals(op)) {
            delete(req, resp);
        }
    }*/

    @PostMapping("delete")
    public Object delete(@RequestBody Map map) {
        Integer id = (Integer) map.get("id");
        issueService.deleteIssue(id);

        return ResponseUtil.ok();

    }

    @PostMapping("update")
    public Object update(@RequestBody Map map) {
        Integer id = (Integer) map.get("id");
        String question = (String) map.get("question");
        String answer = (String) map.get("answer");
        String addTime = (String) map.get("addTime");
        String updateTime = (String) map.get("updateTime");
        String delete = (String) map.get("delete");
        issueService.updateIssue(id, question, answer, addTime, updateTime, delete);
        return ResponseUtil.ok(issueService.updateIssue(id, question, answer, addTime, updateTime, delete));

    /*    String requestBody = req.getReader().readLine();
        Integer id = JacksonUtil.parseInteger(requestBody, "id");
        String question = JacksonUtil.parseString(requestBody, "question");
        String answer = JacksonUtil.parseString(requestBody, "answer");
        String addTime = JacksonUtil.parseString(requestBody, "addTime");
        String updateTime = JacksonUtil.parseString(requestBody, "updateTime");
        String delete = JacksonUtil.parseString(requestBody, "false");

        MarketIssue marketIssue = issueService.updateIssue(id, question, answer, addTime, updateTime, delete);
        Object ok = ResponseUtil.ok(marketIssue);
        resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(ok));*/

    }

    @PostMapping("create")
    public Object create(@RequestBody Map map) {
        String question = (String) map.get("question");
        String answer = (String) map.get("answer");
        issueService.createIssue(question, answer);

        return ResponseUtil.ok(issueService.createIssue(question, answer));

       /* String requestBody = req.getReader().readLine();
        String question = JacksonUtil.parseString(requestBody, "question");
        String answer = JacksonUtil.parseString(requestBody, "answer");
        MarketIssue marketIssue = issueService.createIssue(question, answer);

        Object ok = ResponseUtil.ok(marketIssue);
        resp.getWriter().println(JacksonUtil.getObjectMapper().writeValueAsString(ok));*/
    }
}
