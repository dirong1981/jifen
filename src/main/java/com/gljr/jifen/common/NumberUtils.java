package com.gljr.jifen.common;

import org.apache.commons.lang3.StringUtils;

/**
 * 数值工具类
 *
 * Created by Administrator on 2017/9/27 0027.
 */
public class NumberUtils {

    public static String random() {
        int num = (int) (Math.random() * 10000);
        if (num == 0) {
            random();
        }
        return StringUtils.rightPad(String.valueOf(num), 4, "0");
    }

    public static Integer getInt(String str) {
        if (StringUtils.isBlank(str)) {
            return 0;
        }
        int num;
        try {
            num = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            num = 0;
        }
        return num;
    }

    public static Double getDouble(String str) {
        if (StringUtils.isBlank(str)) {
            return 0D;
        }
        double num;
        try {
            num = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            num = 0;
        }
        return num;
    }

    public static void main(String[] args) {
        for(int i=0;i<100;i++){
            System.out.println(random());
        }
    }
}
