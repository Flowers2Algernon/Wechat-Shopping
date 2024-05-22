package com.cskaoyan.market.service.impl;

import com.cskaoyan.market.db.domain.MarketAdmin;
import com.cskaoyan.market.db.domain.MarketAdminExample;
import com.cskaoyan.market.db.mapper.MarketAdminMapper;
import com.cskaoyan.market.service.AdminAdminService;
import com.cskaoyan.market.util.MybatisUtils;
import com.cskaoyan.market.vo.MarketAdminListVo;
import com.cskaoyan.market.vo.MarketAdminUpdateVo;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AdminAdminServiceImpl implements AdminAdminService {
    @Autowired
    MarketAdminMapper marketAdminMapper;

    @Override
    public List<MarketAdminListVo> list(Integer limit, Integer page, String username, String sort, String order) {
        MarketAdminExample marketAdminExample = new MarketAdminExample();
        MarketAdminExample.Criteria criteria = marketAdminExample.createCriteria();
        marketAdminExample.setOrderByClause(sort+" "+order);
        if (!StringUtils.isEmpty(username)){
            criteria.andUsernameLike("%"+username+"%");
        }
        //分页操作
        PageHelper.startPage(page, limit);
        List<MarketAdmin> marketAdmins =marketAdminMapper.selectByExampleSelective(marketAdminExample, MarketAdmin.Column.id, MarketAdmin.Column.avatar, MarketAdmin.Column.roleIds, MarketAdmin.Column.username);
        //此处需要把实体对象marketAdmin转换为新创建的marketAdminListVo
        ArrayList<MarketAdminListVo> marketAdminListVos = new ArrayList<>();
        for (MarketAdmin marketAdmin : marketAdmins) {
            marketAdminListVos.add(new MarketAdminListVo(marketAdmin.getId(),marketAdmin.getUsername(),marketAdmin.getAvatar(),marketAdmin.getRoleIds()));
        }

        return marketAdminListVos;
    }
    @Override
    public MarketAdmin create(String username, String avatar, String password, Integer id, Integer[] roleIds, LocalDateTime updateTime, LocalDateTime addTime) {
        //此处为创建管理员的具体逻辑
        MarketAdminExample marketAdminExample = new MarketAdminExample();
        MarketAdmin marketAdmin = new MarketAdmin();
        marketAdmin.setId(id);
        marketAdmin.setAvatar(avatar);
        marketAdmin.setPassword(password);
        marketAdmin.setUsername(username);
        marketAdmin.setAddTime(addTime);
        marketAdmin.setUpdateTime(updateTime);
        marketAdmin.setRoleIds(roleIds);
        marketAdminMapper.insert(marketAdmin);
        return marketAdmin;
    }

    @Override
    public void update(MarketAdminUpdateVo marketAdmin1) {
        MarketAdminExample marketAdminExample = new MarketAdminExample();
        MarketAdmin marketAdmin = marketAdminMapper.selectByPrimaryKey(marketAdmin1.getId());
        marketAdmin.setAvatar(marketAdmin1.getAvatar());
        marketAdmin.setUpdateTime(LocalDateTime.now());
        marketAdmin.setPassword(marketAdmin1.getPassword());
        marketAdmin.setRoleIds(marketAdmin1.getRoleIds());
        marketAdmin.setUsername(marketAdmin1.getUsername());
        marketAdminMapper.updateByPrimaryKeySelective(marketAdmin);
    }
    @Override
    public void delete( MarketAdmin marketAdmin ) {
        Integer id = marketAdmin.getId();
        marketAdminMapper.deleteByPrimaryKey(id);
    }
}
