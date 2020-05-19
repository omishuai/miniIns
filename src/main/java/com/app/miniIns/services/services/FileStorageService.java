package com.app.miniIns.services.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public interface FileStorageService {

    URL upload(String s3Key, MultipartFile file) throws IOException;

    URL getUrl(String s3Key) throws MalformedURLException;

}
