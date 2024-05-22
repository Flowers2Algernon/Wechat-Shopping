package com.cskaoyan.market.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @ClassName AppListener
 * @Description: TODO
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/3/18 10:11
 * @Version V1.0
 **/
@WebListener
public class AppListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //读取白名单配置，放入context域中
        try {
            ServletContext servletContext = sce.getServletContext();
            List<String> uris = loadWhiteList();
            Map<String, String> params = loadProperties();
            servletContext.setAttribute("uris", uris);
            servletContext.setAttribute("params", params);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //用来去读取位于classpath目录下的properties配置文件
    private Map<String, String> loadProperties() throws IOException {
        Map<String, String> params = new HashMap<>();
        InputStream inputStream = AppListener.class.getClassLoader().getResourceAsStream("app.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        String uploadPath = properties.getProperty("upload.path");
        String domain = properties.getProperty("app.domain");
        params.put("uploadPath", uploadPath);
        params.put("domain", domain);
        return params;
    }

    private List<String> loadWhiteList() throws IOException {
        //需要读取位于classpath目录下的whitelist.txt文件
        InputStream inputStream = AppListener.class.getClassLoader().getResourceAsStream("whitelist.txt");
        List<String> uris = new ArrayList<>();
        String line = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = bufferedReader.readLine()) != null){
            uris.add(line);
        }
        return uris;
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
