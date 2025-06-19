// DtoOrderRequest.java
package com.team_one.expressoh.dto;

import java.util.List;

public class DtoOrderRequest {
    private List<DtoOrderProduct> products;

    public List<DtoOrderProduct> getProducts() {
        return products;
    }

    public void setProducts(List<DtoOrderProduct> products) {
        this.products = products;
    }
}
