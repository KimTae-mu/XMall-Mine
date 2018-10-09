package com.alva.content.service.impl;

import com.alva.common.pojo.DataTablesResult;
import com.alva.content.service.ContentService;
import com.alva.manager.mapper.TbItemMapper;
import com.alva.manager.mapper.TbPanelContentMapper;
import com.alva.manager.pojo.TbItem;
import com.alva.manager.pojo.TbPanelContent;
import com.alva.manager.pojo.TbPanelContentExample;
import org.apache.commons.fileupload.util.LimitedInputStream;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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
}
