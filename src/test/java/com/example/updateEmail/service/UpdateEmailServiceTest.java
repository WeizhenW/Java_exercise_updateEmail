package com.example.updateEmail.service;

import com.example.updateEmail.dao.UpdateEmailDao;
import com.example.updateEmail.entities.EmailAddress;
import com.example.updateEmail.entities.User;
import com.example.updateEmail.entities.UserId;
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

public class UpdateEmailServiceTest {

    private UpdateEmailService updateEmailService;
    private UpdateEmailDao updateEmailDao;
    
    private final String EMAIL_ADDRESS = "janedoe@domain.com";

    @Before
    public void setup() {
        updateEmailService = new UpdateEmailService();
        updateEmailDao = mock(UpdateEmailDao.class);
    }

    @Test
    public void testProcessRequest_InvalidJson_InvalidUserId_WrongLength() {
        JsonNode userNode = createTestObjectNode("0123456789", EMAIL_ADDRESS);
        JsonNode result = updateEmailService.processRequest(userNode);
        Assert.assertEquals(result.toString(), "{\"httpStatus\":\"" + UpdateEmailConstants.HTTP_STATUS_400
                + "\",\"message\":\"" + UpdateEmailConstants.INVALID_USER_ID_MESSAGE + "\"}");
    }

    @Test
    public void testProcessRequest_InvalidJson_InvalidUserId_NonNumeric_SpecialChar() {
        JsonNode userNode = createTestObjectNode("#$%^", EMAIL_ADDRESS);
        JsonNode result = updateEmailService.processRequest(userNode);
        Assert.assertEquals(result.toString(), "{\"httpStatus\":\"" + UpdateEmailConstants.HTTP_STATUS_400
                + "\",\"message\":\"" + UpdateEmailConstants.INVALID_USER_ID_MESSAGE + "\"}");
    }

    @Test
    public void testProcessRequest_InvalidJson_InvalidUserId_NonNumeric_AlphaChars() {
        JsonNode userNode = createTestObjectNode("Asdf", EMAIL_ADDRESS);
        JsonNode result = updateEmailService.processRequest(userNode);
        Assert.assertEquals(result.toString(), "{\"httpStatus\":\"" + UpdateEmailConstants.HTTP_STATUS_400
                + "\",\"message\":\"" + UpdateEmailConstants.INVALID_USER_ID_MESSAGE + "\"}");
    }

    @Test
    public void testProcessRequest_InvalidJson_InvalidEmailAddress_InvalidTopLevelDomain() {
        JsonNode userNode = createTestObjectNode("1234", "janedoe@dance.off");
        JsonNode result = updateEmailService.processRequest(userNode);
        Assert.assertEquals(result.toString(), "{\"httpStatus\":\"" + UpdateEmailConstants.HTTP_STATUS_400
                + "\",\"message\":\"" + UpdateEmailConstants.INVALID_EMAIL_ADDRESS_MESSAGE + "\"}");
    }

    @Test
    public void testProcessRequest_NullJsonObject() {
        JsonNode result = updateEmailService.processRequest(null);
        Assert.assertEquals(result.toString(), "{\"httpStatus\":\"" + UpdateEmailConstants.HTTP_STATUS_500
                + "\",\"message\":\"" + UpdateEmailConstants.INTERNAL_SERVICE_ERROR_MESSAGE + "\"}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateUserId_InvalidUserId_Empty() {
        updateEmailService.validateUserId("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateUserId_InvalidUserId_Null() {
        updateEmailService.validateUserId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateUserId_InvalidUserId_WrongLength() {
        updateEmailService.validateUserId("0123456789");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateUserId_InvalidUserId_NonNumeric_SpecialChars() {
        updateEmailService.validateUserId("$^@*");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateUserId_InvalidUserId_NonNumeric_AlphaChars() {
        updateEmailService.validateUserId("Asdf");
    }

    @Test
//    public void testValidateEmailAddress_InvalidEmailAddress_InvalidTopLevelDomains() {
//        List<String> iValidTopLevelDomains = new ArrayList<>();
//
//        // These top level domains should really be read from a properties file somewhere, not hard-coded in the class.
//        iValidTopLevelDomains.add(".ru");
//        iValidTopLevelDomains.add(".co.uk");
//        iValidTopLevelDomains.add(".ca");
//        iValidTopLevelDomains.add(".nz");
//
//        String emailPrefix = "janedoe@domain";
//        String tempEmail = null;
//        for (String validDomain : iValidTopLevelDomains) {
//            tempEmail = emailPrefix + validDomain;
//            updateEmailService.validateEmailAddress(tempEmail);
//        }
//    }
    public void testValidateEmailAddress_InvalidEmailAddress_InvalidTopLevelDomains() {
        List<String> iValidTopLevelDomains = new ArrayList<>();

        // These top level domains should really be read from a properties file somewhere, not hard-coded in the class.
        iValidTopLevelDomains.add(".ru");
        iValidTopLevelDomains.add(".co.uk");
        iValidTopLevelDomains.add(".ca");
        iValidTopLevelDomains.add(".nz");

        String emailPrefix = "janedoe@domain";
        String tempEmail = null;
        boolean exceptionThrown = false;
        for (String validDomain : iValidTopLevelDomains) {
            tempEmail = emailPrefix + validDomain;
            try {
                updateEmailService.validateEmailAddress(tempEmail);
            } catch (IllegalArgumentException iae) {
                exceptionThrown = true;
            }
        }
        Assert.assertTrue(exceptionThrown);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testValidateEmailAddress_InvalidUserId_Empty() {
        updateEmailService.validateEmailAddress("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateEmailAddress_InvalidUserId_Null() {
        updateEmailService.validateEmailAddress(null);
    }

    @Test
    public void testCallDao() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode mockResult = objectMapper.createObjectNode();
        mockResult.put("httpStatus", UpdateEmailConstants.HTTP_STATUS_200);
        mockResult.put("Message", "The email address has been updated.");

        UserId userId = new UserId(1234);
        EmailAddress emailAddress = new EmailAddress("jane.doe@domain.com");
        User user = new User(userId, emailAddress);

        JsonNode jsonSubmit = objectMapper.valueToTree(user);

        when(updateEmailDao.processRequest(jsonSubmit)).thenReturn(mockResult);
        Assert.assertEquals(updateEmailDao.processRequest(jsonSubmit), mockResult);
        verify(updateEmailDao).processRequest(jsonSubmit);
    }

    private JsonNode createTestObjectNode(String userId, String emailAddress) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("emailAddress", emailAddress);
        ObjectNode objectNode = UpdateEmailAddressUtility.createObjectNode(map);
        return objectNode;
    }
}