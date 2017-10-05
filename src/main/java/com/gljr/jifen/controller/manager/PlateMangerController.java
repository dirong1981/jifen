package com.gljr.jifen.controller.manager;


import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.service.PlateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/manager/plates")
public class PlateMangerController {

    @Autowired
    private PlateService plateService;

    /**
     * 获取所有上线板块
     * @return
     */
    @GetMapping
    @ResponseBody
    public JsonResult selectPlates(){
        JsonResult jsonResult = new JsonResult();

        jsonResult = plateService.selectPlates(jsonResult);

        return jsonResult;
    }

    /**
     * 生成首页
     * @return
     */
    @GetMapping(value = "/generator")
    @ResponseBody
    public JsonResult generator(){
        JsonResult jsonResult = new JsonResult();

        jsonResult = plateService.generatePlates(jsonResult);

        return jsonResult;
    }

    @PutMapping(value = "/order")
    @ResponseBody
    public JsonResult changeCategoryOrder(@RequestParam(value = "cur") Integer cur, @RequestParam(value = "prev") Integer prev){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(cur) || StringUtils.isEmpty(prev)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = plateService.changePlateOrder(cur, prev, jsonResult);

        return  jsonResult;
    }

}
