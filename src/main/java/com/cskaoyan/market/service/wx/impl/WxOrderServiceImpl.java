package com.cskaoyan.market.service.wx.impl;

import com.cskaoyan.market.db.domain.*;
import com.cskaoyan.market.db.mapper.*;
import com.cskaoyan.market.model.enums.ExpressionEnum;
import com.cskaoyan.market.service.wx.WxOrderService;
import com.cskaoyan.market.vo.OrderDetailVo;
import com.cskaoyan.market.vo.OrderGoodsVo;
import com.cskaoyan.market.vo.OrderListVo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 薛松 xuesong_work@163.com
 * @Date: 2024/05/20/18:01
 * @Description:
 */
@Service
public class WxOrderServiceImpl implements WxOrderService {
    @Autowired
    MarketOrderMapper marketOrderMapper;
    @Autowired
    MarketOrderGoodsMapper marketOrderGoodsMapper;
    @Autowired
    MarketCommentMapper marketCommentMapper;
    @Autowired
    MarketAddressMapper marketAddressMapper;
    @Autowired
    MarketCouponMapper marketCouponMapper;
    @Autowired
    MarketCartMapper marketCartMapper;
    @Autowired
    MarketGoodsProductMapper marketGoodsProductMapper;
    @Autowired
    MarketCouponUserMapper marketCouponUserMapper;
    @Autowired
    MarketGoodsMapper marketGoodsMapper;
    @Autowired
    MarketSystemMapper marketSystemMapper;
    //订单列表
    @Override
    public List<OrderListVo> list(MarketUser user, Integer showType, Integer page, Integer limit) {
        List<MarketOrder> allOrder = getAllOrder(user, showType);
        List<OrderListVo> orderListVos = getResultOrderListVo(allOrder, page, limit);
        return orderListVos;
    }

    //订单详情
    @Override
    public OrderDetailVo detail(Integer orderId) {
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        //快递信息 暂存空数组写死
        PutExpressInfo(orderDetailVo);

        //订单商品列表
        MarketOrder marketOrder = putOrderGoodsAndReturnOrderInfo(orderDetailVo, orderId);


        //订单信息
        //包含订单信息 快递信息 按键控制-根据订单信息分类按钮不同
        putOrderInfo(orderDetailVo, marketOrder);

        return orderDetailVo;
    }


    //取消订单
    @Override
    public boolean cancel(Integer orderId) {

        try {
            // 更新订单状态为取消订单状态，
            MarketOrder updateOrder = new MarketOrder();
            updateOrder.setId(orderId);
            updateOrder.setOrderStatus((short) 102); // 102为用户取消
            int rowsAffected = marketOrderMapper.updateByPrimaryKeySelective(updateOrder);

            return rowsAffected > 0;
        } catch (Exception e) {
            throw new RuntimeException("取消订单失败", e);
        }
    }

    @Override
    public boolean prepay(Integer orderId) {
        try {
            // 更新订单状态为已付款状态，
            MarketOrder updateOrder = new MarketOrder();
            updateOrder.setId(orderId);
            updateOrder.setOrderStatus((short) 201); // 201为已付款
            int rowsAffected = marketOrderMapper.updateByPrimaryKeySelective(updateOrder);

            return rowsAffected > 0;
        } catch (Exception e) {
            throw new RuntimeException("取消订单失败", e);
        }
    }

    //删除订单
    @Override
    public boolean delete(Integer orderId) {
        try {

            int rowsAffected = marketOrderMapper.logicalDeleteByPrimaryKey(orderId);

            return rowsAffected > 0;
        } catch (Exception e) {
            throw new RuntimeException("删除失败", e);
        }
    }

    //确认退款
    @Override
    public boolean refund(Integer orderId) {
        try {
            // 更新订单状态为退款确认状态，
            MarketOrder updateOrder = new MarketOrder();
            updateOrder.setId(orderId);
            updateOrder.setOrderStatus((short) 202); // 202为申请退款
            int rowsAffected = marketOrderMapper.updateByPrimaryKeySelective(updateOrder);

            return rowsAffected > 0;
        } catch (Exception e) {
            throw new RuntimeException("退款操作失败", e);
        }
    }

