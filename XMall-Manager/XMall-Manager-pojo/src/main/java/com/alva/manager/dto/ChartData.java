package com.alva.manager.dto;

import java.util.List;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public class ChartData {

    private List<Object> xDatas;

    private List<Object> yDatas;

    private Object countAll;

    public List<Object> getxDatas() {
        return xDatas;
    }

    public void setxDatas(List<Object> xDatas) {
        this.xDatas = xDatas;
    }

    public List<Object> getyDatas() {
        return yDatas;
    }

    public void setyDatas(List<Object> yDatas) {
        this.yDatas = yDatas;
    }

    public Object getCountAll() {
        return countAll;
    }

    public void setCountAll(Object countAll) {
        this.countAll = countAll;
    }
}
