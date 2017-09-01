package com.gljr.jifen.service;

import com.gljr.jifen.pojo.Product;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ProductService {

    int addProduct(Product product);

    List<Product> selectAllProduct();


    Product selectProduct(String id);

    int updateProduct(Product product);

    int deleteProduct(String id);
}
