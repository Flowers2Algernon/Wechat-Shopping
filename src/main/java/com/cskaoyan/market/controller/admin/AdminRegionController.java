package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.service.admin.MarketRegionService;
import com.cskaoyan.market.util.ResponseUtil;
import com.cskaoyan.market.vo.MarketRegionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/5/6 9:36
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/admin/region")
public class AdminRegionController extends HttpServlet {

    @Autowired
    MarketRegionService regionService;

    //无论任何一个接口，都分为三步：1.接收请求参数 抓包  本地  2.处理业务逻辑 思考 3，返回响应 抓包 抓取公网上面部署的项目
    @GetMapping("list")
    public Object list(HttpSession session) throws IOException {
        //行政区域数据需要每次都查询吗？会经常变化吗？可不可以查询一次之后，放入内存中
        //先尝试从context域中获取，如果获取不到，再进行数据库查询
        List<MarketRegionVo> marketRegionVoList = (List<MarketRegionVo>) session.getAttribute("region");
        if(marketRegionVoList == null){
            //如果context域获取不到，则从数据库查询，但是随后需要把查询到的结果更新到context域
            marketRegionVoList = regionService.list();
            session.setAttribute("region", marketRegionVoList);
        }

        Object o = ResponseUtil.okList(marketRegionVoList);
        return o;
    }
}
