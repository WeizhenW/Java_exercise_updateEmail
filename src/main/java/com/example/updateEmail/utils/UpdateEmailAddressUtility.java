package com.example.updateEmail.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UpdateEmailAddressUtility {

    /**
     * Create an ObjectNode from a String/String map
     *
     * @param map The map to process
     * @return The generated ObjectNode
     */
    public static ObjectNode createObjectNode(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            objectNode.put((String)pair.getKey(), (String)pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        return objectNode;
    }

    /**
     * Remove outer double quotes, e.g. ""my string"" --> "my string"
     *
     * @param string The string to process
     * @return The processed string
     */
    public static String removeOuterDoubleQuotes(String string) {
        if (string == null || StringUtils.isEmpty(string)) {
            return null;
        }
        return string.replaceAll("^\"|\"$", "");
    }

    /**
     * Make an error message JSON string, e.g. "{"httpStatus":"200", "message":"Email updated"}"
     *
     * @param httpStatus The HTTP status
     * @param errorMessage The error message
     * @return The generated JSON string
     */
    public static String makeErrorMessageJsonString(String httpStatus, String errorMessage) {
        if (StringUtils.isEmpty(httpStatus) || StringUtils.isEmpty(errorMessage)) {
            return null;
        }
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("httpStatus", httpStatus);
        returnMap.put("message", errorMessage);
        return UpdateEmailAddressUtility.createObjectNode(returnMap).toString();
    }

    /**
     * Get the value for a given JSON key.
     *
     * @param node The JSON node to parse
     * @param key The JSON key to use
     * @return The value for the given JSON key
     */
    public static String getJsonValue(JsonNode node, String key) {
        if (node == null || StringUtils.isEmpty(key)) {
            return null;
        }
        return node.get(key) == null ? null : node.get(key).toString();
    }
}