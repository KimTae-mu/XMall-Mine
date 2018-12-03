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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public int addThanks(TbThanks tbThanks) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(tbThanks.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tbThanks.setDate(date);
        if (tbThanksMapper.insert(tbThanks) != 1) {
            throw new XmallException("添加捐赠失败");
        }
        return 1;
    }

    @Override
    public int updateThanks(TbThanks tbThanks) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = null;
        try {
            date = simpleDateFormat.parse(tbThanks.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tbThanks.setDate(date);
        if (tbThanksMapper.updateByPrimaryKey(tbThanks) != 1) {
            throw new XmallException("编辑捐赠失败");
        }
        return 1;
    }

    @Override
    public int deleteThanks(int[] ids) {

        TbThanksExample example = new TbThanksExample();
        TbThanksExample.Criteria criteria = example.createCriteria();

        criteria.andIdIn(Arrays.stream(ids).boxed().collect(Collectors.toList()));
        if (tbThanksMapper.deleteByExample(example) != 1) {
            throw new XmallException("删除捐赠失败");
        }
//        int result = tbThanksMapper.deleteThanks(ids);
        return 0;
    }

    @Override
    public TbThanks getThankById(int id) {
        TbThanks tbThanks = tbThanksMapper.selectByPrimaryKey(id);
        if (tbThanks == null) {
            throw new XmallException("获取捐赠数据失败");
        }
        return tbThanks;
    }
}
