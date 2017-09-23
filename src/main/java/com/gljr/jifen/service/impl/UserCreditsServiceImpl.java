package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.UserCreditsMapper;
import com.gljr.jifen.pojo.UserCredits;
import com.gljr.jifen.pojo.UserCreditsExample;
import com.gljr.jifen.service.UserCreditsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserCreditsServiceImpl implements UserCreditsService {


    @Autowired
    private UserCreditsMapper userCreditsMapper;

    @Override
    public int insertUserCredits(UserCredits userCredits) {
        List<UserCredits> list = selectUserCreditsByUid(userCredits.getOwnerId());
        if(list.size() == 0){
            userCreditsMapper.insert(userCredits);
        }else{
            userCredits.setId(list.get(0).getId());
            updateUserCreditsById(userCredits);
        }

        return 0;
    }

    @Override
    public List<UserCredits> selectUserCreditsByUid(int uid) {
        UserCreditsExample userCreditsExample = new UserCreditsExample();
        UserCreditsExample.Criteria criteria = userCreditsExample.or();
        criteria.andOwnerIdEqualTo(uid);
        return userCreditsMapper.selectByExample(userCreditsExample);
    }

    @Override
    public int updateUserCreditsById(UserCredits userCredits) throws RuntimeException  {
        return userCreditsMapper.updateByPrimaryKey(userCredits);
    }
}
