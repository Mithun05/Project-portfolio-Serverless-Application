package com.sample.portfolio.mydemosite.service;


import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.sample.portfolio.mydemosite.constants.ProjectBucketConstants;
import com.sample.portfolio.mydemosite.model.ProjectDetails;
import com.sample.portfolio.mydemosite.utils.DynamoClient;
import com.sample.portfolio.mydemosite.utils.S3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class ProjectBucketService {

    @Autowired
    S3Client s3Client;

    @Autowired
    DynamoClient dynamoClient;

    public String uploadProjectFile(MultipartFile multipartFile) {

        String fileUrl = "";
        try {

            File inputFile = new File(multipartFile.getOriginalFilename());
            FileOutputStream fileOutputStream = new FileOutputStream(inputFile);
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.close();

            s3Client.getAmazonS3().putObject(new PutObjectRequest(s3Client.getS3Details().getBucketName(), multipartFile.getOriginalFilename(), inputFile));
            fileUrl = s3Client.getAmazonS3().getUrl(s3Client.getS3Details().getBucketName(), multipartFile.getOriginalFilename()).toExternalForm();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }

    public String createProjectInDB(String bucketUrl) {
        ProjectDetails projectDetails = new ProjectDetails();
        int beginIndex = bucketUrl.indexOf(ProjectBucketConstants.s3UrlPart);
        int lastIndex = bucketUrl.lastIndexOf("/");
        System.out.println(beginIndex + " " + lastIndex);

        String bucketName = bucketUrl.substring(bucketUrl.indexOf("/") + 2, beginIndex - 1);
        String key = bucketUrl.substring(lastIndex + 1);
        System.out.println(bucketName + " " + key);

        S3Object s3Object = s3Client.getAmazonS3().getObject(new GetObjectRequest(bucketName, key));

        System.out.println(s3Object.getObjectMetadata().getContentType());
        System.out.println(s3Object.getObjectMetadata().getContentLength());

        BufferedReader reader = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()));

        String line = "";
        int i = 0;
        try {
                while ((line = reader.readLine()) != null) {
                    String prjArr[] = line.split("\\$");
                    System.out.println("Line" + Arrays.asList(prjArr));
                    projectDetails.setProjectId(Long.parseLong(prjArr[i++]));
                    projectDetails.setProjectName(prjArr[i++]);
                    projectDetails.setProjectDesc(prjArr[i++]);
                    projectDetails.setProjectType(prjArr[i++]);
                    projectDetails.setProjectStartDate(prjArr[i++]);
                    projectDetails.setProjectEndDate(prjArr[i++]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(projectDetails);
        Map<String, AttributeValue> map = new HashMap<>();
        map.put("projectId", new AttributeValue().withN(Long.toString(projectDetails.getProjectId())));
        map.put("projectName", new AttributeValue(projectDetails.getProjectName()));
        map.put("projectDesc", new AttributeValue(projectDetails.getProjectDesc()));
        map.put("projectType", new AttributeValue(projectDetails.getProjectType()));
        map.put("projectStartDate", new AttributeValue(projectDetails.getProjectStartDate()));
        map.put("projectEndDate", new AttributeValue(projectDetails.getProjectEndDate()));

        PutItemRequest request = new PutItemRequest();
        request.setTableName("ProjectData");
        request.setItem(map);
        try {
            this.dynamoClient.getAmazonDynamoDB().putItem(request);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return "successful";
    }

    public List<ProjectDetails> getProjectsInDB() {
        List<ProjectDetails> projectDetailsList = new ArrayList<>();

        ScanResult result = null;
        do {
            ScanRequest request = new ScanRequest();
            request.setTableName("ProjectData");

            if(result!=null) {
                request.setExclusiveStartKey(result.getLastEvaluatedKey());
            }

            result = this.dynamoClient.getAmazonDynamoDB().scan(request);

            List<Map<String, AttributeValue>> items = result.getItems();

            for(Map<String, AttributeValue> row : items) {
                ProjectDetails projectDetails = new ProjectDetails();
                AttributeValue value = row.get("projectId");
                projectDetails.setProjectId(Long.parseLong(value.getN()));
                value = row.get("projectName");
                projectDetails.setProjectName(value.getS());
                value = row.get("projectDesc");
                projectDetails.setProjectDesc(value.getS());
                value = row.get("projectType");
                projectDetails.setProjectType(value.getS());
                value = row.get("projectStartDate");
                projectDetails.setProjectStartDate(value.getS());
                value = row.get("projectEndDate");
                projectDetails.setProjectEndDate(value.getS());
                projectDetailsList.add(projectDetails);
            }

        } while (result.getLastEvaluatedKey() != null);

        return projectDetailsList;
    }

    public String updateProjectInDB(ProjectDetails projectDetails) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("projectId", new AttributeValue().withN(String.valueOf(projectDetails.getProjectId())));

        UpdateItemRequest updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setTableName("ProjectData");
        updateItemRequest.setKey(key);
        updateItemRequest.addAttributeUpdatesEntry("projectDesc", new AttributeValueUpdate().withValue(new AttributeValue().withS(projectDetails.getProjectDesc())));

        this.dynamoClient.getAmazonDynamoDB().updateItem(updateItemRequest);

        return "Successful";
    }

    public String deleteProjectInDB(String projectId) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("projectId", new AttributeValue().withN(projectId));

        DeleteItemRequest deleteItemRequest = new DeleteItemRequest();
        deleteItemRequest.setTableName("ProjectData");
        deleteItemRequest.setKey(key);

        this.dynamoClient.getAmazonDynamoDB().deleteItem(deleteItemRequest);

        return "Successful";
    }

}
