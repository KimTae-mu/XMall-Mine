package com.alva.manager.service.impl;

import com.alva.manager.mapper.TbExpressMapper;
import com.alva.manager.pojo.TbExpress;
import com.alva.manager.pojo.TbExpressExample;
import com.alva.manager.service.ExpressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public class ExpressServiceImpl implements ExpressService {
    @Autowired
    private TbExpressMapper tbExpressMapper;

    @Override
    public List<TbExpress> getExpressList() {

        TbExpressExample example = new TbExpressExample();
        example.setOrderByClause("sort_order asc");
        return tbExpressMapper.selectByExample(example);
    }

    @Override
    public int addExpress(TbExpress tbExpress) {

        tbExpress.setCreated(new Date());
        tbExpressMapper.insert(tbExpress);
        return 1;
    }

    @Override
    public int updateExpress(TbExpress tbExpress) {

        tbExpress.setUpdated(new Date());
        tbExpressMapper.updateByPrimaryKey(tbExpress);
        return 1;
    }

    @Override
    public int delExpress(int id) {
        tbExpressMapper.deleteByPrimaryKey(id);
        return 1;
    }
}
