package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.db.domain.MarketAddress;
import com.cskaoyan.market.db.domain.MarketAddressExample;
import com.cskaoyan.market.db.domain.MarketUser;
import com.cskaoyan.market.db.mapper.MarketAddressMapper;
import com.cskaoyan.market.service.wx.WxAddressService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WxAddressServiceImpl implements WxAddressService {

    @Autowired
    MarketAddressMapper marketAddressMapper;

    @Autowired
    SecurityManager securityManager;
    @Override
    public List<MarketAddress> list() {

        List<MarketAddress> marketAddresses = marketAddressMapper.selectByExampleSelective(new MarketAddressExample());

        return marketAddresses;
    }

    @Override
    public Object detail(Integer id) {

        Object marketDetail = marketAddressMapper.selectByPrimaryKey(id);

        return marketDetail;
    }

    @Override
    public int save(Integer id, String name, String tel, String province, String city,
                    String county, String areaCode, String addressDetail, Object isDefault) {
        MarketAddressExample marketAddressExample = new MarketAddressExample();
        MarketAddress saveAddress = new MarketAddress();

        saveAddress.setId(id);
        saveAddress.setName(name);
        saveAddress.setTel(tel);
        saveAddress.setProvince(province);
        saveAddress.setCity(city);
        saveAddress.setCounty(county);
        saveAddress.setAreaCode(areaCode);
        saveAddress.setAddressDetail(addressDetail);

        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        MarketUser user = (MarketUser) session.getAttribute("user");
        Integer userId = user.getId();
        saveAddress.setUserId(userId);
        if(isDefault instanceof Boolean && (Boolean) isDefault == false){
            saveAddress.setIsDefault(false);
        }else if(isDefault instanceof Boolean && (Boolean) isDefault == true){
            //MarketAddressExample marketAddressExample = new MarketAddressExample();
            marketAddressExample.createCriteria().andIdNotEqualTo(id).andIsDefaultEqualTo(true);
            List<MarketAddress> marketAddresses = marketAddressMapper.selectByExample(marketAddressExample);
            for (MarketAddress marketAddress : marketAddresses) {
                marketAddress.setIsDefault(false);
                marketAddressMapper.updateByPrimaryKey(marketAddress);
            }
            saveAddress.setIsDefault(true);
        }else if(isDefault instanceof Integer && (Integer) isDefault == 0){
            saveAddress.setIsDefault(false);
        }
        if (id ==null || id == 0) {
            return marketAddressMapper.insertSelective(saveAddress);
        }else {
            return marketAddressMapper.updateByPrimaryKey(saveAddress);
        }

    }

    @Override
    public Integer deleteAddress(Integer id) {

        int deleteCount = marketAddressMapper.deleteByPrimaryKey(id);

        return deleteCount;
    }
}
