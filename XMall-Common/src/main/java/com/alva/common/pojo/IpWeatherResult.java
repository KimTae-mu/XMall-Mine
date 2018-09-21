package com.alva.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public class IpWeatherResult implements Serializable {
    private String msg;
    private List<City> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<City> getResult() {
        return result;
    }

    public void setResult(List<City> result) {
        this.result = result;
    }
}
