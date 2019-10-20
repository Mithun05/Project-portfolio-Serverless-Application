package com.sample.portfolio.mydemosite.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.stereotype.Component;

@Component
public class S3Client {

    private S3Details s3Details;
    private AmazonS3 amazonS3;

    S3Client() {
        s3Details = new S3Details();
        AWSCredentials credentials = new BasicAWSCredentials(this.s3Details.getAccessKey(), this.s3Details.getSecretKey());
        this.amazonS3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_1).build();
    }

    public AmazonS3 getAmazonS3() {
        return amazonS3;
    }

    public void setAmazonS3(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public S3Details getS3Details() {
        return s3Details;
    }

    public void setS3Details(S3Details s3Details) {
        this.s3Details = s3Details;
    }
}
