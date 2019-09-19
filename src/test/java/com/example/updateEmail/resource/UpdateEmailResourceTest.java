package com.example.updateEmail.resource;

import com.example.updateEmail.entities.EmailAddress;
import com.example.updateEmail.entities.User;
import com.example.updateEmail.entities.UserId;
import com.example.updateEmail.service.UpdateEmailService;
import com.example.updateEmail.utils.UpdateEmailAddressUtility;
import com.example.updateEmail.utils.UpdateEmailConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class UpdateEmailResourceTest {

    private UpdateEmailResource updateEmailResource;
    private UpdateEmailService updateEmailService;

    @Before
    public void setup() {
        updateEmailResource = new UpdateEmailResource();
        updateEmailService = mock(UpdateEmailService.class);
    }

    @Test
    public void testDoPost_InvalidJson_Empty() {
        String expected = UpdateEmailAddressUtility.makeErrorMessageJsonString(UpdateEmailConstants.HTTP_STATUS_400,
                UpdateEmailConstants.GENERIC_INVALID_DATA_MESSAGE);
        Assert.assertEquals(updateEmailResource.doPost(""), expected);
    }

    @Test
    public void testDoPost_InvalidJson_Null() {
        String expected = UpdateEmailAddressUtility.makeErrorMessageJsonString(UpdateEmailConstants.HTTP_STATUS_400,
                UpdateEmailConstants.GENERIC_INVALID_DATA_MESSAGE);
        Assert.assertEquals(updateEmailResource.doPost(null), expected);
    }

    @Test
    public void testDoPost_InvalidJson_NonJSONStructure() {
        String expected = UpdateEmailAddressUtility.makeErrorMessageJsonString(UpdateEmailConstants.HTTP_STATUS_400,
                UpdateEmailConstants.GENERIC_INVALID_DATA_MESSAGE);
        Assert.assertEquals(updateEmailResource.doPost("Not a JSON structure."), expected);
    }

    @Test
    public void testDoPost_NullUserObject() {
        Map<String, String> map = new HashMap<>();
        map.put("keyA", "valueA");
        map.put("keyB", "valueB");
        ObjectNode objectNode = UpdateEmailAddressUtility.createObjectNode(map);
        String expected = UpdateEmailAddressUtility.makeErrorMessageJsonString(UpdateEmailConstants.HTTP_STATUS_400,
                UpdateEmailConstants.GENERIC_INVALID_USER_MESSAGE);
        Assert.assertEquals(updateEmailResource.doPost(objectNode.toString()), expected);
    }

    @Test
    public void testCallService() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode mockResult = objectMapper.createObjectNode();
        mockResult.put("httpStatus", UpdateEmailConstants.HTTP_STATUS_200);
        mockResult.put("Message", "The email address has been updated.");

        UserId userId = new UserId(1234);
        EmailAddress emailAddress = new EmailAddress("jane.doe@domain.com");
        User user = new User(userId, emailAddress);

        JsonNode jsonSubmit = objectMapper.valueToTree(user);

        when(updateEmailService.processRequest(jsonSubmit)).thenReturn(mockResult);
        Assert.assertEquals(updateEmailService.processRequest(jsonSubmit), mockResult);
        verify(updateEmailService).processRequest(jsonSubmit);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateUserIdInvalidUserId_Empty() {
        updateEmailResource.validateUserId("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateUserIdInvalidUserId_Null() {
        updateEmailResource.validateUserId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateUserIdInvalidUserId_NullString() {
        updateEmailResource.validateUserId("null");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateEmailAddress_InvalidEmailAddress() {
        updateEmailResource.validateEmailAddress("jane doe at domain dot com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateEmailAddress_EmptyEmailAddress() {
        updateEmailResource.validateEmailAddress("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateEmailAddress_NullEmailAddress() {
        updateEmailResource.validateEmailAddress(null);
    }

    @Test
    public void testValidateEmailAddress_ValidEmailAddress() {
        List<String> validEmailAddresses = new ArrayList<>();
        validEmailAddresses.add("janedoe@domain.com");
        validEmailAddresses.add("jim.smith@company.net");
        validEmailAddresses.add("abby.marie.johnson@showbiz.org");

        for (String validEmail : validEmailAddresses) {
            try {
                updateEmailResource.validateEmailAddress(validEmail);
            } catch (IllegalArgumentException iae) {
                Assert.fail("Should not have thrown an illegal argument exception for a valid email");
            }
        }
    }

}