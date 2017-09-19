package com.gljr.jifen.controller;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.Message;
import com.gljr.jifen.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1")
public class MessageController {

    @Autowired
    private MessageService messageService;


    /**
     * 查询商城用户消息，请求头部包含uid
     * @param httpServletRequest
     * @param httpServletResponse
     * @return 消息列表
     */
    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getAllMessage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        JsonResult jsonResult = new JsonResult();

        try{
            String uid = httpServletRequest.getHeader("uid");
            if(uid == null || uid.equals("")){
                jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }else{
                List<Message> messages = messageService.selectAllMessageByUid(Integer.parseInt(uid));

                Map map = new HashMap();
                map.put("data", messages);

                jsonResult.setItem(map);

//                for(Message message : messages){
//                    message.setIsread(new Byte("1"));
//                    messageService.updateAllMessageById(message);
//                }

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            }
        }catch (Exception e){
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return  jsonResult;
    }


    /**
     * 更新用户消息状态，请求头部包含uid
     * @param httpServletResponse
     * @param httpServletRequest
     * @return 状态码
     */
    @RequestMapping(value = "messages", method = RequestMethod.PUT)
    @ResponseBody
    public JsonResult updateMessage(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        try{
            String uid = httpServletRequest.getHeader("uid");
            if(uid == null || uid.equals("")){
                jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
                jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
                return jsonResult;
            }else{
                List<Message> messages = messageService.selectAllMessageByUid(Integer.parseInt(uid));

                for(Message message : messages){
                    message.setReadStatus(new Byte("1"));
                    messageService.updateAllMessageById(message);
                }

                jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
                jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            }
        }catch (Exception e){
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED_MESSAGE);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
        }

        return  jsonResult;
    }

}
