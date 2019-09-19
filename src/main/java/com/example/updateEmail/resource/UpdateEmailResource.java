package com.example.updateEmail.resource;

import com.example.updateEmail.service.UpdateEmailService;
import com.example.updateEmail.utils.UpdateEmailAddressUtility;
import com.example.updateEmail.utils.UpdateEmailConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateEmailResource {

    /**
     * Public API doPost method.
     *
     * @param jsonData The user's JSON structure
     * @return The return JSON structure
     */
    public String doPost(String jsonData) {
        if (StringUtils.isEmpty(jsonData)) {
            return UpdateEmailAddressUtility.makeErrorMessageJsonString(UpdateEmailConstants.HTTP_STATUS_400,
                    UpdateEmailConstants.GENERIC_INVALID_DATA_MESSAGE);
        }

        // Return a generic invalid data message if jsonData is null or empty.
        // (Hint: use UpdateEmailAddressUtility.makeErrorMessageJsonString.)

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readValue(jsonData, JsonNode.class);
        } catch (IOException ioe) {

            // Make a String-to-String map, pass in 400 httpStatus and generic invalid data message key/value pairs,
            Map<String, String> map = new HashMap<>();
            map.put("httpStatus", UpdateEmailConstants.HTTP_STATUS_400);
            map.put("message", UpdateEmailConstants.GENERIC_INVALID_DATA_MESSAGE);

            // and create / return an object node. (Hint: Use UpdateEmailAddressUtility.createObjectNode.)
            return UpdateEmailAddressUtility.createObjectNode(map).toString();
        }

        JsonNode user = jsonNode.get("user");
        if (user == null) {

            // Return a 400/generic invalid user message json String.
            // (Hint: use UpdateEmailAddressUtility.makeErrorMessageJsonString.)

            return UpdateEmailAddressUtility.makeErrorMessageJsonString(UpdateEmailConstants.HTTP_STATUS_400, UpdateEmailConstants.GENERIC_INVALID_USER_MESSAGE);
        }

        // Return a call to doPostHelper, pass in the user object.

        return doPostHelper(user);
    }

    /**
     * Calls the service.
     *
     * @param json The user's JSON structure
     * @return The service's returned JSON structure
     */
    private JsonNode callService(JsonNode json) {

        // Return the call to UpdateEmailService's processRequest method, passing in the json object.
        UpdateEmailService updateEmailService = new UpdateEmailService();
        return updateEmailService.processRequest(json);
    }

    /**
     *Asserts that the userId field is neither empty nor null.
     *
     * @param userId The user ID to validate
     * @throws IllegalArgumentException if an invalid user ID is encountered
     */
    void validateUserId(String userId) throws IllegalArgumentException {

        // If userId is empty or null or equals the literal string "null" (case insensitive),
        // throw an illegal argument exception with UpdateEmailConstants.INVALID_USER_ID_MESSAGE as the
        if(StringUtils.isEmpty(userId) || userId.equalsIgnoreCase("null")) {
            throw new IllegalArgumentException(UpdateEmailConstants.INVALID_USER_ID_MESSAGE);
        }

    }

    /**
     * Asserts that the email address is neither empty nor null,
     * and conforms to the standard email regex pattern.
     *
     * @param emailAddress The email address to validate
     * @throws IllegalArgumentException if an invalid email address is encountered
     */
    void validateEmailAddress(String emailAddress) throws IllegalArgumentException {

        // If email address is empty or null, throw an illegal argument exception with
        // UpdateEmailConstants.INVALID_EMAIL_ADDRESS_MESSAGE as the argument.
        if(emailAddress == null || emailAddress.isEmpty()) {
            throw new IllegalArgumentException(UpdateEmailConstants.INVALID_EMAIL_ADDRESS_MESSAGE);
        }

        // If the email does not match the regular expression pattern "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"
        // throw an illegal argument exception with UpdateEmailConstants.INVALID_EMAIL_ADDRESS_MESSAGE as the argument.
        String pattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(emailAddress);
        if(!m.matches()) {
            throw new IllegalArgumentException(UpdateEmailConstants.INVALID_EMAIL_ADDRESS_MESSAGE);
        }

    }

    private String doPostHelper(JsonNode user) {
        String userString = UpdateEmailAddressUtility.removeOuterDoubleQuotes(user.toString());
        if (userString.equalsIgnoreCase("null") || StringUtils.isEmpty(userString)) {
            return UpdateEmailAddressUtility.makeErrorMessageJsonString(UpdateEmailConstants.HTTP_STATUS_400,
                    UpdateEmailConstants.GENERIC_INVALID_USER_MESSAGE);
        }

        String userId = UpdateEmailAddressUtility.removeOuterDoubleQuotes(UpdateEmailAddressUtility.getJsonValue(user, "userId"));
        String emailAddress = UpdateEmailAddressUtility.removeOuterDoubleQuotes(UpdateEmailAddressUtility.getJsonValue(user,
                "emailAddress"));

        try {
            validateUserId(userId);
            validateEmailAddress(emailAddress);
        } catch (IllegalArgumentException iae) {
            return UpdateEmailAddressUtility.makeErrorMessageJsonString(UpdateEmailConstants.HTTP_STATUS_400, iae.getMessage());
        }

        JsonNode callServiceResponse = callService(user);
        if (StringUtils.isEmpty(callServiceResponse)) {
            return UpdateEmailAddressUtility.makeErrorMessageJsonString(UpdateEmailConstants.HTTP_STATUS_500,
                    "Nothing returned from the service call.");
        }
        return callServiceResponse.toString();
    }
}
