package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.ProductMapper;
import com.gljr.jifen.pojo.Product;
import com.gljr.jifen.pojo.ProductExample;
import com.gljr.jifen.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductMapper productMapper;

    @Override
    public int addProduct(Product product) {
        return productMapper.insert(product);
    }

    @Override
    public List<Product> selectAllProduct() {
        ProductExample productExample = new ProductExample();

        return productMapper.selectByExample(productExample);
    }

    @Override
    public Product selectProduct(String id) {
        return productMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateProduct(Product product) {
        return productMapper.updateByPrimaryKey(product);
    }

    @Override
    public int deleteProduct(String id) {
        return productMapper.deleteByPrimaryKey(id);
    }
}
