package com.alva.manager.controller;

import com.alva.common.pojo.DataTablesResult;
import com.alva.common.pojo.Result;
import com.alva.common.utils.ResultUtil;
import com.alva.manager.pojo.TbThanks;
import com.alva.manager.service.ThanksService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
@RestController
@Api(description = "捐赠管理")
public class ThanksController {

    private static final Logger log = LoggerFactory.getLogger(ThanksController.class);

    @Autowired
    ThanksService thanksService;

    @RequestMapping(value = "/thanks/list", method = RequestMethod.GET)
    @ApiOperation(value = "获取捐赠列表")
    public DataTablesResult getThanksList() {
        DataTablesResult result = thanksService.getThanksList();
        return result;
    }

    @RequestMapping(value = "/thanks/count", method = RequestMethod.GET)
    @ApiOperation(value = "获取捐赠列表总数")
    public Result<Object> getThanksCount() {
        Long result = thanksService.countThanks();
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/thanks/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加捐赠")
    public Result<Object> addThanks(@ModelAttribute TbThanks tbThanks) {

        thanksService.addThanks(tbThanks);

        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/thanks/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑捐赠")
    public Result<Object> updateThanks(@ModelAttribute TbThanks tbThanks) {

        thanksService.updateThanks(tbThanks);

        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/thanks/del/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除捐赠")
    public Result<Object> deleteThanks(@PathVariable int[] ids) {
        thanksService.deleteThanks(ids);

        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/thanks/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获得捐赠")
    public Result<Object> getThanks(@PathVariable int id) {
        TbThanks tbThanks = thanksService.getThank(id);
        return new ResultUtil<Object>().setData(tbThanks);
    }
}
