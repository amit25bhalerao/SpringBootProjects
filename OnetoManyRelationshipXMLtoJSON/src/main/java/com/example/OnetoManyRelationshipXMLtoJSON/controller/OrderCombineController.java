package com.example.OnetoManyRelationshipXMLtoJSON.controller;

import com.example.OnetoManyRelationshipXMLtoJSON.entity.OrderEntity;
import com.example.OnetoManyRelationshipXMLtoJSON.entity.OrderLineEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderCombineController {

    private final List<OrderEntity> orderEntityList = new ArrayList<>();

    @PostMapping("/importFinalDetails")
    public void importOrderLineDetails(@RequestParam("orderDetails") MultipartFile orderDetailsFile,
                                       @RequestParam("orderLineDetails") MultipartFile orderLineDetailsFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Read order details JSON from file
        List<OrderEntity> newOrderEntityList = objectMapper.readValue(orderDetailsFile.getInputStream(), new TypeReference<List<OrderEntity>>() {});

        // Read order line details JSON from file
        List<OrderLineEntity> orderLineEntityList = objectMapper.readValue(orderLineDetailsFile.getInputStream(), new TypeReference<List<OrderLineEntity>>() {});

        // Update existing orderEntityList with new order details
        updateOrderEntityList(newOrderEntityList);

        // Combine the order and order line entities based on OrderId
        Map<Integer, OrderEntity> orderMap = new HashMap<>();
        for (OrderEntity orderEntity : orderEntityList) {
            orderMap.put(orderEntity.getOrderId(), orderEntity);
        }

        for (OrderLineEntity orderLineEntity : orderLineEntityList) {
            int orderId = orderLineEntity.getOrderId();
            OrderEntity orderEntity = orderMap.get(orderId);
            if (orderEntity != null) {
                if (orderEntity.getOrderLineEntity() == null) {
                    orderEntity.setOrderLineEntity(new ArrayList<>());
                }
                orderEntity.getOrderLineEntity().add(orderLineEntity);
            }
        }

        List<OrderEntity> combinedOrderList = new ArrayList<>(orderMap.values());

        String json = objectMapper.writeValueAsString(combinedOrderList);
        String filePath = "C:\\Users\\P7113583\\Desktop\\OnetoManyRelationshipXMLtoJSON\\OrderCombinedDetails.json";
        saveJsonToFile(json, filePath);
        System.out.println("Details converted to JSON and saved to OrderCombinedDetails.json file successfully!");
    }

    @GetMapping("/getAllOrders")
    public List<OrderEntity> getAllOrders() {
        return orderEntityList;
    }

    @GetMapping("/getOrderById/{id}")
    public OrderEntity getOrderById(@PathVariable int id) {
        return orderEntityList.stream()
                .filter(orderEntity -> orderEntity.getOrderId() == id)
                .findFirst()
                .orElse(null);
    }

    @PostMapping("/createOrder")
    public OrderEntity createOrder(@RequestBody OrderEntity orderEntity) {
        int maxOrderId = orderEntityList.stream()
                .mapToInt(OrderEntity::getOrderId)
                .max()
                .orElse(0);
        orderEntity.setOrderId(maxOrderId + 1);
        orderEntityList.add(orderEntity);
        return orderEntity;
    }

    @PutMapping("/updateOrder/{id}")
    public OrderEntity updateOrder(@PathVariable int id, @RequestBody OrderEntity updatedOrderEntity) {
        OrderEntity existingOrderEntity = orderEntityList.stream()
                .filter(orderEntity -> orderEntity.getOrderId() == id)
                .findFirst()
                .orElse(null);
        if (existingOrderEntity != null)
        {
            existingOrderEntity.setOrderDetails(updatedOrderEntity.getOrderDetails());
            existingOrderEntity.setOrderLineEntity(updatedOrderEntity.getOrderLineEntity());
            return existingOrderEntity;
        }
        return null;
    }

    @DeleteMapping("/deleteOrder/{id}")
    public boolean deleteOrder(@PathVariable int id) {
        OrderEntity orderEntityToRemove = orderEntityList.stream()
                .filter(orderEntity -> orderEntity.getOrderId() == id)
                .findFirst()
                .orElse(null);
        if (orderEntityToRemove != null) {
            orderEntityList.remove(orderEntityToRemove);
            return true;
        }
        return false;
    }

    private void updateOrderEntityList(List<OrderEntity> newOrderEntityList) {
        Map<Integer, OrderEntity> orderMap = orderEntityList.stream()
                .collect(Collectors.toMap(OrderEntity::getOrderId, orderEntity -> orderEntity));

        for (OrderEntity newOrderEntity : newOrderEntityList) {
            int orderId = newOrderEntity.getOrderId();
            if (orderMap.containsKey(orderId)) {
                OrderEntity existingOrderEntity = orderMap.get(orderId);
                existingOrderEntity.setOrderDetails(newOrderEntity.getOrderDetails());
            } else {
                orderMap.put(orderId, newOrderEntity);
            }
        }

        orderEntityList.clear();
        orderEntityList.addAll(orderMap.values());
    }

    private void saveJsonToFile(String json, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(json);
        }
    }
}
