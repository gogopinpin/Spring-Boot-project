package com.Ray.springbootmall.dto;

import jakarta.validation.constraints.NotNull;

public class BuyItem {

    @NotNull
//    購買的商品的 ID
    private Integer productId;

    @NotNull
//    購買的商品數量
    private Integer quantity;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
