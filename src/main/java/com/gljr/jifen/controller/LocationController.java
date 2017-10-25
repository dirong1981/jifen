package com.gljr.jifen.controller;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.pojo.Location;
import com.gljr.jifen.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/locations")
public class LocationController extends BaseController {


    @Autowired
    private LocationService locationService;


    @GetMapping
    @ResponseBody
    public JsonResult selectLocation(){
        JsonResult jsonResult = new JsonResult();

        try {
            Map map = new HashMap();

            List<Location> locations = locationService.selectLocationByParentCode(0);

            map.put("province", locations);

            locations = locationService.selectLocationByParentCode(450000);

            map.put("city", locations);

            locations = locationService.selectLocationByNotParentCode();
            map.put("district", locations);

            jsonResult.setErrorCode(GlobalConstants.OPERATION_SUCCEED);
            jsonResult.setMessage(GlobalConstants.OPERATION_SUCCEED_MESSAGE);
            jsonResult.setItem(map);
        }catch (Exception e){
            System.out.println(e);
            jsonResult.setMessage(GlobalConstants.OPERATION_FAILED);
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED_MESSAGE);
        }


        return jsonResult;
    }
}
