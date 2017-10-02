package com.gljr.jifen.common;

import java.util.List;

public class ValidCheck {

    /**
     * 验证列表是否为空,如果为空返回ture
     * @param list
     * @return
     */
    public static boolean validList(List list){
        boolean result = false;
        if(list == null || list.size() == 0){
            result = true;
        }
        return result;
    }

    /**
     * 验证一个pojo对象，如果为空返回ture
     * @param object
     * @return
     */
    public static boolean validPojo(Object object){
        boolean result = false;
        if(object == null){
            result = true;
        }
        return result;
    }

}
