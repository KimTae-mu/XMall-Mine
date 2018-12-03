package com.alva.manager.controller;

import com.alva.common.pojo.Result;
import com.alva.common.pojo.ZTreeNode;
import com.alva.common.utils.ResultUtil;
import com.alva.content.service.PanelService;
import com.alva.manager.pojo.TbPanel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
@RestController
@Api(description = "板块列表")
public class PanelController {

    private final static Logger log = LoggerFactory.getLogger(PanelController.class);

    @Autowired
    private PanelService panelService;

    @RequestMapping(value = "/panel/index/list", method = RequestMethod.GET)
    @ApiOperation(value = "获得首页板块列表不含轮播")
    public List<ZTreeNode> getIndexPanel() {
        List<ZTreeNode> result = panelService.getPanelList(0, false);

        return result;
    }

    @RequestMapping(value = "/panel/indexAll/list", method = RequestMethod.GET)
    @ApiOperation(value = "获得首页板块列表含轮播")
    public List<ZTreeNode> getAllIndexPanel() {

        List<ZTreeNode> list = panelService.getPanelList(0, true);
        return list;
    }

    @RequestMapping(value = "/panel/indexBanner/list", method = RequestMethod.GET)
    @ApiOperation(value = "获得首页轮播板块列表")
    public List<ZTreeNode> getIndexBannerPanel() {

        List<ZTreeNode> list = panelService.getPanelList(-1, true);
        return list;
    }

    @RequestMapping(value = "/panel/other/list", method = RequestMethod.GET)
    @ApiOperation(value = "获得其它添加板块")
    public List<ZTreeNode> getRecommendPanel() {

        List<ZTreeNode> list = panelService.getPanelList(1, false);
        list.addAll(panelService.getPanelList(2, false));
        return list;
    }

    @RequestMapping(value = "/panel/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加板块")
    public Result<Object> addContentCategory(@ModelAttribute TbPanel tbPanel) {
        panelService.addPanel(tbPanel);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/panel/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑内容分类")
    public Result<Object> updateContentCategory(@ModelAttribute TbPanel tbPanel) {

        panelService.updatePanel(tbPanel);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/panel/del/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除内容分类")
    public Result<Object> deleteContentCategory(@PathVariable int[] ids) {

        for (int id : ids) {
            panelService.deletePanel(id);
        }
        return new ResultUtil<Object>().setData(null);
    }
}
