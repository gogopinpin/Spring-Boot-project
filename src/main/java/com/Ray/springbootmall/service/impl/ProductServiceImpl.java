package com.Ray.springbootmall.service.impl;

import com.Ray.springbootmall.dto.ProductQueryParams;
import com.Ray.springbootmall.dto.ProductRequest;
import com.Ray.springbootmall.model.Product;
import com.Ray.springbootmall.service.ProductService;
import com.Ray.springbootmall.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {

//      Service層直接去 call ProductDao的 getProductById 方法
    @Autowired
    private ProductDao productDao;

//      根據提供的商品查詢條件，返回符合條件的商品總數量。
    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        return productDao.countProduct(productQueryParams);
    }

//      根據提供的商品查詢條件，返回符合條件的商品列表
    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        return productDao.getProducts(productQueryParams);
    }

//      根據提供的商品ID，返回該商品的詳細資訊
    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }

//      創建商品
    @Override
    public Integer createProduct(ProductRequest productRequest) {
        return productDao.createProduct(productRequest);
    }
//      更新商品
    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {

        productDao.updateProduct(productId, productRequest);
    }
//      刪除商品
    @Override
    public void deleteProductById(Integer productId) {
        productDao.deleteProductById(productId);

    }
}
