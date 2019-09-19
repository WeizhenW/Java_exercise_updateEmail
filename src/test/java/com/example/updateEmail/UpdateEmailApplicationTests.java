package com.example.updateEmail;

import com.example.updateEmail.utils.UpdateEmailConstants;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.example.updateEmail.utils.UpdateEmailConstants.GENERIC_INVALID_DATA_MESSAGE;

public class UpdateEmailApplicationTests {
	
	private String arg;
	
	private final String GENERIC_INVALID_DATA_MESSAGE_JSON_FORMAT = "{\"httpStatus\":\"" + UpdateEmailConstants.HTTP_STATUS_400
			+ "\",\"message\":\"" + GENERIC_INVALID_DATA_MESSAGE + "\"}";

	private final String INVALID_EMAIL_ADDRESS_MESSAGE_JSON_FORMAT = "{\"httpStatus\":\"" + UpdateEmailConstants.HTTP_STATUS_400
			+ "\",\"message\":\"" + UpdateEmailConstants.INVALID_EMAIL_ADDRESS_MESSAGE + "\"}";

	private final String INVALID_USER_ID_MESSAGE_JSON_FORMAT = "{\"httpStatus\":\"" + UpdateEmailConstants.HTTP_STATUS_400
			+ "\",\"message\":\"" + UpdateEmailConstants.INVALID_USER_ID_MESSAGE + "\"}";

	private final String GENERIC_INVALID_USER_MESSAGE_JSON_FORMAT = "{\"httpStatus\":\"" + UpdateEmailConstants.HTTP_STATUS_400
			+ "\",\"message\":\"" + UpdateEmailConstants.GENERIC_INVALID_USER_MESSAGE + "\"}";

	private final String EMAIL_UPDATE_SUCCESS_MESSAGE_JSON_FORMAT = "{\"httpStatus\":\"" + UpdateEmailConstants.HTTP_STATUS_200 + "\",\"message\":\""
			+ UpdateEmailConstants.EMAIL_UPDATE_SUCCESS_MESSAGE + "\"}";

	/*
	 * (Happy-path testing for this class is handled in UpdateEmailApplicationIntegrationTest.)
	 */
	
	/*
	 * Invalid JSON arguments
	 */

	@Test
	public void testRun_InvalidJson_NullArg() {
		Assert.assertEquals(UpdateEmailApplication.run(null), GENERIC_INVALID_DATA_MESSAGE_JSON_FORMAT);
	}

	@Test
	public void testRun_InvalidJson_EmptyArg() {
		Assert.assertEquals(UpdateEmailApplication.run(""), GENERIC_INVALID_DATA_MESSAGE_JSON_FORMAT);
	}

	@Test
	public void testRun_InvalidJson_EmptyJson() {
		Assert.assertEquals(UpdateEmailApplication.run("{}"), GENERIC_INVALID_USER_MESSAGE_JSON_FORMAT);
	}

	/*
	 * Invalid user arguments
	 */

	@Test
	public void testRun_InvalidJson_NoUser() {
		arg = "{\"user\":}";
		Assert.assertEquals(UpdateEmailApplication.run(arg), GENERIC_INVALID_DATA_MESSAGE_JSON_FORMAT);
	}

	@Test
	public void testRun_InvalidJson_EmptyUser() {
		arg = "{\"user\":\"\"}";
		Assert.assertEquals(UpdateEmailApplication.run(arg), GENERIC_INVALID_USER_MESSAGE_JSON_FORMAT);
	}

	@Test
	public void testRun_InvalidJson_NullUser() {
		arg = "{\"user\":null}";
		Assert.assertEquals(UpdateEmailApplication.run(arg), GENERIC_INVALID_USER_MESSAGE_JSON_FORMAT);
	}

	/*
	 * Invalid user ID arguments
	 */

	@Test
	public void testRun_InvalidUserId_Missing() {
		arg = "{\"user\":{\"emailAddress\":\"janedoe@domain.com\"}}";
		Assert.assertEquals(UpdateEmailApplication.run(arg), INVALID_USER_ID_MESSAGE_JSON_FORMAT);
	}

	@Test
	public void testRun_InvalidUserId_MissingValue() {
		arg = "{\"user\":{\"userId\":,\"emailAddress\":\"janedoe@domain.com\"}}";
		Assert.assertEquals(UpdateEmailApplication.run(arg), GENERIC_INVALID_DATA_MESSAGE_JSON_FORMAT);
	}

	@Test
	public void testRun_InvalidUserId_NullValue() {
		arg = "{\"user\":{\"userId\":null,\"emailAddress\":\"janedoe@domain.com\"}}";
		Assert.assertEquals(UpdateEmailApplication.run(arg), INVALID_USER_ID_MESSAGE_JSON_FORMAT);
	}

	@Test
	public void testRun_InvalidUserId_EmptyValue() {
		arg = "{\"user\":{\"userId\":\"\",\"emailAddress\":\"janedoe@domain.com\"}}";
		Assert.assertEquals(UpdateEmailApplication.run(arg), INVALID_USER_ID_MESSAGE_JSON_FORMAT);
	}

