package com.app.miniIns.cucumber.bdd;

import com.app.miniIns.services.AbstractS3;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class InMemoryS3Service implements AbstractS3 {


    @Override
    public URL upload(String bucketName, String s3Key, MultipartFile file) throws IOException {
        return new URL("http://www.google.com/"+s3Key);
    }

    @Override
    public URL getUrl(String bucket, String s3Key) throws MalformedURLException {
        return new URL("http://www.google.com/"+s3Key);
    }
}
