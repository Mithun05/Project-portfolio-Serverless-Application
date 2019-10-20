package com.sample.portfolio.mydemosite.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.stereotype.Component;

@Component
public class DynamoClient {

    private S3Details s3Details;
    private AmazonDynamoDB amazonDynamoDB;

    DynamoClient() {
        s3Details = new S3Details();
        AWSCredentials awsCredentials = new BasicAWSCredentials(this.s3Details.getAccessKey(), this.s3Details.getSecretKey());
        this.amazonDynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
    }

    public S3Details getS3Details() {
        return s3Details;
    }

    public void setS3Details(S3Details s3Details) {
        this.s3Details = s3Details;
    }

    public AmazonDynamoDB getAmazonDynamoDB() {
        return amazonDynamoDB;
    }

    public void setAmazonDynamoDB(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
    }
}
