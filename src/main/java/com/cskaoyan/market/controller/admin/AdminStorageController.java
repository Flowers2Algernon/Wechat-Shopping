package com.cskaoyan.market.controller.admin;

import com.cskaoyan.market.annotation.RequiresPermissionsDesc;
import com.cskaoyan.market.db.domain.MarketStorage;
import com.cskaoyan.market.service.admin.AdminStorageService;
import com.cskaoyan.market.util.ResponseUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("admin/storage")
@RestController
@MultipartConfig
public class AdminStorageController {
    @Autowired
    AdminStorageService adminStorageService;
//    private AdminStorageService storageService = new AdminStorageServiceImpl1();
    @Autowired
    SecurityManager securityManager;

    @RequiresPermissions("admin:storage:delete")
    @RequiresPermissionsDesc(menu = {"系统管理","对象存储"},button = "删除")
    @PostMapping("delete")
    public Object delete(@RequestBody MarketStorage marketStorage){
        adminStorageService.delete(marketStorage);
        return (ResponseUtil.ok());
    }
    @RequiresPermissions("admin:storage:update")
    @RequiresPermissionsDesc(menu = {"系统管理","对象存储"},button = "更新")
    @PostMapping("update")
    public Object update(@RequestBody MarketStorage marketStorage) {
        //此处是更新对象名并返回更新
        //{"id":203,"key":"yo5w688nnrwfyq6lc3fn.jpg","name":"ku12221nknu.jpg","type":"image/jpeg","size":17026,"url":"http://39.101.189.16:8083/wx/storage/fetch/yo5w688nnrwfyq6lc3fn.jpg","addTime":"2024-05-08 11:25:54","updateTime":"2024-05-08 11:25:54","deleted":false}
        //todo 此处marketStorage中没有avatar，导致无法显示图片
        adminStorageService.update(marketStorage);
        return ResponseUtil.ok(marketStorage);
    }
    @RequiresPermissions("admin:storage:list")
    @RequiresPermissionsDesc(menu = {"系统管理","对象存储"},button = "查询")
    @GetMapping("list")
    public Object list(HttpServletRequest req){
        //此处是要返回查询的对象
        //key=value&key = value类型
        String pageParam = req.getParameter("page");
        String limitParam = req.getParameter("limit");
        String key = req.getParameter("key");
        String name = req.getParameter("name");
        String sort = req.getParameter("sort");
        String order = req.getParameter("order");
        Integer page = null;
        Integer limit = null;
        try {
            page = Integer.parseInt(pageParam);
            limit = Integer.parseInt(limitParam);
        }catch (Exception e){
            return (ResponseUtil.badArgument());
        }
        List<MarketStorage> list =  adminStorageService.list(limit, page,key,name,sort,order);
        List<MarketStorage> resultList = new ArrayList<>();
        for (MarketStorage marketStorage : list) {
            String url = marketStorage.getUrl();
            marketStorage.setUrl("http://localhost:8083"+url);
            resultList.add(marketStorage);
        }
        Object o = ResponseUtil.okList(resultList);
        return o;
    }
    @RequiresPermissions("admin:storage:create")
    @RequiresPermissionsDesc(menu = {"系统管理","对象存储"},button = "新建")
    @PostMapping("create")
    public Object create(@RequestParam("file")MultipartFile file, HttpSession session) throws  IOException {
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        //对其文件名进行更改，采用随即文件名
        byte[] bytes = file.getBytes();
        long size = file.getSize();
        String key = UUID.randomUUID()+"-"+fileName;
        //todo 此处改为使用配置文件读取
        String basePath = "D:\\image";//此处放置想将该图片保存在服务器中的哪个路径，可以是任意盘符
        Path path = Paths.get(basePath+"\\"+key);
        Files.write(path,bytes);
    //文件存储到本地没问题
    //        part.write(basePath+"\\"+key);//注意此处basePath和key之间有一个\,否则图片无法正常输出
        //上述是将文件上传到盘符的任意位置
        //下述代码是将图片输出给客户端
        //首先需要将该文件上传记录上传到数据库中
        MarketStorage marketStorage = new MarketStorage();
        marketStorage.setDeleted(false);
        marketStorage.setAddTime(LocalDateTime.now());
        marketStorage.setUpdateTime(LocalDateTime.now());
        marketStorage.setSize((int)size);
        marketStorage.setName(fileName);
        marketStorage.setKey(key);
        //此处Key是最终的名字，fileName是上传的名字
        //如果将url前半部分按网页返回的内容设置为主机+端口号+路径+文件名
        //当后续主机或者端口号发生变化时，图片都会失效，所以此时不能这么存
        //数据库中只存端口号后半部分的位置+文件名，前半部分的位置需要输出的再拼接即可
        String relativePath = "/wx/storage/fetch/"+key;
        marketStorage.setUrl(key);
        marketStorage.setType(contentType);
        //将上述参数都传入到数据库中去，使用service层的实现来处理
        adminStorageService.create(marketStorage);

        //此处再对url进行处理，追加上主机和端口号
        //todo 此处可以写入配置文件中
        String url = marketStorage.getUrl();
        marketStorage.setUrl("http://localhost:8083"+relativePath);
        //输出回复文件，注意此时图片上传后还是客户端还是无法看到文件
        //存储到Session中，方便adminadmin中取出
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        Session session1 = subject.getSession();
        session1.setAttribute("url",marketStorage.getUrl());
        return ResponseUtil.ok( marketStorage);
    }
}
