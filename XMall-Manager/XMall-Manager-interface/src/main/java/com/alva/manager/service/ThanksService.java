package com.alva.manager.service;

import com.alva.common.pojo.DataTablesResult;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public interface ThanksService {
    /**
     * 获取捐赠列表
     *
     * @return
     */
    DataTablesResult getThanksList();

    /**
     * 获取捐赠列表总数
     *
     * @return
     */
    Long countThanks();

}
