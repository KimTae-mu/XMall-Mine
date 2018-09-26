package com.alva.manager.service;

import com.alva.manager.pojo.TbLog;
import com.alva.manager.pojo.TbShiroFilter;

import java.util.List;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public interface SystemService {
    List<TbShiroFilter> getShiroFilter();

    /**
     * 添加日志
     * @param tbLog
     * @return
     */
    int addLog(TbLog tbLog);
}
