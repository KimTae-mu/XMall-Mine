package com.alva.common.constant;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public class PaymentConstant {
    /**
     * 未付款
     */
    public static final Integer UNPAID = 0;

    /**
     * 已付款
     */
    public static final Integer PAID = 1;

    /**
     * 未发货
     */
    public static final Integer NOTSHIPPED = 2;

    /**
     * 已发货
     */
    public static final Integer SHIPPED = 3;

    /**
     * 交易成功
     */
    public static final Integer ORDERSUCCESS = 4;

    /**
     * 交易关闭
     */
    public static final Integer ORDERCLOSED = 5;

    /**
     * 交易失败
     */
    public static final Integer ORDERFAILED = 6;
}
