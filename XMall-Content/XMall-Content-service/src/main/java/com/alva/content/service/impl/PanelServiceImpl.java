package com.alva.content.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alva.common.pojo.ZTreeNode;
import com.alva.content.service.PanelService;
import com.alva.manager.dto.DtoUtil;
import com.alva.manager.mapper.TbPanelMapper;
import com.alva.manager.pojo.TbPanel;
import com.alva.manager.pojo.TbPanelExample;
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
public class PanelServiceImpl implements PanelService {

    @Autowired
    private TbPanelMapper tbPanelMapper;


    @Override
    public List<ZTreeNode> getPanelList(int position, boolean showAll) {

        TbPanelExample tbPanelExample = new TbPanelExample();
        TbPanelExample.Criteria criteria = tbPanelExample.createCriteria();

        if (position == 0 && !showAll) {
            //除去非轮播
            criteria.andTypeNotEqualTo(0);
        } else if (position == -1) {
            position = 0;
            criteria.andTypeEqualTo(0);
        }

        //首页板块
        criteria.andPositionEqualTo(position);
        tbPanelExample.setOrderByClause("sort_order");
        List<TbPanel> tbPanels = tbPanelMapper.selectByExample(tbPanelExample);

        List<ZTreeNode> result = new ArrayList<>();
        for (TbPanel temp : tbPanels) {
            ZTreeNode zTreeNode = DtoUtil.TbPanel2ZTreeNode(temp);
            result.add(zTreeNode);
        }

        return result;
    }
}
