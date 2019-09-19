package com.example.updateEmail;

import com.example.updateEmail.utils.UpdateEmailConstants;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class UpdateEmailApplicationIntegrationTest {

    private String testEmail;
    private String arg;

    private final String DIRECTORY = "./database/";
    private final String EXTENSION = ".txt";

    @Test
    public void testDoPost_ValidJson_RecordExists() {
        String userId = "9999";
        testEmail = "testuser@domain.com";
        try {
            createTestFile(DIRECTORY, userId, EXTENSION, testEmail);
        } catch (FileNotFoundException fnfe) {
            Assert.fail(fnfe.getLocalizedMessage());
        }

		arg = "{\"user\":{\"userId\":\"" + userId + "\",\"emailAddress\":\"" + testEmail + "\"}}";
		String expected = "{\"httpStatus\":\"" + UpdateEmailConstants.HTTP_STATUS_200 + "\",\"message\":\""
                + UpdateEmailConstants.EMAIL_UPDATE_SUCCESS_MESSAGE + "\"}";
		Assert.assertEquals(UpdateEmailApplication.run(arg), expected);

		try {
            Assert.assertTrue(testEmailWasSaved(DIRECTORY, userId, EXTENSION, testEmail));
        } catch (IOException ioe) {
		    Assert.fail(ioe.getLocalizedMessage());
        }

        // Normally we'd delete the test file here, but Windows is giving me crap, so forget it.
    }

    @Test
    public void testDoPost_ValidJson_RecordDoesNotExist() {
        String fakeUserId = "9998";
        testEmail = "should.not@exist.com";
        if (fileExists(DIRECTORY, fakeUserId, EXTENSION)) {
            Assert.fail("Attempting to validate 404 on existing file.");
        }

        arg = "{\"user\":{\"userId\":\"" + fakeUserId + "\",\"emailAddress\":\"" + testEmail + "\"}}";
        String expected = "{\"httpStatus\":\"" + UpdateEmailConstants.HTTP_STATUS_404 + "\",\"message\":\"" +
                UpdateEmailConstants.USER_ID_ENTRY_NOT_FOUND + "\"}";
        Assert.assertEquals(UpdateEmailApplication.run(arg), expected);
    }

    private void createTestFile(String directory, String fileName, String extension, String testEmail)
            throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(directory + fileName + extension);
        writer.println(testEmail);
        writer.close();
    }

    private boolean fileExists(String directory, String fileName, String extension) {
        String fullFileName = directory + fileName + extension;
        File file = new File(fullFileName);
        return file.exists() && !file.isDirectory();
    }

    private boolean testEmailWasSaved(String directory, String fileName, String extension, String testEmail)
            throws IOException {
        String fullFileName = directory + fileName + extension;
        File file = new File(fullFileName);
        if(file.exists() && !file.isDirectory()) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String thisLine;
            while ((thisLine = bufferedReader.readLine()) != null) {
                if (thisLine.equalsIgnoreCase(testEmail)) {
                    return true;
                }
            }
        }
        return false;
    }
}