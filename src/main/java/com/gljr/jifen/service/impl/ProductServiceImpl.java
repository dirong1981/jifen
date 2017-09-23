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
        productExample.setOrderByClause("id desc");
        return productMapper.selectByExample(productExample);
    }

    @Override
    public List<Product> selectAllProduct(int sort) {
        ProductExample productExample = new ProductExample();
        ProductExample.Criteria criteria = productExample.createCriteria();
        criteria.andStatusEqualTo(new Byte("1"));

        //设置排序
        if(sort == 0){
            productExample.setOrderByClause("id desc");
        }else if (sort == 1){
            productExample.setOrderByClause("sales desc, id desc");
        }else if (sort == 2){
            productExample.setOrderByClause("sales asc, id desc");
        }else if (sort == 3){
            productExample.setOrderByClause("integral desc, id desc");
        }else if (sort == 4){
            productExample.setOrderByClause("integral asc, id desc");
        }

        return productMapper.selectByExample(productExample);
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
    public Product selectProductById(int id) {
        return productMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Product> selectProductByKeyword(String keyword, int sort) {
        ProductExample productExample = new ProductExample();
        ProductExample.Criteria criteria = productExample.createCriteria();
        criteria.andStatusEqualTo(new Byte("1"));
        criteria.andNameLike("%" + keyword + "%");
        //设置排序
        if(sort == 0){
            productExample.setOrderByClause("id desc");
        }else if (sort == 1){
            productExample.setOrderByClause("sales desc, id desc");
        }else if (sort == 2){
            productExample.setOrderByClause("sales asc, id desc");
        }else if (sort == 3){
            productExample.setOrderByClause("integral desc, id desc");
        }else if (sort == 4){
            productExample.setOrderByClause("integral asc, id desc");
        }
        return productMapper.selectByExample(productExample);
    }

    @Override
    public List<Product> selectCategoryProduct(int code, int sort) {
        ProductExample productExample = new ProductExample();
        ProductExample.Criteria criteria = productExample.createCriteria();
        criteria.andStatusEqualTo(new Byte("1"));
        criteria.andCategoryCodeEqualTo(code);
        //设置排序
        if(sort == 0){
            productExample.setOrderByClause("id desc");
        }else if (sort == 1){
            productExample.setOrderByClause("sales desc, id desc");
        }else if (sort == 2){
            productExample.setOrderByClause("sales asc, id desc");
        }else if (sort == 3){
            productExample.setOrderByClause("integral desc, id desc");
        }else if (sort == 4){
            productExample.setOrderByClause("integral asc, id desc");
        }
        return productMapper.selectByExample(productExample);
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

    @Override
    public List<ProductPhoto> selectProductPhotos() {
        return productPhotoMapper.selectByExample(null);
    }

    @Override
    public List<ProductPhoto> selectProductPhotoById(int pid) {
        ProductPhotoExample productPhotoExample = new ProductPhotoExample();
        ProductPhotoExample.Criteria criteria = productPhotoExample.createCriteria();
        criteria.andPidEqualTo(pid);
        return productPhotoMapper.selectByExample(productPhotoExample);
    }
}
