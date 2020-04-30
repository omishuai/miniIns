package com.app.miniIns.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public interface AbstractS3 {

    public abstract URL upload(String bucketName, String s3Key, MultipartFile file) throws IOException;

    public abstract URL getUrl(String bucket, String s3Key) throws MalformedURLException;

}
