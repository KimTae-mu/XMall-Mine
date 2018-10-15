package com.alva.manager.controller;

import com.alva.manager.service.ExpressService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
@RestController
@Api(description = "快递")
public class ExpressController {

    @Autowired
    private ExpressService expressService;
}
