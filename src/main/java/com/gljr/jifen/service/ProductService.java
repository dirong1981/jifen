package com.gljr.jifen.service;

import com.gljr.jifen.pojo.Product;
import com.gljr.jifen.pojo.ProductPhoto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ProductService {

    /**
     * 添加商品，更新上传图片的临时id为商品id
     * @param product
     * @return
     */
    int addProduct(Product product, Integer random);


    /**
     * 查找素有商品，包括未审核，但不显示删除商品
     * @return
     */
    List<Product> selectAllProduct();

    List<Product> selectAllProduct(int sort);

    List<Product> selectCategoryProduct(int code, int sort);

    Product selectProductById(int id);

    List<Product> selectProductByKeyword(String keyword, int sort);



    int updateProduct(Product product);



    int deleteProduct(Product product);

    /**
     * 根据商品id查询图片数量
     * @param pid
     * @return
     */
    Long selectProductPhotoCountByPid(Integer pid);












    //图片相关

    int insertProductPhoto(ProductPhoto productPhoto);

    List<ProductPhoto> selectProductPhoto(int productPhoto);

    int updateProductPhoto(ProductPhoto productPhoto);

    List<ProductPhoto> selectProductPhotos();

    List<ProductPhoto> selectProductPhotoById(int pid);



}