    //确认订单
    @Override
    public boolean confirm(Integer orderId) {
        try {
            MarketOrder updateOrder = new MarketOrder();
            updateOrder.setId(orderId);
            updateOrder.setOrderStatus((short) 401); // 401为用户收货
            int rowsAffected = marketOrderMapper.updateByPrimaryKeySelective(updateOrder);

            return rowsAffected > 0;
        } catch (Exception e) {
            throw new RuntimeException("确认订单操作失败", e);
        }
    }

    //订单评价的商品详情
    @Override
    public OrderGoodsVo goods(Integer orderId, Integer goodsId) {
        OrderGoodsVo orderGoodsVo = new OrderGoodsVo();
        MarketOrder marketOrder = marketOrderMapper.selectByPrimaryKey(orderId);
        MarketOrderGoodsExample marketOrderGoodsExample = new MarketOrderGoodsExample();
        marketOrderGoodsExample.createCriteria().andOrderIdEqualTo(orderId).andGoodsIdEqualTo(goodsId);
        MarketOrderGoods marketOrderGoods = marketOrderGoodsMapper.selectOneByExample(marketOrderGoodsExample);
        orderGoodsVo.setAddTime(marketOrder.getAddTime());
        orderGoodsVo.setComment(0);
        orderGoodsVo.setDeleted(marketOrder.getDeleted());
        orderGoodsVo.setGoodsId(marketOrderGoods.getGoodsId());
        orderGoodsVo.setGoodsName(marketOrderGoods.getGoodsName());
        orderGoodsVo.setGoodsSn(marketOrderGoods.getGoodsSn());
        orderGoodsVo.setId(marketOrderGoods.getId());
        orderGoodsVo.setNumber(marketOrderGoods.getNumber());
        orderGoodsVo.setOrderId(marketOrder.getId());
        orderGoodsVo.setPicUrl(marketOrderGoods.getPicUrl());
        orderGoodsVo.setPrice(marketOrderGoods.getPrice());
        orderGoodsVo.setProductId(marketOrderGoods.getProductId());
        orderGoodsVo.setSpecifications(marketOrderGoods.getSpecifications());
        orderGoodsVo.setUpdateTime(marketOrder.getUpdateTime());
        return orderGoodsVo;
    }

    //订单评价
    @Override
    public Integer comment(Integer userId, String content, Boolean hasPicture, Integer orderGoodsId, Integer star, String[] picUrls) {
        MarketOrderGoods marketOrderGoods = marketOrderGoodsMapper.selectByPrimaryKey(orderGoodsId);
        String goodsSn = marketOrderGoods.getGoodsSn();
        MarketComment marketComment = new MarketComment();
        marketComment.setDeleted(false);
        marketComment.setContent(content);
        marketComment.setPicUrls(picUrls);
        marketComment.setValueId(Integer.parseInt(goodsSn));
        marketComment.setType((byte) 0);
        marketComment.setHasPicture(hasPicture);
        short i = 0;
        if (star != null) {
            i = star.shortValue();
        }
        marketComment.setStar(i);
        marketComment.setUserId(userId);
        marketComment.setAddTime(LocalDateTime.now());
        marketComment.setUpdateTime(LocalDateTime.now());
        //插入上述comment
        int row = marketCommentMapper.insert(marketComment);
        //更新orderGoods表和order表
        //更新orderGoods
        Integer marketCommentId = marketComment.getId();
        marketOrderGoods.setComment(marketCommentId);
        marketOrderGoodsMapper.updateByPrimaryKey(marketOrderGoods);

        //更新order
        Integer orderId = marketOrderGoods.getOrderId();
        MarketOrder marketOrder = marketOrderMapper.selectByPrimaryKey(orderId);
        marketOrder.setComments((short) (marketOrder.getComments() - 1));
        marketOrderMapper.updateByPrimaryKey(marketOrder);
        return row;
    }
        /*MarketComment marketComment = new MarketComment();
        marketComment.setDeleted(false);
        marketComment.setContent(content);
        marketComment.setPicUrls(picUrls);
        marketComment.setValueId(orderGoodsId);
        marketComment.setType((byte) 1);
        marketComment.setHasPicture(hasPicture);
        short i = 0;
        if (star != null) {
            i = star.shortValue();
        }
        marketComment.setStar(i);
        marketComment.setUserId(userId);
        marketComment.setAddTime(LocalDateTime.now());
        marketComment.setUpdateTime(LocalDateTime.now());
        return marketCommentMapper.insert(marketComment);
    }*/
//===================================================================================

