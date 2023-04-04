# Send Money

This project is an example of API Gateway integration with OpenAPI and Lambda.

## Prerequisites
- Java 11+
- Apache Maven
- AWS CLI
- [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html)

## Building the project

    mvn clean package


## Deployment

    ./deploy.sh

## Integration tests
Open the FunctionsIT integration test and make sure that the static constant labeled AWS_GATEWAY_URL matches the URL 
displayed by the script deploy.sh. Then run the command:

    mvn test-compile failsafe:integration-test


