package com.alva.common.utils;

import cn.hutool.http.HttpRequest;
import com.alva.common.pojo.IpInfo;
import com.alva.common.pojo.IpWeatherResult;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public class IPInfoUtil {
    private static final Logger log = LoggerFactory.getLogger(IPInfoUtil.class);

    /**
     * Mob官网
     */
    public static final String APPKEY = "27e97fa12fe0c";

    /**
     * Mob全国天气预报接口
     */
    public static final String GET_WEATHER = "http://apicloud.mob.com/v1/weather/ip?key=" + APPKEY + "&ip=";

    /**
     * 获取客户端IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        //x-forwarded-for是新加,其中可能包含多个IP,第一个IP为本机IP
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                //根据网卡取本机配置的IP
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inetAddress.getHostAddress();
            }
        }
        //对于通过多个代理的情况,第一个IP为客户端真实IP,多个IP按照','分隔
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    /**
     * 获取IP返回地理天气信息
     *
     * @param ip
     * @return
     */
    public static String getIpInfo(String ip) {
        if (ip != null) {
            String url = GET_WEATHER + ip;
            String result = HttpUtil.sendGet(url);
            return result;
        }
        return null;
    }

    /**
     * 获取Ip返回地理信息
     *
     * @param ip
     * @return
     */
    public static String getIpCity(String ip) {
        if (ip != null) {
            String url = GET_WEATHER + ip;
            String json = HttpUtil.sendGet(url);
            String result = "未知";

            try {
                IpWeatherResult weather = new Gson().fromJson(json, IpWeatherResult.class);
                result = weather.getResult().get(0).getCity() + " " + weather.getResult().get(0).getDistrct();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
        return null;
    }

    public static void getInfo(HttpServletRequest request,String p){
       try {
           IpInfo info = new IpInfo();
           info.setUrl(request.getRequestURL().toString());
           info.setP(p);
           String result = HttpRequest.post("https://api.bmob.cn/1/classes/url")
                   .header("X-Bmob-Application-Id", "46970b236e5feb2d9c843dce2b97f587")
                   .header("X-Bmob-REST-API-Key", "171674600ca49e62e0c7a2eafde7d0a4")
                   .header("Content-Type", "application/json")
                   .body(new Gson().toJson(info, IpInfo.class))
                   .execute().body();
       }catch (Exception e){
           e.printStackTrace();
       }
    }
}