	@Test
	public void testRun_InvalidUserId_WrongLength() {
		arg = "{\"user\":{\"userId\":\"0123456789\",\"emailAddress\":\"janedoe@domain.com\"}}";
		Assert.assertEquals(UpdateEmailApplication.run(arg), INVALID_USER_ID_MESSAGE_JSON_FORMAT);
	}

	@Test
	public void testRun_InvalidUserId_SpecialChars() {
		arg = "{\"user\":{\"userId\":\"#$%^\",\"emailAddress\":\"janedoe@domain.com\"}}";
		String returnedMessage = UpdateEmailApplication.run(arg);
		Assert.assertEquals(UpdateEmailApplication.run(arg), INVALID_USER_ID_MESSAGE_JSON_FORMAT);
	}

	@Test
	public void testRun_InvalidUserId_AlphaChars() {
		arg = "{\"user\":{\"userId\":\"asdf\",\"emailAddress\":\"janedoe@domain.com\"}}";
		Assert.assertEquals(UpdateEmailApplication.run(arg), INVALID_USER_ID_MESSAGE_JSON_FORMAT);
	}

	/*
	 * Invalid email address arguments
	 */

	@Test
	public void testRun_InvalidEmailAddress_Missing() {
		arg = "{\"user\":{\"userId\":\"1234\"}}";
		Assert.assertEquals(UpdateEmailApplication.run(arg), INVALID_EMAIL_ADDRESS_MESSAGE_JSON_FORMAT);
	}

	@Test
	public void testRun_InvalidEmailAddress_MissingValue() {
		arg = "{\"user\":{\"userId\":\"1234\",\"emailAddress\":}}";
		Assert.assertEquals(UpdateEmailApplication.run(arg), GENERIC_INVALID_DATA_MESSAGE_JSON_FORMAT);
	}

	@Test
	public void testRun_InvalidEmailAddress_EmptyValue() {
		arg = "{\"user\":{\"userId\":\"1234\",\"emailAddress\":\"\"}}";
		Assert.assertEquals(UpdateEmailApplication.run(arg), INVALID_EMAIL_ADDRESS_MESSAGE_JSON_FORMAT);
	}

	@Test
	public void testRun_InvalidEmailAddress_NullValue() {
		arg = "{\"user\":{\"userId\":\"1234\",\"emailAddress\":null}}";
		Assert.assertEquals(UpdateEmailApplication.run(arg), INVALID_EMAIL_ADDRESS_MESSAGE_JSON_FORMAT);
	}

	@Test
	public void testRun_InvalidEmailAddress_InvalidPattern() {
		arg = "{\"user\":{\"userId\":\"9876\",\"emailAddress\":\"jane doe at domain dot com\"}}";
		Assert.assertEquals(UpdateEmailApplication.run(arg), INVALID_EMAIL_ADDRESS_MESSAGE_JSON_FORMAT);
	}

	@Test
	public void testRun_InvalidEmailAddress_IllegalTopLevelDomain() {
		arg = "{\"user\":{\"userId\":\"9876\",\"emailAddress\":\"janedoe@domain.biz\"}}";
		Assert.assertEquals(UpdateEmailApplication.run(arg), INVALID_EMAIL_ADDRESS_MESSAGE_JSON_FORMAT);
	}

	@Test
	public void testValidateEmailAddress_InvalidEmailAddress_ValidDomains() {
		List<String> validTopLevelDomains = new ArrayList<>();
		// These top level domains should really be read from a properties file somewhere, not hard-coded in the class.
		validTopLevelDomains.add(".com");
		validTopLevelDomains.add(".net");
		validTopLevelDomains.add(".gov");
		validTopLevelDomains.add(".org");

		String emailPrefix = "janedoe@domain";
		String tempEmail = null;
		for (String validDomain : validTopLevelDomains) {
			tempEmail = emailPrefix + validDomain;
			arg = "{\"user\":{\"userId\":\"9876\",\"emailAddress\":\"" + tempEmail + "\"}}";
			Assert.assertNotEquals(UpdateEmailApplication.run(arg), INVALID_EMAIL_ADDRESS_MESSAGE_JSON_FORMAT);
		}
	}

	@Test
	public void testValidateEmailAddress_InvalidEmailAddress_InvalidTopLevelDomains() {
		List<String> iValidTopLevelDomains = new ArrayList<>();
		// These top level domains should really be read from a properties file somewhere, not hard-coded in the class.
		iValidTopLevelDomains.add(".ru");
		iValidTopLevelDomains.add(".co.uk");
		iValidTopLevelDomains.add(".ca");
		iValidTopLevelDomains.add(".nz");

		String emailPrefix = "janedoe@domain";
		String tempEmail = null;
		for (String validDomain : iValidTopLevelDomains) {
			tempEmail = emailPrefix + validDomain;
			arg = "{\"user\":{\"userId\":\"9876\",\"emailAddress\":\"" + tempEmail + "\"}}";
			Assert.assertEquals(UpdateEmailApplication.run(arg), INVALID_EMAIL_ADDRESS_MESSAGE_JSON_FORMAT);
		}
	}
}