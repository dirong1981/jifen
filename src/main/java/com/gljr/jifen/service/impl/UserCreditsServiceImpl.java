package com.gljr.jifen.service.impl;

import com.gljr.jifen.constants.DBConstants;
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
    public UserCredits getUserCredits(Integer ownerId, DBConstants.OwnerType ownerType) {
        return this.userCreditsMapper.getUserCredits(ownerId, ownerType.getCode());
    }

    @Override
    public UserCredits getStoreCreditsByManagerId(Integer managerId) {
        return this.userCreditsMapper.getStoreCreditsByManagerId(managerId);
    }
}