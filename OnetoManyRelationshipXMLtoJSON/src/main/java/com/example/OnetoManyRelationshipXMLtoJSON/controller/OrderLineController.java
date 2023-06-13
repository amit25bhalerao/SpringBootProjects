package com.example.OnetoManyRelationshipXMLtoJSON.controller;

import com.example.OnetoManyRelationshipXMLtoJSON.entity.OrderLineEntity;
import com.example.OnetoManyRelationshipXMLtoJSON.repository.OrderLineRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class OrderLineController {
    private final OrderLineRepository orderLineRepository;

    @Autowired
    public OrderLineController(OrderLineRepository orderLineRepository) {
        this.orderLineRepository = orderLineRepository;
    }

    @PostMapping("/importOrderLineDetails")
    public void importOrderLineDetails(MultipartFile file) throws ParserConfigurationException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        InputStream inputStream = file.getInputStream();
        org.w3c.dom.Document document;
        try {
            document = dBuilder.parse(inputStream);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        document.getDocumentElement().normalize();
        org.w3c.dom.NodeList orderList = document.getElementsByTagName("Order");
        List<OrderLineEntity> orderLineEntityList = new ArrayList<>();

        for (int i = 0; i < orderList.getLength(); i++) {
            org.w3c.dom.Node orderNode = orderList.item(i);
            if (orderNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                org.w3c.dom.Element orderElement = (org.w3c.dom.Element) orderNode;
                int orderId = Integer.parseInt(orderElement.getElementsByTagName("OrderId").item(0).getTextContent());
                org.w3c.dom.NodeList orderLineList = orderElement.getElementsByTagName("OrderLine");
                List<OrderLineEntity> orderLines = new ArrayList<>();

                for (int j = 0; j < orderLineList.getLength(); j++) {
                    org.w3c.dom.Node orderLineNode = orderLineList.item(j);
                    if (orderLineNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                        org.w3c.dom.Element orderLineElement = (org.w3c.dom.Element) orderLineNode;
                        int orderLineId = Integer.parseInt(orderLineElement.getElementsByTagName("OrderLineId").item(0).getTextContent());
                        String orderDetails = orderLineElement.getElementsByTagName("OrderLineDetails").item(0).getTextContent();

                        OrderLineEntity orderLineEntity = new OrderLineEntity(orderLineId, orderId, orderDetails);
                        orderLines.add(orderLineEntity);
                    }
                }
                orderLineEntityList.addAll(orderLines);
            }
        }

        if (!orderLineEntityList.isEmpty()) {
            String json = convertToJson(orderLineEntityList);
            saveJsonToFile(json);
            System.out.println("OrderLine details converted to JSON and saved to OrderLineDetails.json file successfully!");
        } else {
            System.out.println("No order line details found in the XML file.");
        }
    }

    private String convertToJson(List<OrderLineEntity> orderLineEntityList) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper.writeValueAsString(orderLineEntityList);
    }

    private void saveJsonToFile(String json) throws IOException {
        File outputFile = new File("OrderLineDetails.json");
        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(json.getBytes());
        }
    }
}
