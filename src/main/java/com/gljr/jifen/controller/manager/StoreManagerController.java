package com.gljr.jifen.controller.manager;

import com.gljr.jifen.common.CommonResult;
import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.constants.GlobalConstants;
import com.gljr.jifen.controller.BaseController;
import com.gljr.jifen.filter.AuthPassport;
import com.gljr.jifen.pojo.Admin;
import com.gljr.jifen.pojo.Product;
import com.gljr.jifen.service.ProductService;
import com.gljr.jifen.service.StorageService;
import com.gljr.jifen.service.StoreInfoService;
import com.gljr.jifen.service.StoreManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.Timestamp;


@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/v1/store/manager")
public class StoreManagerController extends BaseController {

    @Autowired
    private StoreManagerService storeManagerService;


    /**
     * 管理员登录
     * @param admin
     * @return
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public JsonResult login(Admin admin){
        JsonResult jsonResult = new JsonResult();

        jsonResult = storeManagerService.login(admin, jsonResult);

        return  jsonResult;
    }

    /**获取商户信息
     *
     * @return
     */
    @GetMapping(value = "/info")
    @ResponseBody
    @AuthPassport(permission_code = "#11#")
    public JsonResult getStoreInfo(){
        JsonResult jsonResult = new JsonResult();

        String aid = request.getHeader("aid");

        jsonResult = storeManagerService.getStoreInfo(aid, jsonResult);

        return jsonResult;
    }


    /**
     * 查询所有商品包括未上架商品
     * @return
     */
    @GetMapping("/products")
    @ResponseBody
    public JsonResult selectProducts(){
        JsonResult jsonResult = new JsonResult();

        String aid = request.getHeader("aid");

        jsonResult = storeManagerService.selectAllProduct(aid, jsonResult);

        return jsonResult;
    }


    /**
     * 查询商户在线订单
     * @return
     */
    @GetMapping("/online-orders")
    @ResponseBody
    public JsonResult selectOnlineOrders(){
        JsonResult jsonResult = new JsonResult();

        String aid = request.getHeader("aid");

        jsonResult = storeManagerService.selectOnlineOrders(aid, jsonResult);

        return jsonResult;
    }



}
