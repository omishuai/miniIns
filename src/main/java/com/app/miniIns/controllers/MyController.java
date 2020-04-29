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
import org.springframework.web.servlet.ModelAndView;
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

    @GetMapping (path = "/register")
    public ModelAndView register(ModelAndView modelAndView, ServerUser user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @PostMapping(path = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ClientUser register(ServerUser user) throws Exception {
        ServerUser res = userService.addUser(user);
        return new ClientUser(res.getUsername(), res.getEmail(), res.getAge(), user.getGender());
    }

    @GetMapping (path = "/login")
    public ModelAndView login(ModelAndView modelAndView, ServerUser user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("login");
        return modelAndView;
    }

    //Bucket is creatd by root and assumed to exist
    @PostMapping(path="/upload")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();

        String u = (String) auth.getPrincipal();
        ServerUser user = userService.findByUsername(u);
        Photo photo = new Photo(user, "miniins-bucket", file.getOriginalFilename());
        photo.setS3_key(photo.getId().toString());
        photoService.addPhoto(photo);

        s3Service.upload(photo.getS3_bucket(), photo.getS3_key(), file);
        URL url = s3Service.getUrl(photo.getS3_bucket(), photo.getS3_key());
        return  url.toString();
    }


    @PostMapping(path = "/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ClientUser login(ServerUser user) throws Exception {
        ServerUser res = userService.verifyInfo(user);
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
