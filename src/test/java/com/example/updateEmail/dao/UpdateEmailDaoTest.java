package com.example.updateEmail.dao;

import com.example.updateEmail.utils.UpdateEmailConstants;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Test;

public class UpdateEmailDaoTest {

    @Test
    public void testProcessRequest_NullJsonObject() {
        UpdateEmailDao updateEmailDao = new UpdateEmailDao();
        JsonNode result = updateEmailDao.processRequest(null);
        Assert.assertEquals(result.toString(), "{\"httpStatus\":\"" + UpdateEmailConstants.HTTP_STATUS_500
                + "\",\"message\":\"" + UpdateEmailConstants.INTERNAL_SERVICE_ERROR_MESSAGE + "\"}");
    }
}