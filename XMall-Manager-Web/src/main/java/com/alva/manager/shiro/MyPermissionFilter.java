package com.alva.manager.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public class MyPermissionFilter extends AuthorizationFilter {

    private static final Logger log = LoggerFactory.getLogger(MyPermissionFilter.class);

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {

        Subject subject = this.getSubject(servletRequest, servletResponse);

        String[] perms = (String[]) ((String[]) o);
        boolean isPermitted = true;

        if (subject.getPrincipal() == null) {
            if (FilterUtil.isAjax(servletRequest)) {
                log.info("未登录或登录时间过长,是ajax!");
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("success", false);
                resultMap.put("message", "您还未登录或登录时间过长,请重新登录!");
                FilterUtil.out(servletResponse, resultMap);
            } else {
                log.info("未登录或登录时间过长,不是ajax!");
                this.saveRequestAndRedirectToLogin(servletRequest, servletResponse);
            }
        } else {
            if ((perms != null && perms.length > 0)) {
                if (perms.length == 1) {
                    if (!subject.isPermitted(perms[0])) {
                        isPermitted = false;
                    }
                } else if (!subject.isPermittedAll(perms)) {
                    isPermitted = false;
                }
            }
            if (!isPermitted) {
                if (FilterUtil.isAjax(servletRequest)) {
                    log.info("没有该权限,并且是Ajax请求");
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("success", false);
                    resultMap.put("message", "抱歉,您没有该权限!");
                } else {
                    return isPermitted;
                }
            }
        }
        return isPermitted;
    }
}
