package com.alva.content.service;

import com.alva.common.pojo.DataTablesResult;
import com.alva.manager.pojo.TbPanelContent;

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

    /**
     * 添加板块内容
     * @param tbPanelContent
     */
    int addPanelContent(TbPanelContent tbPanelContent);

    /**
     * 编辑板块内容
     * @param tbPanelContent
     * @return
     */
    int updateContent(TbPanelContent tbPanelContent);

    /**
     * 删除板块
     * @param id
     * @return
     */
    int deletePanelContent(int id);
}
