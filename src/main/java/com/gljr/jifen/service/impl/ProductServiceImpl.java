package com.gljr.jifen.service.impl;

import com.gljr.jifen.dao.ProductMapper;
import com.gljr.jifen.dao.ProductPhotoMapper;
import com.gljr.jifen.pojo.Product;
import com.gljr.jifen.pojo.ProductExample;
import com.gljr.jifen.pojo.ProductPhoto;
import com.gljr.jifen.pojo.ProductPhotoExample;
import com.gljr.jifen.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductPhotoMapper productPhotoMapper;

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
    public Product selectProduct(int id) {
        return productMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateProduct(Product product) {
        return productMapper.updateByPrimaryKey(product);
    }

    @Override
    public int deleteProduct(int id) {
        return productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertProductPhoto(ProductPhoto productPhoto) {
        return productPhotoMapper.insert(productPhoto);
    }

    @Override
    public List<ProductPhoto> selectProductPhoto(int productPhoto) {
        ProductPhotoExample productPhotoExample = new ProductPhotoExample();
        ProductPhotoExample.Criteria criteria = productPhotoExample.createCriteria();
        criteria.andPidEqualTo(productPhoto);
        return productPhotoMapper.selectByExample(productPhotoExample);
    }

    @Override
    public int updateProductPhoto(ProductPhoto productPhoto) {

        return productPhotoMapper.updateByPrimaryKey(productPhoto);
    }
}