    private void putOrderInfo(OrderDetailVo orderDetailVo, MarketOrder marketOrder) {
        HashMap<String, Object> orderInfo = new HashMap<>();
        setOrderInfoAboutOrder(orderInfo, marketOrder);
        setOrderInfoAboutExpression(orderInfo, marketOrder);
        setOrderInfoAboutHandle0ption(orderInfo, marketOrder);
        orderDetailVo.setOrderInfo(orderInfo);
    }


    private void setOrderInfoAboutOrder(HashMap<String, Object> orderInfo, MarketOrder marketOrder) {
        HashMap<String, Boolean> handleOption = new HashMap<>();
        setHandleOption(handleOption, marketOrder);
        orderInfo.put("handleOption", handleOption);
    }

    private void setHandleOption(HashMap<String, Boolean> handleOption, MarketOrder marketOrder) {
        short orderStatus = marketOrder.getOrderStatus();
        if (orderStatus == 101) {
            //未付款订单   付款、取消按钮
            handleOptionUnpaid(handleOption);
        } else if (orderStatus == 102 || orderStatus == 103 || orderStatus == 203) {
            //已取消订单/已退款订单
            handleOptionCancelAndRefunded(handleOption);
        } else if (orderStatus == 301) {
            //已发货订单  确认收货、再来一单按钮！！！！
            handleOptionShiped(handleOption);
        } else if (orderStatus == 201) {
            //已付款订单  再来一单、申请退款按钮
            handlerOptionpaid(handleOption);
        } else if (orderStatus == 202) {
            //申请退款订单  无按钮
            handleOptionNeedRefunded(handleOption);
        } else if (orderStatus == 401 || orderStatus == 402) {
            //已收货订单  申请售后、评价、删除、再来一单按钮
            //需要判断是否可以评论 多传入一个marketOrder
            handleOptionReceived(handleOption, marketOrder);
        }

    }

    //已收货订单
    private void handleOptionReceived(HashMap<String, Boolean> handleOption, MarketOrder marketOrder) {
        handleOption.put("aftersale", true);
        handleOption.put("cancel", false);
        handleOption.put("confirm", false);
        handleOption.put("delete", true);
        handleOption.put("pay", false);
        handleOption.put("rebuy", true);
        handleOption.put("refund", false);
        Boolean toComment = AbleToComment(marketOrder);
        handleOption.put("comment", toComment);
    }

    private Boolean AbleToComment(MarketOrder marketOrder) {
        Short goodsCanCommentNum = marketOrder.getComments();
        if (goodsCanCommentNum==null){
            //todo
            return false;
        }
        if (goodsCanCommentNum < 0) {
            return false;
        }
        Integer marketOrderId = marketOrder.getId();
        MarketOrderGoodsExample marketOrderGoodsExample = new MarketOrderGoodsExample();
        marketOrderGoodsExample.createCriteria().andOrderIdEqualTo(marketOrderId);
        List<MarketOrderGoods> marketOrderGoodsList = marketOrderGoodsMapper.selectByExample(marketOrderGoodsExample);
        for (MarketOrderGoods marketOrderGoods : marketOrderGoodsList) {
            if (marketOrderGoods.getComment() == 0) {
                return true;
            }
        }
        return false;
    }

    //申请退款
    private void handleOptionNeedRefunded(HashMap<String, Boolean> handleOption) {
        handleOption.put("aftersale", false);
        handleOption.put("cancel", false);
        handleOption.put("comment", false);
        handleOption.put("confirm", false);
        handleOption.put("delete", false);
        handleOption.put("pay", false);
        handleOption.put("rebuy", false);
        handleOption.put("refund", false);
    }

    //已付款订单
    private void handlerOptionpaid(HashMap<String, Boolean> handleOption) {
        handleOption.put("aftersale", false);
        handleOption.put("cancel", false);
        handleOption.put("comment", false);
        handleOption.put("confirm", false);
        handleOption.put("delete", false);
        handleOption.put("pay", false);
        handleOption.put("rebuy", true);
        handleOption.put("refund", true);
    }

