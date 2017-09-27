package com.gljr.jifen.service;

import com.gljr.jifen.pojo.Location;

import java.util.List;

public interface LocationService {
    List<Location> selectLocationByParentCode(int code);
    List<Location> selectLocationByNotParentCode();
    List<Location> selectLocation();
}
