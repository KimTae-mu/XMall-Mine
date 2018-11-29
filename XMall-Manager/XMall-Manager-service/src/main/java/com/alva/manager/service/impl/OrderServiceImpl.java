package com.alva.manager.service.impl;

import com.alva.common.constant.PaymentConstant;
import com.alva.common.constant.ThanksConstant;
import com.alva.common.exception.XmallException;
import com.alva.common.jedis.JedisClient;
import com.alva.common.pojo.DataTablesResult;
import com.alva.manager.dto.OrderDetail;
import com.alva.manager.mapper.TbOrderItemMapper;
import com.alva.manager.mapper.TbOrderMapper;
import com.alva.manager.mapper.TbOrderShippingMapper;
import com.alva.manager.mapper.TbThanksMapper;
import com.alva.manager.pojo.*;
import com.alva.manager.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sun.awt.X11FontManager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        PageInfo<TbOrder> pageInfo = new PageInfo<>(list);

        result.setRecordsFiltered((int) pageInfo.getTotal());
        result.setRecordsTotal(Math.toIntExact(cancelOrder()));

        result.setDraw(draw);
        result.setData(list);
        return result;
    }

    @Override
    public Long countOrder() {
        TbOrderExample example = new TbOrderExample();
        Long result = tbOrderMapper.countByExample(example);
        if (result == null) {
            throw new XmallException("统计订单数目失败");
        }
        return result;
    }

    @Override
    public OrderDetail getOrderDetail(String orderId) {
        OrderDetail orderDetail = new OrderDetail();
        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(orderId);

        TbOrderItemExample example = new TbOrderItemExample();
        TbOrderItemExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        List<TbOrderItem> list = tbOrderItemMapper.selectByExample(example);
        TbOrderShipping tbOrderShipping = tbOrderShippingMapper.selectByPrimaryKey(orderId);

        orderDetail.setTbOrder(tbOrder);
        orderDetail.setTbOrderItems(list);
        orderDetail.setTbOrderShipping(tbOrderShipping);

        return orderDetail;
    }

    @Override
    public int deliver(String orderId, String shippingName, String shippingCode, BigDecimal postFee) {
        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(orderId);
        tbOrder.setShippingName(shippingName);
        tbOrder.setShippingCode(shippingCode);
        tbOrder.setPostFee(postFee);
        tbOrder.setConsignTime(new Date());
        tbOrder.setUpdateTime(new Date());

        tbOrder.setStatus(PaymentConstant.SHIPPED);
        tbOrderMapper.updateByPrimaryKey(tbOrder);
        return 1;
    }

    @Override
    public int remark(String orderId, String message) {
        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(orderId);
        tbOrder.setBuyerMessage(message);
        tbOrder.setUpdateTime(new Date());
        tbOrderMapper.updateByPrimaryKey(tbOrder);

        return 1;
    }

    @Override
    public int cancelOrderByAdmin(String orderId) {
        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(orderId);
        tbOrder.setCloseTime(new Date());
        tbOrder.setUpdateTime(new Date());
        tbOrder.setStatus(PaymentConstant.ORDERCLOSED);
        tbOrderMapper.updateByPrimaryKey(tbOrder);
        return 1;
    }

    @Override
    public int deleteOrder(String id) {
        if (tbOrderMapper.deleteByPrimaryKey(id) != 1) {
            throw new XmallException("删除订单数失败");
        }

        TbOrderItemExample example = new TbOrderItemExample();
        TbOrderItemExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(id);
        List<TbOrderItem> list = tbOrderItemMapper.selectByExample(example);

        for (TbOrderItem tbOrderItem : list) {
            if (tbOrderItemMapper.deleteByPrimaryKey(tbOrderItem.getId()) != 1) {
                throw new XmallException("删除订单商品失败");
            }
        }
        if (tbOrderShippingMapper.deleteByPrimaryKey(id) != 1) {
            throw new XmallException("删除物流失败");
        }

        return 1;
    }

    @Override
    public int cancelOrder() {
        TbOrderExample example = new TbOrderExample();
        List<TbOrder> tbOrders = tbOrderMapper.selectByExample(example);
        for (TbOrder tbOrder : tbOrders) {
            judgeOrder(tbOrder);
        }
        return 1;
    }

    /**
     * 判断订单是否超时未支付
     *
     * @param tbOrder
     * @return
     */
    private String judgeOrder(TbOrder tbOrder) {
        String result = null;
        if (tbOrder.getStatus() == 0) {
            //判断是否已超一天
            long diff = System.currentTimeMillis() - tbOrder.getCreateTime().getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            if (days >= 1) {
                //设置失效
                tbOrder.setStatus(PaymentConstant.ORDERCLOSED);
                tbOrder.setCloseTime(new Date());
                if (tbOrderMapper.updateByPrimaryKey(tbOrder) != 1) {
                    throw new XmallException("设置订单关闭失败");
                }
            } else {
                //返回到期时间
                long time = tbOrder.getCreateTime().getTime() + 1000 * 60 * 60 * 24;
                result = String.valueOf(time);
            }
        }
        return result;
    }

    @Override
    public int passPay(String tokenName, String token, String id) {

        //验证token
        if (StringUtils.isBlank(tokenName) || StringUtils.isBlank(token) || StringUtils.isBlank(id)) {
            return -1;
        }
        //通过redis获取token的内容
        String value = jedisClient.get(tokenName);
        if (!value.equals(token)) {
            return -1;
        }

        //展示捐赠
        TbThanks tbThanks = tbThanksMapper.selectByPrimaryKey(Integer.valueOf(id));
        if (tbThanks == null) {
            return 0;
        }
        tbThanks.setState(ThanksConstant.CONFIRMATION_DISPLAY);
        if (tbThanksMapper.updateByPrimaryKey(tbThanks) != 1) {
            return 0;
        }

        //修改订单状态
        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(tbThanks.getOrderId());
        if (tbOrder != null) {
            tbOrder.setStatus(PaymentConstant.ORDERSUCCESS);
            tbOrder.setUpdateTime(new Date());
            tbOrder.setEndTime(new Date());
            if (tbOrderMapper.updateByPrimaryKey(tbOrder) != 1) {
                return 0;
            }
        }

        //发送通知邮箱
        if (StringUtils.isNotBlank(tbThanks.getEmail()) && EmailUtil.checkEmail(tbThanks.getEmail())) {
            String content = "您的订单已支付成功，十分感谢您的捐赠!";
            emailUtil.sendEmailPayResult(tbThanks.getEmail(), "[XMALL]支付捐赠成功通知", content);
        }
        return 1;
    }

    @Override
    public int backPay(String tokenName, String token, String id) {

        if (StringUtils.isBlank(tokenName) || StringUtils.isBlank(token) || StringUtils.isBlank(id)) {
            return -1;
        }
        String value = jedisClient.get(tokenName);

        if (!value.equals(token)) {
            return -1;
        }

        //展示捐赠
        TbThanks tbThanks = tbThanksMapper.selectByPrimaryKey(Integer.valueOf(id));
        if (tbThanks == null) {
            return 0;
        }
        tbThanks.setState(ThanksConstant.TURN_DOWN);
        if (tbThanksMapper.updateByPrimaryKey(tbThanks) != 1) {
            return 0;
        }
        //修改订单状态
        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(tbThanks.getOrderId());
        if (tbOrder != null) {
            tbOrder.setStatus(PaymentConstant.ORDERFAILED);
            tbOrder.setUpdateTime(new Date());
            tbOrder.setCloseTime(new Date());
            if (tbOrderMapper.updateByPrimaryKey(tbOrder) != 1) {
                return 0;
            }
        }

        //发送通知邮箱
        if (StringUtils.isNotBlank(tbThanks.getEmail()) && EmailUtil.checkEmail(tbThanks.getEmail())) {
            String content = "抱歉，由于您支付不起或其他原因，您的订单支付失败，请尝试重新支付！";
            emailUtil.sendEmailPayResult(tbThanks.getEmail(), "[XMALL]支付失败通知", content);
        }
        return 1;
    }

    @Override
    public int notShowPay(String tokenName, String token, String id) {

        if (StringUtils.isBlank(tokenName) || StringUtils.isBlank(token) || StringUtils.isBlank(id)) {
            return -1;
        }
        String value = jedisClient.get(tokenName);
        if (!value.equals(token)) {
            return -1;
        }

        TbThanks tbThanks = tbThanksMapper.selectByPrimaryKey(Integer.valueOf(id));
        if (tbThanks == null) {
            return 0;
        }
        tbThanks.setState(ThanksConstant.NOT_SHOWING);
        if (tbThanksMapper.updateByPrimaryKey(tbThanks) != 1) {
            return 0;
        }

        TbOrder tbOrder = tbOrderMapper.selectByPrimaryKey(tbThanks.getOrderId());
        if (tbOrder != null) {
            tbOrder.setStatus(PaymentConstant.ORDERSUCCESS);
            tbOrder.setUpdateTime(new Date());
            tbOrder.setCloseTime(new Date());
            if (tbOrderMapper.updateByPrimaryKey(tbOrder) != 1) {
                return 0;
            }
        }

        //发送通知邮箱
        if (StringUtils.isNotBlank(tbThanks.getEmail()) && EmailUtil.checkEmail(tbThanks.getEmail())) {
            String content = "您的订单已支付成功，十分感谢您的捐赠！<br>但由于您的支付金额过低或其他原因，将不会在捐赠名单中显示，敬请谅解！";
            emailUtil.sendEmailPayResult(tbThanks.getEmail(), "[XMALL]支付捐赠成功通知", content);
        }

        return 0;
    }

    @Override
    public int editPay(String tokenName, String token, TbThanks tbThanks) {

        if (StringUtils.isBlank(tokenName) || StringUtils.isBlank(token) || StringUtils.isBlank(tbThanks.getId().toString())) {
            return -1;
        }
        String value = jedisClient.get(tokenName);
        if (!value.equals(token)) {
            return -1;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(tbThanks.getTime());
            tbThanks.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (tbThanksMapper.updateByPrimaryKey(tbThanks) != 1) {
            return 0;
        }

        return 1;
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
