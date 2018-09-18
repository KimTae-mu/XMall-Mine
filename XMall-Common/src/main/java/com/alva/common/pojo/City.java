package com.alva.common.pojo;

import java.io.Serializable;

/**
 * <一句话描述>,
 * 城市Pojo
 *
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public class City implements Serializable {
    private String city;
    private String distrct;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrct() {
        return distrct;
    }

    public void setDistrct(String distrct) {
        this.distrct = distrct;
    }
}
