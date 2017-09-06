package com.gljr.jifen.service;

import com.gljr.jifen.pojo.Product;
import com.gljr.jifen.pojo.ProductPhoto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ProductService {

    int addProduct(Product product);

    List<Product> selectAllProduct();


    Product selectProduct(int id);

    int updateProduct(Product product);

    int deleteProduct(int id);


    //图片相关

    int insertProductPhoto(ProductPhoto productPhoto);

    List<ProductPhoto> selectProductPhoto(int productPhoto);

    int updateProductPhoto(ProductPhoto productPhoto);

}
