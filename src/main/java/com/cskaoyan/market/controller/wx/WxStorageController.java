package com.cskaoyan.market.controller.wx;

import com.cskaoyan.market.cloud.CloudService;
import com.cskaoyan.market.db.domain.MarketStorage;
import com.cskaoyan.market.service.wx.MarketStorageService;
import com.cskaoyan.market.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@RestController
@RequestMapping("wx/storage")
public class WxStorageController {
    @Autowired
    CloudService cloudService;
    @Autowired
    MarketStorageService marketStorageService;

    @PostMapping("upload")
    public Object upload(MultipartFile file){
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String path = cloudService.oss(inputStream);
        //此处得到url
        long size = file.getSize();
        String contentType = file.getContentType();
        String name = file.getOriginalFilename();
        MarketStorage marketStorage = new MarketStorage();
        marketStorage.setUpdateTime(LocalDateTime.now());
        marketStorage.setAddTime(LocalDateTime.now());
        marketStorage.setUrl(path);
        marketStorage.setType(contentType);
        marketStorage.setSize((int) size);
        marketStorage.setName(name);
        marketStorage.setKey(file.getName());
        MarketStorage result= marketStorageService.upload(marketStorage);
        return ResponseUtil.ok(result);
    }
}
