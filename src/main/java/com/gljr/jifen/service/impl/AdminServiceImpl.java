package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.AdminMapper;
import com.gljr.jifen.pojo.Admin;
import com.gljr.jifen.pojo.AdminExample;
import com.gljr.jifen.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;


    @Override
    public List<Admin> login(Admin admin) {

        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        //System.out.printf(admin.getaName());
        criteria.andANameEqualTo(admin.getaName());
        //criteria.andAPasswordEqualTo(admin.getaPassword());

        List<Admin> list = adminMapper.selectByExample(adminExample);
        //System.out.printf(list.size()+"");

        return list;
    }
}
