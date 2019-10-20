package com.sample.portfolio.mydemosite.controller;

import com.sample.portfolio.mydemosite.model.ProjectDetails;
import com.sample.portfolio.mydemosite.service.ProjectBucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectBucketController {

    @Autowired
    ProjectBucketService projectBucketService;

    @PostMapping("/upload")
    public String uploadProjectFileToS3(@RequestPart(value="file") MultipartFile file) {
        return this.projectBucketService.uploadProjectFile(file);
    }

    @PostMapping("/create")
    public String createProjectInDynamoDB(@RequestPart(value="url") String bucketUrl) {
        return this.projectBucketService.createProjectInDB(bucketUrl);
    }

    @GetMapping("/get")
    public List<ProjectDetails> getProjectsInDynamoDB() {
        return this.projectBucketService.getProjectsInDB();
    }

    @PostMapping("/update")
    public String updateProjectInDynamoDB(@RequestBody ProjectDetails projectDetails) {
        return this.projectBucketService.updateProjectInDB(projectDetails);
    }

    @DeleteMapping("/delete/{projectid}")
    public String deleteProjectInDynamoDB(@PathVariable(value = "projectid") String projectId) {
        return this.projectBucketService.deleteProjectInDB(projectId);
    }
}