    //已发货订单
    private void handleOptionShiped(HashMap<String, Boolean> handleOption) {
        handleOption.put("aftersale", false);
        handleOption.put("cancel", false);
        handleOption.put("comment", false);
        handleOption.put("confirm", true);
        handleOption.put("delete", false);
        handleOption.put("pay", false);
        handleOption.put("rebuy", true);
        handleOption.put("refund", false);
    }


    //已取消或已退款订单
    private void handleOptionCancelAndRefunded(HashMap<String, Boolean> handleOption) {
        handleOption.put("aftersale", false);
        handleOption.put("cancel", false);
        handleOption.put("comment", false);
        handleOption.put("confirm", false);
        handleOption.put("delete", true);
        handleOption.put("pay", false);
        handleOption.put("rebuy", false);
        handleOption.put("refund", false);
    }

    //未付款订单
    private void handleOptionUnpaid(HashMap<String, Boolean> handleOption) {
        handleOption.put("aftersale", false);
        handleOption.put("cancel", true);
        handleOption.put("comment", false);
        handleOption.put("confirm", false);
        handleOption.put("delete", false);
        handleOption.put("pay", true);
        handleOption.put("rebuy", false);
        handleOption.put("refund", false);
    }

    private void setOrderInfoAboutExpression(HashMap<String, Object> orderInfo, MarketOrder marketOrder) {
        String shipChannel = marketOrder.getShipChannel();
        if (shipChannel != null && !shipChannel.isEmpty()) {
            for (ExpressionEnum value : ExpressionEnum.values()) {
                if (value.getExpCode().equals(shipChannel)) {
                    orderInfo.put("expCode", shipChannel);
                    orderInfo.put("expName", value.getExpName());
                    orderInfo.put("expNo", value.getExpNo());
                }
            }
        }
    }

    private void setOrderInfoAboutHandle0ption(HashMap<String, Object> orderInfo, MarketOrder marketOrder) {
        BigDecimal actualPrice = marketOrder.getActualPrice();
        LocalDateTime addTime = marketOrder.getAddTime();
        String address = marketOrder.getAddress();
        String consignee = marketOrder.getConsignee();
        BigDecimal couponPrice = marketOrder.getCouponPrice();
        BigDecimal freightPrice = marketOrder.getFreightPrice();
        Integer id = marketOrder.getId();
        String message = marketOrder.getMessage();
        String mobile = marketOrder.getMobile();
        String orderSn = marketOrder.getOrderSn();
        Short orderStatus = marketOrder.getOrderStatus();

        orderInfo.put("actualPrice", actualPrice);
        orderInfo.put("addTime", addTime);
        orderInfo.put("address", address);
        orderInfo.put("consignee", consignee);
        orderInfo.put("couponPrice", couponPrice);
        orderInfo.put("freightPrice", freightPrice);
        orderInfo.put("id", id);
        orderInfo.put("message", message);
        orderInfo.put("mobile", mobile);
        orderInfo.put("orderSn", orderSn);

        String res = "";
        if (orderStatus == 101) {
            res = "未付款";
        } else if (orderStatus == 102) {
            res = "用户取消";
        } else if (orderStatus == 103) {
            res = "系统取消";
        } else if (orderStatus == 201) {
            res = "已付款";
        } else if (orderStatus == 202) {
            res = "申请退款";
        } else if (orderStatus == 203) {
            res = "已退款";
        } else if (orderStatus == 301) {
            res = "已发货";
        } else if (orderStatus == 401) {
            res = "用户收货";
        } else if (orderStatus == 402) {
            res = "系统收货";
        }
        orderInfo.put("orderStatusText", res);
    }


    private MarketOrder putOrderGoodsAndReturnOrderInfo(OrderDetailVo orderDetailVo, Integer orderId) {
        MarketOrderGoodsExample marketOrderGoodsExample = new MarketOrderGoodsExample();
        marketOrderGoodsExample.createCriteria().andOrderIdEqualTo(orderId);
        List<MarketOrderGoods> marketOrderGoods = marketOrderGoodsMapper.selectByExample(marketOrderGoodsExample);
        orderDetailVo.setOrderGoods(marketOrderGoods);
        return marketOrderMapper.selectByPrimaryKey(orderId);
    }

