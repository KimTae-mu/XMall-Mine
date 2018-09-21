package com.alva.manager.service;

import com.alva.manager.pojo.TbExpress;

import java.util.List;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public interface ExpressService {

    /**
     * 获取快递列表
     * @return
     */
    List<TbExpress> getExpressList();

    /**
     * 添加快递
     * @param tbExpress
     * @return
     */
    int addExpress(TbExpress tbExpress);

    /**
     * 更新快递
     * @param tbExpress
     * @return
     */
    int updateExpress(TbExpress tbExpress);

    /**
     * 删除快递
     * @param tbExpress
     * @return
     */
    int delExpress(TbExpress tbExpress);

}
