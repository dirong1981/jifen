package com.gljr.jifen.service.impl;


import com.gljr.jifen.common.ValidCheck;
import com.gljr.jifen.constants.DBConstants;
import com.gljr.jifen.dao.ProductMapper;
import com.gljr.jifen.dao.ProductPhotoMapper;
import com.gljr.jifen.pojo.Product;
import com.gljr.jifen.pojo.ProductExample;
import com.gljr.jifen.pojo.ProductPhoto;
import com.gljr.jifen.pojo.ProductPhotoExample;
import com.gljr.jifen.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductPhotoMapper productPhotoMapper;


    @Override
    @Transactional
    public int addProduct(Product product, Integer random) {

        productMapper.insert(product);

        ProductPhotoExample productPhotoExample = new ProductPhotoExample();
        ProductPhotoExample.Criteria criteria = productPhotoExample.or();
        criteria.andPidEqualTo(random);

        List<ProductPhoto> productPhotos = productPhotoMapper.selectByExample(productPhotoExample);

        if(!ValidCheck.validList(productPhotos)){
            for (ProductPhoto productPhoto : productPhotos){
                productPhoto.setPid(product.getId());
                productPhotoMapper.updateByPrimaryKey(productPhoto);
            }
        }

        return 0;
    }

    @Override
    public List<Product> selectAllProduct() {
        ProductExample productExample = new ProductExample();
        ProductExample.Criteria criteria = productExample.or();
        criteria.andStatusNotEqualTo(DBConstants.ProductStatus.DELETED.getCode());
        productExample.setOrderByClause("id desc");
        return productMapper.selectByExample(productExample);
    }

    @Override
    public List<Product> selectAllProduct(int sort) {
        ProductExample productExample = new ProductExample();
        ProductExample.Criteria criteria = productExample.createCriteria();
        criteria.andStatusEqualTo(DBConstants.ProductStatus.ON_SALE.getCode());

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
    public int deleteProduct(Product product) {
        return productMapper.updateByPrimaryKey(product);
    }

    @Override
    public Long selectProductPhotoCountByPid(Integer pid) {
        ProductPhotoExample productPhotoExample = new ProductPhotoExample();
        ProductPhotoExample.Criteria criteria = productPhotoExample.or();
        criteria.andPidEqualTo(pid);
        return productPhotoMapper.countByExample(productPhotoExample);
    }

    @Override
    public Product selectProductById(int id) {
        return productMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Product> selectProductByKeyword(String keyword, int sort) {
        ProductExample productExample = new ProductExample();
        ProductExample.Criteria criteria = productExample.createCriteria();
        criteria.andStatusEqualTo(DBConstants.ProductStatus.ON_SALE.getCode());
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
        criteria.andStatusEqualTo(DBConstants.ProductStatus.ON_SALE.getCode());
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
