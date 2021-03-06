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

    /**
     * 获取首页缓存
     * @return
     */
    String getIndexRedis();

    /**
     * 同步首页缓存
     */
    int updateIndexRedis();

    /**
     * 获取推荐板块缓存
     * @return
     */
    String getRecommendRedis();

    /**
     * 刷新推荐板块缓存
     * @return
     */
    int updateRecommendRedis();

    /**
     * 获取捐赠板块缓存
     * @return
     */
    String getThankRedis();

    /**
     * 刷新捐赠板块缓存
     * @return
     */
    int updateThankRedis();
}
