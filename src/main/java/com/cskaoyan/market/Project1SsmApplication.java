package com.cskaoyan.market;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//要保障mapper接口和mapper映射文件在一起
@MapperScan("com.cskaoyan.market.db.mapper")
public class Project1SsmApplication {

    public static void main(String[] args) {
        SpringApplication.run(Project1SsmApplication.class, args);
    }

}