    private void PutExpressInfo(OrderDetailVo orderDetailVo) {
        orderDetailVo.setExpressInfo(new ArrayList<>());
    }


//===================================================================================
    /*private List<MarketOrderGoods> orderGoods(Integer orderId) {
        MarketOrderGoodsExample marketOrderGoodsExample = new MarketOrderGoodsExample();
        MarketOrderGoodsExample.Criteria criteria = marketOrderGoodsExample.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        return marketOrderGoodsMapper.selectByExample(marketOrderGoodsExample);
    }

    private Map<String, Object> orderInfo(Integer orderId) {
        HashMap<String, Object> orderInfo = new HashMap<>();

        MarketOrder marketOrder = marketOrderMapper.selectByPrimaryKey(orderId);

        String consignee = marketOrder.getConsignee();//
        String address = marketOrder.getAddress();//
        LocalDateTime addTime = marketOrder.getAddTime();//
        String orderSn = marketOrder.getOrderSn();//
        BigDecimal actualPrice = marketOrder.getActualPrice();//
        String mobile = marketOrder.getMobile();//
        String message = marketOrder.getMessage();//
        Short orderStatus = marketOrder.getOrderStatus();//
        Short aftersaleStatus = marketOrder.getAftersaleStatus();//
        BigDecimal goodsPrice = marketOrder.getGoodsPrice();//
        BigDecimal couponPrice = marketOrder.getCouponPrice();//
        Integer id = marketOrder.getId();//
        BigDecimal freightPrice = marketOrder.getFreightPrice();//

        String expName = "";
        String expNo = "";
        String shipChannel = marketOrder.getShipChannel();
        if (shipChannel != null) {
            switch (shipChannel) {
                case "ZTO":
                    expName = "中通快递";
                    expNo = "1171111";
                    break;
                case "STO":
                    expName = "申通快递";
                    expNo = "1172222";
                    break;
                case "YTO":
                    expName = "圆通快递";
                    expNo = "1173333";
                    break;
                case "EMS":
                    expName = "中国邮政EMS";
                    expNo = "1174444";
                    break;
                case "SF":
                    expName = "顺丰速运";
                    expNo = "1175555";
                    break;
                case "HTKY":
                    expName = "百世汇通";
                    expNo = "1176666";
                    break;
            }
        }
        if (!expName.isEmpty()) {
            orderInfo.put("expName", expName);
            orderInfo.put("expNo", expNo);
            orderInfo.put("shipChannel", shipChannel);
        }

        Boolean deleted = marketOrder.getDeleted();

        Map<String, Object> handleOption = new HashMap<>();
        Map<String, Object> resultHandleOption = resultHandleOption(orderStatus, handleOption);

        orderInfo.put("actualPrice", actualPrice);
        orderInfo.put("addTime", addTime);
        orderInfo.put("address", address);
        orderInfo.put("aftersaleStatus", aftersaleStatus);
        orderInfo.put("consignee", consignee);
        orderInfo.put("couponPrice", couponPrice);
        orderInfo.put("freightPrice", freightPrice);
        orderInfo.put("goodsPrice", goodsPrice);
        orderInfo.put("message", message);
        orderInfo.put("mobile", mobile);
        orderInfo.put("ordersn", orderSn);
        orderInfo.put("orderStatus", orderStatus);
        orderInfo.put("handleOption", resultHandleOption);
        orderInfo.put("id", id);
        orderInfo.put("deleted", deleted);
        return orderInfo;

    }

    private Map<String, Object> resultHandleOption(Short orderStatus, Map<String, Object> handleOption) {
        //101 未付款订单  可以付款 取消
        if (orderStatus == 101) {
            handleOption.put("aftersale", false);
            handleOption.put("cancel", true);
            handleOption.put("comment", false);
            handleOption.put("confirm", false);
            handleOption.put("delete", false);
            handleOption.put("pay", true);
            handleOption.put("rebuy", false);
            handleOption.put("refund", false);
        }
        //102 103 取消的订单  删除
        else if (orderStatus == 102 || orderStatus == 103 ) {
            handleOption.put("aftersale", false);
            handleOption.put("cancel", false);
            handleOption.put("comment", false);
            handleOption.put("confirm", false);
            handleOption.put("delete", true);
            handleOption.put("pay", false);
            handleOption.put("rebuy", false);
            handleOption.put("refund", false);
        }
        //401 402 收货的订单  申请售后 评价 删除 再来一单
        else if (orderStatus == 401 || orderStatus == 402) {
            handleOption.put("aftersale", true);
            handleOption.put("cancel", false);
            handleOption.put("comment", true);
            handleOption.put("confirm", false);
            handleOption.put("delete", true);
            handleOption.put("pay", false);
            handleOption.put("rebuy", true);
            handleOption.put("refund", false);
        }
        //203 已经退款的订单  删除
        else if (orderStatus == 203) {
            handleOption.put("aftersale", false);
            handleOption.put("cancel", false);
            handleOption.put("comment", false);
            handleOption.put("confirm", false);
            handleOption.put("delete", true);
            handleOption.put("pay", false);
            handleOption.put("rebuy", false);
            handleOption.put("refund", false);
        }
        //201 301已经发货/付款的订单  申请退款 再来一单
        else if (orderStatus == 201 || orderStatus == 301) {
            handleOption.put("aftersale", false);
            handleOption.put("cancel", false);
            handleOption.put("comment", false);
            handleOption.put("confirm", true);
            handleOption.put("delete", false);
            handleOption.put("pay", false);
            handleOption.put("rebuy", true);
            handleOption.put("refund", true);
        }
        //申请退款 无按键
        else {
            handleOption.put("aftersale", false);
            handleOption.put("cancel", false);
            handleOption.put("comment", false);
            handleOption.put("confirm", false);
            handleOption.put("delete", false);
            handleOption.put("pay", false);
            handleOption.put("rebuy", false);
            handleOption.put("refund", false);
        }
        return handleOption;
    }

    private List<Integer> expressionInfo(Integer orderId) {
        return new ArrayList<>();
    }*/

//====================================================================================

