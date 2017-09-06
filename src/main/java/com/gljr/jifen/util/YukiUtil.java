package com.gljr.jifen.util;


import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.UUID;

public class YukiUtil {

    public static String[] roots= {"所有权限","类别管理","商品管理","商户管理","页面模块","订单管理","用户管理"};


    /**
     * 数值转换
     * @param dictionary 字典,所有的中文 如:public static String[] roots= {"所有权限","类别管理","商品管理","商户管理","页面模块","订单管理","用户管理"};
     * @param number 要转换的值
     * @param basis
     * @return
     */
    public static String getStr(String[] dictionary,String number,String basis)
    {
        String[] datas = number.split(",");
        String result = "";
        for (String c:datas)
        {
            result += dictionary[Integer.parseInt(c)]+basis;
        }

        result = result.substring(0,result.length()-1);

        return result;
    }

    /**
     * 根据给定的符号合成字符串
     * @param data
     * @param fgf
     * @return
     */
    public static String getStr(String[] data,String fgf)
    {
        String result = "";
        for (String c:data)
        {
            result += c+fgf;
        }

        result = result.substring(0,result.length()-1);

        return result;
    }



    /**
     * 获得一个UUID
     * @return String UUID
     */
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString();
        //去掉“-”符号
        return uuid.replaceAll("-", "");
    }





    /**
     * 生成含有随机盐的密码
     */
//    public static String generate(String password) {
//        Random r = new Random();
//        StringBuilder sb = new StringBuilder(16);
//        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
//        int len = sb.length();
//        if (len < 16) {
//            for (int i = 0; i < 32 - len; i++) {
//                sb.append("0");
//            }
//        }
//        String salt = sb.toString();
//        password = md5Hex(password + salt);
//        char[] cs = new char[48];
//        for (int i = 0; i < 48; i += 3) {
//            cs[i] = password.charAt(i / 3 * 2);
//            char c = salt.charAt(i / 3);
//            cs[i + 1] = c;
//            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
//        }
//
//        return new String(cs);
//    }

    /**
     * 校验密码是否正确
     */
//    public static boolean verify(String password, String md5) {
//        char[] cs1 = new char[32];
//        char[] cs2 = new char[16];
//        for (int i = 0; i < 48; i += 3) {
//            cs1[i / 3 * 2] = md5.charAt(i);
//            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
//            cs2[i / 3] = md5.charAt(i + 1);
//        }
//        String salt = new String(cs2);
//        return md5Hex(password + salt).equals(new String(cs1));
//    }

    /**
     * 获取十六进制字符串形式的MD5摘要
     */
//    public static String md5Hex(String src) {
//        try {
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            byte[] bs = md5.digest(src.getBytes());
//            return new String(new Hex().encode(bs));
//        } catch (Exception e) {
//            return null;
//        }
//    }


    /**
     * 返回带格式的时间 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getYMDHMS(Object time)
    {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String times = date.format(time);
        return times;
    }

    /**
     * 返回固定的长度的随机数
     * @param strLength
     * @return
     */
    public static String getID(int strLength) {

        Random rm = new Random();

        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);

        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);

        // 返回固定的长度的随机数
        return fixLenthString.substring(2, strLength + 1);
    }
}
