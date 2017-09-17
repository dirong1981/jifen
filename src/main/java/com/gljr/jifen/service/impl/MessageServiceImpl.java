package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.MessageMapper;
import com.gljr.jifen.pojo.Message;
import com.gljr.jifen.pojo.MessageExample;
import com.gljr.jifen.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;


    @Override
    public List<Message> selectAllMessageByUid(Integer uid) {
        MessageExample messageExample = new MessageExample();
        MessageExample.Criteria criteria = messageExample.or();
        criteria.andUidEqualTo(uid);
        return messageMapper.selectByExample(messageExample);
    }

    @Override
    public int updateAllMessageById(Message message) {
        return messageMapper.updateByPrimaryKey(message);
    }

}
