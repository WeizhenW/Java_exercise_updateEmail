package com.example.updateEmail.service;

import com.example.updateEmail.dao.UpdateEmailDao;
import com.example.updateEmail.utils.UpdateEmailAddressUtility;
import com.example.updateEmail.utils.UpdateEmailConstants;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.util.StringUtils;

import java.util.*;

public class UpdateEmailService {

    private final int USER_ID_MAX_LENGTH = 4;

    // These top level domains should really be read from a properties file somewhere, not hard-coded in the class:
    private final List<String> VALID_TOP_LEVEL_DOMAINS = new ArrayList<>(Arrays.asList(".com", ".net", ".gov", ".org"));

    /**
     * Process the service request
     *
     * @param user The JSON user node to process
     * @return The JSON result
     * @throws IllegalArgumentException If sent null user
     */
    public JsonNode processRequest(JsonNode user) throws IllegalArgumentException {
        if (user == null) {

            // Create a String-to-String map, pass in httpStatus --> UpdateEmailConstants.HTTP_STATUS_500
            // and message --> UpdateEmailConstants.INTERNAL_SERVICE_ERROR_MESSAGE
            // Return the call to UpdateEmailAddressUtility's createObjectNode method (with the map as the argument).
            Map<String, String> map = new HashMap<>();
            map.put("httpStatus", UpdateEmailConstants.HTTP_STATUS_500);
            map.put("message", UpdateEmailConstants.INTERNAL_SERVICE_ERROR_MESSAGE);
            return UpdateEmailAddressUtility.createObjectNode(map);
        }

        String userId = UpdateEmailAddressUtility.removeOuterDoubleQuotes(UpdateEmailAddressUtility.getJsonValue(user, "userId"));
        String emailAddress = UpdateEmailAddressUtility.removeOuterDoubleQuotes(UpdateEmailAddressUtility.getJsonValue(user, "emailAddress"));

        // Write a try/catch block to validate the user ID and validate the email address (one line for each method call)
        // catch the illegal argument exception, create a String-to-String map, add httpStatus -->
        // UpdateEmailConstants.HTTP_STATUS_400 and message --> the exceptions getMessage method,
        // and return the call to UpdateEmailAddressUtility's createObjectNode method (with the map as the argument).
        try{
            validateUserId(userId);
            validateEmailAddress(emailAddress);
        } catch(IllegalArgumentException error){
            Map<String, String > map = new HashMap<>();
            map.put("httpStatus", UpdateEmailConstants.HTTP_STATUS_400);
            map.put("message", error.getMessage());
            return UpdateEmailAddressUtility.createObjectNode(map);

        }

        return callDao(user);
    }

    /**
     * Validate a user ID
     *
     * @param userId The user ID to validate
     * @throws IllegalArgumentException if invalid user ID encountered
     */
    void validateUserId(String userId) throws IllegalArgumentException {

        // If userId is null or empty, throw an illegal argument exception with
        // UpdateEmailConstants.INVALID_USER_ID_MESSAGE as the argument.
        if(StringUtils.isEmpty(userId)) {
            throw new IllegalArgumentException(UpdateEmailConstants.INVALID_USER_ID_MESSAGE);
        }
        // If userId's length is not exactly USER_ID_MAX_LENGTH
        // throw an illegal argument exception with UpdateEmailConstants.INVALID_USER_ID_MESSAGE
        // as the argument.
        if(userId.length() != USER_ID_MAX_LENGTH) {
            throw new IllegalArgumentException(UpdateEmailConstants.INVALID_USER_ID_MESSAGE);
        }

        // Write a try/catch block and try to convert userId to an integer.
        // Catch the number format exception and throw a new illegal argument exception with
        // UpdateEmailConstants.INVALID_USER_ID_MESSAGE as the argument
        try{
            Integer.parseInt(userId);
        } catch(NumberFormatException error){
            throw new IllegalArgumentException(UpdateEmailConstants.INVALID_USER_ID_MESSAGE);
        }
    }

    /**
     * Validate an email address
     *
     * @param emailAddress The email address to validate
     * @throws IllegalArgumentException if an invalid email address is encountered
     */
    void validateEmailAddress(String emailAddress) throws IllegalArgumentException {

        // If the email address is null or empty
        // throw an illegal argument exception with UpdateEmailConstants.INVALID_EMAIL_ADDRESS_MESSAGE as the argument.
        if(StringUtils.isEmpty(emailAddress)) {
            throw new IllegalArgumentException(UpdateEmailConstants.INVALID_EMAIL_ADDRESS_MESSAGE);
        }
        // Get the top-level domain of the email address, e.g. user@domain.com --> .com.
        String topLevelDomain;
        topLevelDomain = emailAddress.substring(emailAddress.indexOf(".", emailAddress.indexOf("@")));
        System.out.println("top domain" + topLevelDomain);
        // Create a boolean and set it to false.
        boolean domainAllowed = false;
        // For each valid top-level domain in VALID_TOP_LEVEL_DOMAINS, if the top-level domain you found
        // is a match, set the boolean to true and break out of the loop.
        for(String domain: VALID_TOP_LEVEL_DOMAINS) {
            if(domain.equals(topLevelDomain)) {
                System.out.println("in top domain");
                domainAllowed = true;
                break;
            }
        }
        System.out.println("domain allowed = " + domainAllowed);
        if(!domainAllowed) {
            System.out.println("in exception");
            throw new IllegalArgumentException(UpdateEmailConstants.INVALID_EMAIL_ADDRESS_MESSAGE);
        }
        // If the boolean is false, throw a new illegal argument exception with
        // UpdateEmailConstants.INVALID_EMAIL_ADDRESS_MESSAGE as the argument.

    }

    private JsonNode callDao(JsonNode user) {
        UpdateEmailDao updateEmailDao = new UpdateEmailDao();
        return updateEmailDao.processRequest(user);
    }
}