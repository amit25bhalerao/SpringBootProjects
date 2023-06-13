package com.example.OnetoManyRelationshipXMLtoJSON.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "OrderTable")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderId")
    private int orderId;

    @Column(name = "OrderDetails")
    private String orderDetails;

    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL)
    private List<OrderLineEntity> orderLineEntity;

    public OrderEntity() {
    }

    public OrderEntity(int orderId, String orderDetails, List<OrderLineEntity> orderLineEntity) {
        this.orderId = orderId;
        this.orderDetails = orderDetails;
        this.orderLineEntity = orderLineEntity;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(String orderDetails) {
        this.orderDetails = orderDetails;
    }

    public List<OrderLineEntity> getOrderLineEntity() {
        return orderLineEntity;
    }

    public void setOrderLineEntity(List<OrderLineEntity> orderLineEntity) {
        this.orderLineEntity = orderLineEntity;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "orderId=" + orderId +
                ", orderDetails='" + orderDetails + '\'' +
                ", orderLineEntity=" + orderLineEntity +
                '}';
    }
}
