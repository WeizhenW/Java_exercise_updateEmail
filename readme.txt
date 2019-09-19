This is a toy email update program.

The idea is to submit a json string to the program like so: 

{"user":{"userId":"1234","emailAddress":"janedoe@domain.com"}}

A user has a userId and emailAddress, and userId and emailAddress each have String values.

The system looks in the ./database/ directory for a file that has the userId's name, opens it if found, and replaces whatever value is there with the submitted email address.

Bad JSON sent = 400. No file found = 404. System error = 500. All of these have been tested.

All the test cases have been written, but the implementation of 4 classes -- UpdateEmailApplication, UpdateEmailResource, UpdateEmailService, and UpdateEmailDao -- have been partially emptied out and replaced with hints and suggestions. Most of the tests will fail. The goal is to write code to get each test, one at a time, to pass. 

The workflow goes like this:

DOWN THE STACK: 

JSON String --> UpdateEmailApplication (light validation) --> UpdateEmailResource (validate, pass on) --> UpdateEmailService (validate, pass on) --> UpdateEmailDao (update "database")

BACK UP THE STACK:

UpdateEmailDao (creates json response object) --> UpdateEmailService (passes up the json object) --> UpdateEmailResource (passes up the json object) --> UpdateEmailApplication (returns the final json object as a string to the user.

===

Sample program arguments and their expected json response strings: 

"{\"user\":{\"userId\":\"1234\",\"emailAddress\":\"janedoe@domain.com\"}}"

updateEmailAddress response: {"httpStatus":"200","message":"Email address successfully updated."}

"{\"user\":{\"userId\":\"0000\",\"emailAddress\":\"janedoe@domain.com\"}}"

updateEmailAddress response: {"httpStatus":"404","message":"No database entry found for given user ID."}

"{\"user\":{\"userId\":\"$$$$\",\"emailAddress\":\"queen@king.com\"}}"

updateEmailAddress response: {"httpStatus":"400","message":"Invalid user ID received. Please see usage specifications."}

"{\"user\":{\"userId\":\"1234\",\"emailAddress\":\"not an email\"}}"

updateEmailAddress response: {"httpStatus":"400","message":"Invalid email address received. Please see usage specifications."}

There's also a 500 response for system errors.