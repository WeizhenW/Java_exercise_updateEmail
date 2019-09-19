package com.example.updateEmail;

import com.example.updateEmail.resource.UpdateEmailResource;
import com.example.updateEmail.utils.UpdateEmailAddressUtility;
import com.example.updateEmail.utils.UpdateEmailConstants;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

@SpringBootApplication
public class UpdateEmailApplication {

	/**
	 * Main: where it all begins. Accept program arguments and run program.
	 *
	 * Sample program argument (copy all quotes):
	 * "{\"user\":{\"userId\":\"1234\",\"emailAddress\":\"janedoe@domain.com\"}}"
	 *
	 * (userId 9999 is reserved for integration testing)
	 *
	 * @param args The program arguments
	 */
	public static void main(String[] args) {
		if (args == null || args.length != 1 || StringUtils.isEmpty(args[0])) {
			System.out.println("\nError: You must submit exactly one non-empty, non-null argument.");
		} else {
			System.out.println("\nupdateEmailAddress response: " + run(args[0]));
		}
	}

	/**
	 * Run the program
	 *
	 * @param arg The JSON argument to process
	 * @return The result of the service call
	 */
	static String run(String arg) {

		// Return 400 / generic invalid data message if arg is null or empty.
		// (Hint: use UpdateEmailAddressUtility.makeErrorMessageJsonString.)
		if (StringUtils.isEmpty(arg)) {
			return UpdateEmailAddressUtility.makeErrorMessageJsonString(UpdateEmailConstants.HTTP_STATUS_400,
					UpdateEmailConstants.GENERIC_INVALID_DATA_MESSAGE);
		}
			// Create an updateEmailResource object and pass in the arg to its doPost method.
		UpdateEmailResource updateEmailResource = new UpdateEmailResource();

			// Return whatever doPost returns.
		return updateEmailResource.doPost(arg);
	}

}