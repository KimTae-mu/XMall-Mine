package com.alva.manager.service.impl;

import com.alva.common.jedis.JedisClient;
import com.alva.common.pojo.DataTablesResult;
import com.alva.manager.dto.OrderDetail;
import com.alva.manager.mapper.TbOrderItemMapper;
import com.alva.manager.mapper.TbOrderMapper;
import com.alva.manager.mapper.TbOrderShippingMapper;
import com.alva.manager.mapper.TbThanksMapper;
import com.alva.manager.pojo.TbOrder;
import com.alva.manager.pojo.TbThanks;
import com.alva.manager.service.OrderService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper tbOrderMapper;
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;
    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;
    @Autowired
    private TbThanksMapper tbThanksMapper;

    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private EmailUtil emailUtil;

    @Override
    public DataTablesResult getOrderList(int draw, int start, int length, String search, String orderCol, String orderDir) {
        DataTablesResult result = new DataTablesResult();
        //分页
        PageHelper.startPage(start + length + 1, length);
        List<TbOrder> list = tbOrderMapper.selectByMulti("%" + search + "%", orderCol, orderDir);
    }

    @Override
    public Long countOrder() {
        return null;
    }

    @Override
    public OrderDetail getOrderDetail(String orderId) {
        return null;
    }

    @Override
    public int deliver(String orderId, String shippingName, String shippingCode, BigDecimal postFee) {
        return 0;
    }

    @Override
    public int remark(String orderId, String message) {
        return 0;
    }

    @Override
    public int cancelOrderByAdmin(String orderId) {
        return 0;
    }

    @Override
    public int deleteOrder(String id) {
        return 0;
    }

    @Override
    public int cancelOrder() {
        return 0;
    }

    @Override
    public int passPay(String tokenName, String token, String id) {
        return 0;
    }

    @Override
    public int backPay(String tokenName, String token, String id) {
        return 0;
    }

    @Override
    public int notShowPay(String tokenName, String token, String id) {
        return 0;
    }

    @Override
    public int editPay(String tokenName, String token, TbThanks tbThanks) {
        return 0;
    }

    @Override
    public int payDelNotNotify(String tokenName, String token, String id) {
        return 0;
    }

    @Override
    public int payDel(String tokenName, String token, String id) {
        return 0;
    }
}
