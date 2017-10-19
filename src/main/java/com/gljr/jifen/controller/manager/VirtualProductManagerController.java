package com.gljr.jifen.controller.manager;

import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.controller.BaseController;
import com.gljr.jifen.pojo.VirtualProduct;
import com.gljr.jifen.service.VirtualProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/manager/virtualproducts")
public class VirtualProductManagerController extends BaseController{

    @Autowired
    private VirtualProductService virtualProductService;


    /**
     * 查询可用的系统虚拟商品类型
     * @return
     */
    @GetMapping(value = "/system")
    @ResponseBody
    public JsonResult selectEnabelSystemVirtualProduct(){
        JsonResult jsonResult = new JsonResult();

        jsonResult = virtualProductService.selectEnabelSystemVirtualProduct(jsonResult);

        return jsonResult;
    }


    @PostMapping
    @ResponseBody
    public JsonResult insertVirtualProduct(VirtualProduct virtualProduct, HttpServletRequest httpServletRequest){
        JsonResult jsonResult = new JsonResult();

        //获取开始结束时间
        String start = httpServletRequest.getParameter("start");
        String end = httpServletRequest.getParameter("end");
        //时间转换
        try {
            if(!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)){
                String pattern="yyyy-MM-dd";
                SimpleDateFormat dateFormat=new SimpleDateFormat(pattern);

                virtualProduct.setValidFrom(dateFormat.parse(start));
                virtualProduct.setValidTo(dateFormat.parse(end));
            }
        }catch (Exception e){
            jsonResult.setMessage("时间设置错误");
            jsonResult.setErrorCode(GlobalConstants.OPERATION_FAILED);
            return jsonResult;
        }

        virtualProduct.setAllowCancel(0);
        virtualProduct.setCreateTime(new Timestamp(System.currentTimeMillis()));
        virtualProduct.setRemainingAmount(virtualProduct.getAmount());

        jsonResult = virtualProductService.insertVirtualProduct(virtualProduct, jsonResult);


        return jsonResult;
    }

    @GetMapping(value = "/all")
    @ResponseBody
    public JsonResult selectAllVirtualProducts(){
        JsonResult jsonResult = new JsonResult();

        jsonResult = virtualProductService.selectVirtualProduct(jsonResult);

        return jsonResult;
    }


    @GetMapping
    @ResponseBody
    public JsonResult selectEnableVirtualProducts(){
        JsonResult jsonResult = new JsonResult();

        jsonResult = virtualProductService.selectEnableVirtualProduct(jsonResult);

        return jsonResult;
    }


    @GetMapping(value = "/{id}/acceptance")
    @ResponseBody
    public JsonResult startVirtualProducts(@PathVariable(value = "id") Integer id){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(id)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = virtualProductService.startVirtualProduct(id, jsonResult);

        return jsonResult;
    }

    @GetMapping(value = "/{id}/rejection")
    @ResponseBody
    public JsonResult stopVirtualProducts(@PathVariable(value = "id") Integer id){
        JsonResult jsonResult = new JsonResult();

        if(StringUtils.isEmpty(id)){
            CommonResult.noObject(jsonResult);
            return jsonResult;
        }

        jsonResult = virtualProductService.stopVirtualProduct(id, jsonResult);

        return jsonResult;
    }

}
