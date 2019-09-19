package com.example.updateEmail.dao;

import com.example.updateEmail.utils.UpdateEmailAddressUtility;
import com.example.updateEmail.utils.UpdateEmailConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.security.UnresolvedPermission;
import java.util.HashMap;
import java.util.Map;

public class UpdateEmailDao {

    /**
     * Process the JSON request from the service layer
     *
     * @param user The user to process
     * @return the result of the update
     */
    public JsonNode processRequest(JsonNode user) {

        // If the user node is null
        // return a 500 / internal service error message json node.
        // (Hint: use createReturnMessage and variables from UpdateEmailConstants.)
        if(user == null) {
            return createReturnMessage(UpdateEmailConstants.HTTP_STATUS_500, UpdateEmailConstants.INTERNAL_SERVICE_ERROR_MESSAGE);
        }
        String userId = UpdateEmailAddressUtility.removeOuterDoubleQuotes(UpdateEmailAddressUtility.getJsonValue(user, "userId"));
        String emailAddress = UpdateEmailAddressUtility.removeOuterDoubleQuotes(UpdateEmailAddressUtility.getJsonValue(user,
                "emailAddress"));

        // Write a try/catch block to run updateEmailAddressFile(userId, emailAddress);
        // The block should have two catch statements: one for an illegal argument exception, which returns
        // UpdateEmailConstants.HTTP_STATUS_404 and the exception's getMessage method,
        // and one for an IO exception, which returns UpdateEmailConstants.HTTP_STATUS_500 and the exception's
        // getMessage method.

        // Return the call to createReturnMessage, passing in UpdateEmailConstants.HTTP_STATUS_200 and
        // UpdateEmailConstants.EMAIL_UPDATE_SUCCESS_MESSAGE as the arguments.
        try{
            updateEmailAddressFile(userId, emailAddress);
        } catch(IllegalArgumentException error){
            return createReturnMessage(UpdateEmailConstants.HTTP_STATUS_404, error.getMessage());
        } catch (IOException error){
            return createReturnMessage(UpdateEmailConstants.HTTP_STATUS_500, error.getMessage());
        }
        return createReturnMessage(UpdateEmailConstants.HTTP_STATUS_200, UpdateEmailConstants.EMAIL_UPDATE_SUCCESS_MESSAGE);
    }

    /**
     * Update the email address to the "database" LOL
     *
     * @param fileName The userId file name
     * @param emailAddress The email address to update
     * @throws IllegalArgumentException if user ID file not found
     * @throws IOException if file I/O error occurs
     */
    private void updateEmailAddressFile(String fileName, String emailAddress) throws IllegalArgumentException, IOException {
        String fileNameWithPathAndExtension = "./database/" + fileName + ".txt";
        File file = new File(fileNameWithPathAndExtension);
        if(!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException(UpdateEmailConstants.USER_ID_ENTRY_NOT_FOUND);
        } else {
            File fileContents = new File(fileNameWithPathAndExtension);
            try {
                FileWriter fileWriter = new FileWriter(fileContents, false);
                fileWriter.write(emailAddress);
                fileWriter.close();
            } catch (IOException ioe) {
                throw new IOException(ioe.getLocalizedMessage());
            }
        }
    }

    private ObjectNode createReturnMessage(String httpStatus, String message) {
        Map<String, String> map = new HashMap<>();
        map.put("httpStatus", httpStatus);
        map.put("message", message);
        return UpdateEmailAddressUtility.createObjectNode(map);
    }
}