package com.alva.content.service.impl;

import com.alva.common.exception.XmallException;
import com.alva.common.pojo.DataTablesResult;
import com.alva.content.service.ContentService;
import com.alva.manager.mapper.TbItemMapper;
import com.alva.manager.mapper.TbPanelContentMapper;
import com.alva.manager.pojo.TbItem;
import com.alva.manager.pojo.TbPanelContent;
import com.alva.manager.pojo.TbPanelContentExample;
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

    @Autowired
    private TbPanelContentMapper tbPanelContentMapper;

    @Autowired
    private TbItemMapper tbItemMapper;

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
}
