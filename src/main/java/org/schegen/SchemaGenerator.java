package org.schegen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class SchemaGenerator {

    Map<String, String> typeMapping = Map.of(
            "Long", "integer",
            "String", "string",
            "LocalDate", "string",
            "Date", "string",
            "BigDecimal", "number",
            "Double", "number",
            "Float", "number",
            "Boolean", "boolean"
            );
    public void generateSchema() {

        String inputFilePath = "src/main/resources/entity.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode schema = mapper.createObjectNode();

            schema.put("type", "object");
            ObjectNode properties = mapper.createObjectNode();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineParts = line.split("\\s+");
                if (lineParts.length >= 2) {
                    String variableType = lineParts[0];
                    String variableName = lineParts[1].replace(";", "");

                    if (!variableType.startsWith("List")) {
                        ObjectNode propertyNode;
                        switch (variableType) {
                            case "Long":
                                propertyNode = gerPropertyNode(variableType);
                                propertyNode.put("format", "int64");
                                propertyNode.put("descriptions", "");
                                propertyNode.put("example", "1234");
                                properties.set(variableName, propertyNode);
                                break;
                            case "String":
                                propertyNode = gerPropertyNode(variableType);
                                propertyNode.put("descriptions", "");
                                propertyNode.put("example", "");
                                properties.set(variableName, propertyNode);
                                break;
                            case "LocalDate":
                                propertyNode = gerPropertyNode(variableType);
                                propertyNode.put("format", "date");
                                propertyNode.put("descriptions", "");
                                propertyNode.put("example", "2024-01-01");
                                properties.set(variableName, propertyNode);
                                break;
                            case "Date":
                                propertyNode = gerPropertyNode(variableType);
                                propertyNode.put("format", "date-time");
                                propertyNode.put("descriptions", "");
                                propertyNode.put("example", "2024-01-01");
                                properties.set(variableName, propertyNode);
                                break;
                            case "BigDecimal":
                                propertyNode = gerPropertyNode(variableType);
                                propertyNode.put("descriptions", "");
                                propertyNode.put("example", "1234");
                                properties.set(variableName, propertyNode);
                                break;
                            case "Double":
                                propertyNode = gerPropertyNode(variableType);
                                propertyNode.put("format", "double");
                                propertyNode.put("descriptions", "");
                                propertyNode.put("example", "1.234");
                                properties.set(variableName, propertyNode);
                                break;
                            case "Float":
                                propertyNode = gerPropertyNode(variableType);
                                propertyNode.put("format", "float");
                                propertyNode.put("descriptions", "");
                                propertyNode.put("example", "1.234");
                                properties.set(variableName, propertyNode);
                                break;
                            case "Boolean":
                                propertyNode = gerPropertyNode(variableType);
                                propertyNode.put("descriptions", "");
                                propertyNode.put("example", "false");
                                properties.set(variableName, propertyNode);
                                break;
                            case "byte[]":
                                propertyNode = gerPropertyNode(variableType);
                                propertyNode.put("format", "byte");
                                propertyNode.put("descriptions", "");
                                properties.set(variableName, propertyNode);
                                break;
                            default:
                                propertyNode = mapper.createObjectNode();
                                propertyNode.put("descriptions", "");
                                propertyNode.put("$ref", typeMapping.getOrDefault(variableType, "./"));
                                properties.set(variableName, propertyNode);
                        }
                    }

                    if (variableType.startsWith("List<")) {
                        String itemType = variableType.substring(5, variableType.length() - 1);
                        ObjectNode arrayNode = mapper.createObjectNode();
                        arrayNode.put("type", "array");
                        ObjectNode itemsNode = mapper.createObjectNode();
                        itemsNode.put("$ref", typeMapping.getOrDefault(itemType, "./"));
                        arrayNode.set("items", itemsNode);
                        properties.set(variableName, arrayNode);
                    }
                }
            }

            schema.set("properties", properties);

            String outputFilePath = "src/main/resources/schema.json";
            try (FileWriter fileWriter = new FileWriter(outputFilePath)) {
                fileWriter.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema));
            }


            System.out.println("OpenAPI schema generated and saved to " + outputFilePath);

        } catch (IOException e) {
            System.out.print("Error" +  e.getMessage());
        }
    }

    private ObjectNode gerPropertyNode(String variableType) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode propertyNode = mapper.createObjectNode();
        propertyNode.put("type", this.typeMapping.getOrDefault(variableType, ""));
        return propertyNode;
    }
}
