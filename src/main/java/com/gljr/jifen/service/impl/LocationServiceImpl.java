package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.LocationMapper;
import com.gljr.jifen.pojo.Location;
import com.gljr.jifen.pojo.LocationExample;
import com.gljr.jifen.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationMapper locationMapper;


    @Override
    public List<Location> selectLocationByParentCode(int code) {
        LocationExample locationExample = new LocationExample();
        LocationExample.Criteria criteria = locationExample.or();
        criteria.andParentCodeEqualTo(code);
        locationExample.setOrderByClause("id asc");
        return locationMapper.selectByExample(locationExample);
    }

    @Override
    public List<Location> selectLocationByNotParentCode() {
        LocationExample locationExample = new LocationExample();
        LocationExample.Criteria criteria = locationExample.or();
        criteria.andParentCodeNotEqualTo(0);
        criteria.andParentCodeNotEqualTo(450000);
        locationExample.setOrderByClause("id asc");
        return locationMapper.selectByExample(locationExample);
    }

    @Override
    public List<Location> selectLocation() {
        return locationMapper.selectByExample(null);
    }
}
