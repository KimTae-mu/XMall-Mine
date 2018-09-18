package com.alva.common.utils;

import java.util.Random;

/**
 * <一句话描述>,
 * <详细介绍>,
 *
 * @author 穆国超
 * @since 设计wiki | 需求wiki
 */
public class IDUtil {

    /**
     * 随机Id生成
     *
     * @return
     */
    public static long getRandomId(){
        long millis = System.currentTimeMillis();
        //加上两位随机数
        Random random = new Random();
        int end2 = random.nextInt(99);

        //如果不足两位前面补0
        String str = millis + String.format("%02d",end2);
        long id = new Long(str);
        return id;
    }
}
