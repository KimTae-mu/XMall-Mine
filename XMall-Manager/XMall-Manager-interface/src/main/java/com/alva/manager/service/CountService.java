package com.alva.manager.service;

import com.alva.manager.dto.OrderChartData;

import java.util.Date;
import java.util.List;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public interface CountService {
    /**
     * 统计订单销量
     *
     * @param type
     * @param startTime
     * @param endTime
     * @param year
     * @return
     */
    List<OrderChartData> getOrderCountData(int type, Date startTime,Date endTime,int year);
}