    private List<OrderListVo> getResultOrderListVo(List<MarketOrder> allOrder, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<OrderListVo> orderListVos = new ArrayList<>();

        allOrder.sort(Comparator.comparing(MarketOrder::getAddTime).reversed());

        for (MarketOrder marketOrder : allOrder) {
            OrderListVo orderListVo = new OrderListVo();
            Integer marketOrderId = marketOrder.getId();

            MarketOrderGoodsExample marketOrderGoodsExample = new MarketOrderGoodsExample();
            marketOrderGoodsExample.createCriteria().andOrderIdEqualTo(marketOrderId);
            List<MarketOrderGoods> marketOrderGoodsList = marketOrderGoodsMapper.selectByExample(marketOrderGoodsExample);

            orderListVo.setActualPrice(marketOrder.getActualPrice());
            orderListVo.setAftersaleStatus(orderListVo.getAftersaleStatus());
            orderListVo.setGoodsList(marketOrderGoodsList);
            orderListVo.setHandleOperation(createHandleOperation());
            orderListVo.setId(marketOrder.getId());
            orderListVo.setIsGroupin(false);
            orderListVo.setDeleted(false);
            orderListVo.setOrderSn(marketOrder.getOrderSn());
            orderListVo.setOrderStatusText(statusText(marketOrder.getOrderStatus()));
            orderListVos.add(orderListVo);
        }
        return orderListVos;

    }

    private String statusText(Short orderStatus) {
        String res = "";
        if (orderStatus == 401) {
            res = "用户收货";
        } else if (orderStatus == 402) {
            res = "系统收货";
        } else if (orderStatus == 203) {
            res = "已退款";
        } else if (orderStatus == 202) {
            res = "申请退款";
        } else if (orderStatus == 103) {
            res = "系统取消";
        } else if (orderStatus == 301) {
            res = "已发货";
        } else if (orderStatus == 101) {
            res = "未付款";
        } else if (orderStatus == 102) {
            res = "用户取消";
        } else if (orderStatus == 201) {
            res = "已付款";
        }
        return res;
    }

    private Map<String, Boolean> createHandleOperation() {
        Map<String, Boolean> handleOption = new HashMap<>();
        handleOption.put("aftersale", false);
        handleOption.put("cancel", false);
        handleOption.put("comment", false);
        handleOption.put("delete", false);
        handleOption.put("pay", false);
        handleOption.put("rebuy", false);
        handleOption.put("refund", false);
        return handleOption;
    }

