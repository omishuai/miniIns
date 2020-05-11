package com.app.miniIns.controllers;

import com.app.miniIns.entities.*;
import com.app.miniIns.services.FileStorageService;
import com.app.miniIns.services.PhotoService;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserService userService;

    public PhotoReformatter getPhotoReformatter() {
        return photoReformatter;
    }

    public void setPhotoReformatter(PhotoReformatter photoReformatter) {
        this.photoReformatter = photoReformatter;
    }

    @Autowired
    private PhotoReformatter photoReformatter;

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

    public FileStorageService getFileStorageService() {
        return fileStorageService;
    }

    public void setFileStorageService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/photo/{photoId}/like")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ClientPhoto addLike(@PathVariable("photoId") String pid) throws MalformedURLException {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = (String) context.getAuthentication().getPrincipal();
        User user = userService.findByUsername(username);
        Photo photo = photoService.likedByUser(user, UUID.fromString(pid));
        return photoReformatter.constructClientPhoto(photo);
    }


    @PostMapping("/photo/{photoId}/unlike")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ClientPhoto removeLike(@PathVariable("photoId")  String pid) throws MalformedURLException {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = (String) context.getAuthentication().getPrincipal();
        User user = userService.findByUsername(username);
        Photo photo = photoService.unlikedByUser(user, UUID.fromString(pid));
        return photoReformatter.constructClientPhoto(photo);
    }


    @PostMapping(path="/photo/upload")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ClientPhoto uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();

        String u = (String) auth.getPrincipal();
        User user = userService.findByUsername(u);

        //In case the destination becomes disk, we make controller unaware of s3 bucket
        Photo photo = new Photo(user, file.getOriginalFilename());

        URL url =  fileStorageService.upload(photo.getUuid().toString(), file);

        photo = photoService.addPhoto(photo);

        // Return client photo without likedBy and comments  (new)
        return  new ClientPhoto(user.getUsername(), url, photo.getUuid());
    }

    @GetMapping("/explore")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<ClientPhoto> getPhotoPool() throws MalformedURLException {

        List<ClientPhoto> res = new ArrayList<>();
        List<Photo> ls = photoService.findAllByCreateDateTimeBetween(LocalDateTime.now().minusDays(1), LocalDateTime.now());
        for (Photo p: ls)
            res.add(new ClientPhoto(p.getUser().getUsername(), fileStorageService.getUrl(p.getUuid().toString()), p.getUuid()));
        return res;
    }

}
