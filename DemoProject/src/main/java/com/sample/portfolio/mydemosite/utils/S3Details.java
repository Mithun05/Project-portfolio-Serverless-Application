package com.sample.portfolio.mydemosite.utils;

import com.sample.portfolio.mydemosite.constants.ProjectBucketConstants;

public class S3Details {

    private String bucketName;
    private String accessKey;
    private String secretKey;

    S3Details() {
        this.bucketName = ProjectBucketConstants.bucketName;
        this.accessKey = ProjectBucketConstants.accessKeyId;
        this.secretKey = ProjectBucketConstants.secretAccessKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
