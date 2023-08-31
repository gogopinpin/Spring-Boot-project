package com.Ray.springbootmall.dao;

import com.Ray.springbootmall.dto.ProductQueryParams;
import com.Ray.springbootmall.dto.ProductRequest;
import com.Ray.springbootmall.model.Product;

import java.util.List;


public interface ProductDao {

    Integer countProduct(ProductQueryParams productQueryParams);

    List<Product> getProducts(ProductQueryParams productQueryParams);

    Product getProductById(Integer productId);

    //根據 productId去查詢這個商品的數據出來

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId,ProductRequest productRequest);

    void updateStock(Integer productId, Integer stock);

    void deleteProductById(Integer productId);
}
