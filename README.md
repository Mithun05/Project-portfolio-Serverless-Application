# Project-portfolio-Serverless-Application

This sample project is serverless application developed using AWS API gateway, lambda, EC2, S3, DynamoDB.
Utilized the AWS SDK in Java.

Architecture:
--------------------------------------------

1) The UI is hosted on S3. (http://project-workflow-ui-code.s3-website-us-east-1.amazonaws.com)
2) Once you click on 'My Projects' ----> It calls the API Gateway API call ---> In turns invoke Lambda function -----> In turns invoke backend services hosted on EC2 ----> In turns return JSON response
3) Written REST API's CRUD operations using Spring boot and Gradle
4) Use DynamoDB to store the data 

REST APIS AVAILABLE
--------------------------------------

1) Upload project description text file ----> uploads to S3  -----> returns the file url 
2) Add the project description ---------> using the file url got in the first step  -----> downloads the file ------------> add to the database
3) Get the project descriptions
4) Update the project descriptions
5) Delete the project descriptons
