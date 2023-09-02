package com.Ray.springbootmall.service;


import com.Ray.springbootmall.dto.ProductQueryParams;
import com.Ray.springbootmall.model.Product;
import com.Ray.springbootmall.dto.ProductRequest;


import java.util.List;


public interface ProductService {

//    計算符合指定查詢條件的商品數量。
    Integer countProduct(ProductQueryParams productQueryParams);
//    根據指定的查詢條件獲取商品列表。
    List<Product> getProducts(ProductQueryParams productQueryParams);
//    根據商品 ID 獲取單個商品的詳細資訊。
    Product getProductById(Integer productId);
//    創建一個新的商品。
    Integer createProduct(ProductRequest productRequest);
//    更新現有的商品資訊。
    void updateProduct(Integer productId,ProductRequest productRequest);
//    刪除指定 ID 的商品。
    void deleteProductById(Integer productId);


}
