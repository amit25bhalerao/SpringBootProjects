package com.example.OnetoManyRelationshipXMLtoJSON.controller;

import com.example.OnetoManyRelationshipXMLtoJSON.entity.OrderEntity;
import com.example.OnetoManyRelationshipXMLtoJSON.entity.OrderLineEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderCombineController {

    @PostMapping("/importFinalDetails")
    public void importOrderLineDetails(@RequestParam("orderDetails") MultipartFile orderDetailsFile,
                                       @RequestParam("orderLineDetails") MultipartFile orderLineDetailsFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Read order details JSON from file
        List<OrderEntity> orderEntityList = null;
        try {
            orderEntityList = objectMapper.readValue(orderDetailsFile.getInputStream(), new TypeReference<List<OrderEntity>>() {});
        } catch (IOException e) {
        }

        // Read order line details JSON from file
        List<OrderLineEntity> orderLineEntityList = objectMapper.readValue(orderLineDetailsFile.getInputStream(), new TypeReference<List<OrderLineEntity>>() {});

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

    private void saveJsonToFile(String json, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(json);
        }
    }
}
