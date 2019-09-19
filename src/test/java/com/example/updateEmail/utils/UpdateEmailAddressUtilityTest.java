package com.example.updateEmail.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UpdateEmailAddressUtilityTest {

    @Test
    public void testCreateObjectNode_ValidMap() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode expectedNode = objectMapper.createObjectNode();
        expectedNode.put("key0", "value0");
        expectedNode.put("key1", "value1");
        expectedNode.put("key2", "value2");

        Map<String, String> map = new HashMap<>();
        map.put("key0", "value0");
        map.put("key1", "value1");
        map.put("key2", "value2");

        Assert.assertEquals(UpdateEmailAddressUtility.createObjectNode(map), expectedNode);
    }

    @Test
    public void testRemoveOuterDoubleQuotes() {
        String withDoubleQuotes = "\"This is a test string.\"";
        String expected = "This is a test string.";
        Assert.assertEquals(UpdateEmailAddressUtility.removeOuterDoubleQuotes(withDoubleQuotes), expected);
    }

    @Test
    public void testMakeErrorMessageJsonString_ValidInputs() {
        final String httpStatus = "404";
        final String errorMessage = "Resource not found";
        String expected = "{\"httpStatus\":\"" + httpStatus + "\",\"message\":\"" + errorMessage + "\"}";
        Assert.assertEquals(UpdateEmailAddressUtility.makeErrorMessageJsonString(httpStatus, errorMessage), expected);
    }

    @Test
    public void testGetJsonValue_ValidData() {
        String userId = "9876";
        String emailAddress = "janedoe@domain.com";
        String jsonData = "{\"user\":{\"userId\":\"" + userId + "\",\"emailAddress\":\"" + emailAddress + "\"}}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readValue(jsonData, JsonNode.class);
        } catch (IOException ioe) {
            Assert.fail("IOException thrown.");
        }
        JsonNode user = jsonNode.get("user");
        if (user == null) {
            Assert.fail("Null user node encountered.");
        }
        Assert.assertEquals(UpdateEmailAddressUtility.removeOuterDoubleQuotes(UpdateEmailAddressUtility.getJsonValue(user,
                "userId")), userId);
        Assert.assertEquals(UpdateEmailAddressUtility.removeOuterDoubleQuotes(UpdateEmailAddressUtility.getJsonValue(user,
                "emailAddress")), emailAddress);
    }

    @Test
    public void testGetJsonValue_InvalidData_NullNode() {
        Assert.assertNull(UpdateEmailAddressUtility.getJsonValue(null, "fake JSON"));
    }

    @Test
    public void testGetJsonValue_InvalidData_NullNodeDotGet() {
        JsonNode jsonNode = makeDumbJsonNode();
        Assert.assertNull(UpdateEmailAddressUtility.getJsonValue(jsonNode, "no such key"));
    }

    @Test
    public void testGetJsonValue_InvalidData_NullKey() {
        JsonNode jsonNode = makeDumbJsonNode();
        Assert.assertNull(UpdateEmailAddressUtility.getJsonValue(jsonNode, null));
    }

    private JsonNode makeDumbJsonNode() {
        Map<String, String> map = new HashMap<>();
        map.put("keyA", "valueA");
        map.put("keyB", "valueB");
        map.put("keyC", "valueC");
        return UpdateEmailAddressUtility.createObjectNode(map);
    }

    @Test
    public void testGetJsonValue_InvalidData_EmptyKey() {

    }
}