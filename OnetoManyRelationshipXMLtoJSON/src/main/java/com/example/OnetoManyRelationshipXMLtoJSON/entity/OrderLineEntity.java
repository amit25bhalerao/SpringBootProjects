package com.example.OnetoManyRelationshipXMLtoJSON.entity;

import javax.persistence.*;

@Entity
@Table(name = "OrderLineTable")
public class OrderLineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "OrderLineId")
    private int orderLineId;

    @Column(name = "OrderId")
    private int orderId;

    @Column(name = "OrderLineDetails")
    private String orderLineDetails;

    @ManyToOne
    @JoinColumn(name = "OrderId", referencedColumnName = "OrderId", insertable = false, updatable = false)
    private OrderEntity orderEntity;

    public int getOrderLineId() {
        return orderLineId;
    }

    public void setOrderLineId(int orderLineId) {
        this.orderLineId = orderLineId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderLineDetails() {
        return orderLineDetails;
    }

    public void setOrderLineDetails(String orderLineDetails) {
        this.orderLineDetails = orderLineDetails;
    }

    public OrderLineEntity(){

    }
    public OrderLineEntity(int orderLineId, int orderId, String orderLineDetails) {
        this.orderLineId = orderLineId;
        this.orderId = orderId;
        this.orderLineDetails = orderLineDetails;
    }
}
