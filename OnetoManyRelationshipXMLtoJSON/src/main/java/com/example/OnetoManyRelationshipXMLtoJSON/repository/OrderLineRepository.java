package com.example.OnetoManyRelationshipXMLtoJSON.repository;

import com.example.OnetoManyRelationshipXMLtoJSON.entity.OrderLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLineEntity, Long> {
}
