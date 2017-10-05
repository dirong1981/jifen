package com.gljr.jifen.controller;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.service.PlateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/index")
public class PlateController {

    @Autowired
    private PlateService plateService;

    @GetMapping
    @ResponseBody
    public JsonResult index(){
        JsonResult jsonResult = new JsonResult();

        jsonResult = plateService.generatePlates(jsonResult);

        return jsonResult;
    }

}
