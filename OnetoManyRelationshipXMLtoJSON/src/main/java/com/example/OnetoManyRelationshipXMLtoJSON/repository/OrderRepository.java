package com.example.OnetoManyRelationshipXMLtoJSON.repository;

import com.example.OnetoManyRelationshipXMLtoJSON.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository <OrderEntity, Long> {
}