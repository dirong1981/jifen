package com.gljr.jifen.service;

import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.pojo.UserCredits;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserCreditsService {

    UserCredits getUserCredits(Integer ownerId, DBConstants.OwnerType ownerType);

    UserCredits getStoreCreditsByManagerId(Integer managerId);

}
