package com.alva.content.service;

import com.alva.common.pojo.DataTablesResult;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public interface ContentService {

    /**
     * 通过panelId获取板块具体内容
     * @param panelId
     * @return
     */
    DataTablesResult getPanelContentListByPanelId(int panelId);
}
