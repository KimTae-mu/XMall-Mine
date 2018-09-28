package com.alva.manager.controller;

import cn.hutool.core.util.ReUtil;
import com.alva.common.pojo.DataTablesResult;
import com.alva.common.pojo.Result;
import com.alva.common.utils.ResultUtil;
import com.alva.manager.pojo.TbShiroFilter;
import com.alva.manager.pojo.TbUser;
import com.alva.manager.service.SystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
@RestController
@Api(description = "系统配置管理")
public class SystemController {

    @Autowired
    private SystemService systemService;

    @RequestMapping(value = "/sys/shiro/list",method = RequestMethod.GET)
    @ApiOperation(value = "获取Shiro过滤链配置")
    public DataTablesResult getShiroList(@ModelAttribute TbUser tbUser){
        DataTablesResult result = new DataTablesResult();
        List<TbShiroFilter> shiroFilters = systemService.getShiroFilter();
        result.setData(shiroFilters);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/sys/shiro/count",method = RequestMethod.GET)
    @ApiOperation(value = "统计Shiro过滤链数")
    public Result<Object> getUserCount(){
        Long result = systemService.countShiroFilter();
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/sys/shiro/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加Shiro过滤链")
    public Result<Object> addShiro(@ModelAttribute TbShiroFilter tbShiroFilter){
        systemService.addShiroFilter(tbShiroFilter);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/sys/shiro/update",method = RequestMethod.POST)
    @ApiOperation( value = "更新Shiro过滤链")
    public Result<Object> updateShiro(@ModelAttribute TbShiroFilter tbShiroFilter){
        systemService.updateShiro(tbShiroFilter);
        return new ResultUtil<Object>().setData(null);
    }
}
