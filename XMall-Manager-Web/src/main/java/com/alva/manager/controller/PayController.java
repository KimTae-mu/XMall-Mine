package com.alva.manager.controller;

import com.alva.common.pojo.Result;
import com.alva.common.utils.ResultUtil;
import com.alva.manager.pojo.TbThanks;
import com.alva.manager.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @RequestMapping(value = "/pay/back", method = RequestMethod.GET)
    @ApiOperation(value = "支付审核驳回")
    public Result<Object> backPay(String tokenName, String token, String id) {
        int result = orderService.backPay(tokenName, token, id);

        if (result == -1) {
            return new ResultUtil<Object>().setErrorMsg("无效的Token或链接");
        }
        if (result == 0) {
            return new ResultUtil<Object>().setErrorMsg("数据处理出错");
        }
        return new ResultUtil<Object>().setData("处理成功");
    }

    @RequestMapping(value = "/pay/passNotShow", method = RequestMethod.GET)
    @ApiOperation(value = "支付审核通过但不显示")
    public Result<Object> payNotShow(String tokenName, String name, String id) {

        int result = orderService.notShowPay(tokenName, tokenName, id);
        if (result == -1) {
            return new ResultUtil<Object>().setErrorMsg("无效的Token或链接");
        }
        if (result == 0) {
            return new ResultUtil<Object>().setErrorMsg("数据处理出错");
        }
        return new ResultUtil<Object>().setData("处理成功");
    }

    @RequestMapping(value = "/pay/edit", method = RequestMethod.GET)
    @ApiOperation(value = "支付审核编辑")
    public Result<Object> payEdit(String tokenName, String token, @ModelAttribute TbThanks tbThanks) {

        int result = orderService.editPay(tokenName, token, tbThanks);
        if (result == -1) {
            return new ResultUtil<Object>().setErrorMsg("无效的Token或链接");
        }
        if (result == 0) {
            return new ResultUtil<Object>().setErrorMsg("数据处理出错");
        }
        return new ResultUtil<Object>().setData("处理成功");
    }

    @RequestMapping(value = "/pay/delNotNotify", method = RequestMethod.GET)
    @ApiOperation(value = "支付删除不发送邮件通知")
    public Result<Object> delNotNotify(String tokenName, String token, String id) {

        int result = orderService.payDelNotNotify(tokenName, token, id);
        if (result == -1) {
            return new ResultUtil<Object>().setErrorMsg("无效的Token或链接");
        }
        if (result == 0) {
            return new ResultUtil<Object>().setErrorMsg("数据处理出错");
        }
        return new ResultUtil<Object>().setData("处理成功");
    }

    @RequestMapping(value = "/pay/del", method = RequestMethod.GET)
    @ApiOperation(value = "支付删除")
    public Result<Object> payDel(String tokenName, String token, String id) {

        int result = orderService.payDel(tokenName, token, id);
        if (result == -1) {
            return new ResultUtil<Object>().setErrorMsg("无效的Token或链接");
        }
        if (result == 0) {
            return new ResultUtil<Object>().setErrorMsg("数据处理出错");
        }
        return new ResultUtil<Object>().setData("处理成功");
    }
}
