package com.alva.manager.service.impl;

import com.alva.common.exception.XmallException;
import com.alva.common.pojo.DataTablesResult;
import com.alva.manager.mapper.TbBaseMapper;
import com.alva.manager.mapper.TbLogMapper;
import com.alva.manager.mapper.TbOrderItemMapper;
import com.alva.manager.mapper.TbShiroFilterMapper;
import com.alva.manager.pojo.*;
import com.alva.manager.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public class SystemServiceImpl implements SystemService {

    @Autowired
    private TbShiroFilterMapper tbShiroFilterMapper;
    @Autowired
    private TbBaseMapper tbBaseMapper;
    @Autowired
    private TbLogMapper tbLogMapper;
    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Value("1")
    private String BASE_ID;

    @Override
    public List<TbShiroFilter> getShiroFilter() {
        TbShiroFilterExample example = new TbShiroFilterExample();
        example.setOrderByClause("sort_order");
        List<TbShiroFilter> list = tbShiroFilterMapper.selectByExample(example);
        return list;
    }

    @Override
    public int addLog(TbLog tbLog) {
        if (tbLogMapper.insert(tbLog) != 1) {
            throw new XmallException("保存日志失败");
        }
        return 1;
    }

    @Override
    public Long countShiroFilter() {
        TbShiroFilterExample example = new TbShiroFilterExample();
        Long result = tbShiroFilterMapper.countByExample(example);
        if (result == null) {
            throw new XmallException("获取Shiro过滤链数目失败");
        }
        return result;
    }

    @Override
    public int addShiroFilter(TbShiroFilter tbShiroFilter) {
        if (tbShiroFilterMapper.insert(tbShiroFilter) != 1) {
            throw new XmallException("添加Shiro过滤链失败");
        }
        return 1;
    }

    @Override
    public int updateShiro(TbShiroFilter tbShiroFilter) {
        if (tbShiroFilterMapper.updateByPrimaryKey(tbShiroFilter) != 1) {
            throw new XmallException("更新Shiro过滤链失败");
        }
        return 1;
    }

    @Override
    public int deleteShiroFilter(int id) {
        if (tbShiroFilterMapper.deleteByPrimaryKey(id) != 1) {
            throw new XmallException("删除Shiro过滤链失败");
        }
        return 1;
    }

    @Override
    public TbBase getBase() {
        TbBase tbBase = tbBaseMapper.selectByPrimaryKey(Integer.valueOf(BASE_ID));
        if (tbBase == null) {
            throw new XmallException("获取基础设置失败");
        }
        return tbBase;
    }

    @Override
    public int updateBase(TbBase tbBase) {
        if(tbBaseMapper.updateByPrimaryKey(tbBase) != 1){
            throw new XmallException("更新基础设置失败");
        }
        return 1;
    }

    @Override
    public TbOrderItem getWeekHot() {
        List<TbOrderItem> list = tbOrderItemMapper.getWeekHot();
        return null;
    }

    @Override
    public DataTablesResult getLogList(int draw, int start, int length, String search, String orderColumn, String orderDir) {
        return null;
    }

    @Override
    public Long countLog() {
        return null;
    }

    @Override
    public int deleteLog(int id) {
        return 0;
    }

    @Override
    public int deleteLogs(int[] ids) {
        return 0;
    }
}
