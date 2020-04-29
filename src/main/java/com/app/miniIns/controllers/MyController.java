package com.app.miniIns.controllers;

import com.app.miniIns.entities.*;
import com.app.miniIns.services.PhotoService;
import com.app.miniIns.services.S3Service;
import com.app.miniIns.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URL;

@Controller
public class MyController {

    public UserService getUserService() {
        return userService;
    }
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    public PhotoService getPhotoService() {
        return photoService;
    }
    public void setPhotoService(PhotoService photoService) {
        this.photoService = photoService;
    }
    public S3Service getS3Service() { return s3Service; }
    public void setS3Service(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @Autowired
    private PhotoService photoService;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private UserService userService;

    @PostMapping(path = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ClientUser register(@RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("age") int age,
                               @RequestParam("gender") String gender) throws Exception {

        ServerUser res = userService.addUser(new ServerUser(username, email, password, age, gender));
        return new ClientUser(res.getUsername(), res.getEmail(), res.getAge(), res.getGender());
    }

    //Bucket is creatd by root and assumed to exist
    @PostMapping(path="/upload")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ClientPhoto uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();

        String u = (String) auth.getPrincipal();
        ServerUser user = userService.findByUsername(u);
        Photo photo = new Photo(user, "miniins-bucket", file.getOriginalFilename());

        URL url =  s3Service.upload(photo.getS3_bucket(), photo.getId().toString(), file);
        photoService.addPhoto(photo);

        ClientPhoto clientPhoto = new ClientPhoto();
        clientPhoto.setUrl(url);
        clientPhoto.setUsername(user.getUsername());
        clientPhoto.setUuid(photo.getId());
        return clientPhoto;
    }


    @PostMapping(path = "/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ClientUser login(@RequestParam("user") String accountName, String password) throws Exception {
        ServerUser res = userService.verifyInfo(accountName, password);
        return new ClientUser(res.getUsername(), res.getEmail(), res.getAge(), res.getGender());
    }

    //home for user
    @GetMapping("/{user}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ClientUser getGreetingPageForUser(@PathVariable  String user){
        ServerUser res = userService.findByUsername(user);
        if (res == null) res = userService.findByEmail(user);
        return new ClientUser(res.getUsername(), res.getEmail(), res.getAge(), res.getGender());
    }

}
