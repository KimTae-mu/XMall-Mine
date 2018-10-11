package com.alva.manager.service.impl;

import com.alva.common.pojo.ZTreeNode;
import com.alva.manager.dto.DtoUtil;
import com.alva.manager.mapper.TbItemCatMapper;
import com.alva.manager.pojo.TbItemCat;
import com.alva.manager.pojo.TbItemCatExample;
import com.alva.manager.service.ItemCatService;
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
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public TbItemCat getItemCatById(Long id) {
        return null;
    }

    @Override
    public List<ZTreeNode> getItemCatList(int parentId) {
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        //排序
        example.setOrderByClause("sort_order");
        //条件查询
        criteria.andParentIdEqualTo(new Long(parentId));

        List<TbItemCat> tbItemCats = tbItemCatMapper.selectByExample(example);

        //转换成ZtreeNode
        List<ZTreeNode> resultList = new ArrayList<>();
        for (TbItemCat tbItemCat : tbItemCats) {
            ZTreeNode node = DtoUtil.TbItemCat2ZTreeNode(tbItemCat);
            resultList.add(node);
        }
        return resultList;
    }

    @Override
    public int addItemCat(TbItemCat tbItemCat) {
        return 0;
    }

    @Override
    public int updateItemCat(TbItemCat tbItemCat) {
        return 0;
    }

    @Override
    public void deleteItemCat(Long id) {

    }

    @Override
    public void deleteZTree(Long id) {

    }
}
