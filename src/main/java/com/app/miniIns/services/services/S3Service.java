package com.app.miniIns.services.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;


@Service
@Profile("!test")
public class S3Service implements FileStorageService {

    private String bucketName = "miniins-bucket";
    private AmazonS3 s3client;

    public S3Service(@Value("${s3.admin.accessKey}") String accessKey,  @Value("${s3.admin.secretKey}") String secretKey) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
        s3client = AmazonS3ClientBuilder
                .standard() // returns a client builder
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    public URL upload(String s3Key, MultipartFile file) throws IOException {

        InputStream is = file.getInputStream();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getBytes().length);
        objectMetadata.setLastModified(new Date());

        PutObjectRequest putRequest = new PutObjectRequest(bucketName, s3Key, is, objectMetadata);

        s3client.putObject(putRequest);
        return s3client.getUrl(bucketName, s3Key);
    }


    public void download(String key,  String filename) throws IOException {
        S3Object o =s3client.getObject(bucketName, key);
        S3ObjectInputStream inputStream = o.getObjectContent();
        FileUtils.copyInputStreamToFile(inputStream, new File(filename));//rename the downloaded file
    }

    public void deleteObject(String s3key) {
        s3client.deleteObject(bucketName, s3key);
    }

    public void batchDelete(String[] targets) {
        DeleteObjectsRequest delObjReq = new DeleteObjectsRequest(bucketName)
                .withKeys(targets);
        s3client.deleteObjects(delObjReq);
    }

    public URL getUrl(String s3Key) {
        return s3client.getUrl(bucketName, s3Key);
    }
}
