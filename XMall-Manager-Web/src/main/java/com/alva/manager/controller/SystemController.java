package com.alva.manager.controller;

import cn.hutool.core.util.ReUtil;
import com.alva.common.pojo.DataTablesResult;
import com.alva.common.pojo.Result;
import com.alva.common.utils.IPInfoUtil;
import com.alva.common.utils.ResultUtil;
import com.alva.manager.pojo.TbBase;
import com.alva.manager.pojo.TbOrderItem;
import com.alva.manager.pojo.TbShiroFilter;
import com.alva.manager.pojo.TbUser;
import com.alva.manager.service.SystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping(value = "/sys/shiro/list", method = RequestMethod.GET)
    @ApiOperation(value = "获取Shiro过滤链配置")
    public DataTablesResult getShiroList(@ModelAttribute TbUser tbUser) {
        DataTablesResult result = new DataTablesResult();
        List<TbShiroFilter> shiroFilters = systemService.getShiroFilter();
        result.setData(shiroFilters);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/sys/shiro/count", method = RequestMethod.GET)
    @ApiOperation(value = "统计Shiro过滤链数")
    public Result<Object> getUserCount() {
        Long result = systemService.countShiroFilter();
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/sys/shiro/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加Shiro过滤链")
    public Result<Object> addShiro(@ModelAttribute TbShiroFilter tbShiroFilter) {
        systemService.addShiroFilter(tbShiroFilter);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/sys/shiro/update", method = RequestMethod.POST)
    @ApiOperation(value = "更新Shiro过滤链")
    public Result<Object> updateShiro(@ModelAttribute TbShiroFilter tbShiroFilter) {
        systemService.updateShiro(tbShiroFilter);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/sys/shiro/del/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除SHiro过滤链")
    public Result<Object> delShiro(@PathVariable int[] ids) {
        for (int id : ids) {
            systemService.deleteShiroFilter(id);
        }
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/sys/base", method = RequestMethod.GET)
    @ApiOperation(value = "获取基本设置")
    public Result<TbBase> getBase() {
        TbBase tbBase = systemService.getBase();
        return new ResultUtil<TbBase>().setData(tbBase);
    }

    @RequestMapping(value = "/sys/base/update", method = RequestMethod.POST)
    @ApiOperation(value = "编辑基本设置")
    public Result<Object> updateBase(@ModelAttribute TbBase tbBase) {
        systemService.updateBase(tbBase);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/sys/weekHot", method = RequestMethod.GET)
    @ApiOperation(value = "获取本周热销商品数据")
    public Result<TbOrderItem> getWeekHot() {
        TbOrderItem tbOrderItem = systemService.getWeekHot();
        return new ResultUtil<TbOrderItem>().setData(tbOrderItem);
    }

    @RequestMapping(value = "/sys/weather", method = RequestMethod.GET)
    @ApiOperation(value = "获取天气信息")
    public Result<Object> getWeather(HttpServletRequest request) {
        String result = IPInfoUtil.getIpInfo(IPInfoUtil.getIpAddr(request));
        return new ResultUtil<Object>().setData(result);
    }

    @RequestMapping(value = "/sys/log", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取系统日志")
    public DataTablesResult getLog(int draw, int start, int length, @RequestParam("search[value]") String search,
                                   @RequestParam("order[0][column]") int orderCol, @RequestParam("order[0][dir]") String orderDir) {

    }
}