    //根据不同的showtype，获取不同的订单列表
    private List<MarketOrder> getAllOrder(MarketUser user, Integer showType) {
        Integer userId = user.getId();
        if (userId != null) {
            MarketOrderExample marketOrderExample = new MarketOrderExample();
            MarketOrderExample.Criteria criteria = marketOrderExample.createCriteria();
            criteria.andUserIdEqualTo(userId);
            criteria.andDeletedEqualTo(false);
            //待付款
            if (showType == 1) {
                criteria.andOrderStatusEqualTo((short) 101);
            }
            //待发货
            else if (showType == 2) {
                criteria.andOrderStatusEqualTo((short) 201);
            }
            //待收货
            else if (showType == 3) {
                criteria.andOrderStatusEqualTo((short) 301);
            }
            //待评价
            else if (showType == 4) {
                criteria.andOrderStatusEqualTo((short) 401);
                //criteria.andOrderStatusEqualTo((short) 402);
            }
            return marketOrderMapper.selectByExample(marketOrderExample);
        }
        return null;
    }

    @Override
    public Integer submit(Integer addressId, Integer cartId, Integer couponId, Integer grouponRulesId, Integer grouponLinkId, String message, Integer userCouponId, Integer userId, BigDecimal money) {
        MarketAddress marketAddress = marketAddressMapper.selectByPrimaryKey(addressId);
        MarketCoupon marketCoupon = marketCouponMapper.selectByPrimaryKey(couponId);

        //计算需要支付的金额
        BigDecimal amount = money;
        BigDecimal discount = BigDecimal.ZERO;
        if(marketCoupon!=null){
            discount = marketCoupon.getDiscount();
        }else {
            discount = BigDecimal.ZERO;
        }
        BigDecimal goodsPrice = amount.subtract(discount);


        //添加Order和order Goods记录

        //添加order
        MarketOrder marketOrder = new MarketOrder();
        //设置orderSn
        marketOrder.setOrderSn(generateOrderSn());
        //设置userId
        marketOrder.setUserId(userId);
        //设置consignee 取name给consignee
        marketOrder.setConsignee(marketAddress.getName());
        //设置mobile
        marketOrder.setMobile(marketAddress.getTel());
        //设置address
        marketOrder.setAddress(marketAddress.getAddressDetail());
        //设置message
        marketOrder.setMessage(message);
        //设置goodsPrice
        marketOrder.setGoodsPrice(goodsPrice);
        //设置couponPrice
        marketOrder.setCouponPrice(discount);
        //设置orderPrice
        marketOrder.setOrderPrice(amount);
        //设置actualPrice list和detail显示的是这个
        marketOrder.setActualPrice(amount);
        //设置addTime
        marketOrder.setAddTime(LocalDateTime.now());
        //设置可评论
        marketOrder.setComments((short) 1);
        //订单状态定义为待付款 orderStatus = 101  虽然submit后会自动调用prepay再设置成 201已付款
        marketOrder.setOrderStatus(Short.valueOf("101"));
        marketOrder.setDeleted(false);
        marketOrder.setGrouponPrice(BigDecimal.ZERO);
        marketOrder.setIntegralPrice(BigDecimal.ZERO);
        marketOrder.setMessage("已下单");

        //设置运费金额
        MarketSystemExample marketSystemExample = new MarketSystemExample();
        marketSystemExample.createCriteria().andKeyNameEqualTo("market_express_freight_min");
        MarketSystem marketSystem = marketSystemMapper.selectOneByExample(marketSystemExample);
        MarketSystemExample marketSystemExample1 = new MarketSystemExample();
        marketSystemExample1.createCriteria().andKeyNameEqualTo("market_express_freight_value");
        MarketSystem marketSystem1 = marketSystemMapper.selectOneByExample(marketSystemExample1);
        BigDecimal actualPrice = BigDecimal.ZERO;
        if (marketSystem != null && marketSystem1 != null) {
            //系统设置不为Null时，使用系统设置值
            BigDecimal minFreeFreightAmount = BigDecimal.valueOf(Long.parseLong(marketSystem.getKeyValue()));
            BigDecimal freightPrice = BigDecimal.valueOf(Long.parseLong(marketSystem1.getKeyValue()));
            if (money.compareTo(minFreeFreightAmount)>=0){
                //此时满足满减规则
                marketOrder.setFreightPrice(BigDecimal.ZERO);
                //无需运费
            }else{
                //此时需要运费
                marketOrder.setFreightPrice(freightPrice);
            }
        } else {
            //系统设置缺失时，使用默认满88-8
            if (money.compareTo(BigDecimal.valueOf(88))>=0){
                //此时满足满减规则
                marketOrder.setFreightPrice(BigDecimal.ZERO);
                //无需运费
            }else{
                //此时需要运费
                marketOrder.setFreightPrice(BigDecimal.valueOf(8));
            }
        }
        marketOrderMapper.insert(marketOrder);
        MarketOrderExample marketOrderExample = new MarketOrderExample();
        marketOrderExample.createCriteria().andOrderSnEqualTo(marketOrder.getOrderSn());
        MarketOrder submitOrder = marketOrderMapper.selectOneByExample(marketOrderExample);


        //todo 添加orderGoods 从购物车获取checked=1的goodsId 循环set


        MarketCartExample marketCartExample = new MarketCartExample();
        if (cartId!=0){
            marketCartExample.createCriteria().andIdEqualTo(cartId).andDeletedEqualTo(false);
        }else {
            marketCartExample.createCriteria().andUserIdEqualTo(userId).andCheckedEqualTo(true).andDeletedEqualTo(false);
        }
        List<MarketCart> marketCarts = marketCartMapper.selectByExample(marketCartExample);
        if (marketCarts.isEmpty()){
            //已经提交过订单
            return -1;
        }
        for (MarketCart marketCart : marketCarts) {
            //先插入再删除
            if(marketCart.getChecked()) {
                MarketOrderGoods marketOrderGoods = new MarketOrderGoods();
                //设置orderId
                marketOrderGoods.setOrderId(marketOrder.getId());
                marketOrderGoods.setGoodsName(marketCart.getGoodsName());
                marketOrderGoods.setGoodsSn(marketCart.getGoodsSn());
                marketOrderGoods.setProductId(marketCart.getProductId());
                marketOrderGoods.setNumber(marketCart.getNumber());
                marketOrderGoods.setPrice(marketCart.getPrice());
                marketOrderGoods.setSpecifications(marketCart.getSpecifications());
                marketOrderGoods.setPicUrl(marketCart.getPicUrl());
                marketOrderGoods.setAddTime(LocalDateTime.now());
                marketOrderGoods.setGoodsId(marketCart.getGoodsId());
                marketOrderGoods.setComment(0);
                marketOrderGoodsMapper.insert(marketOrderGoods);
            }


            //删除购物车中的记录
            //具体的删除购物车中物品的逻辑
            marketCart.setChecked(false);
            marketCart.setDeleted(true);
            marketCartMapper.updateByPrimaryKey(marketCart);
        }
        //减去相应商品的库存--库存不足抛出异常
        for (MarketCart marketCart : marketCarts) {
            MarketGoodsProductExample marketGoodsProductExample = new MarketGoodsProductExample();
            marketGoodsProductExample.createCriteria().andGoodsIdEqualTo(marketCart.getGoodsId());
            MarketGoodsProduct marketGoodsProduct = marketGoodsProductMapper.selectOneByExample(marketGoodsProductExample);
            if (marketCart.getNumber() > marketGoodsProduct.getNumber()) {
                //库存不足，抛出异常
                throw new RuntimeException(marketCart.getGoodsName() + "库存不足");
            }
        }
        //如果使用了优惠券，将优惠券标记为已使用
        MarketCouponUserExample marketCouponUserExample = new MarketCouponUserExample();
        marketCouponUserExample.createCriteria().andUserIdEqualTo(userId).andIdEqualTo(couponId);
        MarketCouponUser marketCouponUser = marketCouponUserMapper.selectOneByExample(marketCouponUserExample);
        if (marketCouponUser!=null){
            marketCouponUser.setStatus((short) 1);
            marketCouponUserMapper.updateByPrimaryKey(marketCouponUser);
        }

        return submitOrder.getId();

    }
    private String generateOrderSn() {
        // 获取当前日期时间并格式化为"yyyyMMdd"格式
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String datePart = now.format(formatter);

        // 生成六位随机数
        Random random = new Random();
        int randomPart = random.nextInt(900000) + 100000; // 生成100000到999999之间的随机数

        // 拼接日期部分和随机数生成订单号
        String orderSn = datePart + String.valueOf(randomPart);

        return orderSn;
    }
}
