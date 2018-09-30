package com.alva.manager.controller;

import com.alva.common.annotation.SystemControllerLog;
import com.alva.common.pojo.Result;
import com.alva.common.utils.GeetestLib;
import com.alva.common.utils.ResultUtil;
import com.alva.manager.pojo.TbRole;
import com.alva.manager.pojo.TbUser;
import com.alva.manager.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
@RestController
@Api(description = "管理员管理")
public class UserController {

    static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ApiOperation(value = "用户登录")
    @SystemControllerLog(description = "登录系统")
    public Result<Object> login(String username, String password,
                                String challenge, String validate, String seccode,
                                HttpServletRequest request) {
        //极验验证
        GeetestLib geetestLib = new GeetestLib(GeetestLib.id, GeetestLib.key, GeetestLib.newfailback);

        //获取session中的极验的key值
        int gt_Server_Status_Code = (Integer) request.getSession().getAttribute(geetestLib.gtServerStatusSessionKey);

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<>();

        int gtResult = 0;

        if (gt_Server_Status_Code == 1) {
            //gt_server正常,进行二次验证
            gtResult = geetestLib.enhencedValidateRequest(challenge, validate, seccode, param);
            log.info(username + "极验二次验证结果: " + gtResult);
        }else {
            //gt_server非正常的情况下,进行failBack模式验证
            log.info("failback:use your own server captcha validate");
            geetestLib.failbackValidateRequest(challenge, validate, seccode);
            log.info(username + "极验failback验证结果: " + gtResult);
        }

        if(gtResult == 1){
            //验证成功
            Subject subject = SecurityUtils.getSubject();
            //MD5加密
            String md5Pass = DigestUtils.md5DigestAsHex(password.getBytes());
            UsernamePasswordToken token = new UsernamePasswordToken(username,md5Pass);
            try {
                subject.login(token);
                return new ResultUtil<Object>().setData(null);
            }catch (Exception e){
                return new ResultUtil<Object>().setErrorMsg("用户名或密码错误");
            }
        }else {
            //验证失败
            return new ResultUtil<Object>().setErrorMsg("验证失败");
        }
    }

    @RequestMapping(value = "/user/logout",method = RequestMethod.GET)
    @ApiOperation(value = "用户登出")
    public Result<Object> logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/user/getAllRoles",method = RequestMethod.GET)
    @ApiOperation(value = "获取所有角色")
    public Result<List<TbRole>> getAllRoles(){
       List<TbRole> list = userService.getAllRoles();
       return new ResultUtil<List<TbRole>>().setData(list);
    }

    @RequestMapping(value = "/user/addUser",method = RequestMethod.POST)
    @ApiOperation(value = "注册用户")
    public Result<Object> addUser(@ModelAttribute TbUser tbUser){
        userService.addUser(tbUser);
        return new ResultUtil<Object>().setData(null);
    }


    @RequestMapping(value = "/user/userInfo", method = RequestMethod.GET)
    @ApiOperation(value = "获取登录用户信息")
    public Result<TbUser> getUserInfo() {
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        TbUser user = userService.getUserByUsername(username);
        user.setPassword(null);
        return new ResultUtil<TbUser>().setData(user);
    }

    @RequestMapping(value = "/user/updateUser",method = RequestMethod.POST)
    @ApiOperation(value = "更新用户信息")
    public Result<Object> updateUser(@ModelAttribute TbUser tbUser){
        userService.updateUser(tbUser);
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/user/addRole",method = RequestMethod.POST)
    @ApiOperation(value = "添加角色")
    public Result<Object> addRole(@ModelAttribute TbRole role){
        userService.addRole(role);
        return new ResultUtil<Object>().setData(null);
    }


}
