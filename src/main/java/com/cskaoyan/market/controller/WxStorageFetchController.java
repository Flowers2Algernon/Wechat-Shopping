package com.cskaoyan.market.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;


@RequestMapping("wx/storage")
@Controller
public class WxStorageFetchController {
    @GetMapping("fetch/{filename}")
    public void fetch(@PathVariable String filename, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //wx/storage/fetch/xxxx.jpg-----最后这部分就是文件的key值，也就是文件的名称
        //文件在D:/image/目录中
//        String requestURI = req.getRequestURI();
//        requestURI = URLDecoder.decode(requestURI, "utf-8");
//        System.out.println(requestURI);
//        String filename = requestURI.replace(req.getContextPath() + "/wx/storage/fetch/", "");
        //todo 文件的存储目录也可以放到配置文件中
        Path file = Paths.get("D://image//"+filename);
        Resource resource = new UrlResource(file.toUri());
        if (Files.exists(file)&&Files.isReadable(file)){
            resp.setContentType("image/jpeg");
            ServletOutputStream outputStream = resp.getOutputStream();
            Files.copy(file,outputStream);
            outputStream.flush();
        }else {
            throw  new IOException();
        }
    }
}