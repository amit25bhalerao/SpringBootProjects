package com.example.OnetoManyRelationshipXMLtoJSON.controller;

import com.example.OnetoManyRelationshipXMLtoJSON.entity.OrderEntity;
import com.example.OnetoManyRelationshipXMLtoJSON.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping("/importOrderDetails")
    public void importOrderDetails(MultipartFile file) throws ParserConfigurationException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputStream inputStream = file.getInputStream();
        org.w3c.dom.Document document = null;
        try {
            document = dBuilder.parse(inputStream);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        document.getDocumentElement().normalize();
        org.w3c.dom.NodeList nodeList = document.getElementsByTagName("Order");
        List<OrderEntity> orderEntityList = new ArrayList<>();

        int orderId = 101; // Starting order ID

        for (int i = 0; i < nodeList.getLength(); i++) {
            org.w3c.dom.Node node = nodeList.item(i);
            if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setOrderId(orderId); // Set the order ID
                orderEntity.setOrderDetails(element.getElementsByTagName("OrderDetails").item(0).getTextContent());
                orderEntityList.add(orderEntity);
                orderId++; // Increment the order ID for the next order
            }
        }
        String json = convertToJson(orderEntityList);
        saveJsonToFile(json);
        System.out.println("Order details converted to JSON and saved to OrderDetails.json file successfully!");
    }


    private String convertToJson(List<OrderEntity> orderEntityList) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper.writeValueAsString(orderEntityList);
    }

    private void saveJsonToFile(String json) throws IOException {
        File outputFile = new File("OrderDetails.json");
        try (Writer writer = new FileWriter(outputFile)) {
            writer.write(json);
        }
    }
}