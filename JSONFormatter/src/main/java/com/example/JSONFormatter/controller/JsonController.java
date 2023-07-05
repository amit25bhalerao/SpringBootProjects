package com.example.JSONFormatter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.Map;

@RestController
public class JsonController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(value = "/correct", produces = "application/json")
    public ResponseEntity<Object> correctJson(@RequestBody String inputJson) {
        try {
            // Try parsing the input JSON
            objectMapper.readTree(inputJson);
            // If parsing is successful, return the original JSON as it is valid
            return ResponseEntity.ok(inputJson);
        } catch (MismatchedInputException e) {
            // JSON parsing failed, attempt to correct the JSON syntax
            String correctedJson = correctJsonSyntax(inputJson);
            if (correctedJson != null) {
                // Return the corrected JSON
                return ResponseEntity.ok(correctedJson);
            } else {
                // If correction is not possible, return an error message
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid JSON syntax and unable to correct.");
            }
        } catch (JsonMappingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid JSON format: " + e.getMessage());
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the JSON: " + e.getMessage());
        }
    }

    private String correctJsonSyntax(String inputJson) {
        try {
            // Parse the input JSON to a JsonNode
            JsonNode jsonNode = objectMapper.readTree(inputJson);

            // Correct the JSON syntax recursively
            correctJsonNodeSyntax(jsonNode);

            // Convert the corrected JsonNode back to a string
            String correctedJson = objectMapper.writeValueAsString(jsonNode);

            return correctedJson;
        } catch (JsonProcessingException e) {
            return null; // Correction failed
        }
    }

    private void correctJsonNodeSyntax(JsonNode jsonNode) {
        if (jsonNode.isObject()) {
            // Iterate over the object fields
            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jsonNode.fields();
            while (fieldsIterator.hasNext()) {
                Map.Entry<String, JsonNode> field = fieldsIterator.next();

                // Correct the syntax of the field key
                String correctedKey = correctJsonStringSyntax(field.getKey());

                // Correct the syntax of the field value recursively
                correctJsonNodeSyntax(field.getValue());

                // Update the field key if it was corrected
                if (!field.getKey().equals(correctedKey)) {
                    ((ObjectNode) jsonNode).remove(field.getKey());
                    ((ObjectNode) jsonNode).set(correctedKey, field.getValue());
                }
            }
        } else if (jsonNode.isArray()) {
            // Iterate over the array elements
            for (int i = 0; i < jsonNode.size(); i++) {
                JsonNode element = jsonNode.get(i);

                // Correct the syntax of the array element recursively
                correctJsonNodeSyntax(element);
            }
        } else if (jsonNode.isValueNode()) {
            // Correct the syntax of the string value
            if (jsonNode.isTextual()) {
                String correctedValue = correctJsonStringSyntax(jsonNode.asText());
                ((ValueNode) jsonNode).asText(correctedValue);
            }
        }
    }

    private String correctJsonStringSyntax(String jsonString) {
        // Replace single quotes with double quotes
        String correctedJson = jsonString.replace("'", "\"");

        // Replace trailing commas within string values
        correctedJson = correctedJson.replaceAll(",(\\s*[\"\\]])", "$1");

        // Escape offensive characters in string values
        correctedJson = correctedJson.replaceAll("([\"'])(.*?[^\\\\])([\"'])", "$1$2\\\\$3");

        // Remove leading zeroes from numbers within string values
        correctedJson = correctedJson.replaceAll("\"(\\d+)\"", "$1");

        // Convert unsupported object types (e.g., dates) within string values to strings
        correctedJson = correctedJson.replaceAll(":\\s*(\\{[^\\}]*\\}|\\[[^\\]]*\\])", ": \"$1\"");

        return correctedJson;
    }

}
