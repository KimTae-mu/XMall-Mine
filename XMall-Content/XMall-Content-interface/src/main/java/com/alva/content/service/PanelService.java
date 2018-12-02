package com.alva.content.service;

import com.alva.common.pojo.ZTreeNode;
import com.alva.manager.pojo.TbPanel;

import java.util.List;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public interface PanelService {
    /**
     * 获得首页板块列表不含轮播
     *
     * @param i
     * @param b
     * @return
     */
    List<ZTreeNode> getPanelList(int position, boolean showAll);

    /**
     * 添加板块
     *
     * @param tbPanel
     */
    int addPanel(TbPanel tbPanel);

    /**
     * 更新板块
     *
     * @param tbPanel
     */
    int updatePanel(TbPanel tbPanel);

    /**
     * 删除板块
     *
     * @param id
     */
    int deletePanel(int id);
}
