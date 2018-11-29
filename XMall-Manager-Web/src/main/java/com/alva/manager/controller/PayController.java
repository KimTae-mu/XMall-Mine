package com.alva.manager.controller;

import com.alva.common.pojo.Result;
import com.alva.common.utils.ResultUtil;
import com.alva.manager.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
@RestController
@Api(description = "支付")
public class PayController {

    @Autowired
    OrderService orderService;

    private static final Logger log = LoggerFactory.getLogger(PayController.class);

    @RequestMapping(value = "/pay/pass", method = RequestMethod.GET)
    @ApiOperation(value = "支付审核通过")
    public Result<Object> payPass(String tokenName, String token, String id) {
        int result = orderService.passPay(tokenName, token, id);
        if (result == -1) {
            return new ResultUtil<Object>().setErrorMsg("无效的Token或链接");
        }
        if (result == 0) {
            return new ResultUtil<Object>().setErrorMsg("数据处理出错");
        }
        return new ResultUtil<Object>().setData("处理成功");
    }
}
