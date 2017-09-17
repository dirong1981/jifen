package com.gljr.jifen.service;

import com.gljr.jifen.pojo.Message;

import java.util.List;

public interface MessageService {

    /**
     * 查询用户所有消息
     * @param uid 用户id
     * @return 消息列表
     */
    List<Message> selectAllMessageByUid(Integer uid);

    /**
     * 更新用户消息状态
     * @param message
     * @return
     */
    int updateAllMessageById(Message message);

}
