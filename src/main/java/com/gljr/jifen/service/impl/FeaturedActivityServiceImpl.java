package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.FeaturedActivityMapper;
import com.gljr.jifen.pojo.FeaturedActivity;
import com.gljr.jifen.pojo.FeaturedActivityExample;
import com.gljr.jifen.service.FeaturedActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FeaturedActivityServiceImpl implements FeaturedActivityService {

    @Autowired
    private FeaturedActivityMapper featuredActivityMapper;


    @Override
    public List<FeaturedActivity> selectFeaturedActivitys() {
        FeaturedActivityExample featuredActivityExample = new FeaturedActivityExample();
        FeaturedActivityExample.Criteria criteria = featuredActivityExample.or();
        criteria.andStatusLessThanOrEqualTo(2);
        featuredActivityExample.setOrderByClause("id desc");
        return featuredActivityMapper.selectByExample(featuredActivityExample);
    }

    @Override
    public List<FeaturedActivity> selectFeaturedActivitysByStatus(Integer status) {
        FeaturedActivityExample featuredActivityExample = new FeaturedActivityExample();
        FeaturedActivityExample.Criteria criteria = featuredActivityExample.or();
        criteria.andStatusEqualTo(status);
        featuredActivityExample.setOrderByClause("sort desc");
        return featuredActivityMapper.selectByExample(featuredActivityExample);
    }

    @Override
    public FeaturedActivity selectFeaturedActivityById(Integer id) {
        return featuredActivityMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateFeaturedActivityById(FeaturedActivity featuredActivity) {
        return featuredActivityMapper.updateByPrimaryKey(featuredActivity);
    }

    @Override
    public int deleteFeaturedActivitysById(FeaturedActivity featuredActivity) {
        return featuredActivityMapper.updateByPrimaryKey(featuredActivity);
    }

    @Override
    public int insertFeaturedActivity(FeaturedActivity featuredActivity) {
        return featuredActivityMapper.insert(featuredActivity);
    }
}
