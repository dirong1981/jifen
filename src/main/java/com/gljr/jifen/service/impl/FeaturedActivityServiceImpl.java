package com.gljr.jifen.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.dao.AdminMapper;
import com.gljr.jifen.dao.FeaturedActivityMapper;
import com.gljr.jifen.dao.ModuleAggregationMapper;
import com.gljr.jifen.pojo.*;
import com.gljr.jifen.service.FeaturedActivityService;
import com.gljr.jifen.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class FeaturedActivityServiceImpl implements FeaturedActivityService {

    @Autowired
    private FeaturedActivityMapper featuredActivityMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private StorageService storageService;

    @Autowired
    private ModuleAggregationMapper moduleAggregationMapper;


    @Override
    public JsonResult selectFeaturedActivitys(JsonResult jsonResult) {
        try {
            FeaturedActivityExample featuredActivityExample = new FeaturedActivityExample();
            FeaturedActivityExample.Criteria criteria = featuredActivityExample.or();
            criteria.andStatusNotEqualTo(DBConstants.FeaturedActivityStatus.DELETED.getCode());
            featuredActivityExample.setOrderByClause("sort desc");
            List<FeaturedActivity> featuredActivities = featuredActivityMapper.selectByExample(featuredActivityExample);

            //找到
            if(!ValidCheck.validList(featuredActivities)){

                for(FeaturedActivity featuredActivitie : featuredActivities){

                    Admin admin = adminMapper.selectByPrimaryKey(featuredActivitie.getManagerId());

                    //没有找到
                    if(ValidCheck.validPojo(admin)){
                        featuredActivitie.setAdmin("已删除");
                    }else{
                        featuredActivitie.setAdmin(admin.getUsername());
                    }
                }
            }

            Map map = new HashMap();
            map.put("data", featuredActivities);

            CommonResult.success(jsonResult);
            jsonResult.setItem(map);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult selectFeaturedActivitysEnabled(Integer page, Integer per_page, JsonResult jsonResult) {

        try {
            FeaturedActivityExample featuredActivityExample = new FeaturedActivityExample();
            FeaturedActivityExample.Criteria criteria = featuredActivityExample.or();
            criteria.andStatusEqualTo(DBConstants.FeaturedActivityStatus.ACTIVED.getCode());
            featuredActivityExample.setOrderByClause("sort desc");


            PageHelper.startPage(page,per_page);
            List<FeaturedActivity> featuredActivities = featuredActivityMapper.selectByExample(featuredActivityExample);

            for(FeaturedActivity featuredActivity : featuredActivities){
                featuredActivity.setPictureKey(featuredActivity.getPictureKey() + "!featured");

                if(featuredActivity.getLinkType() == 1){
                    String[] links = featuredActivity.getLinkUrl().split("/");
                    ModuleAggregationExample moduleAggregationExample = new ModuleAggregationExample();
                    ModuleAggregationExample.Criteria criteria1 = moduleAggregationExample.or();
                    criteria1.andLinkCodeEqualTo(links[2]);
                    List<ModuleAggregation> moduleAggregations = moduleAggregationMapper.selectByExample(moduleAggregationExample);

                    if(!ValidCheck.validList(moduleAggregations)){
                        if(moduleAggregations.get(0).getType() == 2){
                            featuredActivity.setLinkType(3);
                        }
                    }
                }
            }

            PageInfo pageInfo = new PageInfo(featuredActivities);

            Map map = new HashMap();
            map.put("data", featuredActivities);

            map.put("pages", pageInfo.getPages());

            map.put("total", pageInfo.getTotal());
            //当前页
            map.put("pageNum", pageInfo.getPageNum());

            jsonResult.setItem(map);
            CommonResult.success(jsonResult);

        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult stopFeaturedActivity(Integer featuredId, JsonResult jsonResult) {
        try{
            FeaturedActivity featuredActivity =featuredActivityMapper.selectByPrimaryKey(featuredId);
            //没有找到
            if(ValidCheck.validPojo(featuredActivity)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            featuredActivity.setStatus(DBConstants.FeaturedActivityStatus.INACTIVE.getCode());
            featuredActivityMapper.updateByPrimaryKey(featuredActivity);

            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult startFeaturedActivity(Integer featuredId, JsonResult jsonResult) {
        try{
            FeaturedActivity featuredActivity = featuredActivityMapper.selectByPrimaryKey(featuredId);
            if(ValidCheck.validPojo(featuredActivity)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }
            featuredActivity.setStatus(DBConstants.FeaturedActivityStatus.ACTIVED.getCode());
            featuredActivityMapper.updateByPrimaryKey(featuredActivity);

            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult deleteFeaturedActivitysById(Integer featuredId, JsonResult jsonResult) {
        try {
            FeaturedActivity featuredActivity = featuredActivityMapper.selectByPrimaryKey(featuredId);
            if(ValidCheck.validPojo(featuredActivity)){
                CommonResult.noObject(jsonResult);
                return jsonResult;
            }

            if(featuredActivity.getStatus() == DBConstants.FeaturedActivityStatus.ACTIVED.getCode()){
                CommonResult.objIsUsed(jsonResult);
                return jsonResult;
            }

            featuredActivity.setStatus(DBConstants.FeaturedActivityStatus.DELETED.getCode());
            featuredActivityMapper.updateByPrimaryKey(featuredActivity);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    public JsonResult insertFeaturedActivity(FeaturedActivity featuredActivity, String aid, MultipartFile file, JsonResult jsonResult) {
        try {
            //上传图片
            String _key = storageService.uploadToPublicBucket("featuredactivity", file);
            if (StringUtils.isEmpty(_key)) {
                CommonResult.uploadFailed(jsonResult);
                return jsonResult;
            }
            featuredActivity.setPictureKey(_key);


            featuredActivity.setStatus(DBConstants.FeaturedActivityStatus.INACTIVE.getCode());
            featuredActivity.setManagerId(Integer.parseInt(aid));
            featuredActivity.setCreateTime(new Timestamp(System.currentTimeMillis()));

            featuredActivityMapper.insert(featuredActivity);

            featuredActivity.setSort(featuredActivity.getId());

            featuredActivityMapper.updateByPrimaryKey(featuredActivity);

            CommonResult.success(jsonResult);

        }catch (Exception e){
            System.out.println(e);
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }

    @Override
    @Transactional
    public JsonResult changeFeaturedActivitysOrder(Integer cur, Integer prev, JsonResult jsonResult) {
        try {
            FeaturedActivity featuredActivity = featuredActivityMapper.selectByPrimaryKey(cur);

            FeaturedActivity featuredActivity1 = featuredActivityMapper.selectByPrimaryKey(prev);

            cur = featuredActivity.getSort();

            prev = featuredActivity1.getSort();

            featuredActivity.setSort(prev);
            featuredActivity1.setSort(cur);

            featuredActivityMapper.updateByPrimaryKey(featuredActivity);
            featuredActivityMapper.updateByPrimaryKey(featuredActivity1);
            CommonResult.success(jsonResult);
        }catch (Exception e){
            CommonResult.sqlFailed(jsonResult);
        }
        return jsonResult;
    }


}
