package com.alva.manager.dto;

import com.alva.manager.pojo.TbOrder;
import com.alva.manager.pojo.TbOrderItem;
import com.alva.manager.pojo.TbOrderShipping;

import java.io.Serializable;
import java.util.List;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public class OrderDetail implements Serializable {

    private TbOrder tbOrder;

    private List<TbOrderItem> tbOrderItems;

    private TbOrderShipping tbOrderShipping;

    public TbOrder getTbOrder() {
        return tbOrder;
    }

    public void setTbOrder(TbOrder tbOrder) {
        this.tbOrder = tbOrder;
    }

    public List<TbOrderItem> getTbOrderItems() {
        return tbOrderItems;
    }

    public void setTbOrderItems(List<TbOrderItem> tbOrderItems) {
        this.tbOrderItems = tbOrderItems;
    }

    public TbOrderShipping getTbOrderShipping() {
        return tbOrderShipping;
    }

    public void setTbOrderShipping(TbOrderShipping tbOrderShipping) {
        this.tbOrderShipping = tbOrderShipping;
    }
}
