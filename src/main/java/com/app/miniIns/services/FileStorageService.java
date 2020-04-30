package com.app.miniIns.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public interface FileStorageService {

    public abstract URL upload(String s3Key, MultipartFile file) throws IOException;

    public abstract URL getUrl(String s3Key) throws MalformedURLException;

}
