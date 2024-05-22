package com.cskaoyan.market.filter;

import com.cskaoyan.market.util.JacksonUtil;
import com.cskaoyan.market.util.ResponseUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName AuthFilter
 * @Description: TODO
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/3/18 9:32
 * @Version V1.0
 **/
//@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        //除了登录（/admin/auth/login）、注销(/admin/auth/logout)之外的其他接口，全部需要加上条件，仅允许登录过之后才可以访问
        //首先，我们需要知道当前用户访问的是哪个接口？
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        //只有不是options请求的时候才会去做认证，options方法不做认证，因为没必要
        //因为肯定不通过，options就压根不会携带cookie
        if(!"OPTIONS".equals(method)){
            //如果没有携带cookie，那么始终创建新的session对象
            HttpSession session = request.getSession();

            System.out.println(method + ":::" + requestURI + ":::" + session.getId());
            //从context域中获取uris
            ServletContext servletContext = request.getServletContext();
            List<String> uris = (List<String>) servletContext.getAttribute("uris");
            //因为我想用list.contains方法，所以接下来希望可以将requestURI里面的应用名部分去掉
            requestURI = requestURI.replace(request.getContextPath(), "");
            //if(!(requestURI.equals(request.getContextPath() + "/admin/auth/login")) && !(requestURI.equals(request.getContextPath() + "/admin/auth/logout"))){
            if(!uris.contains(requestURI)){
                //仅允许登录才可以访问
                //因为登录之后会在session域中存入一个数据，所以通过获取该数据就可以知道当前是否登录
                Object attribute = session.getAttribute("admin");

                if(attribute == null){
                    //没有登录
                    //返回一个json响应信息即可
                    response.getWriter().println(JacksonUtil.writeValueAsString(ResponseUtil.unlogin()));
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
