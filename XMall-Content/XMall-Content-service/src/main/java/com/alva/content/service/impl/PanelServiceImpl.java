package com.alva.content.service.impl;

import com.alva.common.exception.XmallException;
import com.alva.common.jedis.JedisClient;
import com.alva.common.pojo.ZTreeNode;
import com.alva.content.service.PanelService;
import com.alva.manager.dto.DtoUtil;
import com.alva.manager.mapper.TbPanelMapper;
import com.alva.manager.pojo.TbPanel;
import com.alva.manager.pojo.TbPanelExample;
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
public class PanelServiceImpl implements PanelService {

    @Autowired
    private TbPanelMapper tbPanelMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("PRODUCT_HOME")
    private String PRODUCT_HOME;


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

    @Override
    public int addPanel(TbPanel tbPanel) {
        if (tbPanel.getType() == 0) {
            TbPanelExample example = new TbPanelExample();
            TbPanelExample.Criteria criteria = example.createCriteria();
            criteria.andTypeEqualTo(0);
            List<TbPanel> list = tbPanelMapper.selectByExample(example);
            if (list != null && list.size() > 0) {
                throw new XmallException("已有轮播图板块,轮播图仅能添加1个!");
            }
        }

        tbPanel.setCreated(new Date());
        tbPanel.setUpdated(new Date());

        if (tbPanelMapper.insert(tbPanel) != 1) {
            throw new XmallException("添加板块失败");
        }
        //同步缓存
        deleteHomeRedis();
        return 1;
    }

    @Override
    public int updatePanel(TbPanel tbPanel) {
        TbPanel old = getTbPanelById(tbPanel.getId());
        tbPanel.setUpdated(new Date());

        if (tbPanelMapper.updateByPrimaryKey(tbPanel) != 1) {
            throw new XmallException("更新板块失败");
        }
        //同步缓存
        deleteHomeRedis();
        return 1;
    }

    @Override
    public int deletePanel(int id) {
        if (tbPanelMapper.deleteByPrimaryKey(id) != 1) {
            throw new XmallException("删除内容分类失败");
        }
        //同步缓存
        deleteHomeRedis();
        return 1;
    }

    private TbPanel getTbPanelById(Integer id) {
        TbPanel tbPanel = tbPanelMapper.selectByPrimaryKey(id);
        if (tbPanel == null) {
            throw new XmallException("通过id获得板块失败");
        }
        return tbPanel;
    }

    /**
     * 同步首页缓存
     */
    private void deleteHomeRedis() {
        try {
            jedisClient.del(PRODUCT_HOME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
