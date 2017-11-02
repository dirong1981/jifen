package com.gljr.jifen.service;

import com.gljr.jifen.common.JsonResult;
import com.gljr.jifen.pojo.SystemVirtualProduct;
import com.gljr.jifen.pojo.VirtualProduct;

public interface VirtualProductService {

    JsonResult selectEnabelSystemVirtualProduct(JsonResult jsonResult);

    JsonResult insertVirtualProduct(VirtualProduct virtualProduct, JsonResult jsonResult);

    JsonResult selectVirtualProduct(JsonResult jsonResult);

    JsonResult startVirtualProduct(Integer id, JsonResult jsonResult);

    JsonResult stopVirtualProduct(Integer id, JsonResult jsonResult);

    JsonResult selectEnableVirtualProduct(JsonResult jsonResult);

    JsonResult selectVirtualProductById(Integer virtualId, JsonResult jsonResult);

    JsonResult deleteVirtualProductById(Integer id, JsonResult jsonResult);


}
