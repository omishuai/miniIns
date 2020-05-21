package com.app.miniIns.cucumber.bdd;

import com.app.miniIns.services.services.FileStorageService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
@Profile("test")
public class InMemoryS3Service implements FileStorageService {

    public URL upload(String s3Key, MultipartFile file) throws IOException {
        return new URL("http://www.google.com/"+s3Key);
    }

    public URL getUrl(String s3Key) throws MalformedURLException {
        return new URL("http://www.google.com/"+s3Key);
    }


}
