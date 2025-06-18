package com.team_one.expressoh.model;

import java.io.Serializable;
import java.util.Objects;

public class OrderProductId implements Serializable {

    private Integer order;
    private Integer product;

    public OrderProductId() {}

    public OrderProductId(Integer order, Integer product) {
        this.order = order;
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderProductId)) return false;
        OrderProductId that = (OrderProductId) o;
        return Objects.equals(order, that.order) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, product);
    }
}
