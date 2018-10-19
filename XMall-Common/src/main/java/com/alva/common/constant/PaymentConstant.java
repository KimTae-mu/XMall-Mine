package com.alva.common.constant;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public interface PaymentConstant {
    /**
     * 未付款
     */
    Integer UNPAID = 0;

    /**
     * 已付款
     */
    Integer PAID = 1;

    /**
     * 未发货
     */
    Integer NOTSHIPPED = 2;

    /**
     * 已发货
     */
    Integer SHIPPED = 3;

    /**
     * 交易成功
     */
    Integer ORDERSUCCESS = 4;

    /**
     * 交易关闭
     */
    Integer ORDERCLOSED = 5;
}
