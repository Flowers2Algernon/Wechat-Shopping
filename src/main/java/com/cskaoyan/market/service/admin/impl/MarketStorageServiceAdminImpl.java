package com.cskaoyan.market.service.admin.impl;

import com.cskaoyan.market.db.domain.MarketStorage;
import com.cskaoyan.market.db.mapper.MarketStorageMapper;
import com.cskaoyan.market.service.admin.MarketStroageService;
import com.cskaoyan.market.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

/**
 * @ClassName StorageServiceImpl
 * @Description: TODO
 * @Author 远志 zhangsong@cskaoyan.onaliyun.com
 * @Date 2024/3/16 11:06
 * @Version V1.0
 **/
@Service
public class MarketStorageServiceAdminImpl implements MarketStroageService {
    @Override
    public void insertOne(MarketStorage marketStorage) {
        SqlSession session = MybatisUtils.getSession();
        MarketStorageMapper storageMapper = session.getMapper(MarketStorageMapper.class);
        //获取插入的数据的编号，执行完插入之后，mybatis会帮助我们进行一次查询，会将查询的结果再次封装给marketStorage的id属性
        storageMapper.insert(marketStorage);
        //记得一定要提交事务
        session.commit();
        session.close();
    }
}
