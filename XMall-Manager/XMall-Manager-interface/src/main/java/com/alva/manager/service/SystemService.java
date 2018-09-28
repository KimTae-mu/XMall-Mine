package com.alva.manager.service;

import com.alva.common.pojo.DataTablesResult;
import com.alva.manager.pojo.TbBase;
import com.alva.manager.pojo.TbLog;
import com.alva.manager.pojo.TbOrderItem;
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

    /**
     * 统计过滤链数目
     * @return
     */
    Long countShiroFilter();

    /**
     * 添加Shiro过滤链
     * @param tbShiroFilter
     * @return
     */
    int addShiroFilter(TbShiroFilter tbShiroFilter);

    /**
     * 更新Shiro过滤链
     * @param tbShiroFilter
     * @return
     */
    int updateShiro(TbShiroFilter tbShiroFilter);

    /**
     * 删除Shiro过滤链
     * @param id
     * @return
     */
    int deleteShiroFilter(int id);

    /**
     * 获取网站基础设置
     * @return
     */
    TbBase getBase();

    /**
     * 更新网站基础设置
     * @param tbBase
     * @return
     */
    int updateBase(TbBase tbBase);

    /**
     * 获取本周热销商品
     * @return
     */
    TbOrderItem getWeekHot();

    /**
     * 获取日志列表
     * @param draw
     * @param start
     * @param length
     * @param search
     * @param orderColumn
     * @param orderDir
     * @return
     */
    DataTablesResult getLogList(int draw, int start, int length, String search, String orderColumn, String orderDir);

    /**
     * 获取系统日志总数
     * @return
     */
    Long countLog();

    /**
     * 删除日志
     * @param id
     * @return
     */
    int deleteLog(int id);

    /**
     * 删除日志
     * @param ids
     * @return
     */
    int deleteLogs(int[] ids);
}
