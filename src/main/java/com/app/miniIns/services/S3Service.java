package com.app.miniIns.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class S3Service implements AbstractS3{

    AmazonS3 s3client;

    public S3Service(@Value("${s3.admin.accessKey}") String accessKey,  @Value("${s3.admin.secretKey}") String secretKey) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
        s3client = AmazonS3ClientBuilder
                .standard() // returns a client builder
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    public List<Bucket> listBuckets() {
        return s3client.listBuckets();
    }

    public void deleteBucket(String bucket) throws AmazonServiceException{
        s3client.deleteBucket(bucket);
    }

    public URL upload(String bucketName, String s3Key, MultipartFile file) throws IOException {

        InputStream is = file.getInputStream();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getBytes().length);
        objectMetadata.setLastModified(new Date());

        PutObjectRequest putRequest = new PutObjectRequest(bucketName, s3Key, is, objectMetadata);

        s3client.putObject(putRequest);
        return s3client.getUrl(bucketName, s3Key);
    }


    public List<String> listObjects(String bucket) {
        ObjectListing objectList = s3client.listObjects(bucket);

        List<String> res = new ArrayList<>();
        for (S3ObjectSummary os: objectList.getObjectSummaries()) {
            res.add(os.getKey());
        }
        return res;
    }

    public void download(String bucket, String key,  String filename) throws IOException {
        S3Object o =s3client.getObject(bucket, key);
        S3ObjectInputStream inputStream = o.getObjectContent();
        FileUtils.copyInputStreamToFile(inputStream, new File(filename));//rename the downloaded file
    }

    public void copyObject(String fromBucket, String fromS3Key, String toBucket, String toKey) {
        s3client.copyObject(fromBucket, fromS3Key, toBucket, toKey);

    }
    public void deleteObject(String  bucket, String s3key) {
        s3client.deleteObject(bucket, s3key);
    }

    public void moveObject(String fromBucket, String fromS3Key, String toBucket, String toKey) {
        copyObject(fromBucket, fromS3Key, toBucket, toKey);
        deleteObject(fromBucket, fromS3Key);
    }

    public void batchDelete(String[] targets, String bucket) {
        DeleteObjectsRequest delObjReq = new DeleteObjectsRequest(bucket)
                .withKeys(targets);
        s3client.deleteObjects(delObjReq);
    }

    public URL getUrl(String bucket, String s3Key) {
        return s3client.getUrl(bucket, s3Key);
    }
}
