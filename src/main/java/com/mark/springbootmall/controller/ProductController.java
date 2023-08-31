package com.Ray.springbootmall.controller;

import com.Ray.springbootmall.model.Product;
import com.Ray.springbootmall.constant.ProductCategory;
import com.Ray.springbootmall.dto.ProductQueryParams;
import com.Ray.springbootmall.dto.ProductRequest;
import com.Ray.springbootmall.service.ProductService;
import com.Ray.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Validated

//      處理商品列表查詢的端點。
//      Products這個 s 很重要一定要加，這是在 RESTful 的設計原則，商品列表一定是有很多的商品
    @GetMapping("/products")

//      商品列表查詢

    public ResponseEntity<Page<Product>> getProducts(
//      查詢條件 Filtering
            @RequestParam(required = false) ProductCategory category,//required = false表示 這個參數是一個可選的參數
            @RequestParam(required = false) String search,

//      排序 Sorting
            @RequestParam(defaultValue = "created_date") String orderBy,   //根據什麼樣的欄位來進行排序
            @RequestParam(defaultValue = "desc") String sort,       //使用升序或是降序來排序

//      分頁 Pagination
            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,  //要取得幾筆商品數據
            @RequestParam(defaultValue = "0") @Min(0) Integer offset  //要去跳過多少筆數據


    ) {

        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

//          取得 product List
        List<Product> productList = productService.getProducts(productQueryParams);

//          取得 product 總數
        Integer total = productService.countProduct(productQueryParams);

//          分頁
        Page<Product> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResults(productList);

        return ResponseEntity.ok(page);
    }

//          單一商品查詢
    @GetMapping("/products/{productId}") //前端來請求這個 url 路徑
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);
//          透過 productService 的 getProductById 方法
//          去資料庫中去查詢這一筆商品的數據出來

        if (product != null) {
//          商品數據值不是 null 回傳一個 http 狀態碼 200 OK
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } else {
//          404 Not Found 表示這個商品找不到
            return ResponseEntity.notFound().build();
        }
    }

//          商品創建
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {
//          ProductRequest 裡面有 @NotNull 的註解一定要記得加上 @Valid 的註解

//          productService 會提供一個createProduct 的方法，參數就是 productRequest
//          另外這個 createProduct 的方法他要去返回資料庫所生成的productId
        Integer productId = productService.createProduct(productRequest);

//          使用這個 productId去查詢這個商品的數據回來
        Product product = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

//          商品更新
    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest) {

//          檢查 product 是否存在
        Product product = productService.getProductById(productId);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

//          修改商品數據
//          productService會提供 updateProduct 的方法那他會有兩個參數
        productService.updateProduct(productId, productRequest);

//          使用 productId去查詢更新後的商品數據回來
        Product updateProduct = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }
//          商品刪除

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProductById(@PathVariable Integer productId) {
        productService.deleteProductById(productId);

//      不論如何只要確定商品消失不見
//      那就表示這個刪除商品的功能是成功
//     因此我們就要回傳成功的 204 No Content 給前端
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
