package com.Ray.springbootmall.dao.impl;

import com.Ray.springbootmall.dao.ProductDao;
import com.Ray.springbootmall.dto.ProductQueryParams;
import com.Ray.springbootmall.dto.ProductRequest;
import com.Ray.springbootmall.model.Product;
import com.Ray.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
//      計算符合指定查詢條件的商品總數
    public Integer countProduct(ProductQueryParams productQueryParams) {
        String sql = "SELECT count(*) FROM product WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

//      查詢條件
        sql = addFilteringSql(sql, map, productQueryParams);

        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);//把count(*)轉換成 Integer

        return total;
    }

    @Override
//      獲取符合指定查詢條件的商品列表
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
//      把 product table 中的所有商品數據全部都去給查詢出來
        String sql = "SELECT product_id, product_name, category, image_url, " +
                "price, stock, description, created_date, last_modified_date " +
                "FROM product WHERE 1=1";   //1=1 寫法可以拼接後來加上的sql語句

        Map<String, Object> map = new HashMap<>();

//      查詢條件
        sql = addFilteringSql(sql, map, productQueryParams);

//      排序
        sql = sql + " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();

//      分頁
        sql = sql + " LIMIT :limit OFFSET :offset";
        map.put("limit", productQueryParams.getLimit());
        map.put("offset", productQueryParams.getOffset());

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        return productList;
    }

    @Override
//      根據商品 ID 獲取特定的商品
    public Product getProductById(Integer productId) {

        String sql = "SELECT product_id, product_name, category, image_url, " +
                "price, stock, description, created_date, last_modified_date " +
                "FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
//      productList 的 ＜＞就是我們在 ProductRowMapper 裡面mapRow 方法所返回的類型所以就是 Product

        if (productList.size() > 0) {
            return productList.get(0);
        } else {
            return null;
        }
    }

    @Override
//      創建新的商品
    public Integer createProduct(ProductRequest productRequest) {

//      設定每一個欄位的值
        String sql = "INSERT INTO product (product_name, category, image_url, " +
                "price, stock, description, created_date, last_modified_date) " +
                "VALUES (:productName, :category, :imageUrl, :price, :stock, " +
                ":description, :createdDate, :lastModifiedDate)";

//      把前端所傳過來的 productRequest 中的參數加到這個 map 裡面
        Map<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());// Enum 類型轉成 String
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

//      把這個當下的時間當成是這個商品的創建時間以及最後修改的時間
        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

//      使用 KeyHolder去儲存資料庫自動生成的 productId
 //     然後再將這個 productId給回傳出去
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int productId = keyHolder.getKey().intValue();

        return productId;
    }

    @Override
//      更新現有的商品資訊
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = "UPDATE product\n" +
                "SET product_name       = :productName,\n" +
                "    category           = :category,\n" +
                "    image_url          = :imageUrl,\n" +
                "    price              = :price,\n" +
                "    stock              = :stock,\n" +
                "    description        = :description,\n" +
                "    last_modified_date = :lastModifiedDate\n" +
                "WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());// Enum 類型轉成 String
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());
        //紀錄商品最後更新時間
        map.put("lastModifiedDate", new Date());

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
//      更新商品的庫存數量
    public void updateStock(Integer productId, Integer stock) {
        String sql = "UPDATE product SET stock = :stock, last_modified_date = :lastModifiedDate " +
                "WHERE product_id = :productId";
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("stock", stock);
        map.put("lastModifiedDate", new Date());

        namedParameterJdbcTemplate.update(sql,map);
    }

    @Override
//      根據商品 ID 刪除商品
    public void deleteProductById(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        namedParameterJdbcTemplate.update(sql, map);

    }

//      依據傳入的查詢參數，可以根據商品類別和搜索關鍵字進行過濾。
    private String addFilteringSql(String sql, Map<String, Object> map, ProductQueryParams productQueryParams) {
        if (productQueryParams.getCategory() != null) {
            sql = sql + " AND category = :category";     //AND前一定要預留空格
            map.put("category", productQueryParams.getCategory().name());  //.name是為了轉換文字
        }

        if (productQueryParams.getSearch() != null) {
            sql = sql + " AND product_name LIKE :search";
            map.put("search", "%" + productQueryParams.getSearch() + "%"); //表示product_name LIKE '%search%'
        }
        return sql;
    }


}
