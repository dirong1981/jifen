package com.gljr.jifen.service;

import com.gljr.jifen.pojo.Product;
import com.gljr.jifen.pojo.ProductPhoto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ProductService {

    int addProduct(Product product);



    List<Product> selectAllProduct(int sort);

    List<Product> selectCategoryProduct(int code, int sort);

    Product selectProductById(int id);

    List<Product> selectProductByKeyword(String keyword, int sort);



    int updateProduct(Product product);



    int deleteProduct(int id);












    //图片相关

    int insertProductPhoto(ProductPhoto productPhoto);

    List<ProductPhoto> selectProductPhoto(int productPhoto);

    int updateProductPhoto(ProductPhoto productPhoto);

    List<ProductPhoto> selectProductPhotos();

    List<ProductPhoto> selectProductPhotoById(int pid);



}
