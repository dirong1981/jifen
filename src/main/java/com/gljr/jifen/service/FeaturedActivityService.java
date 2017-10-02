package com.gljr.jifen.service;

import com.gljr.jifen.pojo.FeaturedActivity;

import java.util.List;

public interface FeaturedActivityService {

    //查询


    /**
     * 查询所有精选内容，状态0,1,2
     * @return
     */
    List<FeaturedActivity> selectFeaturedActivitys();

    /**
     * 按照状态查询精选页
     * @param status
     * @return
     */
    List<FeaturedActivity> selectFeaturedActivitysByStatus(Integer status);

    /**
     * 按照id查询精选页
     * @param id
     * @return
     */
    FeaturedActivity selectFeaturedActivityById(Integer id);



    //修改

    /**
     * 修改一个精选页
     * @param featuredActivity
     * @return
     */
    int updateFeaturedActivityById(FeaturedActivity featuredActivity);


    //删除


    /**
     * 删除一个精选页
     * @param featuredActivity
     * @return
     */
    int deleteFeaturedActivitysById(FeaturedActivity featuredActivity);


    //添加

    /**
     * 添加一个精选页
     * @param featuredActivity
     * @return
     */
    int insertFeaturedActivity(FeaturedActivity featuredActivity);
}
