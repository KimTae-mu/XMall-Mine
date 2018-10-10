package com.alva.content.service.impl;

import com.alva.common.exception.XmallException;
import com.alva.common.jedis.JedisClient;
import com.alva.common.pojo.DataTablesResult;
import com.alva.content.service.ContentService;
import com.alva.manager.mapper.TbItemMapper;
import com.alva.manager.mapper.TbPanelContentMapper;
import com.alva.manager.pojo.TbItem;
import com.alva.manager.pojo.TbPanelContent;
import com.alva.manager.pojo.TbPanelContentExample;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public class ContentServiceImpl implements ContentService {

    private static final Logger log = LoggerFactory.getLogger(ContentServiceImpl.class);

    @Autowired
    private TbPanelContentMapper tbPanelContentMapper;

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${PRODUCT_HOME}")
    private String PRODUCT_HOME;

    @Value("${PRODUCT_ITEM}")
    private String PRODUCT_ITEM;

    @Value("${RECOMEED_PANEL_ID}")
    private Integer RECOMEED_PANEL_ID;

    @Value("${THANK_PANEL_ID}")
    private Integer THANK_PANEL_ID;

    @Value("${RECOMEED_PANEL}")
    private String RECOMEED_PANEL;

    @Value("${THANK_PANEL}")
    private String THANK_PANEL;

    @Value("${ITEM_EXPIRE}")
    private int ITEM_EXPIRE;

    @Value("${HEADER_PANEL_ID}")
    private int HEADER_PANEL_ID;

    @Value("${HEADER_PANEL}")
    private String HEADER_PANEL;

    @Override
    public DataTablesResult getPanelContentListByPanelId(int panelId) {
        DataTablesResult result = new DataTablesResult();
        List<TbPanelContent> list = new ArrayList<>();

        TbPanelContentExample example = new TbPanelContentExample();
        TbPanelContentExample.Criteria criteria = example.createCriteria();
        criteria.andPanelIdEqualTo(panelId);

        list = tbPanelContentMapper.selectByExample(example);

        for (TbPanelContent tbPanelContent : list) {
            if (tbPanelContent.getProductId() != null) {
                TbItem tbItem = tbItemMapper.selectByPrimaryKey(tbPanelContent.getProductId());
                tbPanelContent.setProductName(tbItem.getTitle());
                tbPanelContent.setSalePrice(tbItem.getPrice());
                tbPanelContent.setSubTitle(tbItem.getSellPoint());
            }
        }
        result.setData(list);
        return result;
    }

    @Override
    public int addPanelContent(TbPanelContent tbPanelContent) {
        tbPanelContent.setCreated(new Date());
        tbPanelContent.setUpdated(new Date());
        if (tbPanelContentMapper.insert(tbPanelContent) != 1) {
            throw new XmallException("添加首页板块内容失败");
        }

        //同步导航栏缓存
        if (tbPanelContent.getPanelId() == HEADER_PANEL_ID) {
            updateNavListRedis();
        }
        //同步缓存
        deleteHomeRedis();
        return 1;
    }

    @Override
    public int updateContent(TbPanelContent tbPanelContent) {

        TbPanelContent old = getTbPanelContentById(tbPanelContent.getId());
        if (StringUtils.isBlank(tbPanelContent.getPicUrl())) {
            tbPanelContent.setPicUrl(old.getPicUrl());
        }
        if (StringUtils.isBlank(tbPanelContent.getPicUrl2())) {
            tbPanelContent.setPicUrl2(old.getPicUrl2());
        }
        if (StringUtils.isBlank(tbPanelContent.getPicUrl3())) {
            tbPanelContent.setPicUrl3(old.getPicUrl3());
        }
        tbPanelContent.setCreated(old.getCreated());
        tbPanelContent.setUpdated(new Date());

        if (tbPanelContentMapper.updateByPrimaryKey(tbPanelContent) != 1) {
            throw new XmallException("更新板块内容信息失败");
        }

        //同步导航栏缓存
        if (tbPanelContent.getPanelId() == HEADER_PANEL_ID) {
            updateNavListRedis();
        }
        //同步缓存
        deleteHomeRedis();
        return 1;
    }

    @Override
    public int deletePanelContent(int id) {
        if (tbPanelContentMapper.deleteByPrimaryKey(id) != 1) {
            throw new XmallException("删除板块失败");
        }
        //同步导航栏缓存
        if (id == HEADER_PANEL_ID) {
            updateNavListRedis();
        }
        //同步缓存
        deleteHomeRedis();
        return 1;
    }

    @Override
    public String getIndexRedis() {
        try {
            String json = jedisClient.get(PRODUCT_HOME);
            return json;
        } catch (Exception e) {
            log.error(e.toString());
        }
        return "";
    }

    @Override
    public int updateIndexRedis() {
        deleteHomeRedis();
        return 1;
    }

    @Override
    public String getRecommendRedis() {
        try {
            String json = jedisClient.get(RECOMEED_PANEL);
            return json;
        } catch (Exception e) {
            log.error(e.toString());
        }
        return "";
    }

    @Override
    public int updateRecommendRedis() {
        try {
            jedisClient.del(RECOMEED_PANEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public String getThankRedis() {
        try {
            String json = jedisClient.get(THANK_PANEL);
            return json;
        } catch (Exception e) {
            log.error(e.toString());
        }
        return "";
    }

    @Override
    public int updateThankRedis() {
        try {
            jedisClient.del(THANK_PANEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    private TbPanelContent getTbPanelContentById(Integer id) {
        TbPanelContent tbPanelContent = tbPanelContentMapper.selectByPrimaryKey(id);
        if (tbPanelContent == null) {
            throw new XmallException("通过ID获取板块内容失败");
        }
        return tbPanelContent;
    }

    private int deleteHomeRedis() {
        try {
            jedisClient.del(PRODUCT_HOME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    private int updateNavListRedis() {
        try {
            jedisClient.del(HEADER_PANEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}
