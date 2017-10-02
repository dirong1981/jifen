package com.gljr.jifen.common;

import com.gljr.jifen.constants.GlobalConstants;

public class CommonResult {

    /**
     * 正确返回
     * @param jsonResult
     * @return
     */
    public static JsonResult success(JsonResult jsonResult){
        jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
        jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
        return jsonResult;
    }

    /**
     *返回错误
     * @param jsonResult
     * @return
     */
    public static JsonResult failed(JsonResult jsonResult){
        jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
        jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        return jsonResult;
    }

    /**
     * 数据库错误
     * @param jsonResult
     * @return
     */
    public static JsonResult sqlFailed(JsonResult jsonResult){
        jsonResult.setMessage(GlobalConstants.SQL_FAILED);
        jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        return jsonResult;
    }

    /**
     * 没有找到相关数据
     * @param jsonResult
     * @return
     */
    public static JsonResult noObject(JsonResult jsonResult){
        jsonResult.setMessage(GlobalConstants.OBJECT_NOT_FOUND_MESSAGE);
        jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        return jsonResult;
    }

    /**
     * 不能为空
     * @param jsonResult
     * @return
     */
    public static JsonResult notNull(JsonResult jsonResult){
        jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        jsonResult.setMessage(GlobalConstants.NOTNULL);
        return jsonResult;
    }

    /**
     * 没有提交数据
     * @param jsonResult
     * @return
     */
    public static JsonResult noChoice(JsonResult jsonResult){
        jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        jsonResult.setMessage(GlobalConstants.NOTHING_SELECT);
        return jsonResult;
    }

    /**
     * 没有添加商户或商品
     * @param jsonResult
     * @return
     */
    public static JsonResult noSelected(JsonResult jsonResult){
        jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        jsonResult.setMessage(GlobalConstants.NO_SELECTED);
        return jsonResult;
    }

    /**
     * 图片上传错误
     * @param jsonResult
     * @return
     */
    public static JsonResult uploadFailed(JsonResult jsonResult){
        jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        jsonResult.setMessage(GlobalConstants.UPLOAD_PICTURE_FAILED_MESSAGE);
        return jsonResult;
    }

    /**
     * 上传的资源大于约定数量
     * @param jsonResult
     * @return
     */
    public static JsonResult greatThan5(JsonResult jsonResult){
        jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        jsonResult.setMessage(GlobalConstants.GREAT_THAN);
        return jsonResult;
    }

    /**
     * 该类型的操作不允许
     * @param jsonResult
     * @return
     */
    public static JsonResult notAllowedOperation(JsonResult jsonResult){
        jsonResult.setMessage(GlobalConstants.NOT_ALLOWED_OPERATION);
        jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        return jsonResult;
    }

    /**
     * 对象正在使用
     * @param jsonResult
     * @return
     */
    public static JsonResult objIsUsed(JsonResult jsonResult){
        jsonResult.setMessage(GlobalConstants.OBJ_IS_USED);
        jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        return jsonResult;
    }

    /**
     * 够力过来时没有带用户信息或者用户不存在
     * @param jsonResult
     * @return
     */
    public static JsonResult userNotExit(JsonResult jsonResult){
        jsonResult.setMessage(GlobalConstants.USER_NO_EXIST);
        jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        return jsonResult;
    }

    /**
     * 密码错误
     * @param jsonResult
     * @return
     */
    public static JsonResult passwordError(JsonResult jsonResult){
        jsonResult.setMessage(GlobalConstants.ADMIN_PASSWORD_ERROR);
        jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        return jsonResult;
    }

}
