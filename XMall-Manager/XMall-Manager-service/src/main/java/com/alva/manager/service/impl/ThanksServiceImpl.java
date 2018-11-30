package com.alva.manager.service.impl;

import com.alva.common.exception.XmallException;
import com.alva.common.pojo.DataTablesResult;
import com.alva.manager.mapper.TbThanksMapper;
import com.alva.manager.pojo.TbThanks;
import com.alva.manager.pojo.TbThanksExample;
import com.alva.manager.service.ThanksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public class ThanksServiceImpl implements ThanksService {

    @Autowired
    private TbThanksMapper tbThanksMapper;

    @Override
    public DataTablesResult getThanksList() {
        DataTablesResult result = new DataTablesResult();
        TbThanksExample example = new TbThanksExample();

        List<TbThanks> tbThanks = tbThanksMapper.selectByExample(example);

        if (tbThanks == null) {
            throw new XmallException("获取捐赠列表失败");
        }
        result.setData(tbThanks);
        result.setSuccess(true);
        return result;
    }

    @Override
    public Long countThanks() {

        TbThanksExample example = new TbThanksExample();

        Long reslut = tbThanksMapper.countByExample(example);

        if (reslut == null) {
            throw new XmallException("统计捐赠数目失败");
        }
        return reslut;
    }
}