package com.app.miniIns.controllers;

import com.app.miniIns.entities.*;
import com.app.miniIns.services.S3Service;
import com.app.miniIns.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;

@Controller
public class MyController {

    public UserService getUserService() {
        return userService;
    }
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private UserService userService;

    public S3Service getS3Service() {
        return s3Service;
    }

    public void setS3Service(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @Autowired
    private S3Service s3Service;

    //Main page
    @GetMapping(path = "/greeting")
    public String getGreetingPage(ModelAndView modelAndView) {
        return "greeting";
    }


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
        s3Service.upload("miniins-bucket", file.getOriginalFilename(), file);
        return "miniins-bucket/"+file.getOriginalFilename();
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
    public ClientUser getGreetingPageForUser(ModelAndView modelAndView, @PathVariable  String user){ //@PathVariable String username) {
        ServerUser res = userService.findByUsername(user);
        if (res == null) res = userService.findByEmail(user);

//        modelAndView.addObject("usernmae", res.getUsername());
//        modelAndView.setViewName("greeting");
//        return modelAndView;
        return new ClientUser(res.getUsername(), res.getEmail(), res.getAge(), res.getGender());
    }

}
