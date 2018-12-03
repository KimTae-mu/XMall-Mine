package com.alva.manager.service;

import com.alva.common.pojo.DataTablesResult;
import com.alva.manager.pojo.TbThanks;

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

    /**
     * 添加捐赠
     *
     * @param tbThanks
     * @return
     */
    int addThanks(TbThanks tbThanks);

    /**
     * 编辑捐赠
     *
     * @param tbThanks
     * @return
     */
    int updateThanks(TbThanks tbThanks);

    /**
     * 删除捐赠
     *
     * @param ids
     * @return
     */
    int deleteThanks(int[] ids);

    /**
     * 通过id获取捐赠
     *
     * @param id
     * @return
     */
    TbThanks getThank(int id);
}
