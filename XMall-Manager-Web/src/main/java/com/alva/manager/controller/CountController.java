package com.alva.manager.controller;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alva.common.constant.CountConstant;
import com.alva.common.pojo.Result;
import com.alva.common.utils.ResultUtil;
import com.alva.manager.dto.ChartData;
import com.alva.manager.dto.OrderChartData;
import com.alva.manager.service.CountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
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
@RestController
@Api(description = "统计")
public class CountController {

    @Autowired
    private CountService countService;

    @RequestMapping(value = "/count/order", method = RequestMethod.GET)
    @ApiOperation(value = "通过panelId获得板块内容列表")
    public Result<Object> countOrder(@RequestParam int type
            , @RequestParam(required = false) String startTime
            , @RequestParam(required = false) String endTime
            , @RequestParam(required = false) int year) {
        ChartData data = new ChartData();
        Date startDate = null, endDate = null;
        if (type == CountConstant.CUSTOM_DATE) {
            startDate = DateUtil.beginOfDay(DateUtil.parse(startTime));
            endDate = DateUtil.endOfDay(DateUtil.parse(endTime));
            long between = DateUtil.between(startDate, endDate, DateUnit.DAY);
            if (between > 31) {
                return new ResultUtil<Object>().setErrorMsg("所选日期范围过长,最多不能超过31天");
            }
        }

        List<OrderChartData> orderCountData = countService.getOrderCountData(type, startDate, endDate, year);
        List<Object> xDatas = new ArrayList<>();
        List<Object> yDatas = new ArrayList<>();
        BigDecimal countAll = new BigDecimal("0");

        for (OrderChartData orderData : orderCountData) {
            if (type == CountConstant.CUSTOM_YEAR) {
                xDatas.add(DateUtil.format(orderData.getTime(), "yyyy-MM"));
            } else {
                xDatas.add(DateUtil.formatDate(orderData.getTime()));
            }
            yDatas.add(orderData.getMoney());
            countAll = countAll.add(orderData.getMoney());
        }
        data.setxDatas(xDatas);
        data.setyDatas(yDatas);
        data.setCountAll(countAll);
        return new ResultUtil<Object>().setData(data);
    }
}
