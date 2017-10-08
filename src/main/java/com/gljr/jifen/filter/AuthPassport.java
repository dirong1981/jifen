package com.gljr.jifen.filter;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthPassport {

    /**
     * 是否需要写权限
     * @return
     */
    boolean validate() default true;

    /**
     * 权限类型
     * @return
     */
    String permission_code() default "";
}
