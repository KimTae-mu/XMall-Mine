package com.alva.manager.task;

import cn.hutool.core.date.DateUtil;
import com.alva.manager.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public class CancelOrderJob {

    static final Logger log = LoggerFactory.getLogger(CancelOrderJob.class);

    @Autowired
    private OrderService orderService;

    /**
     * 每一个小时判断订单是否失效
     */
    @Scheduled(cron = "0 0 */1 * * ?")
    public void run(){
        log.info("执行了自动取消订单定时任务 - "+ DateUtil.now());
        orderService.cancelOrder();
    }
}
