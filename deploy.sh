#!/bin/bash
RANDOM=$$
BUCKET_NAME=bucketname-$RANDOM
STAGE_NAME=dev
AWS_REGION=$(aws configure list | grep region | awk '{print $2}')
aws s3 mb s3://$BUCKET_NAME
echo $BUCKET_NAME > bucket-name.txt
aws s3 cp open-api.yaml s3://$BUCKET_NAME/openapi.yaml
sam deploy --s3-bucket $BUCKET_NAME --stack-name money-transfer-stack --capabilities CAPABILITY_IAM --parameter-overrides BucketName=$BUCKET_NAME
aws cloudformation wait stack-create-complete --stack-name money-transfer-stack
API_ID=$(aws apigateway get-rest-apis --query "items[?name=='send-money-api'].id" --output text)
aws apigateway create-deployment --rest-api-id $API_ID --stage-name $STAGE_NAME >/dev/null 2>&1
echo "Your API with ID $API_ID is deployed and ready to be tested at https://$API_ID.execute-api.$AWS_REGION.amazonaws.com/$STAGE_NAME"
